package com.gregorgott.mitmfoxserver.server.methodHandlers;

import com.google.gson.Gson;
import com.gregorgott.mitmfoxserver.MitmFoxServer;
import com.gregorgott.mitmfoxserver.ui.RequestAndResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

public class PostMethod extends Method {
    public PostMethod(String ip, InputStream inputStream) throws IOException {
        JsonObject bodyAsJsonObject = new Gson().fromJson(
                readRequestBody(inputStream), JsonObject.class
        );

        MitmFoxServer.mainWindowController.getTable().addToTable(
                List.of(jsonObjectToDataModel(ip, bodyAsJsonObject)),
                true
        );

        setResponse("success");
    }

    private String readRequestBody(InputStream inputStream) throws IOException {
        try (
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8)
                )
        ) {
            return br
                    .lines()
                    .collect(Collectors.joining("\n"));
        }
    }

    private RequestAndResponse jsonObjectToDataModel(String ip, JsonObject jsonObject) {
        String requestBody = removeEscapeChars(jsonObject.get("request_body").getAsString());
        String responseBody = removeEscapeChars(jsonObject.get("response_body").getAsString());

        return new RequestAndResponse.RequestAndResponseBuilder()
                .setIp(ip)
                .setMethod(jsonObject.get("method").getAsString())
                .setRequestHeader(jsonObject.get("request_headers").getAsString())
                .setRequestBody(requestBody)
                .setUrl(jsonObject.get("url").getAsString())
                .setResponseHeader(jsonObject.get("response_headers").getAsString())
                .setResponseBody(responseBody)
                .setResponseStatusCode(jsonObject.get("status_code").getAsInt())
                .setTime(LocalDateTime.now())
                .build();
    }

    private String removeEscapeChars(String s) {
        return s
                .replaceAll("\\n", "")
                .replaceAll("\\r", "");
    }
}
