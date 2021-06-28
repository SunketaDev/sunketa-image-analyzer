/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsockets.imageanalyzer.manager;

import io.vertx.core.Launcher;
import io.vertx.core.Vertx;

/**
 *
 * @author suranga
 */
public class MyLauncher extends Launcher {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVerticle(), ar -> {
            System.out.println("deployVerticle...........");
        });
    }

}
