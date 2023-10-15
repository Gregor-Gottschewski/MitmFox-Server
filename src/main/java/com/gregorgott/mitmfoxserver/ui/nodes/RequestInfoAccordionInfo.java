package com.gregorgott.mitmfoxserver.ui.nodes;

import com.gregorgott.mitmfoxserver.MitmFoxServer;
import com.gregorgott.mitmfoxserver.ui.i18n.I18N;
import javafx.scene.control.Button;

public class RequestInfoAccordionInfo extends InfoTitledPanesAccordion {
    private final InfoTitledPane urlPane;
    private final InfoTitledPane headerPane;
    private final InfoTitledPane bodyPane;

    private static InfoTitledPane createUrlInfoTitledPane() {
        InfoTitledPane infoTitledPane = new InfoTitledPane(I18N.getString("url"));

        Button openInBrowserButton = new Button(I18N.getString("button.openInBrowser"));
        openInBrowserButton.setOnAction(actionEvent -> {
            MitmFoxServer.hostServices.showDocument(infoTitledPane.getCodeArea().getText());
        });

        infoTitledPane.getTopHBox().getChildren().add(openInBrowserButton);

        return infoTitledPane;
    }

    public RequestInfoAccordionInfo() {
        urlPane = createUrlInfoTitledPane();
        headerPane = new InfoTitledPane(I18N.getString("header"));
        bodyPane = createBodyInfoTitledPane();

        this.getPanes().addAll( urlPane, headerPane, bodyPane );
    }

    public InfoTitledPane getUrlPane() {
        return urlPane;
    }

    public InfoTitledPane getHeaderPane() {
        return headerPane;
    }

    public InfoTitledPane getBodyPane() {
        return bodyPane;
    }
}
