package com.gregorgott.mitmfoxserver.ui.alerts;

import com.gregorgott.mitmfoxserver.ui.i18n.I18N;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.util.Optional;

public class SetPortAlert extends Alert {
    private final TextField textField;

    public SetPortAlert(Window root) {
        super(AlertType.CONFIRMATION, I18N.getString("alert.confirmation.setPortDialog.title"));
        this.setHeaderText(I18N.getString("alert.confirmation.setPortDialog.header"));

        textField = getPortNumTextField();

        this.initOwner(root);
        this.initModality(Modality.WINDOW_MODAL);
        this.getDialogPane().setContent(getCenterVBox());
        this.getButtonTypes().clear();
        this.getButtonTypes().add(ButtonType.APPLY);
    }

    public int showAndGetPort() {
        Optional<ButtonType> result = this.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.APPLY) {
            // TODO: 18.10.2023 handle NumberFormatException ?
            return Integer.parseInt(textField.getText());
        }

        return 0;
    }

    private VBox getCenterVBox() {
        VBox vBox = new VBox(
                getContentLabel(), textField
        );
        vBox.setSpacing(10);
        return vBox;
    }

    private TextField getPortNumTextField() {
        TextField tf = new TextField();
        tf.setPromptText(I18N.getString("textField.promptText.portNum"));
        tf.setText(String.valueOf(8060));
        return tf;
    }

    private Label getContentLabel() {
        Label label = new Label(I18N.getString("alert.confirmation.setPortDialog.content"));
        label.setWrapText(true);
        return label;
    }
}
