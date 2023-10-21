package com;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.SpringApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test suite for the DemoApplication class.
 */
@SpringBootTest
public class DemoApplicationTests {

    // Mock of the SpringApplication for testing the main method without starting
    // the entire Spring context
    @MockBean
    private SpringApplication springApplication;

    // Temporary directory for safely conducting file system tests without altering
    // the real system
    @TempDir
    Path temporaryDirectory;

    /**
     * Set up routine run before each test.
     * Sets a temporary directory as the working directory for file system
     * operations.
     */
    @BeforeEach
    public void setUp() {
        System.setProperty("user.dir", temporaryDirectory.toString());
    }
}
