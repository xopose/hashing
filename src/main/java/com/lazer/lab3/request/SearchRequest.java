package com.lazer.lab3.request;

public class SearchRequest {
    private String searchString;

    public SearchRequest(String searchString) {
        this.searchString = searchString;
    }

    public SearchRequest() {}

    public String getSearchString() {
        return searchString;
    }
}
