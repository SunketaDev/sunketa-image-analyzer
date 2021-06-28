/*
 * To change this license header, choose PrintJob Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsockets.imageanalyzer.base;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.popsockets.imageanalyzer.dataaccess.DAOTag;
import com.popsockets.imageanalyzer.dataobject.computervision.DOFaultData;
import com.popsockets.imageanalyzer.dataobject.computervision.DOImageAnalyze;
import com.popsockets.imageanalyzer.dataobject.DOImageAnalyzeRequest;
import com.popsockets.imageanalyzer.dataobject.DOImageTag;
import com.popsockets.imageanalyzer.dataobject.computervision.DOAnalyzeRequest;
import com.popsockets.imageanalyzer.dataobject.computervision.DOResultError;
import com.popsockets.imageanalyzer.dataobject.computervision.DOTag;
import com.popsockets.imageanalyzer.util.DataUtil;
import com.popsockets.imageanalyzer.exceptions.CustomException;
import com.popsockets.imageanalyzer.exceptions.DoesNotExistException;
import com.popsockets.imageanalyzer.exceptions.InvalidInputException;
import com.popsockets.imageanalyzer.exceptions.PersistanceApiFailedException;
import com.sunketa.mysql.db.IDbFactory;
import com.sunketa.mysql.db.IDbSession;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Suranga
 */
public class ImageAnalyzer {

    private Gson gson = new Gson();
    IDbFactory dbFactory;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public ImageAnalyzer(IDbFactory db) {
        this.dbFactory = db;
    }

    public JsonObject analyze(JsonObject request) throws CustomException {
        boolean isFile = request.getBoolean("isFile");
        DOImageAnalyzeRequest imageAnalyzeRequest = null;
        if (isFile) {
            imageAnalyzeRequest = new DOImageAnalyzeRequest();
            double confidenceThreshold = 0;
            try {
                if (request.getString("confidenceThreshold") != null) {
                    confidenceThreshold = Double.parseDouble(request.getString("confidenceThreshold"));
                }
            } catch (Exception e) {
                throw new InvalidInputException("Invalid Confidence Threshold.");
            }
            imageAnalyzeRequest.setConfidenceThreshold(confidenceThreshold);

            String tags = request.getString("tags");
            if (tags != null) {
                tags = tags.trim();
                if (tags.isEmpty()) {
                    tags = null;
                }
            }

            if (tags != null) {
                try {
                    Type type = new TypeToken<ArrayList<DOImageTag>>() {
                    }.getType();
                    ArrayList<DOImageTag> tagList = gson.fromJson(tags, type);
                    imageAnalyzeRequest.setTags(tagList);
                } catch (Exception e) {
                    throw new InvalidInputException("Invalid tags");
                }
            }
            imageAnalyzeRequest.setUrl(request.getString("uploadedFileName"));

        } else {
            imageAnalyzeRequest = gson.fromJson(request.encode(), DOImageAnalyzeRequest.class);

        }

        DOImageAnalyze result = analyze(request, imageAnalyzeRequest);

        return new JsonObject(Json.encode(result));

    }

    private DOImageAnalyze analyze(JsonObject request, DOImageAnalyzeRequest imageAnalyzeRequest) throws CustomException {
        boolean imageResized = false;
        String url = DataUtil.COMPUTER_VISION_API_PATH + request.getString("lastUrl");

        if (imageAnalyzeRequest.getConfidenceThreshold() < 0 || imageAnalyzeRequest.getConfidenceThreshold() > 1) {
            throw new InvalidInputException("Invalid Confidence Threshold.");
        }
        if (imageAnalyzeRequest.getTags() != null && imageAnalyzeRequest.getTags().size() > 0) {
            for (DOImageTag imageTag : imageAnalyzeRequest.getTags()) {

                if (imageTag.getTagName() == null) {
                    throw new InvalidInputException("Invalid tag name.");
                }
                imageTag.setTagName(imageTag.getTagName().trim());

                if (imageTag.getTagName().isEmpty()) {
                    throw new InvalidInputException("Invalid tag name.");
                }

                if (imageTag.getConfidenceThreshold() < 0 || imageTag.getConfidenceThreshold() > 1) {
                    throw new InvalidInputException("Invalid Confidence Threshold. tag name : " + imageTag.getTagName());
                }
            }

        }

        boolean isFile = request.getBoolean("isFile");

        DOImageAnalyze imageAnalyze = null;
        if (!isFile) {
            if (imageAnalyzeRequest.getUrl() == null) {
                throw new InvalidInputException("Invalid url.");
            }

            int errorNo = 0;
            try {

                imageAnalyze = analyze(url, imageAnalyzeRequest.getUrl());

            } catch (CustomException ex) {
                if (ex.getErrorNo() == 400) {
                    errorNo = ex.getErrorNo();
                } else {
                    throw ex;
                }
            }

            if (errorNo != 0) {
                BufferedImage img = null;
                byte[] bytesFile = null;

                try {
                    img = ImageIO.read(new URL(imageAnalyzeRequest.getUrl()));

                } catch (IOException ex) {
                    logger.error(ex.getMessage());
                    throw new InvalidInputException("File read error");
                }

                bytesFile = resize(img, request.getString("contentType"));

                if (bytesFile == null) {
                    throw new InvalidInputException("File read error");
                }

                imageAnalyze = analyzeByFile(url, bytesFile);
                imageResized = true;
            }
        } else {

            if (imageAnalyzeRequest.getUrl() == null) {
                throw new DoesNotExistException("File does not exists.");
            }

            File file = new File(imageAnalyzeRequest.getUrl());
            if (!file.exists()) {
                throw new DoesNotExistException("File does not exists.");
            }
            long bytes = file.length();
            long maxSize = 4 * 1024 * 1024;

            if (bytes == 0) {
                throw new InvalidInputException("File is empty");
            }
            BufferedImage img = null;
            byte[] bytesFile = null;
            if (bytes >= maxSize) {
                //   throw new InvalidInputException("Image file size must be less than 4MB");
                try {
                    img = ImageIO.read(file);
                } catch (IOException ex) {
                    logger.error(ex.getMessage());
                    throw new InvalidInputException("File read error");
                }
                bytesFile = resize(img, request.getString("contentType"));
                imageResized = true;

            } else {
                try {
                    bytesFile = Files.readAllBytes(file.toPath());
                } catch (IOException ex) {
                    logger.error(ex.getMessage());
                    throw new InvalidInputException("File read error");
                }
            }
            if (bytesFile == null) {
                throw new InvalidInputException("File read error");
            }

            imageAnalyze = analyzeByFile(url, bytesFile);

        }

        ArrayList<DOTag> selectedTags = new ArrayList<>();

        if (imageAnalyzeRequest.getTags() != null && imageAnalyzeRequest.getTags().size() > 0) {

            if (imageAnalyze.getTags() != null && imageAnalyze.getTags().size() > 0) {

                for (DOTag tag : imageAnalyze.getTags()) {
                    boolean selected = false;
                    boolean checked = false;

                    for (DOImageTag imageTag : imageAnalyzeRequest.getTags()) {

                        if (imageTag.getTagName().equals(tag.getName())) {
                            checked = true;
                            if (tag.getConfidence() >= imageTag.getConfidenceThreshold()) {
                                selected = true;

                            }

                        }

                    }
                    if (!checked && imageAnalyzeRequest.getConfidenceThreshold() > 0 && tag.getConfidence() >= imageAnalyzeRequest.getConfidenceThreshold()) {
                        selected = true;
                    }
                    if (selected) {
                        selectedTags.add(tag);
                    }

                }

                imageAnalyze.setTags(selectedTags);
            }

        } else {
            if (imageAnalyzeRequest.getConfidenceThreshold() > 0) {
                if (imageAnalyze.getTags() != null && imageAnalyze.getTags().size() > 0) {
                    for (DOTag tag : imageAnalyze.getTags()) {
                        if (tag.getConfidence() >= imageAnalyzeRequest.getConfidenceThreshold()) {
                            selectedTags.add(tag);
                        }

                    }
                    imageAnalyze.setTags(selectedTags);
                }

            }
        }

        imageAnalyze.setImageResized(imageResized);

        return imageAnalyze;
    }

    private DOImageAnalyze analyze(String url, String imageUrl) throws CustomException {
        HttpPost client = new HttpPost(url);
        client.addHeader("content-type", "application/json");
        client.addHeader("Ocp-Apim-Subscription-Key", DataUtil.COMPUTER_VISION_KEY);
        DOAnalyzeRequest requestObj = new DOAnalyzeRequest();
        DOImageAnalyze result;
        String resultData = "";
        int statusCode = 0;
        try {

            requestObj.setUrl(imageUrl);
            client.setEntity(new StringEntity(gson.toJson(requestObj)));

            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                    CloseableHttpResponse response = httpClient.execute(client)) {
                statusCode = response.getStatusLine().getStatusCode();
                resultData = EntityUtils.toString(response.getEntity());
            }

            if (statusCode == 200) {
                result = gson.fromJson(resultData, DOImageAnalyze.class);
            } else {
                DOResultError resultError = gson.fromJson(resultData, DOResultError.class);
                DOFaultData faultData = resultError.getError();
                CustomException ex = new CustomException(statusCode, faultData.getMessage(), "API Failed", "Computer Vision API Exception.");
                throw ex;
            }
        } catch (CustomException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new PersistanceApiFailedException("analyze api failed: " + ex.getMessage());
        }

        return result;
    }

    private DOImageAnalyze analyzeByFile(String url, byte[] bytes) throws CustomException {
        HttpPost client = new HttpPost(url);
        client.addHeader("content-type", "application/octet-stream");
        client.addHeader("Ocp-Apim-Subscription-Key", DataUtil.COMPUTER_VISION_KEY);
        DOImageAnalyze result;
        String resultData = "";
        int statusCode = 0;
        try {
            client.setEntity(new ByteArrayEntity(bytes));
            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                    CloseableHttpResponse response = httpClient.execute(client)) {
                statusCode = response.getStatusLine().getStatusCode();
                resultData = EntityUtils.toString(response.getEntity());
            }

            if (statusCode == 200) {
                result = gson.fromJson(resultData, DOImageAnalyze.class);
            } else {
                DOResultError resultError = gson.fromJson(resultData, DOResultError.class);
                DOFaultData faultData = resultError.getError();
                CustomException ex = new CustomException(518, faultData.getMessage(), "API Failed", "Computer Vision API Exception.");
                throw ex;
            }
        } catch (CustomException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new PersistanceApiFailedException("analyze api failed: " + ex.getMessage());
        }

        return result;
    }

    private byte[] resize(BufferedImage original, String mime) throws InvalidInputException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        BufferedImage resized = null;
        int changedHeight = 0;
        int changedWidth = 0;
        String Image_type = "jpg";
        logger.info("resizing Image ");
        int imgType = original.getType();
        if (imgType == 0) {
            if (mime.contains("png")) {
                imgType = BufferedImage.TYPE_INT_ARGB;
                Image_type = "png";
            } else {
                imgType = BufferedImage.TYPE_INT_RGB;
                Image_type = "jpg";
            }
        }
        changedWidth = 480;
        double ratio = (double) original.getHeight() / (double) original.getWidth();
        changedHeight = (int) (ratio * changedWidth);

        resized = new BufferedImage(changedWidth, changedHeight, imgType);
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, changedWidth, changedHeight, null);
        g.dispose();

        try {
            ImageIO.write(resized, Image_type, bout);

        } catch (IOException ex) {
            throw new InvalidInputException("File resize error. " + ex.getMessage());
        }
        return bout.toByteArray();

    }

    public JsonObject saveTags(JsonObject request) throws CustomException {
        boolean isFile = request.getBoolean("isFile");
        DOImageAnalyzeRequest imageAnalyzeRequest = null;
        DAOTag tagDAO = new DAOTag();

        String email = request.getString("email");

        if (email == null) {
            throw new InvalidInputException("Invalid email.");
        }
        email = email.trim();

        if (email.isEmpty()) {
            throw new InvalidInputException("Invalid email.");
        }

        if (isFile) {
            imageAnalyzeRequest = new DOImageAnalyzeRequest();
            double confidenceThreshold = 0;
            try {
                if (request.getString("confidenceThreshold") != null) {
                    confidenceThreshold = Double.parseDouble(request.getString("confidenceThreshold"));
                }
            } catch (Exception e) {
                throw new InvalidInputException("Invalid Confidence Threshold.");
            }
            imageAnalyzeRequest.setConfidenceThreshold(confidenceThreshold);

            String tags = request.getString("tags");
            if (tags != null) {
                tags = tags.trim();
                if (tags.isEmpty()) {
                    tags = null;
                }
            }

            if (tags != null) {
                try {
                    Type type = new TypeToken<ArrayList<DOImageTag>>() {
                    }.getType();
                    ArrayList<DOImageTag> tagList = gson.fromJson(tags, type);
                    imageAnalyzeRequest.setTags(tagList);
                } catch (Exception e) {
                    throw new InvalidInputException("Invalid tags");
                }
            }
            imageAnalyzeRequest.setUrl(request.getString("uploadedFileName"));

        } else {
            imageAnalyzeRequest = gson.fromJson(request.encode(), DOImageAnalyzeRequest.class);

        }

        DOImageAnalyze result = analyze(request, imageAnalyzeRequest);

        String recipeId = request.getString("recipeId");

        if (result.getTags() != null && result.getTags().size() > 0) {
            IDbSession dbs;
            try {
                dbs = dbFactory.getNewDbSession();
                try {
                    if (tagDAO.isExistsByRecipeIdAndEmail(dbs, recipeId, email)) {
                        tagDAO.delete(dbs, recipeId, email);
                    }
                    for (DOTag tag : result.getTags()) {
                        tagDAO.create(dbs, recipeId, email, tag);
                    }
                } catch (Exception ex) {
                    throw new PersistanceApiFailedException(ex.getMessage());
                } finally {
                    dbs.close();
                }
            } catch (Exception ex) {
                throw new PersistanceApiFailedException("analyze api failed: " + ex.getMessage());
            }

        }

        return new JsonObject(Json.encode(result));
    }

    public ArrayList<DOTag> tagList(String recipeId, String email) throws CustomException {
        IDbSession dbs;
        ArrayList<DOTag> list;
        DAOTag tagDAO = new DAOTag();
        try {

            if (recipeId == null) {
                throw new InvalidInputException("Invalid recipe id.");
            }
            recipeId = recipeId.trim();

            if (recipeId.isEmpty()) {
                throw new InvalidInputException("Invalid recipe id.");
            }

            if (email == null) {
                throw new InvalidInputException("Invalid email.");
            }
            email = email.trim();

            if (email.isEmpty()) {
                throw new InvalidInputException("Invalid email.");
            }

            dbs = dbFactory.getNewDbSession();
            try {
                list = tagDAO.list(dbs, recipeId, email);
            } catch (Exception ex) {
                throw new PersistanceApiFailedException(ex.getMessage());
            } finally {
                dbs.close();
            }

        } catch (CustomException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new PersistanceApiFailedException("analyze api failed: " + ex.getMessage());
        }
        return list;
    }

}
