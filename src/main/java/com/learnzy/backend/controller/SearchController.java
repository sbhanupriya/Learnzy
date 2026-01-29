package com.learnzy.backend.controller;

import com.learnzy.backend.models.search.SearchResponse;
import com.learnzy.backend.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @GetMapping
    public ResponseEntity<SearchResponse> search(@RequestParam("q") String keyword){
        SearchResponse searchResponse = searchService.search(keyword);
        return new ResponseEntity<>(searchResponse, HttpStatus.OK);
    }
}
