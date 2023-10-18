package com.gregorgott.mitmfoxserver.io;

import com.gregorgott.mitmfoxserver.MitmFoxServer;
import javafx.stage.Stage;

import java.io.File;

public class WorkspaceService {
    private File currentFile;

    public WorkspaceService() {
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public boolean isFileOpen() {
        return currentFile != null;
    }

    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
        if (isFileOpen()) {
            ((Stage) MitmFoxServer.rootWindow).setTitle("MitmFox Server - " + currentFile.getName());
        }
    }
}
