package com.gregorgott.mitmfoxserver.server;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    private final HttpServer httpServer;

    public Server(int portNumber) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(portNumber), 0);
        httpServer.createContext("/", new RequestHandler());
        httpServer.setExecutor(null);
        httpServer.start();
    }

    public HttpServer getHttpServer() {
        return httpServer;
    }
}
