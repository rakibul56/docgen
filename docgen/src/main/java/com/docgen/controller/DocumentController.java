package com.docgen.controller;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.docgen.model.DocumentRequest;
import com.docgen.service.PdfGeneratorService;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {

    // Spring injects PdfGeneratorService here automatically
    // because we marked it with @Service
    private final PdfGeneratorService pdfService;

    // Constructor Injection — Spring provides the PdfGeneratorService
    public DocumentController(PdfGeneratorService pdfService) {
        this.pdfService = pdfService;
    }

    // ==========================================
    // ENDPOINT 1: Health Check (unchanged)
    // ==========================================
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "docgen",
            "version", "1.0.0"
        ));
    }

    // ==========================================
    // ENDPOINT 2: Service Info (unchanged)
    // ==========================================
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        return ResponseEntity.ok(Map.of(
            "name", "DocGen Service",
            "description", "Document Output Management API",
            "version", "1.0.0",
            "endpoints", Map.of(
                "health",   "GET  /api/v1/documents/health",
                "info",     "GET  /api/v1/documents/info",
                "generate", "POST /api/v1/documents/generate"
            )
        ));
    }

    // ==========================================
    // ENDPOINT 3: Generate PDF   *** NEW ***
    // URL:    POST http://localhost:8080/api/v1/documents/generate
    // Body:   JSON with templateName, outputFilename, and data
    // Returns: PDF file as download
    //
    // This is the CORE of what Cartago does:
    //   Template + Data → Document
    // ==========================================
    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateDocument(@RequestBody DocumentRequest request) {

        // Call our service to generate the PDF
        byte[] pdfBytes = pdfService.generatePdf(
            request.getTemplateName(),
            request.getData()
        );

        // Set HTTP headers so the browser knows this is a PDF download
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData(
            "attachment",
            request.getOutputFilename() + ".pdf"
        );
        headers.setContentLength(pdfBytes.length);

        // Return PDF bytes with headers
        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }
}