package com.gregorgott.mitmfoxserver.server;

import com.gregorgott.mitmfoxserver.MitmFoxServer;
import com.gregorgott.mitmfoxserver.ui.i18n.I18N;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ServerManager {
    private Thread thread;
    private HttpServer httpServer;

    public void start(int port) {
        stop();
        thread = new Thread(() -> {
            try {
                httpServer = createServer(port);
            } catch (IOException e) {
                MitmFoxServer.mainWindowController.setStatusLabel(I18N.getString("label.error.serverCannotStart"));
                throw new RuntimeException(e);
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void stop() {
        if (thread == null) return;

        httpServer.stop(1);
        thread.interrupt();
        thread = null;
    }

    private HttpServer createServer(int portNumber) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(portNumber), 0);
        httpServer.createContext("/", new RequestHandler());
        httpServer.setExecutor(null);
        httpServer.start();
        return httpServer;
    }
}
