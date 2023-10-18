package com.gregorgott.mitmfoxserver.ui;

import com.gregorgott.mitmfoxserver.MitmFoxServer;
import com.gregorgott.mitmfoxserver.io.SaveLoadHandler;
import com.gregorgott.mitmfoxserver.ui.alerts.SetPortAlert;
import com.gregorgott.mitmfoxserver.ui.i18n.I18N;
import com.gregorgott.mitmfoxserver.ui.nodes.DataTable;
import com.gregorgott.mitmfoxserver.ui.nodes.RequestInfoAccordionInfo;
import com.gregorgott.mitmfoxserver.ui.nodes.ResponseInfoAccordion;
import com.gregorgott.mitmfoxserver.ui.utils.SearchHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class MainWindowController {
    @FXML
    private Label statusLabel;
    @FXML
    private Label entriesLabel;
    @FXML
    private TextField searchTextField;
    @FXML
    private MenuItem saveMenuItem;
    @FXML
    private ScrollPane tableScrollPane;
    @FXML
    private VBox requestInfoVBox;
    @FXML
    private VBox responseInfoVBox;

    private DataTable table;
    private final SaveLoadHandler saveLoadHandler;

    public MainWindowController() {
        saveLoadHandler = new SaveLoadHandler();
    }

    public DataTable getTable() {
        return table;
    }

    @FXML
    private void onSaveButton() {
        save();
    }

    public void save() {
        if (!MitmFoxServer.workspaceService.isFileOpen()) {
            onSaveAsButton();
        }

        try {
            saveLoadHandler.save(MitmFoxServer.workspaceService.getCurrentFile());
        } catch (IOException e) {
            SaveLoadHandler.showIoError(e);
        }
    }

    @FXML
    private void onSaveAsButton() {
        try {
            saveLoadHandler.saveAs();
            saveMenuItem.setDisable(false);
        } catch (IOException e) {
            SaveLoadHandler.showIoError(e);
        }
    }

    @FXML
    private void onOpenButton() {
        try {
            saveLoadHandler.load();
            saveMenuItem.setDisable(false);
        } catch (IOException e) {
            SaveLoadHandler.showIoError(e);
        }
    }

    @FXML
    private void onDeleteDataButton() {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION, I18N.getString("alert.confirmation.title")
        );
        alert.setHeaderText(I18N.getString("alert.confirmation.deleteEntries.header"));
        alert.setContentText(I18N.getString("alert.confirmation.deleteEntries.content"));
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> getTable().clearAllData());
    }

    @FXML
    private void onSearchButton() {
        String searchQuery = searchTextField.getText();

        if (searchQuery.isEmpty()) {
            if (table.isTableFiltered()) {
                table.setTableFiltered(false);
                table.refillTable();
            }
        } else {
            SearchHandler searchHandler = new SearchHandler(
                    searchQuery, getTable().getDataListUnmodifiable()
            );
            getTable().getItems().setAll(searchHandler.getFilteredData());
            table.setTableFiltered(true);
        }
    }

    @FXML
    private void startThread() {
        int port = new SetPortAlert(MitmFoxServer.rootWindow).showAndGetPort();

        if (port != 0) {
            MitmFoxServer.serverManager.start(port);
            setStatusLabel(I18N.getString("label.running"));
        }
    }

    @FXML
    private void stopThread() {
        MitmFoxServer.serverManager.stop();
        setStatusLabel(I18N.getString("label.stopped"));
    }

    @FXML
    private void quit() {
        Stage stage = (Stage) MitmFoxServer.rootWindow;
        stage.fireEvent(
                new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST)
        );
    }

    public void setStatusLabel(String text) {
        Platform.runLater(() -> statusLabel.setText(I18N.getString("label.serverStatus", text)));
    }

    @FXML
    public void initialize() {
        RequestInfoAccordionInfo requestInfoAccordion = new RequestInfoAccordionInfo();
        ResponseInfoAccordion responseInfoAccordion = new ResponseInfoAccordion();

        table = new DataTable(requestInfoAccordion, responseInfoAccordion);
        table.tableSizeIntegerPropertyProperty().addListener(
                (value, old, newNum) -> Platform.runLater(
                        () -> entriesLabel.setText(newNum.intValue() + " " + I18N.getString("label.entries"))
                )
        );

        setStatusLabel(I18N.getString("label.stopped"));

        requestInfoVBox.getChildren().add(requestInfoAccordion);
        responseInfoVBox.getChildren().add(responseInfoAccordion);
        tableScrollPane.setContent(table);
    }
}
