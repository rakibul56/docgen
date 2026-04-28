package com.docgen.service;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document.OutputSettings.Syntax;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

// @Service tells Spring: "Create one instance of this class and
// manage it for me." When DocumentController needs this service,
// Spring automatically provides it (this is called Dependency Injection).
@Service
public class PdfGeneratorService {

    // Thymeleaf's template engine — Spring auto-configures this for us
    // because we added spring-boot-starter-thymeleaf to pom.xml
    private final TemplateEngine templateEngine;

    // Constructor Injection: Spring sees that this class needs a
    // TemplateEngine, finds one in its container, and passes it here.
    // This is the recommended way to get dependencies in Spring.
    public PdfGeneratorService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    // ==========================================
    // THE MAIN METHOD: Generate a PDF
    //
    // Step 1: Take template name + data map
    // Step 2: Thymeleaf fills the HTML template with data
    // Step 3: OpenHTMLtoPDF converts filled HTML → PDF bytes
    // Step 4: Return the PDF as a byte array
    // ==========================================
    public byte[] generatePdf(String templateName, Map<String, Object> data) {

        // --- Step 1: Prepare the data for Thymeleaf ---
        // Context is Thymeleaf's way of holding template variables.
        // After this, the template can use ${customerName}, ${invoiceDate}, etc.
        Context context = new Context();
        context.setVariables(data);

        // --- Step 2: Process the template ---
        // Thymeleaf reads the HTML file from src/main/resources/templates/
        // and replaces all ${...} placeholders with actual values.
        // Input:  <h1 th:text="${customerName}">placeholder</h1>
        // Output: <h1>Max Mustermann</h1>
        String htmlContent = templateEngine.process(templateName, context);

        // --- Step 3: Convert HTML to PDF ---
        // ByteArrayOutputStream holds the PDF bytes in memory
        // (instead of writing to a file on disk)
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // Parse HTML5 with jsoup and serialize as XHTML for OpenHTMLtoPDF
            org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(htmlContent);
            jsoupDoc.outputSettings().syntax(Syntax.xml);
            String xhtml = jsoupDoc.html();

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(xhtml, null);
            builder.toStream(outputStream);
            builder.run();    // This does the actual conversion

            return outputStream.toByteArray();

        } catch (Exception e) {
            // If anything goes wrong, throw an error with a clear message
            throw new RuntimeException("PDF generation failed: " + e.getMessage(), e);
        }
    }
}