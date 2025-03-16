package com.example.demo.services;

import org.springframework.stereotype.Service;
import org.testcontainers.containers.Container.ExecResult;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.Transferable;
import org.testcontainers.utility.DockerImageName;
import com.example.demo.models.TestCase;
import com.example.demo.repositories.TestCaseRepository;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CodeExecutionService {

    private final TestCaseRepository testCaseRepository;

    // Map language to Docker image
    private static final Map<String, String> LANGUAGE_TO_IMAGE = new HashMap<>();
    // Map language to code filename
    private static final Map<String, String> LANGUAGE_TO_FILENAME = new HashMap<>();

    static {
        LANGUAGE_TO_IMAGE.put("python", "python:3.8");
        LANGUAGE_TO_FILENAME.put("python", "main.py");
    }

    public CodeExecutionService(TestCaseRepository testCaseRepository) {
        this.testCaseRepository = testCaseRepository;
    }

    /**
     * Executes user-submitted code in a sandboxed Docker container and evaluates it against a test case.
     *
     * @param code      The user-submitted code
     * @param language  The programming language (e.g., "java", "python")
     * @param problemId The ID of the problem to fetch the test case
     * @return "Accepted" if the output matches the expected output, "Wrong Answer" otherwise, or an error message
     */
    public String executeCode(String code, String language, Long problemId) {
        // Validate language support
        String imageName = LANGUAGE_TO_IMAGE.get(language);
        String fileName = LANGUAGE_TO_FILENAME.get(language);
        if (imageName == null || fileName == null) {
            return "Error: Unsupported language: " + language;
        }

        // Retrieve test case
        List<TestCase> testCase = testCaseRepository.findByProblemId(problemId);
        if (testCase == null) {
            return "Error: No test case found for problem ID: " + problemId;
        }

        // Generate execution command with test case input
        String executionCommand = generateExecutionCommand(language, fileName, testCase.get(0).getInput());

        // Configure and run the container
        try (GenericContainer<?> container = new GenericContainer<>(DockerImageName.parse(imageName))
                .withCopyToContainer(
                        Transferable.of(code.getBytes(StandardCharsets.UTF_8)),  // Copy code directly to container
                        "/" + fileName
                )
                .withCommand("tail", "-f", "/dev/null")  // Keep container running
                .withStartupTimeout(Duration.ofSeconds(30))
                .withCreateContainerCmdModifier(cmd -> 
                cmd.getHostConfig().withMemory(256 * 1024 * 1024L) // 256 MB
            )) {

            container.start();
            ExecResult result = container.execInContainer("sh", "-c", "echo '" + testCase.get(0).getInput() + "' | python /" + fileName);
            String output = result.getStdout().trim();
            String error = result.getStderr().trim();
            container.stop();

            // Evaluate the output
            String logs = error.isEmpty() ? output : output + "\nError: " + error;
            return evaluateOutput(logs, testCase.get(0).getExpectedOutput());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Generates the command to execute the code with the test case input.
     */
    private String generateExecutionCommand(String language, String fileName, String input) {
        String inputCommand = "echo '" + input + "' | ";  // Pipe input into the program
        switch (language) {
            case "python":
                return inputCommand + "python " + fileName;
            default:
                throw new IllegalArgumentException("Unsupported language: " + language);
        }
    }

    /**
     * Evaluates the container's output against the expected output.
     */
    private String evaluateOutput(String actualOutput, String expectedOutput) {
        String trimmedActual = actualOutput.trim();
        String trimmedExpected = expectedOutput.trim();
        if (trimmedActual.equals(trimmedExpected)) {
            return "Accepted";
        } else {
            return "Wrong Answer\nExpected: " + trimmedExpected + "\nGot: " + trimmedActual;
        }
    }

    /**
     * Simple method to test Testcontainers setup.
     */
    public String testDockerClient() {
        try (GenericContainer<?> container = new GenericContainer<>(DockerImageName.parse("python:3.8"))
                .withCommand("python", "-c", "print('Hello, World!')")
                .withStartupTimeout(Duration.ofSeconds(30))) {
            container.start();
            String logs = container.getLogs();
            System.out.println("Test logs: " + logs);
            container.stop();
            return "Success";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}