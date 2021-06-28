/*
 * To change this license header, choose PrintJob Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsockets.imageanalyzer.manager;

import com.popsockets.imageanalyzer.base.RecipeManager;
import com.popsockets.imageanalyzer.vertx.util.ErrorResponse;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.logging.Logger;
import io.vertx.ext.web.FileUpload;

/**
 *
 * @author Suranga
 */
public class ImageAnalyzerHandler {

    Vertx vertx;
    Router router;
    Router subRouter;
    String mountPoint = "/v1.0/images";
    final String EB_ADDRESS = "popsockets.imageanalyzer.image";
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public ImageAnalyzerHandler(Vertx vertx, Router router) {
        this.vertx = vertx;
        this.router = router;
        subRouter = Router.router(vertx);
        subRouter.route().handler(BodyHandler.create());
        subRouter.post("/analyze").handler(this::handleAnalyze);
        subRouter.post("/recipe/analyze").handler(this::handleAnalyzeByRecipeId);
        subRouter.post("/recipe/save_tags").handler(this::handleSaveTags);
        subRouter.get("/recipe/:recipe_id/tags").handler(this::handleGetTags);
    }

    public String getMountPoint() {
        return mountPoint;
    }

    public Router getSubRouter() {
        return subRouter;
    }

    private void handleAnalyze(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();
        try {
            JsonObject reqObj = ctx.getBodyAsJson();
            if (reqObj == null) {
                reqObj = new JsonObject();
                String uploadedFileName = null;
                String contentType = null;
                for (FileUpload f : ctx.fileUploads()) {
                    uploadedFileName = f.uploadedFileName();
                    contentType = f.contentType();
                    break;
                }
                reqObj.put("confidenceThreshold", ctx.request().getParam("confidenceThreshold"));
                reqObj.put("uploadedFileName", uploadedFileName);
                reqObj.put("contentType", contentType);
                reqObj.put("tags", ctx.request().getParam("tags"));
                reqObj.put("isFile", true);
            } else {
                reqObj.put("isFile", false);
            }

            String path = ctx.request().uri();
            String pathArray[] = path.split("/");
            if (pathArray != null && pathArray.length > 0) {
                int length = pathArray.length;
                reqObj.put("lastUrl", pathArray[length - 1]);
            }

            JsonObject msg = EventBusMessageUtil.buildMessage("image.analyze", reqObj);
            vertx.eventBus().request(EB_ADDRESS, msg, res -> {
                if (res.succeeded()) {
                    JsonObject msgResult = (JsonObject) res.result().body();
                    if (msgResult.getBoolean("success")) {
                        response.putHeader("content-type", "application/json").end(msgResult.getJsonObject("data").encodePrettily());
                    } else {
                        ErrorResponse.errorResponse(ctx, msgResult.getJsonObject("fault"), 400);
                    }
                } else {
                    ErrorResponse.internalError(ctx, res.cause());
                }
            });
        } catch (Exception ex) {
            logger.error("Exception: " + ex.getMessage());
            ErrorResponse.internalError(ctx, ex);
        }

    }

    private void handleAnalyzeByRecipeId(RoutingContext ctx) {

        HttpServerResponse response = ctx.response();
        try {
            String recipeId = ctx.request().getParam("recipeId");
            JsonObject reqObj = ctx.getBodyAsJson();
            if (reqObj == null) {
                reqObj = new JsonObject();
            }
            RecipeManager recipeManager = new RecipeManager();
            String imageUrl = recipeManager.getAssetUrl(recipeId);
            reqObj.put("isFile", false);
            reqObj.put("url", imageUrl);

            String path = ctx.request().uri();
            String pathArray[] = path.split("/");
            if (pathArray != null && pathArray.length > 0) {
                int length = pathArray.length;
                reqObj.put("lastUrl", pathArray[length - 1]);
            }

            JsonObject msg = EventBusMessageUtil.buildMessage("image.analyze", reqObj);
            vertx.eventBus().request(EB_ADDRESS, msg, res -> {
                if (res.succeeded()) {
                    JsonObject msgResult = (JsonObject) res.result().body();
                    if (msgResult.getBoolean("success")) {
                        response.putHeader("content-type", "application/json").end(msgResult.getJsonObject("data").encodePrettily());
                    } else {
                        ErrorResponse.errorResponse(ctx, msgResult.getJsonObject("fault"), 400);
                    }
                } else {
                    ErrorResponse.internalError(ctx, res.cause());
                }
            });
        } catch (Exception ex) {
            logger.error("Exception: " + ex.getMessage());
            ErrorResponse.internalError(ctx, ex);
        }
    }

    private void handleSaveTags(RoutingContext ctx) {

        HttpServerResponse response = ctx.response();
        try {
            String recipeId = ctx.request().getParam("recipeId");
            String email = ctx.request().getParam("email");
            JsonObject reqObj = ctx.getBodyAsJson();
            if (reqObj == null) {
                reqObj = new JsonObject();
            }
            RecipeManager recipeManager = new RecipeManager();
            String imageUrl = recipeManager.getAssetUrl(recipeId);
            reqObj.put("isFile", false);
            reqObj.put("url", imageUrl);
            reqObj.put("email", email);
            reqObj.put("recipeId", recipeId);
            String lastUrl = "analyze?visualFeatures=Tags";
            reqObj.put("lastUrl", lastUrl);
            JsonObject msg = EventBusMessageUtil.buildMessage("image.save_tags", reqObj);
            vertx.eventBus().request(EB_ADDRESS, msg, res -> {
                if (res.succeeded()) {
                    JsonObject msgResult = (JsonObject) res.result().body();
                    if (msgResult.getBoolean("success")) {
                        response.putHeader("content-type", "application/json").end(msgResult.getJsonObject("data").encodePrettily());
                    } else {
                        ErrorResponse.errorResponse(ctx, msgResult.getJsonObject("fault"), 400);
                    }
                } else {
                    ErrorResponse.internalError(ctx, res.cause());
                }
            });
        } catch (Exception ex) {
            logger.error("Exception: " + ex.getMessage());
            ErrorResponse.internalError(ctx, ex);
        }
    }

    private void handleGetTags(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();
        String recipeId = ctx.request().getParam("recipe_id");
        String email = ctx.request().getParam("email");
        JsonObject reqObj = new JsonObject();
        reqObj.put("recipe_id", recipeId);
        reqObj.put("email", email);
        JsonObject msg = EventBusMessageUtil.buildMessage("image.tags_list", reqObj);
        vertx.eventBus().request(EB_ADDRESS, msg, res -> {
            if (res.succeeded()) {
                JsonObject msgResult = (JsonObject) res.result().body();
                if (msgResult.getBoolean("success")) {
                    response.putHeader("content-type", "application/json").end(msgResult.getJsonArray("data").encodePrettily());
                } else {
                    ErrorResponse.errorResponse(ctx, msgResult.getJsonObject("fault"), 400);
                }
            } else {
                ErrorResponse.internalError(ctx, res.cause());
            }
        });
    }

}
