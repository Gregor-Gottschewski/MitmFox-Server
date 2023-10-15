package com.gregorgott.mitmfoxserver.ui.nodes;

import com.gregorgott.mitmfoxserver.io.SaveLoadHandler;
import com.gregorgott.mitmfoxserver.ui.i18n.I18N;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;

import java.io.IOException;

public class InfoTitledPanesAccordion extends Accordion {
    public static InfoTitledPane createBodyInfoTitledPane() {
        InfoTitledPane infoTitledPane = new InfoTitledPane(I18N.getString("body"));

        Button saveBodyButton = new Button(I18N.getString("saveBody"));
        saveBodyButton.setOnAction(actionEvent -> {
            try {
                SaveLoadHandler.saveStringAsFile(infoTitledPane.getCodeArea().getText());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        infoTitledPane.getTopHBox().getChildren().add(saveBodyButton);

        return infoTitledPane;
    }
}
