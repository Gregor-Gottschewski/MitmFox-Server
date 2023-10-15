module com.gregorgott.mitmfoxclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires jdk.httpserver;
    requires com.google.gson;
    requires org.fxmisc.richtext;
    requires org.fxmisc.flowless;
    requires reactfx;

    exports com.gregorgott.mitmfoxserver;
    opens com.gregorgott.mitmfoxserver to javafx.fxml;

    exports com.gregorgott.mitmfoxserver.server;
    opens com.gregorgott.mitmfoxserver.server to javafx.fxml;

    exports com.gregorgott.mitmfoxserver.ui;
    opens com.gregorgott.mitmfoxserver.ui to javafx.fxml;

    exports com.gregorgott.mitmfoxserver.ui.utils;
    opens com.gregorgott.mitmfoxserver.ui.utils to javafx.fxml;

    exports com.gregorgott.mitmfoxserver.ui.nodes;
    opens com.gregorgott.mitmfoxserver.ui.nodes to javafx.fxml;

    exports com.gregorgott.mitmfoxserver.io;
}