/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsockets.imageanalyzer.manager;

import io.vertx.core.json.JsonObject;

/**
 *
 * @author Suranga
 */
public class EventBusMessageUtil {

    public static JsonObject buildMessage(String method, JsonObject data) {
        JsonObject msg = new JsonObject().put("method", method).put("data", data);
        return msg;
    }
}
