package com.thepathways.callee_service.controller;

import com.thepathways.callee_service.service.UnstableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UnstableController {

    // 1. Declare the Service
    private final UnstableService unstableService;

    // 2. Inject it via Constructor
    public UnstableController(UnstableService unstableService) {
        this.unstableService = unstableService;
    }

    @GetMapping("/unstable")
    public ResponseEntity<String> unstable() throws InterruptedException {
        
        // 3. Call the logical method that may fail
            boolean success = unstableService.processWithPotentialFailure();
            if (!success) {
                // We throw a custom exception instead of returning a manual 500
                throw new RuntimeException("Service Failure"); 
            }
            return ResponseEntity.ok("Success");
    }
}