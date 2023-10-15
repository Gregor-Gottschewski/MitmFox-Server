package com.gregorgott.mitmfoxserver.ui.nodes;

import com.gregorgott.mitmfoxserver.ui.i18n.I18N;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;

public class InfoTitledPane extends TitledPane {
    private final HBox topHBox;
    private final CodeArea textArea;

    public InfoTitledPane(String title) {
        setText(title);

        Button copyButton = new Button(I18N.getString("copy"));
        copyButton.setOnAction(actionEvent -> onCopyButton());

        topHBox = new HBox(
                copyButton
        );
        topHBox.setSpacing(5);

        textArea = new CodeArea();
        textArea.setEditable(false);
        VirtualizedScrollPane<CodeArea> sp = new VirtualizedScrollPane<>(textArea);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(
                topHBox, sp
        );
        vBox.setSpacing(5);
        VBox.setVgrow(sp, Priority.ALWAYS);

        setContent(vBox);
    }

    public HBox getTopHBox() {
        return topHBox;
    }

    public CodeArea getCodeArea() {
        return textArea;
    }

    private void onCopyButton() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(textArea.getText());
        clipboard.setContent(content);
    }
}
