package com.gregorgott.mitmfoxserver.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.gregorgott.mitmfoxserver.MitmFoxServer;
import com.gregorgott.mitmfoxserver.ui.RequestAndResponse;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SaveLoadHandler {
    private final Gson gson;

    public static void showIoError(IOException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR, "IO Error");
        alert.setHeaderText("Error while saving/loading file.");
        alert.setContentText("File could not be saved/loaded. (IO Error): " + e.getMessage());
        alert.showAndWait();
    }

    public static void saveStringAsFile(String s) throws IOException {
        FileChooser fileChooser = createFileChooser();
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().setAll(
                new ExtensionFilter("HTML", "*.html"),
                new ExtensionFilter("Plain Text", "*.txt"),
                new ExtensionFilter("All Files", "*")
        );

        File file = fileChooser.showSaveDialog(MitmFoxServer.rootWindow);

        if (file != null) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write(s);
            }
        }
    }

    public static FileChooser createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MitmFox File", "*.mitmfox"),
                new FileChooser.ExtensionFilter("JSON", "*.json"),
                new FileChooser.ExtensionFilter("All Files", "*"));
        return fileChooser;
    }

    public SaveLoadHandler() {
        gson = new GsonBuilder()
                .registerTypeAdapter(RequestAndResponse.class, new InterceptedDataTypeAdapter())
                .create();
    }

    public void saveAs() throws IOException {
        File file = createFileChooser().showSaveDialog(MitmFoxServer.rootWindow);
        save(file);
    }

    public void save(File file) throws IOException {
        if (file == null) {
            return;
        }

        List<RequestAndResponse> requestAndResponseList = MitmFoxServer.mainWindowController.getTable().getDataListUnmodifiable();

        JsonArray interceptedDataJsonArray = new JsonArray();
        for (RequestAndResponse requestAndResponse : requestAndResponseList) {
            interceptedDataJsonArray.add(gson.toJsonTree(requestAndResponse));
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        gson.toJson(interceptedDataJsonArray, bw);
        bw.close();
    }

    public void load() throws IOException {
        File file = createFileChooser().showOpenDialog(MitmFoxServer.rootWindow);

        if (file == null) {
            return;
        }

        MitmFoxServer.mainWindowController.getTable().clearAllData();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            JsonArray jsonArray = gson.fromJson(br, JsonArray.class);
            MitmFoxServer.mainWindowController.getTable().addToTable(readJsonToList(jsonArray), true);
            MitmFoxServer.workspaceService.setCurrentFile(file);
        }
    }

    private List<RequestAndResponse> readJsonToList(JsonArray jsonArray) {
        List<RequestAndResponse> requestAndResponseList = new ArrayList<>();

        for (JsonElement jsonElement : jsonArray) {
            requestAndResponseList.add(
                    gson.fromJson(jsonElement, RequestAndResponse.class)
            );
        }
        return requestAndResponseList;
    }
}
