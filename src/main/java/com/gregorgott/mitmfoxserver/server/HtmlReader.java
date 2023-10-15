package com.gregorgott.mitmfoxserver.server;

import com.gregorgott.mitmfoxserver.MitmFoxServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HtmlReader {
    private final String resource;
    private final HashMap<Integer, String> replaceMap;
    public HtmlReader(String resource) {
        this.resource = resource;
        replaceMap = new HashMap<>();
    }

    public void addReplacement(int i, String s) {
        replaceMap.put(i, s);
    }

    public String read() throws IOException {
        StringBuilder sb = new StringBuilder();

        BufferedReader br = new BufferedReader(
                new InputStreamReader(MitmFoxServer.class.getResourceAsStream(resource))
        );

        while (true) {
            String line = br.readLine();
            if (line == null) {
                break;
            }
            sb.append(line);
        }

        String s = sb.toString();

        for (Map.Entry<Integer, String> entry : replaceMap.entrySet()) {
            s = s.replace("{{$" + entry.getKey() + "}}", entry.getValue());
        }

        return s;
    }
}
