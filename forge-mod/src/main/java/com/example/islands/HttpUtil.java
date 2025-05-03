package com.example.islands;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpUtil {

    public static void sendCreateIsland(String ownerUuid) {
        try {
            String fullUrl = Config.API_URL.get() + "/create";
            URL url = new URL(fullUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);

            String json = String.format("{\"owner_uuid\":\"%s\"}", ownerUuid);

            IslandsMod.LOGGER.info("Sending POST to " + fullUrl);
            IslandsMod.LOGGER.info("Request body: " + json);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            IslandsMod.LOGGER.info("Response code: " + responseCode);
        } catch (Exception e) {
            IslandsMod.LOGGER.error("HTTP request failed: ", e);
        }
    }

    public static void sendUpdateActivity(String ownerUuid) {
        try {
            String fullUrl = Config.API_URL.get() + "/update_activity";
            URL url = new URL(fullUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);

            String json = String.format("{\"owner_uuid\":\"%s\"}", ownerUuid);

            IslandsMod.LOGGER.info("Sending activity update POST to " + fullUrl);
            IslandsMod.LOGGER.info("Request body: " + json);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            IslandsMod.LOGGER.info("Activity update response code: " + responseCode);
        } catch (Exception e) {
            IslandsMod.LOGGER.error("Activity update HTTP request failed: ", e);
        }
    }
}
