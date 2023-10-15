package com.gregorgott.mitmfoxserver.ui.utils;

import com.gregorgott.mitmfoxserver.ui.RequestAndResponse;
import com.gregorgott.mitmfoxserver.ui.nodes.RequestInfoAccordionInfo;
import com.gregorgott.mitmfoxserver.ui.nodes.ResponseInfoAccordion;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.fxmisc.richtext.CodeArea;

import java.util.ArrayList;
import java.util.List;

public class SelectionModelListenerUpdateService {
    private final RequestAndResponse requestAndResponse;
    private final ResponseInfoAccordion responseInfoAccordion;
    private final RequestInfoAccordionInfo requestInfoAccordion;
    private final List<Thread> threadList;

    public SelectionModelListenerUpdateService(
            RequestInfoAccordionInfo requestInfoAccordion, ResponseInfoAccordion responseInfoAccordion,
            RequestAndResponse requestAndResponse
    ) {
        this.requestInfoAccordion = requestInfoAccordion;
        this.responseInfoAccordion = responseInfoAccordion;
        this.requestAndResponse = requestAndResponse;

        threadList = new ArrayList<>(getThreads());
    }

    private List<Thread> getThreads() {
        return List.of(
                createThread(
                        requestAndResponse.getUrl(),
                        requestInfoAccordion.getUrlPane().getCodeArea()
                ),
                createThread(
                        requestAndResponse.getRequestHeader(),
                        requestInfoAccordion.getHeaderPane().getCodeArea()
                ),
                createThread(
                        requestAndResponse.getRequestBody(),
                        requestInfoAccordion.getBodyPane().getCodeArea()
                ),
                createThread(
                        requestAndResponse.getResponseBody(),
                        responseInfoAccordion.getBodyTitledPane().getCodeArea()
                ),
                createThread(
                        requestAndResponse.getResponseHeader(),
                        responseInfoAccordion.getHeaderTitledPane().getCodeArea()
                )
        );
    }

    public final void startThreads() {
        for (Thread t : threadList) {
            t.start();
        }
    }

    private Thread createThread(String longString, CodeArea textArea) {
        Thread t = new Thread(createTask(longString, textArea));
        t.setDaemon(true);
        return t;
    }

    private Task<Void> createTask(String longString, CodeArea textArea) {
        return new Task<>() {
            @Override
            protected Void call() {
                Platform.runLater(() -> textArea.replaceText(longString));
                return null;
            }
        };
    }
}
