package com.lazer.lab3.controller;

import com.lazer.lab3.component.Search;
import com.lazer.lab3.request.SearchRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {
    private final Search search = new Search();
    @GetMapping("/search")
    public ResponseEntity<?> findByQuery(@RequestBody SearchRequest query) {
        return ResponseEntity.ok(search.search(query));
    }
}
