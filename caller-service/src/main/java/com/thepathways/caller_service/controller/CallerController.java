package com.thepathways.caller_service.controller;

import com.thepathways.caller_service.service.RemoteServiceClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/test")
public class CallerController {

    private final RemoteServiceClient client;

    public CallerController(RemoteServiceClient client) {
        this.client = client;
    }

    @GetMapping("/{type}")
    public ResponseEntity<String> test(@PathVariable String type) {
        return ResponseEntity.ok(client.call(type));
    }

    @GetMapping("/load/{type}")
    public ResponseEntity<String> load(@PathVariable String type)
            throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 20; i++) {
            executor.submit(() -> client.call(type));
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        return ResponseEntity.ok("Load test done");
    }
}