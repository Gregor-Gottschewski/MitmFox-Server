package com.gregorgott.mitmfoxserver.ui.nodes;

import com.gregorgott.mitmfoxserver.ui.RequestAndResponse;
import com.gregorgott.mitmfoxserver.ui.i18n.I18N;
import com.gregorgott.mitmfoxserver.ui.utils.SelectionModelListenerUpdateService;
import com.gregorgott.mitmfoxserver.ui.utils.TableContextMenu;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataTable extends TableView<RequestAndResponse> {
    private final RequestInfoAccordionInfo requestAccordion;
    private final ResponseInfoAccordion responseAccordion;
    private final TableContextMenu tableContextMenu;
    private final List<RequestAndResponse> dataList;
    private final SimpleIntegerProperty tableSizeIntegerProperty;
    private boolean tableFiltered;

    private static TableColumn<RequestAndResponse, String> createTableColumn(String header, String property) {
        String i18nHeader = I18N.getString("dataTable.column." + header);

        TableColumn<RequestAndResponse, String> tableColumn = new TableColumn<>(i18nHeader);
        tableColumn.setCellValueFactory(
                new PropertyValueFactory<>(property)
        );
        return tableColumn;
    }

    private static List<TableColumn<RequestAndResponse, String>> getTableColumns() {
        return List.of(
                createTableColumn("time", "time"),
                createTableColumn("ip", "ip"),
                createTableColumn("url", "urlShortened"),
                createTableColumn("method", "method"),
                createTableColumn("requestBody", "requestBodyShortened"),
                createTableColumn("statusCode", "responseStatusCode"),
                createTableColumn("responseBody", "responseBodyShortened")
        );
    }

    public DataTable(
            RequestInfoAccordionInfo requestAccordion,
            ResponseInfoAccordion responseAccordion
    ) {
        this.requestAccordion = requestAccordion;
        this.responseAccordion = responseAccordion;

        this.dataList = new ArrayList<>();
        this.tableContextMenu = new TableContextMenu(this);
        this.tableFiltered = false;
        this.tableSizeIntegerProperty = new SimpleIntegerProperty(0);

        this.getColumns().addAll(getTableColumns());

        this.addEventHandler(MouseEvent.MOUSE_CLICKED, showContextMenuOnRightClick());
        this.getSelectionModel()
                .selectedItemProperty()
                .addListener(addSelectionModelListener());
    }

    public List<RequestAndResponse> getDataListUnmodifiable() {
        return Collections.unmodifiableList(dataList);
    }

    public SimpleIntegerProperty tableSizeIntegerPropertyProperty() {
        return tableSizeIntegerProperty;
    }

    private EventHandler<MouseEvent> showContextMenuOnRightClick() {
        return mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                tableContextMenu.show(this, mouseEvent.getScreenX(), mouseEvent.getScreenY());
            } else {
                hideContextMenu();
            }
        };
    }

    private void hideContextMenu() {
        if (tableContextMenu.isShowing()) {
            tableContextMenu.hide();
        }
    }

    private ChangeListener<RequestAndResponse> addSelectionModelListener() {
        return (observableValue, old, newVal) -> {
            if (newVal != null) {
                new SelectionModelListenerUpdateService(
                        requestAccordion,
                        responseAccordion,
                        newVal
                ).startThreads();
            }
        };
    }

    public void addToTable(List<RequestAndResponse> list, boolean newData) {
        if (newData) {
            addToDataList(list);
        }

        if (!isTableFiltered()) {
            tableSizeIntegerProperty.set(dataList.size());

            List<RequestAndResponse> tempList = new ArrayList<>(list);

            Thread t = new Thread(() -> {
                Platform.runLater(() -> this.getItems().addAll(tempList));
            });
            t.start();
        }
    }

    private void addToDataList(List<RequestAndResponse> list) {
        dataList.addAll(list);
    }

    public void clearAllData() {
        this.getSelectionModel().clearSelection();
        this.getItems().clear();
        dataList.clear();
        tableSizeIntegerProperty.set(0);
    }

    public void remove(RequestAndResponse requestAndResponse) {
        this.getSelectionModel().clearSelection();
        this.getItems().remove(requestAndResponse);
        dataList.remove(requestAndResponse);
        tableSizeIntegerProperty.set(tableSizeIntegerProperty.get() - 1);
    }

    public boolean isTableFiltered() {
        return tableFiltered;
    }

    public void setTableFiltered(boolean tableFiltered) {
        this.tableFiltered = tableFiltered;
    }

    public void refillTable() {
        this.getSelectionModel().clearSelection();
        this.getItems().clear();
        addToTable(dataList, false);
    }
}
