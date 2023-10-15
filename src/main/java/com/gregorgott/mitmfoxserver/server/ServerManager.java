package com.gregorgott.mitmfoxserver.server;

import com.gregorgott.mitmfoxserver.MitmFoxServer;
import com.gregorgott.mitmfoxserver.ui.i18n.I18N;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;

public class ServerManager {
    private Thread thread;
    private boolean running;
    private HttpServer httpServer;

    public void start(int port) {
        stop();
        thread = new Thread(
                () -> {
                    try {
                        Server server = new Server(port);
                        httpServer = server.getHttpServer();
                    } catch (IOException e) {
                        MitmFoxServer.mainWindowController.setStatusLabel(
                                I18N.getString("label.error.serverCannotStart")
                        );
                        throw new RuntimeException(e);
                    }
                }
        );
        thread.setDaemon(true);
        thread.start();
        running = true;
    }

    public void stop() {
        if (running) {
            httpServer.stop(1);
            thread.interrupt();
            running = false;
        }
    }
}
