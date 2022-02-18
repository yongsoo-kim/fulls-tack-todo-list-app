package com.yongsookim.todolist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {

        final String status = "UP";

        HealthCheckModel model = new HealthCheckModel(status);

        return ResponseEntity.ok().body(model);
    }

    //TODO: 과연 이 접근자및 클래스 사용은 옳은 것일까?
    //TODO: 싱글톤으로 해야하지 않을까?
    private static class HealthCheckModel {

        private String status;

        public HealthCheckModel(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }

}
