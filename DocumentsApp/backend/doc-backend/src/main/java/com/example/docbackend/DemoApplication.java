package com.example.docbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;

@SpringBootApplication
public class DemoApplication {

	private static final String UPLOAD_DIR = "uploads";

	public static void main(String[] args) {
		ensureDirectoriesExist();
		SpringApplication.run(DemoApplication.class, args);
	}

	private static void ensureDirectoriesExist() {
		Path uploadDir = Paths.get(UPLOAD_DIR);
		if (!Files.exists(uploadDir)) {
			try {
				Files.createDirectories(uploadDir);
			} catch (IOException e) {
				throw new RuntimeException("Could not create upload directory", e);
			}
		}
	}
}
