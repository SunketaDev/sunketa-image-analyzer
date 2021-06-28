/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.popsockets.imageanalyzer.vertx.util;

import io.vertx.core.*;
import io.vertx.core.spi.cluster.ClusterManager;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 *
 * @author nandana
 */
public class VerticleDeployHelper {
private VerticleDeployHelper() {}

static Logger logger = LoggerFactory.getLogger(VerticleDeployHelper.class.getName());

    /**
     *
     * @param vertx
     * @param name
     * @return
     */
    public static Future<Void> deployHelper(final Vertx vertx, final String name){
        final Promise<Void> promise = Promise.promise();
        vertx.deployVerticle(name, res -> {
            if(res.failed()){
                logger.error("Failed to deploy verticle!", name);
                promise.fail(res.cause());
            } else {
                logger.info("{} verticle deployed!", name);
                promise.complete();
            }
        });

        return promise.future();
    }

    /**
     *
     * @param manager
     * @param className
     * @return
     */
    public static Future<Void> deployHelper(final Vertx vertx, final ClusterManager manager, final String className){

        final Promise<Void> promise = Promise.promise();
        final ClusterManager mgr = manager;
        final VertxOptions options = new VertxOptions().setClusterManager(mgr);

        Vertx.clusteredVertx(options, cluster -> {
            if (cluster.succeeded()) {
                try {
                    cluster.result().deployVerticle((Verticle) Class.forName(className).newInstance(), res -> {
                        if(res.succeeded()){
                            logger.info("Deployment id is {}", res.result());
                            promise.complete();
                        } else {
                            logger.error("Deployment failed!", res.cause());
                            promise.fail(res.cause());
                        }
                    });
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    logger.error("Verticle deploy failed!", e);
                }
            } else {
                logger.error("Cluster up failed!", cluster.cause());
                promise.fail(cluster.cause());
            }
        });

        return promise.future();
    }

    public static Future<Void> deployHelper(final Vertx vertx, DeploymentOptions options, final String name){
        final Promise<Void> promise = Promise.promise();
        vertx.deployVerticle(name, options, res -> {
            if(res.failed()){
                logger.error("Failed to deploy verticle!", name);
                promise.fail(res.cause());
            } else {
                logger.info("{} verticle deployed!", name);
                promise.complete();
            }
        });

        return promise.future();
    }

}