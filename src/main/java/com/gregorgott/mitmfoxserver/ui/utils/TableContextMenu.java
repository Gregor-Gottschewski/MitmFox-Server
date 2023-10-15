package com.gregorgott.mitmfoxserver.ui.utils;

import com.gregorgott.mitmfoxserver.ui.RequestAndResponse;
import com.gregorgott.mitmfoxserver.ui.i18n.I18N;
import com.gregorgott.mitmfoxserver.ui.nodes.DataTable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.util.List;

public class TableContextMenu extends ContextMenu {
    private final DataTable dataTable;

    public TableContextMenu(DataTable dataTable) {
        this.dataTable = dataTable;
        this.getItems().add(getDeleteMenuItem());
        this.getItems().add(getCopyMenuItem());
        this.setOnShowing(event -> {
            disableAll(dataTable.getItems().isEmpty());
        });
    }

    private MenuItem getDeleteMenuItem() {
        MenuItem menuItem = new MenuItem(I18N.getString("menuItem.contextMenu.delete"));
        menuItem.setOnAction(actionEvent -> {
            RequestAndResponse id = dataTable.getSelectionModel().getSelectedItem();
            if (id != null) {
                dataTable.remove(id);
            }
        });
        return menuItem;
    }

    private MenuItem getCopyMenuItem() {
        MenuItem menuItem = new MenuItem(I18N.getString("menuItem.contextMenu.copy"));
        menuItem.setOnAction(actionEvent -> {
            RequestAndResponse id = dataTable.getSelectionModel().getSelectedItem();
            if (id != null) {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(id.toString());
                clipboard.setContent(content);
            }
        });
        return menuItem;
    }

    private void disableAll(boolean b) {
        List<MenuItem> menuItemList = this.getItems();
        for (MenuItem mi : menuItemList) {
            mi.setDisable(b);
        }
    }
}
