/*
 * To change this license header, choose PrintJob Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsockets.imageanalyzer.base;

import com.google.gson.Gson;
import com.popsockets.imageanalyzer.exceptions.CustomException;
import com.popsockets.imageanalyzer.exceptions.InvalidInputException;
import com.popsockets.imageanalyzer.exceptions.PersistanceApiFailedException;
import com.popsockets.imageanalyzer.util.DataUtil;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import io.vertx.core.json.JsonArray;
import org.apache.http.client.methods.HttpGet;

/**
 *
 * @author Suranga
 */
public class RecipeManager {

    private Gson gson = new Gson();
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public String getAssetUrl(String id) throws CustomException {
        String imageUrl = null;
        JsonObject recipeData = getRecipeData(id);
        //final String IMAGE_KEY = "dynamic-image-jpgUrl";
        final String IMAGE_KEY = "dynamic-image-jpgUrl";
        try {
            if (recipeData != null) {
                String recipeName = recipeData.getString("name");
                switch (recipeName) {
                    case "PopTop":
                    case "PopGrip":
                    case "Custom PopGrip'":
                    case "Custom PopTop":
                    case "PopGripPackage": {
                        JsonArray components = recipeData.getJsonArray("components");
                        if (components != null && components.size() > 0) {
                            for (int i = 0; i < components.size(); i++) {
                                JsonObject component = components.getJsonObject(i);
                                String code = component.getString("code");
                                if (code.contains("GRIPPACKAGEIMAGE") && component.getJsonObject("component").getJsonObject("custom").getString(IMAGE_KEY) == null) {
                                    //imageUrl = component.getJsonObject("component").getJsonObject("custom").getString(IMAGE_KEY);
                                    //imageUrl = imageUrl.replaceFirst("//", "http://");
                                    break;
                                } else if (code.contains("GRIPIMAGE")) {
                                    imageUrl = component.getJsonObject("component").getJsonObject("custom").getString(IMAGE_KEY);
                                    imageUrl = imageUrl.replaceFirst("//", "http://");
                                    break;
                                }
                            }

                        }
                    }
                    break;
                    case "PopMinis":
                    case "Custom PopMinis": {
                        JsonArray components = recipeData.getJsonArray("components");
                        if (components != null && components.size() > 0) {
                            for (int i = 0; i < components.size(); i++) {
                                JsonObject component = components.getJsonObject(i);
                                String code = component.getString("code");
                                if (code.contains("IMAGE1") || code.contains("IMAGE2") || code.contains("IMAGE3")) {
                                    imageUrl = component.getJsonObject("component").getJsonObject("custom").getString(IMAGE_KEY);
                                    imageUrl = imageUrl.replaceFirst("//", "http://");
                                    break;
                                }
                            }

                        }
                    }
                    break;
                    case "PopWallet":
                    case "Custom PopWallet": {
                        JsonArray components = recipeData.getJsonArray("components");
                        if (components != null && components.size() > 0) {
                            for (int i = 0; i < components.size(); i++) {
                                JsonObject component = components.getJsonObject(i);
                                String code = component.getString("code");
                                if (code.contains("IMAGE") && !code.contains("PACKAGE")) {
                                    imageUrl = component.getJsonObject("component").getJsonObject("custom").getString(IMAGE_KEY);
                                    imageUrl = imageUrl.replaceFirst("//", "http://");
                                    break;
                                }
                            }

                        }
                    }
                    break;
                    case "PopWallet+ Lite": {
                        JsonArray components = recipeData.getJsonArray("components");
                        if (components != null && components.size() > 0) {
                            for (int i = 0; i < components.size(); i++) {
                                JsonObject component = components.getJsonObject(i);
                                String code = component.getString("code");
                                if (code.contains("BASEIMAGE") || code.contains("GRIPIMAGE")) {
                                    imageUrl = component.getJsonObject("component").getJsonObject("custom").getString(IMAGE_KEY);
                                    imageUrl = imageUrl.replaceFirst("//", "http://");
                                    break;
                                }
                            }

                        }
                    }
                    break;
                    case "PopWallet+":
                    case "Integrated PopWallet": {
                        JsonArray components = recipeData.getJsonArray("components");
                        if (components != null && components.size() > 0) {
                            for (int i = 0; i < components.size(); i++) {
                                JsonObject component = components.getJsonObject(i);
                                String code = component.getString("code");
                                if (code.contains("BASEIMAGE") || code.contains("GRIPIMAGE")) {
                                    imageUrl = component.getJsonObject("component").getJsonObject("custom").getString(IMAGE_KEY);
                                    imageUrl = imageUrl.replaceFirst("//", "http://");
                                    break;
                                }
                            }

                        }
                    }
                    break;
                    case "Otter + Pop 7/8": {
                        JsonArray components = recipeData.getJsonArray("components");
                        if (components != null && components.size() > 0) {
                            for (int i = 0; i < components.size(); i++) {
                                JsonObject component = components.getJsonObject(i);
                                String code = component.getString("code");
                                if (code.contains("BASEIMAGE") || code.contains("GRIPIMAGE")) {
                                    imageUrl = component.getJsonObject("component").getJsonObject("custom").getString(IMAGE_KEY);
                                    imageUrl = imageUrl.replaceFirst("//", "http://");
                                    break;
                                }
                            }

                        }
                    }
                    break;
                    default: {
                        JsonArray components = recipeData.getJsonArray("components");
                        if (components != null && components.size() > 0) {
                            for (int i = 0; i < components.size(); i++) {
                                JsonObject component = components.getJsonObject(i);
                                String code = component.getString("code");
                                if (code.contains("BASEIMAGE")) {
                                    imageUrl = component.getJsonObject("component").getJsonObject("custom").getString(IMAGE_KEY);
                                    imageUrl = imageUrl.replaceFirst("//", "http://");
                                    break;
                                } else if (code.contains("GRIPIMAGE")) {
                                    imageUrl = component.getJsonObject("component").getJsonObject("custom").getString(IMAGE_KEY);
                                    imageUrl = imageUrl.replaceFirst("//", "http://");
                                }
                            }

                        }
                    }

                }

            }
        } catch (Exception e) {
            throw new InvalidInputException("Invalid recipe data.Recipe data extract failed.");
        }
        if (imageUrl == null) {
            throw new InvalidInputException("Invalid image url. Recipe data extract failed.");
        }

        return imageUrl;

    }

    private JsonObject getRecipeData(String id) throws CustomException {
        JsonObject recipeData = null;
        String url = DataUtil.URL_RECIPE + id + ".json";
        HttpGet client = new HttpGet(url);
        int statusCode;
        String resultData = "";

        try {

            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                    CloseableHttpResponse response = httpClient.execute(client)) {
                statusCode = response.getStatusLine().getStatusCode();
                resultData = EntityUtils.toString(response.getEntity());
            }
            if (statusCode == 200) {
                recipeData = new JsonObject(resultData);
            } else {
                throw new InvalidInputException("Invalid recipe id. Recipe data extract failed.");

            }

        } catch (CustomException ex) {
            throw ex;

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new PersistanceApiFailedException("Recipe data get api failed: " + ex.getMessage());
        }
        return recipeData;

    }
}
