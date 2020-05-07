package com.embed.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class HttpServerVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServerVerticle.class);
    public static final String CONFIG_HTTP_SERVER_PORT = "http.server.port";
    private ApiHandler requestHandler;

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        requestHandler = new ApiHandler(vertx);

        int portNumber = config().getInteger(CONFIG_HTTP_SERVER_PORT, 9933);
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        Router apiRouter = Router.router(vertx);

        router.get("/").handler(context -> {
            context.response().putHeader("Content-Type", "application/json");
            context.response().end(new JsonObject().put("message", "Welcome to Vert.x").encode());
        });

        apiRouter.post().handler(BodyHandler.create());
        apiRouter.post("/get_balance").handler(requestHandler::getAccountbalance);
        apiRouter.post("/get_min_balance").handler(requestHandler::getMinimumAccountbalance);
        apiRouter.post("/post_batch").handler(requestHandler::postBatchTransactions);
        apiRouter.post("/get_status").handler(requestHandler::getTransactionStatus);
        apiRouter.post("/funds_hold").handler(requestHandler::postHoldFund);
        apiRouter.post("/funds_unhold").handler(requestHandler::postUnHoldFund);

        router.mountSubRouter("/api", apiRouter);
        server.requestHandler(router::accept).listen(portNumber, ar -> {
            if (ar.succeeded()) {
                LOGGER.info("HTTP server running on port :" + portNumber);
                startFuture.complete();
            } else {
                LOGGER.error("Could not start a HTTP server ", ar.cause());
                startFuture.fail(ar.cause());
            }
        });
    }
}
