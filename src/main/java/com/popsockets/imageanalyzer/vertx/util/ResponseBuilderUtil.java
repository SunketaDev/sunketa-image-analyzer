package com.popsockets.imageanalyzer.vertx.util;

import com.popsockets.imageanalyzer.exceptions.CustomException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**
 *
 * @author nandana
 */
public class ResponseBuilderUtil {
    public static JsonObject getFaultResponseContent(CustomException ex){
        
        JsonObject o = new JsonObject();
        o.put("code", ex.getErrorNo());
        o.put("error", ex.getError());
        o.put("type", ex.getErrorType());
        o.put("description", ex.getErrorMsg());
        
        JsonObject ret = new JsonObject();
        ret.put("fault", o);
                
        return ret;
    }
    
    public static void packageErrorResponse(RoutingContext ctx, CustomException ex) {

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
    
    public static void packageErrorResponse(RoutingContext ctx, CustomException ex, int statusCode) {

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
}
