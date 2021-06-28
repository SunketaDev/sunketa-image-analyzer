package com.popsockets.imageanalyzer.manager;

import com.popsockets.imageanalyzer.util.DataUtil;
import com.popsockets.imageanalyzer.vertx.util.VerticleDeployHelper;
import com.sunketa.mysql.db.DatabaseConfig;
import com.sunketa.mysql.dbmanager.DBManager;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.logging.Logger;
import java.util.concurrent.TimeUnit;

public class MainVerticle extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /**
     *
     * @param promise
     */
    @Override
    public void start(Promise<Void> promise) {
        System.out.println("Starting MainVerticle.....");
        configureDb(config(), res -> {
            if (res.succeeded()) {
                deployVerticles(promise);
                logger.debug("Deploy verticles succeeded.....");
            } else {
                logger.debug(res.cause().getMessage());
            }

        });

    }

    private void deployVerticles(Promise<Void> promise) {
        VertxOptions vertxOptions = new VertxOptions();
        vertxOptions.setBlockedThreadCheckIntervalUnit(TimeUnit.MINUTES);
        vertxOptions.setBlockedThreadCheckInterval(10);
        vertx = Vertx.vertx(vertxOptions);
        DeploymentOptions options = new DeploymentOptions().setConfig(config());
        DeploymentOptions options2 = new DeploymentOptions().setConfig(config()).setWorker(true).setInstances(5);

        List<Future> list = new ArrayList<>();
        list.add(VerticleDeployHelper.deployHelper(
                vertx,
                options,
                RestVerticle.class.getName()
        ));
        list.add(VerticleDeployHelper.deployHelper(
                vertx,
                options2,
                ImageAnalyzerVerticle.class.getName()
        ));

        CompositeFuture
                .all(list)
                .onComplete(
                        result -> {
                            if (result.succeeded()) {
                                logger.info("Verticle deployment successfull.");
                                promise.complete();
                            } else {
                                logger.info("Verticle deployment failed.");
                                promise.fail(result.cause());

                            }
                        }
                );
    }

    @Override
    public void stop() {
        logger.info("Stop succeeded ");
    }

    private void configureDb(
            JsonObject config,
            Handler<AsyncResult<Void>> future
    ) {

        vertx.executeBlocking(
                executeBlockingFuture -> {
                    try {

                        DataUtil.COMPUTER_VISION_API_PATH = config().getString("computer_vision.path", "https://psimagevision.cognitiveservices.azure.com/vision/v3.2/");
                        DataUtil.COMPUTER_VISION_KEY = config().getString("computer_vision.key", "3fc9325d0e3b46008c3cd3ae5ba7e61a");
                        DataUtil.URL_RECIPE = config().getString("url_recipe", "http://cz.drrv.co/recipe/");
                        logger.info("computer vision path --- > : " + DataUtil.COMPUTER_VISION_API_PATH);
                        System.out.println("computer vision path --- > : " + DataUtil.COMPUTER_VISION_API_PATH);
                        System.out.println("recipe url --- > : " + DataUtil.URL_RECIPE);
                        String dbName = config.getString("db_name", "recipe-analyzer");
                        String dbUrl = config.getString("db_url", "jdbc:mysql://10.46.16.8:3306/recipe-analyzer");
                        String dbUsername = config.getString("db_username", "pssql");
                        String dbPassword = config.getString("db_password", "R@tP0!son456");
                        DataUtil.URL_RECIPE = config().getString("url_recipe", "http://cz.drrv.co/recipe/");
                        DatabaseConfig dbConfig = new DatabaseConfig();
                        dbConfig.setDb(dbName);
                        dbConfig.setPass(dbPassword);
                        dbConfig.setUrl(dbUrl);
                        dbConfig.setUser(dbUsername);

                        DBManager.getInstance().configure(dbConfig);
                        executeBlockingFuture.complete();
                        //db config end

                    } catch (Exception ex) {
                        logger.error("Database configuring exceptions.");
                        executeBlockingFuture.fail(ex);
                    }
                },
                res -> {
                    if (res.failed()) {
                        future.handle(Future.failedFuture(res.cause()));
                    } else {
                        future.handle(Future.succeededFuture());
                    }
                }
        );
    }

}
