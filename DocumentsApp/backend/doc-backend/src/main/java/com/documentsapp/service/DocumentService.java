package com.documentsapp.service;

import com.documentsapp.model.Document;
import com.documentsapp.model.User;
import com.documentsapp.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class DocumentService {

    private static final String UPLOAD_DIR = "uploads"; // Define your upload directory
    private static final Path UPLOAD_PATH = Paths.get(UPLOAD_DIR);
    // This assumes that there is a user in the database with ID 1. Adjust as
    // necessary.
    private final Long DEFAULT_USER_ID = 1L;

    public DocumentService() {
        // Check if directory exists, if not create it
        if (!Files.exists(UPLOAD_PATH)) {
            try {
                Files.createDirectories(UPLOAD_PATH);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create upload directory!", e);
            }
        }
    }

    @Autowired
    private DocumentRepository repository;

    public List<Document> searchByTitleOrContent(String query) {
        return repository.findByTitleContainingOrContentContaining(query, query);
    }

    public Document saveDocument(Document document) {
        return repository.save(document);
    }

    public Document getDocumentById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void deleteDocument(Long id) {
        repository.deleteById(id);
    }

    public List<Document> getAllDocuments() {
        return repository.findAll();
    }

    public Document storeFile(MultipartFile file) {
        // Normalize file name
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if (originalFileName.contains("..")) {
                throw new RuntimeException("Invalid file path sequence " + originalFileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = Paths.get(UPLOAD_DIR).resolve(originalFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Save file metadata in database
            Document document = new Document();
            document.setFilename(originalFileName);
            document.setFiletype(file.getContentType());
            document.setFileUrl(targetLocation.toString());

            return repository.save(document);
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + originalFileName + ". Please try again!", ex);
        }
    }

    public List<Document> storeFiles(MultipartFile[] files) {
        List<Document> savedDocuments = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = StringUtils.cleanPath(UUID.randomUUID().toString() + "_" + file.getOriginalFilename());
            Path targetLocation = Paths.get(UPLOAD_DIR).resolve(fileName);
            try {
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                Document doc = new Document();
                doc.setFilename(fileName);
                doc.setFiletype(file.getContentType());
                doc.setFileUrl(targetLocation.toString());
                doc.setContent("File uploaded on " + java.time.LocalDateTime.now()); // Placeholder content
                doc.setTitle(fileName); // Set the filename as title for now

                // Setting a default user for this document
                User defaultUser = new User();
                defaultUser.setId(DEFAULT_USER_ID);
                doc.setUser(defaultUser);

                savedDocuments.add(saveDocument(doc));
            } catch (IOException ex) {
                throw new RuntimeException("Failed to store file " + fileName, ex);
            }
        }
        return savedDocuments;
    }

}
