package com.embed.http;

import com.embed.api.Client;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

public class ApiHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiHandler.class);
    private static Client webClient;

    public ApiHandler(Vertx vertx) {
        webClient = new Client(vertx);
    }

    public final void getAccountbalance(RoutingContext context){
        JsonObject json = context.getBodyAsJson();
        Future<JsonObject> future = ApiHandler.setFuture(context);
        webClient.fetchAccountBalance(json, future.completer());
    }

    public final void getMinimumAccountbalance(RoutingContext context){
        JsonObject json = context.getBodyAsJson();
        Future<JsonObject> future = ApiHandler.setFuture(context);
        webClient.fetchMinimumAccountBalance(json, future.completer());
    }

    public final void getTransactionStatus(RoutingContext context){
        JsonObject json = context.getBodyAsJson();
        Future<JsonObject> future = ApiHandler.setFuture(context);
        webClient.fetchTransactionStatus(json, future.completer());
    }

    public final void postBatchTransactions(RoutingContext context){
        JsonObject json = context.getBodyAsJson();
        Future<JsonObject> future = ApiHandler.setFuture(context);
        webClient.addBatchTransactions(json, future.completer());
    }  

    public final void postHoldFund(RoutingContext context){
        JsonObject json = context.getBodyAsJson();
        Future<JsonObject> future = ApiHandler.setFuture(context);
        webClient.holdFund(json, future.completer());
    }

    public final void postUnHoldFund(RoutingContext context){
        JsonObject json = context.getBodyAsJson();
        Future<JsonObject> future = ApiHandler.setFuture(context);
        webClient.unHoldFund(json, future.completer());
    }

    protected static Future<JsonObject> setFuture(RoutingContext context) {
        final Future<JsonObject> future = Future.future();
        future.setHandler(res -> {
            //LOGGER.info(res);
            if (res.succeeded()) {
                context.response().setStatusCode(200);
                context.response().putHeader("Content-Type", "application/json");
                context.response().end(res.result().encode());
            } else {
                context.fail(res.cause());
            }
        });

        return future;
    }
}
