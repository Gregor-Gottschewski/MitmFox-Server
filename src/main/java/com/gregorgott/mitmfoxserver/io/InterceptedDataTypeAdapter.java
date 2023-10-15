package com.gregorgott.mitmfoxserver.io;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.gregorgott.mitmfoxserver.ui.RequestAndResponse;

import java.io.IOException;
import java.time.LocalDateTime;

public class InterceptedDataTypeAdapter extends TypeAdapter<RequestAndResponse> {

    @Override
    public void write(JsonWriter out, RequestAndResponse value) throws IOException {
        out.beginObject();

        out.name("ip").value(value.getIp());
        out.name("method").value(value.getMethod());
        out.name("requestHeader").value(value.getRequestHeader());
        out.name("requestBody").value(value.getRequestBody());
        out.name("url").value(value.getUrl());
        out.name("responseHeader").value(value.getResponseHeader());
        out.name("responseBody").value(value.getResponseBody());
        out.name("responseStatusCode").value(value.getResponseStatusCode());
        out.name("time").value(value.getTime());

        out.endObject();
    }

    @Override
    public RequestAndResponse read(JsonReader in) throws IOException {
        String ip = "";
        String method = "";
        String requestHeader = "";
        String requestBody = "";
        String url = "";
        String responseHeader = "";
        String responseBody = "";
        int responseStatusCode = 0;
        String time = "";

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "ip" -> ip = in.nextString();
                case "method" -> method = in.nextString();
                case "requestHeader" -> requestHeader = in.nextString();
                case "requestBody" -> requestBody = in.nextString();
                case "url" -> url = in.nextString();
                case "responseHeader" -> responseHeader = in.nextString();
                case "responseBody" -> responseBody = in.nextString();
                case "responseStatusCode" -> responseStatusCode = in.nextInt();
                case "time" -> time = in.nextString();
            }
        }
        in.endObject();

        return new RequestAndResponse.RequestAndResponseBuilder()
                .setUrl(url)
                .setIp(ip)
                .setMethod(method)
                .setRequestBody(requestBody)
                .setRequestHeader(requestHeader)
                .setTime(LocalDateTime.parse(time, RequestAndResponse.getI18NDateTimeFormatter()))
                .setResponseHeader(responseHeader)
                .setResponseBody(responseBody)
                .setResponseStatusCode(responseStatusCode)
                .build();
    }
}
