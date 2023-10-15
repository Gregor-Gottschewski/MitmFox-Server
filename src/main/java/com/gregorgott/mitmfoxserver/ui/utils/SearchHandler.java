package com.gregorgott.mitmfoxserver.ui.utils;

import com.gregorgott.mitmfoxserver.ui.RequestAndResponse;

import java.util.ArrayList;
import java.util.List;

public class SearchHandler {
    private final String query;
    private final List<RequestAndResponse> allData;
    private final List<RequestAndResponse> filteredData;
    private final Filter filter;
    private final Column column;

    public SearchHandler(String query, List<RequestAndResponse> allData) {
        this.allData = allData;

        QueryHandler queryHandler = new QueryHandler(query);
        this.query = queryHandler.getSearchQuery();

        filteredData = new ArrayList<>();
        filter = queryHandler.getFilter();
        column = queryHandler.getColumn();
    }

    private static String getDataFromColumn(RequestAndResponse data, Column column) {
        return switch (column) {
            case IP -> data.getIp();
            case URL -> data.getUrl();
            case BODY -> data.getRequestBody();
            case TIME -> data.getTime();
            case TYPE -> data.getResponseHeader();
            case HEADER -> data.getRequestHeader();
            case METHOD -> data.getMethod();
        };
    }

    public List<RequestAndResponse> getFilteredData() {
        filterData();
        return filteredData;
    }

    private boolean doesStringMatchFilter(String s) {
        return switch (filter) {
            case IS -> s.equals(query);
            case IS_NOT -> !s.equals(query);
            case CONTAINS -> s.contains(query);
            case CONTAINS_NOT -> !s.contains(query);
            case ENDS_WITH -> s.endsWith(query);
            case STARTS_WITH -> s.startsWith(query);
        };
    }

    private void filterData() {
        for (RequestAndResponse data : allData) {
            if (column != null) {
                String s = getDataFromColumn(data, column);

                if (doesStringMatchFilter(s)) {
                    filteredData.add(data);
                }
            } else {
                List<String> dataString = List.of(
                        data.getIp(),
                        data.getTime(),
                        data.getResponseHeader(),
                        data.getRequestBody(),
                        data.getRequestHeader(),
                        data.getUrl(),
                        data.getMethod()
                );

                for (String s : dataString) {
                    if (doesStringMatchFilter(s)) {
                        filteredData.add(data);
                        break;
                    }
                }
            }
        }
    }

    private enum Filter {
        CONTAINS,
        CONTAINS_NOT,
        STARTS_WITH,
        ENDS_WITH,
        IS,
        IS_NOT
    }

    private enum Column {
        URL,
        BODY,
        HEADER,
        IP,
        METHOD,
        TYPE,
        TIME
    }

    private record QueryHandler(String query) {
        private String getSearchQuery() {
            char[] queryAsCharArray = query.toCharArray();

            boolean escape = false;
            boolean ignore = false;
            boolean filter = false;
            boolean columnName = false;
            StringBuilder searchQuery = new StringBuilder();

            for (char c : queryAsCharArray) {
                if (c == '\\') {
                    escape = true;
                    continue;
                }

                if (escape) {
                    searchQuery.append(c);
                    continue;
                }

                if (c == '!') {
                    columnName = true;
                    continue;
                }

                if (columnName) {
                    if (c == ' ') {
                        columnName = false;
                        continue;
                    }
                    continue;
                }

                if (c == '<') {
                    filter = true;
                }

                if (filter) {
                    if (c == '>') {
                        filter = false;
                        continue;
                    }
                    continue;
                }

                searchQuery.append(c);
            }

            return searchQuery.toString();
        }

        public Column getColumn() {
            return mapStringToColumn(getTextBetweenSymbols(query, '!', ' '));
        }

        public Filter getFilter() {
            return mapStringToFilter(getTextBetweenSymbols(query, '<', '>'));
        }

        private String getTextBetweenSymbols(String s, char startSymbol, char endSymbol) {
            int startIndex = s.indexOf(startSymbol);
            int endIndex = s.indexOf(endSymbol);
            if (startIndex == -1 || endIndex == -1 || startIndex > endIndex) {
                return "";
            }
            return s.substring(startIndex + 1, endIndex).trim();
        }

        private Column mapStringToColumn(String columnAsString) {
            return switch (columnAsString.toUpperCase()) {
                case "IP" -> Column.IP;
                case "URL" -> Column.URL;
                case "HEADER" -> Column.HEADER;
                case "BODY" -> Column.BODY;
                case "METHOD" -> Column.METHOD;
                case "TIME" -> Column.TIME;
                case "TYPE" -> Column.TYPE;
                default -> null;
            };
        }

        private Filter mapStringToFilter(String filterAsString) {
            return switch (filterAsString.toUpperCase()) {
                case "IS" -> Filter.IS;
                case "IS_NOT" -> Filter.IS_NOT;
                case "STARTS_WITH" -> Filter.STARTS_WITH;
                case "ENDS_WITH" -> Filter.ENDS_WITH;
                case "CONTAINS_NOT" -> Filter.CONTAINS_NOT;
                default -> Filter.CONTAINS;
            };
        }
    }
}
