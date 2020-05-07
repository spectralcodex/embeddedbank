package com.embed.api;

import com.embed.util.HashUtil;
import com.embed.util.PropertiesCache;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

public class Client {
    private static final WebClientOptions OPTIONS = new WebClientOptions().setSsl(true).setTrustAll(true).setVerifyHost(false);
    /*private static final WebClientOptions OPTIONS = new WebClientOptions().setSsl(true).setTrustOptions(
            new JksOptions().setPath("client1.jks").setPassword("client")
    );*/

    private static WebClient webClient;
    private static final String BASE_URI = PropertiesCache.getInstance().getProperty("api.url");
    private static final String SECRET = PropertiesCache.getInstance().getProperty("api.secret");
    private static final String CLIENT_ID = PropertiesCache.getInstance().getProperty("api.client.id");
    private static final String SRC_CODE = PropertiesCache.getInstance().getProperty("api.src.code");
    private static final String PSSWD = PropertiesCache.getInstance().getProperty("api.psswd");
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    public Client(Vertx vertx) {
        if (webClient == null)
            webClient = WebClient.create(vertx, OPTIONS);
    }

    private static final void setRequestHeaders(HttpRequest<Buffer> httpRequest, String requestId) {
        httpRequest.headers().set("Content-Type", "application/json")
                .set("x-client-id", CLIENT_ID).set("x-client-secret", SECRET).set("x-request-id", requestId)
                .set("x-request-token", HashUtil.makeToken(requestId, SRC_CODE, PSSWD));
    }


    public void fetchAccountBalance(JsonObject json, Handler<AsyncResult<JsonObject>> handler) {
        HttpRequest<Buffer> httpRequest = webClient.postAbs(BASE_URI + "account/balance");
        setRequestHeaders(httpRequest, json.getString("requestId"));
        json.remove("requestId");
        LOGGER.info(json);
        httpRequest.sendJsonObject(json, asyncResult(handler));
    }

    public void fetchMinimumAccountBalance(JsonObject json, Handler<AsyncResult<JsonObject>> handler){
        HttpRequest<Buffer> httpRequest = webClient.postAbs(BASE_URI + "account/minimumbalance");
        setRequestHeaders(httpRequest, json.getString("requestId"));
        json.remove("requestId");
        LOGGER.info(json);
        httpRequest.sendJsonObject(json, asyncResult(handler));
    }

    public void holdFund(JsonObject json, Handler<AsyncResult<JsonObject>> handler) {
        HttpRequest<Buffer> httpRequest = webClient.postAbs(BASE_URI + "account/funds/hold");
        setRequestHeaders(httpRequest, json.getString("requestId"));
        json.remove("requestId");
        LOGGER.info(json);
        httpRequest.sendJsonObject(json, asyncResult(handler));
    }

    public void unHoldFund(JsonObject json, Handler<AsyncResult<JsonObject>> handler) {
        HttpRequest<Buffer> httpRequest = webClient.postAbs(BASE_URI + "account/funds/unhold");
        setRequestHeaders(httpRequest, json.getString("requestId"));
        json.remove("requestId");
        LOGGER.info(json);
        httpRequest.sendJsonObject(json, asyncResult(handler));
    }

    public void addBatchTransactions(JsonObject json, Handler<AsyncResult<JsonObject>> handler) {
        HttpRequest<Buffer> httpRequest = webClient.postAbs(BASE_URI + "transaction/batchposting");
        setRequestHeaders(httpRequest, json.getString("requestId"));
        json.remove("requestId");
        LOGGER.info(json);
        httpRequest.sendJsonObject(json, asyncResult(handler));
    }

    public void fetchTransactionStatus(JsonObject json, Handler<AsyncResult<JsonObject>> handler) {
        HttpRequest<Buffer> httpRequest = webClient.postAbs(BASE_URI + "transaction/status");
        setRequestHeaders(httpRequest, json.getString("requestId"));
        json.remove("requestId");
        LOGGER.info(json);
        httpRequest.sendJsonObject(json, asyncResult(handler));
    }



    private final static Handler<AsyncResult<HttpResponse<Buffer>>> asyncResult(Handler<AsyncResult<JsonObject>> handler) {
        return ar -> {
            if (ar.succeeded()) {
                // Obtain response
                HttpResponse<Buffer> response = ar.result();

                LOGGER.info("RESPONSE BODY:::::::::::::::::::" + response.bodyAsString());
                int statusCode = response.statusCode();
                if (statusCode == 200 | statusCode == 201) {
                    handler.handle(Future.succeededFuture(response.bodyAsJsonObject()));
                } else {
                    LOGGER.info("STATUS::::::::::::::: " + response.statusMessage());
                    handler.handle(Future.failedFuture(response.bodyAsString()));
                }
            } else {
                handler.handle(Future.failedFuture(ar.cause().getMessage()));
            }
        };
    }

}
