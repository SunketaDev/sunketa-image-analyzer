package com.popsockets.imageanalyzer.manager;

import com.google.gson.Gson;
import com.popsockets.imageanalyzer.base.ImageAnalyzer;
import com.popsockets.imageanalyzer.dataobject.computervision.DOTag;
import com.popsockets.imageanalyzer.exceptions.CustomException;
import com.popsockets.imageanalyzer.exceptions.InvalidInputException;
import com.popsockets.imageanalyzer.vertx.util.ErrorResponse;
import com.sunketa.mysql.dbmanager.DBManager;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.logging.Logger;
import java.util.ArrayList;

public class ImageAnalyzerVerticle extends AbstractVerticle {

    final String EB_ADDRESS = "popsockets.imageanalyzer.image";
    Gson gson = new Gson();
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    ImageAnalyzer imageAnalyzer;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        imageAnalyzer = new ImageAnalyzer(DBManager.getInstance().getFactory());

        vertx.eventBus().consumer(EB_ADDRESS, (message) -> {

            Object result = null;
            try {
                JsonObject req = (JsonObject) message.body();
                String method = req.getString("method");
                logger.debug("method  ---> " + method);
                logger.debug("request ---> " + gson.toJson(req));
                switch (method) {
                    case "image.analyze":
                        result = analyze(req.getJsonObject("data"));
                        break;
                    case "image.save_tags":
                        result = saveTags(req.getJsonObject("data"));
                        break;
                    case "image.tags_list":
                        result = tagList(req.getJsonObject("data"));
                        break;
                    default:
                        throw new InvalidInputException("Invalid result");
                }

                JsonObject rest = new JsonObject()
                        .put("success", true)
                        .put("data", result);
                logger.debug("output ---> " + rest.toString());
                message.reply(rest);

            } catch (CustomException ex) {
                logger.error(ex.getMessage());
                result = ErrorResponse.getFaultResponseContent(ex)
                        .put("success", false);
                message.reply(result);
            }
        });

    }

    private JsonObject analyze(JsonObject request) throws CustomException {
        return imageAnalyzer.analyze(request);

    }

    private JsonObject saveTags(JsonObject request) throws CustomException {
        return imageAnalyzer.saveTags(request);
    }

    private JsonArray tagList(JsonObject request) throws CustomException {
        ArrayList<DOTag> result = imageAnalyzer.tagList(request.getString("recipe_id"), request.getString("email"));
        return new JsonArray(gson.toJson(result));
    }

}
