package com.embed;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;

public class MainVerticle  extends AbstractVerticle {
    @Override
    public void start(Future<Void> startFuture) throws Exception {

        Future<String> httpVerticleDeployment = Future.future();
        vertx.deployVerticle("com.embed.http.HttpServerVerticle", new DeploymentOptions().setInstances(2),
                httpVerticleDeployment.completer());

        httpVerticleDeployment.setHandler(ar -> {
            if (ar.succeeded()) {
                startFuture.complete();
            } else {
                startFuture.fail(ar.cause());
            }
        });
    }
}
