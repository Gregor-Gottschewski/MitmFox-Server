package com.gregorgott.mitmfoxserver.io;

import com.gregorgott.mitmfoxserver.MitmFoxServer;
import javafx.stage.Stage;

import java.io.File;

public class WorkspaceService {
    private File currentFile;
    private boolean fileOpen;

    public WorkspaceService() {
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public boolean isFileOpen() {
        return fileOpen;
    }

    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
        fileOpen = currentFile != null;
        if (fileOpen) {
            ((Stage) MitmFoxServer.rootWindow).setTitle("MitmFox Server - " + currentFile.getName());
        }
    }
}
