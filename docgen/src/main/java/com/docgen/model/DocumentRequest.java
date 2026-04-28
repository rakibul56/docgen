package com.docgen.model;

import java.util.Map;

// This class represents the JSON body that clients send to us.
// When someone POSTs JSON like:
// {
//   "templateName": "invoice-template",
//   "outputFilename": "invoice-001",
//   "data": { "customerName": "Max", "amount": "500" }
// }
//
// Spring automatically converts it into a DocumentRequest object.
// Each JSON field maps to a Java field with the same name.

public class DocumentRequest {

    // Which HTML template to use (e.g., "invoice-template")
    private String templateName;

    // What to name the output file (e.g., "invoice-001")
    private String outputFilename;

    // Key-value pairs to fill into the template
    // Map<String, Object> means: keys are Strings, values can be anything
    // Example: {"customerName": "Max", "items": [...], "total": 500}
    private Map<String, Object> data;

    // --- Default Constructor ---
    // Spring needs this empty constructor to create the object
    // before filling in the fields from JSON
    public DocumentRequest() {}

    // --- Getters and Setters ---
    // Spring uses these to read/write the fields.
    // Java convention: fields are private, accessed through get/set methods.

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getOutputFilename() {
        return outputFilename;
    }

    public void setOutputFilename(String outputFilename) {
        this.outputFilename = outputFilename;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}