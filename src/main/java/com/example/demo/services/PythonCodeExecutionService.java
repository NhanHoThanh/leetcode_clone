package com.example.demo.services;

import org.springframework.stereotype.Service;
import org.testcontainers.containers.Container.ExecResult;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.Transferable;
import org.testcontainers.utility.DockerImageName;
import com.example.demo.models.TestCase;
import com.example.demo.repositories.TestCaseRepository;
import com.example.demo.models.Problem;
import com.example.demo.repositories.ProblemRepository;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



@Service
public class PythonCodeExecutionService {
    private final TestCaseRepository testCaseRepository;
    private final ProblemRepository problemRepository;


    public PythonCodeExecutionService(TestCaseRepository testCaseRepository, ProblemRepository problemRepository) {
        this.testCaseRepository = testCaseRepository;
        this.problemRepository = problemRepository;
    }

    public String executeCode(String userCode, String language, Long problemId) {
        System.out.println("Executing code");
        if (!language.equals("python")) {
            return "Error: Only Python is supported";
        }

        // Fetch the problem to get the function name
        Problem problem = problemRepository.findById(problemId).orElse(null);
        if (problem == null) {
            return "Error: Problem not found";
        }
        String functionName = problem.getFunctionName();
        if (functionName == null || functionName.isEmpty()) {
            return "Error: Function name not defined for this problem";
        }

        // Fetch all test cases for the problem
        List<TestCase> testCases = testCaseRepository.findByProblemId(problemId);
        if (testCases.isEmpty()) {
            return "Error: No test cases found for this problem";
        }

        StringBuilder result = new StringBuilder();
        System.out.println("User code: " + userCode);
        int testNumber = 1;

        // Run each test case
        for (TestCase testCase : testCases) {
            String completeCode = generateTestCode(userCode, functionName);
            System.out.println("Complete code: " + completeCode);
            String input = testCase.getInput();
            String expectedOutput = testCase.getExpectedOutput();

            String output = runPythonCode(completeCode, input);
            System.out.println("Output: " + output);
            if (output == null) {
                result.append("Test ").append(testNumber).append(": Execution failed\n");
            } else if (output.trim().equals(expectedOutput.trim())) {
                result.append("Test ").append(testNumber).append(": Passed\n");
            } else {
                result.append("Test ").append(testNumber).append(": Failed - Expected ")
                      .append(expectedOutput).append(", got ").append(output).append("\n");
            }
            testNumber++;
        }
        return result.toString();
    }

    private String generateTestCode(String userCode, String functionName) {
        String code =  "import sys\n"+
               "import ast\n"+
               "from typing import List\n\n" +
               "class Solution:\n" +
               indentUserCode(userCode) + "\n\n" +
               "if __name__ == \"__main__\":\n" +
               "    input = sys.stdin.read\n" +
               "    data = input().splitlines()\n" +
               "    nums = ast.literal_eval(data[0])\n" +
               "    target = int(data[1])\n" +
               "    solution = Solution()\n" +
               "    result = solution." + functionName + "(nums, target)\n" +
               "    print(str(result))\n";
        System.out.println("Generated code: " + code);
        return code;
    }

    private String indentUserCode(String userCode) {
        // Indent user code to fit under class Solution
        return userCode.lines()
                       .map(line -> "    " + line)
                       .collect(Collectors.joining("\n"));
    }

    private String runPythonCode(String code, String input) {
        try (GenericContainer<?> container = new GenericContainer<>(DockerImageName.parse("python:3.8"))
                .withCopyToContainer(
                    Transferable.of(code.getBytes(StandardCharsets.UTF_8)),
                    "/main.py"
                )
                .withCommand("tail", "-f", "/dev/null")
                .withStartupTimeout(Duration.ofSeconds(30))) {

            container.start();
            ExecResult result = container.execInContainer("sh", "-c", "echo '" + input + "' | python /main.py");
            String output = result.getStdout().trim();
            String error = result.getStderr().trim();
            container.stop();
            return error.isEmpty() ? output : "Error: " + error;
        } catch (Exception e) {
            return null;
        }
    }
}