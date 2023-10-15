package com.gregorgott.mitmfoxserver.ui.nodes;

import com.gregorgott.mitmfoxserver.ui.i18n.I18N;

public class ResponseInfoAccordion extends InfoTitledPanesAccordion {
    private final InfoTitledPane headerTitledPane;
    private final InfoTitledPane bodyTitledPane;

    public ResponseInfoAccordion() {
        headerTitledPane = new InfoTitledPane(I18N.getString("header"));
        bodyTitledPane = createBodyInfoTitledPane();

        this.getPanes().addAll( headerTitledPane, bodyTitledPane );
    }

    public InfoTitledPane getHeaderTitledPane() {
        return headerTitledPane;
    }

    public InfoTitledPane getBodyTitledPane() {
        return bodyTitledPane;
    }
}
