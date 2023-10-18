package com.gregorgott.mitmfoxserver.ui;

import com.gregorgott.mitmfoxserver.ui.i18n.I18N;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RequestAndResponse {
    private static final int SHORTENED_LENGTH = 20;
    private final SimpleStringProperty ip;
    private final SimpleStringProperty method;
    private final SimpleStringProperty requestHeader;
    private final SimpleStringProperty requestBody;
    private final SimpleStringProperty url;
    private final SimpleStringProperty responseHeader;
    private final SimpleStringProperty responseBody;
    private final SimpleIntegerProperty responseStatusCode;
    private final SimpleObjectProperty<LocalDateTime> time;

    private static String shortString(String s) {
        if (s.length() >= SHORTENED_LENGTH) {
            return s.substring(0, SHORTENED_LENGTH) + "...";
        } else {
            return s;
        }
    }

    public static DateTimeFormatter getI18NDateTimeFormatter() {
        return DateTimeFormatter.ofPattern(
                I18N.getString("format.datetime")
        );
    }

    public RequestAndResponse(String ip, String method, String requestHeader, String requestBody, String url,
                              String responseHeader, String responseBody, int responseStatusCode, LocalDateTime time) {
        this.ip = new SimpleStringProperty(ip);
        this.url = new SimpleStringProperty(url);
        this.method = new SimpleStringProperty(method);

        this.requestHeader = new SimpleStringProperty(requestHeader);
        this.requestBody = new SimpleStringProperty(requestBody);

        this.responseHeader = new SimpleStringProperty(responseHeader);
        this.responseBody = new SimpleStringProperty(responseBody);
        this.responseStatusCode = new SimpleIntegerProperty(responseStatusCode);

        this.time = new SimpleObjectProperty<>(time);
    }

    public String getIp() {
        return ip.get();
    }

    public String getMethod() {
        return method.get();
    }

    public String getRequestHeader() {
        return requestHeader.get();
    }

    public String getRequestBody() {
        return requestBody.get();
    }

    public String getRequestBodyShortened() {
        return shortString(getRequestBody());
    }

    public String getUrl() {
        return url.get();
    }

    public String getUrlShortened() {
        return shortString(getUrl());
    }

    public String getResponseHeader() {
        return responseHeader.get();
    }

    public String getResponseBody() {
        return responseBody.get();
    }

    public String getResponseBodyShortened() {
        return shortString(getResponseBody());
    }

    public int getResponseStatusCode() {
        return responseStatusCode.get();
    }

    public String getTime() {
        return time.get().format(getI18NDateTimeFormatter());
    }

    @Override
    public String toString() {
        return String.join(", ",
                "IP: " + getIp(),
                "Method: " + getMethod(),
                "Header: " + getRequestHeader(),
                "Body: " + getRequestBody(),
                "URL: " + getUrl(),
                "Type: " + getResponseHeader(),
                "Time: " + getTime());
    }

    public static class RequestAndResponseBuilder {
        private String ip;
        private String method;
        private String requestHeader;
        private String requestBody;
        private String url;
        private String responseHeader;
        private String responseBody;
        private int responseStatusCode;
        private LocalDateTime time;

        public RequestAndResponseBuilder setIp(String ip) {
            this.ip = ip;
            return this;
        }

        public RequestAndResponseBuilder setMethod(String method) {
            this.method = method;
            return this;
        }

        public RequestAndResponseBuilder setRequestHeader(String requestHeader) {
            this.requestHeader = requestHeader;
            return this;
        }

        public RequestAndResponseBuilder setRequestBody(String requestBody) {
            this.requestBody = requestBody;
            return this;
        }

        public RequestAndResponseBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public RequestAndResponseBuilder setResponseHeader(String responseHeader) {
            this.responseHeader = responseHeader;
            return this;
        }

        public RequestAndResponseBuilder setResponseBody(String responseBody) {
            this.responseBody = responseBody;
            return this;
        }

        public RequestAndResponseBuilder setResponseStatusCode(int responseStatusCode) {
            this.responseStatusCode = responseStatusCode;
            return this;
        }

        public RequestAndResponseBuilder setTime(LocalDateTime time) {
            this.time = time;
            return this;
        }

        public RequestAndResponse build() {
            return new RequestAndResponse(
                    ip,
                    method,
                    requestHeader,
                    requestBody,
                    url,
                    responseHeader,
                    responseBody,
                    responseStatusCode,
                    time);
        }
    }
}
