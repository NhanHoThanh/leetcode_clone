package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


// import com.example.demo.models.CodeSubmissionRequest;
import com.example.demo.services.CodeExecutionService;
import com.example.demo.services.PythonCodeExecutionService;
import com.example.demo.models.DTOs.CodeSubmissionRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class CodeExecutionController {

    @Autowired
    private CodeExecutionService codeExecutionService;
    @Autowired
    private PythonCodeExecutionService pythonCodeExecutionService;

    // @PostMapping("/submit")
    // public ResponseEntity<String> submitCode(@RequestBody CodeSubmissionRequest request) {
    //     try {
    //         String result = codeExecutionService.executeCode(
    //             request.getCode(),
    //             request.getLanguage(),
    //             request.getProblemId()
    //         );
    //         return ResponseEntity.ok(result);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //             .body("Error: " + e.getMessage());
    //     }
    // }

    @PostMapping("/execute")
    public ResponseEntity<String> executeCode(@RequestBody CodeSubmissionRequest request) {
        System.out.println("Request: " + request);
        String result = pythonCodeExecutionService.executeCode(
            request.getCode(),
            request.getLanguage(),
            request.getProblemId()
        );
        return ResponseEntity.ok(result);
    }

    @GetMapping("/test/helloworld")
    public ResponseEntity<?> TestCode() {
        try {
            String result = codeExecutionService.executeCode(
                "print('hello world')",
                "python",
                Long.valueOf(1)
            );
            // String result = codeExecutionService.testDockerClient();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    @GetMapping("/test/sum")
    public ResponseEntity<?> testSumCode() {
        try {
            String code = "a, b = map(int, input().split())\n" +
                        "print(a + b)";
            String result = codeExecutionService.executeCode(
                code,
                "python",
                Long.valueOf(2)
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
}