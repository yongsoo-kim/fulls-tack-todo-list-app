package com.yongsookim.todolist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodoController {


    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        final String msg = "hello world";
        return ResponseEntity.ok().body(msg);
    }
}
