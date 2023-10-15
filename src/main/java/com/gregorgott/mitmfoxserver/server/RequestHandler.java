package com.gregorgott.mitmfoxserver.server;

import com.gregorgott.mitmfoxserver.server.methodHandlers.Method;
import com.gregorgott.mitmfoxserver.server.methodHandlers.PostMethod;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class RequestHandler implements HttpHandler {
    private static final String ERROR_HTML_SITE = "http/error.html";
    private static final String POST = "POST";
    private static final String GET = "GET";

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();

        Method method = switch (requestMethod) {
            case POST -> processPost(httpExchange);
            case GET -> processGet();
            default -> processUnknown();
        };

        sendResponse(method, httpExchange);
    }

    private Method processPost(HttpExchange httpExchange) throws IOException {
        return new PostMethod(
                httpExchange.getRemoteAddress().getAddress().getHostAddress(),
                httpExchange.getRequestBody()
        );
    }

    private Method processGet() throws IOException {
        Method method = new Method();
        method.setStatus(405);
        method.setResponse(
                getErrorPage("405", "GET not allowed: only POST requests are allowed.")
        );
        return method;
    }

    private Method processUnknown() throws IOException {
        Method method = new Method();
        method.setStatus(501);
        method.setResponse(
                getErrorPage("501", "Unknown request method: Only POST requests are allowed.")
        );
        return method;
    }

    private void sendResponse(Method method, HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(
                method.getStatus(),
                method.getResponseLength()
        );

        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(method.getResponse().getBytes());
        }
    }

    private String getErrorPage(String code, String message) throws IOException {
        HtmlReader htmlReader = new HtmlReader(ERROR_HTML_SITE);
        htmlReader.addReplacement(0, code);
        htmlReader.addReplacement(1, message);
        return htmlReader.read();
    }
}
