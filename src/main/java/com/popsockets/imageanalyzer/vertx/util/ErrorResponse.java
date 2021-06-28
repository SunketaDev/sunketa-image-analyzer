/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsockets.imageanalyzer.vertx.util;

import com.popsockets.imageanalyzer.exceptions.CustomException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**
 *
 * @author nandana
 */
public class ErrorResponse {
    // helper method dealing with failure

    public static void errorResponse(RoutingContext ctx, CustomException ex) {

        JsonObject o = new JsonObject();
        o.put("code", ex.getErrorNo());
        o.put("error", ex.getError());
        o.put("type", ex.getErrorType());
        o.put("description", ex.getErrorMsg());

        JsonObject ret = new JsonObject();
        ret.put("fault", o);

        ctx.response().setStatusCode(400)
                .putHeader("content-type", "application/json")
                .end(ret.encodePrettily());

    }

    public static void errorResponse(RoutingContext ctx, CustomException ex, int statusCode) {

        JsonObject o = new JsonObject();
        o.put("code", ex.getErrorNo());
        o.put("error", ex.getError());
        o.put("type", ex.getErrorType());
        o.put("description", ex.getErrorMsg());

        JsonObject ret = new JsonObject();
        ret.put("fault", o);

        ctx.response().setStatusCode(statusCode)
                .putHeader("content-type", "application/json")
                .end(ret.encodePrettily());

    }

    public static void errorResponse(RoutingContext ctx, JsonObject fault, int statusCode) {

        JsonObject ret = new JsonObject();
        ret.put("fault", fault);

        ctx.response().setStatusCode(statusCode)
                .putHeader("content-type", "application/json")
                .end(ret.encodePrettily());

    }

    public static JsonObject getFaultResponseContent(CustomException ex) {

        JsonObject o = new JsonObject();
        o.put("code", ex.getErrorNo());
        o.put("error", ex.getError());
        o.put("type", ex.getErrorType());
        o.put("description", ex.getErrorMsg());

        JsonObject ret = new JsonObject();
        ret.put("fault", o);

        return ret;
    }

    public static void badRequest(RoutingContext context, Throwable ex) {

        CustomException exx = new CustomException(400, "Bad Request", "BadRequest", ex.getMessage());

        context.response().setStatusCode(400)
                .putHeader("content-type", "application/json")
                .end(getFaultResponseContent(exx).encodePrettily());
    }

    public static void notFound(RoutingContext context) {

        CustomException exx = new CustomException(404, "Not Found", "NotFound", "Not found");

        context.response().setStatusCode(404)
                .putHeader("content-type", "application/json")
                .end(getFaultResponseContent(exx).encodePrettily());
    }

    public static void internalError(RoutingContext context, Throwable ex) {

        CustomException exx = new CustomException(500, "Internal Error", "InternalError", ex.getMessage());

        context.response().setStatusCode(500)
                .putHeader("content-type", "application/json")
                .end(getFaultResponseContent(exx).encodePrettily());
    }

    public static void internalError(RoutingContext context, CustomException exx) {

        context.response().setStatusCode(500)
                .putHeader("content-type", "application/json")
                .end(getFaultResponseContent(exx).encodePrettily());
    }

    public static void notImplemented(RoutingContext context) {

        CustomException exx = new CustomException(501, "Not Implemented", "notImplemented", "Not Implemented");

        context.response().setStatusCode(501)
                .putHeader("content-type", "application/json")
                .end(getFaultResponseContent(exx).encodePrettily());
    }

    public static void badGateway(RoutingContext context, Throwable ex) {

        CustomException exx = new CustomException(404, "Bad Gateway", "badGateway", "Bad Gateway");

        //ex.printStackTrace();
        context.response()
                .setStatusCode(502)
                .putHeader("content-type", "application/json")
                .end(getFaultResponseContent(exx).encodePrettily());
    }

    public static void serviceUnavailable(RoutingContext context) {
        context.fail(503);
    }

    public static void serviceUnavailable(RoutingContext context, Throwable ex) {

        CustomException exx = new CustomException(404, "Service Unavailable", "ServiceUnavailable", ex.getMessage());

        context.response().setStatusCode(503)
                .putHeader("content-type", "application/json")
                .end(getFaultResponseContent(exx).encodePrettily());
    }

    public static void serviceUnavailable(RoutingContext context, String cause) {

        CustomException exx = new CustomException(404, "Service Unavailable", "ServiceUnavailable", cause);

        context.response().setStatusCode(503)
                .putHeader("content-type", "application/json")
                .end(getFaultResponseContent(exx).encodePrettily());
    }
}
