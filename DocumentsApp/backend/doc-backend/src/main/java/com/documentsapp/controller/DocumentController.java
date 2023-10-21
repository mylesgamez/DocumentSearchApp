package com.documentsapp.controller;

import com.documentsapp.model.Document;
import com.documentsapp.model.User;
import com.documentsapp.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService service;

    // Set the default user ID to 1
    private final Long DEFAULT_USER_ID = 1L;
    private static final String UPLOAD_DIR = "uploads";

    @GetMapping
    public List<Document> getAllDocuments() {
        List<Document> docs = service.getAllDocuments();
        System.out.println(docs); // Print out the list of documents
        return docs;
    }

    @GetMapping("/search")
    public List<Document> searchDocuments(@RequestParam String query) {
        return service.searchByTitleOrContent(query);
    }

    @PostMapping
    public Document createDocument(@RequestBody Document document) {
        return service.saveDocument(document);
    }

    @GetMapping("/{id}")
    public Document getDocumentById(@PathVariable Long id) {
        return service.getDocumentById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteDocument(@PathVariable Long id) {
        service.deleteDocument(id);
    }

    @PostMapping("/uploadFiles")
    public List<Document> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw new RuntimeException("No files provided!");
        }

        List<Document> savedDocs = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = StringUtils.cleanPath(UUID.randomUUID().toString() + "_" + file.getOriginalFilename());
            Path targetLocation = Paths.get(UPLOAD_DIR).resolve(fileName);

            try {
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                Document doc = new Document();
                doc.setFilename(fileName);
                doc.setFiletype(file.getContentType());
                doc.setFileUrl(targetLocation.toString());

                // Extract title and content from text files
                if (file.getContentType() != null && file.getContentType().startsWith("text")) {
                    String fileContent = new String(file.getBytes());
                    doc.setTitle(fileName);
                    doc.setContent(fileContent);
                } else {
                    doc.setContent("File uploaded on " + java.time.LocalDateTime.now());
                    doc.setTitle(fileName);
                }

                // Setting a default user for this document
                User defaultUser = new User();
                defaultUser.setId(DEFAULT_USER_ID);
                doc.setUser(defaultUser);

                savedDocs.add(service.saveDocument(doc));
            } catch (IOException ex) {
                throw new RuntimeException("Failed to store file " + fileName, ex);
            }
        }

        return savedDocs;
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        Document doc = service.getDocumentById(id);
        if (doc == null || doc.getFileUrl() == null) {
            return ResponseEntity.notFound().build();
        }
        Path path = Paths.get(doc.getFileUrl());
        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getFilename() + "\"")
                .body(resource);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAllExceptions(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}
