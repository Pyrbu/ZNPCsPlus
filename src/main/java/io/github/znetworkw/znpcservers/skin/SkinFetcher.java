package io.github.znetworkw.znpcservers.skin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SkinFetcher {
    private static final ExecutorService SKIN_EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    private final SkinFetcherBuilder builder;

    public SkinFetcher(SkinFetcherBuilder builder) {
        this.builder = builder;
    }

    public CompletableFuture<JsonObject> doReadSkin(SkinFetcherResult skinFetcherResult) {
        CompletableFuture<JsonObject> completableFuture = new CompletableFuture<>();
        SKIN_EXECUTOR_SERVICE.submit(() -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) (new URL(this.builder.getAPIServer().getURL() + getData())).openConnection();
                connection.setRequestMethod(this.builder.getAPIServer().getMethod());
                if (this.builder.isUrlType()) {
                    connection.setDoOutput(true);
                    try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                        outputStream.writeBytes("url=" + URLEncoder.encode(this.builder.getData(), StandardCharsets.UTF_8));
                    }
                }
                try (Reader reader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)) {
                    completableFuture.complete(JsonParser.parseReader(reader).getAsJsonObject());
                } finally {
                    connection.disconnect();
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                completableFuture.completeExceptionally(throwable);
            }
        });
        completableFuture.whenComplete((response, throwable) -> {
            if (completableFuture.isCompletedExceptionally()) {
                skinFetcherResult.onDone(null, null, throwable);
            } else {
                JsonObject jsonObject = response.getAsJsonObject(this.builder.getAPIServer().getValueKey());
                JsonObject properties = jsonObject.getAsJsonObject(this.builder.getAPIServer().getSignatureKey());
                skinFetcherResult.onDone(properties.get("value").getAsString(), properties.get("signature").getAsString(), null);
            }
        });
        return completableFuture;
    }

    private String getData() {
        return this.builder.isProfileType() ? ("/" + this.builder.getData()) : "";
    }
}
