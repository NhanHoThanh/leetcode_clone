package com.example.demo.models.DTOs;

public class CodeSubmissionRequest {
    private String code;
    private String language;
    private Long problemId;

    // Constructors
    public CodeSubmissionRequest() {}

    public CodeSubmissionRequest(String code, String language, Long problemId) {
        this.code = code;
        this.language = language;
        this.problemId = problemId;
    }

    // Getters and Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Long getProblemId() {
        return problemId;
    }

    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }
}