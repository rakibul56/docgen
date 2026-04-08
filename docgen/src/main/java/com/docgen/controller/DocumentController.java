package com.docgen.controller;

import java.util.Map;

// ResponseEntity — lets us control HTTP status code, headers, and body
import org.springframework.http.ResponseEntity;
// These annotations map HTTP methods to Java methods
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// @RestController tells Spring:
//   "This class handles HTTP requests and returns JSON data"
//   Without this, Spring wouldn't know this class exists.
@RestController
// @RequestMapping sets the BASE URL for all endpoints in this class
// Every method's URL will start with /api/v1/documents
@RequestMapping("/api/v1/documents")
public class DocumentController {

    // ==========================================
    // ENDPOINT 1: Health Check
    // URL:    GET http://localhost:8080/api/v1/documents/health
    // Purpose: Kubernetes will call this every 10 seconds to
    //          check if our app is alive. If it fails,
    //          Kubernetes restarts the container.
    // ==========================================
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {

        // Map.of() creates an immutable map (Java 9+ feature)
        // This gets automatically converted to JSON by Jackson library
        Map<String, String> response = Map.of(
            "status", "UP",
            "service", "docgen",
            "version", "1.0.0"
        );

        // ResponseEntity.ok() = HTTP 200 status code + our data
        return ResponseEntity.ok(response);
    }

    // ==========================================
    // ENDPOINT 2: Service Info
    // URL:    GET http://localhost:8080/api/v1/documents/info
    // Purpose: Tells callers what this API can do.
    //          Useful for documentation and debugging.
    // ==========================================
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        return ResponseEntity.ok(Map.of(
            "name", "DocGen Service",
            "description", "Document Output Management API",
            "version", "1.0.0",
            "endpoints", Map.of(
                "health", "GET  /api/v1/documents/health",
                "info",   "GET  /api/v1/documents/info",
                "generate", "POST /api/v1/documents/generate (coming in Step 5)"
            )
        ));
    }
}