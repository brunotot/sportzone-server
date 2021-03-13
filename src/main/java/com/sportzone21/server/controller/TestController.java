package com.sportzone21.server.controller;

import com.sportzone21.server.entity.TestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping
    @ResponseBody
    public ResponseEntity<TestEntity> index() {
        return ResponseEntity.ok(new TestEntity("Hello world!"));
    }

}
