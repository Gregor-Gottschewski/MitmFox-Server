package com.gregorgott.mitmfoxserver;

import com.gregorgott.mitmfoxserver.io.WorkspaceService;
import com.gregorgott.mitmfoxserver.server.ServerManager;
import com.gregorgott.mitmfoxserver.ui.MainWindowController;
import com.gregorgott.mitmfoxserver.ui.alerts.SaveOnCloseAlert;
import com.gregorgott.mitmfoxserver.ui.i18n.I18N;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class MitmFoxServer extends Application {
    public static MainWindowController mainWindowController;
    public static ServerManager serverManager;
    public static HostServices hostServices;
    public static Window rootWindow;
    public static WorkspaceService workspaceService;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MitmFoxServer.class.getResource("mitmFoxMainView.fxml"), I18N.getBundle());
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("MitmFox Server");
        stage.setScene(scene);
        stage.setMinHeight(550);
        stage.setMinWidth(850);
        stage.setOnCloseRequest(x -> new SaveOnCloseAlert(stage));
        stage.show();

        mainWindowController = fxmlLoader.getController();
        serverManager = new ServerManager();
        hostServices = getHostServices();
        rootWindow = stage;
        workspaceService = new WorkspaceService();
    }

    public static void main(String[] args) {
        launch();
    }
}