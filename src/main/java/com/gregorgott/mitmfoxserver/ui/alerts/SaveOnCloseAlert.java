package com.gregorgott.mitmfoxserver.ui.alerts;

import com.gregorgott.mitmfoxserver.MitmFoxServer;
import com.gregorgott.mitmfoxserver.ui.i18n.I18N;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.util.List;
import java.util.Objects;

public class SaveOnCloseAlert extends Alert {
    private static final String SAVE = I18N.getString("save");

    public SaveOnCloseAlert(Window owner) {
        super(AlertType.CONFIRMATION, SAVE);
        this.initOwner(owner);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setHeaderText(I18N.getString("alert.confirmation.closeDialog.header"));
        this.setContentText(I18N.getString("alert.confirmation.closeDialog.content"));
        this.getButtonTypes().setAll(getButtonTypesList());
        this.showAndWait().ifPresent(response -> {
            if (Objects.equals(response.getText(), SAVE)) {
                MitmFoxServer.mainWindowController.save();
            }
            this.close();
        });
    }

    private List<ButtonType> getButtonTypesList() {
        return List.of(
                ButtonType.CANCEL,
                new ButtonType(SAVE)
        );
    }
}
