/**
 * DocumentController provides the RESTful API for managing documents.
 * It offers functionalities to create, retrieve, update, delete, and handle files associated with documents.
 * 
 * @author Myles Gamez
 * @version 1.0
 */
package com.documentsapp.controller;

import com.documentsapp.model.Document;
import com.documentsapp.model.User;
import com.documentsapp.service.DocumentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final DocumentService service;

    private final Long DEFAULT_USER_ID = 1L;
    private static final String UPLOAD_DIR = "uploads";
    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    @Autowired
    public DocumentController(DocumentService service) {
        this.service = service;
    }

    /**
     * Retrieves all documents.
     *
     * @return List of all documents.
     */
    @GetMapping
    public List<Document> getAllDocuments() {
        List<Document> docs = service.getAllDocuments();
        logger.info("Retrieved {} documents.", docs.size());
        return docs;
    }

    /**
     * Searches documents by title or content.
     *
     * @param query Search query string.
     * @return List of documents matching the query.
     */
    @GetMapping("/search")
    public List<Document> searchDocuments(@RequestParam String query) {
        return service.searchByTitleOrContent(query);
    }

    /**
     * Creates a new document.
     *
     * @param document Document object to be created.
     * @return Created document.
     */
    @PostMapping
    public Document createDocument(@RequestBody Document document) {
        return service.saveDocument(document);
    }

    /**
     * Retrieves a document by its ID.
     *
     * @param id Document ID.
     * @return Document with the specified ID.
     */
    @GetMapping("/{id}")
    public Document getDocumentById(@PathVariable Long id) {
        return service.getDocumentById(id);
    }

    /**
     * Deletes a document by its ID.
     *
     * @param id Document ID to be deleted.
     */
    @DeleteMapping("/{id}")
    public void deleteDocument(@PathVariable Long id) {
        service.deleteDocument(id);
    }

    /**
     * Uploads multiple files and creates documents associated with them.
     *
     * @param files Array of files to be uploaded.
     * @return List of created documents.
     */
    @PostMapping("/uploadFiles")
    public List<Document> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("No files provided!");
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

                if (file.getContentType() != null && file.getContentType().startsWith("text")) {
                    String fileContent = new String(file.getBytes());
                    doc.setTitle(fileName);
                    doc.setContent(fileContent);
                } else {
                    doc.setContent("File uploaded on " + java.time.LocalDateTime.now());
                    doc.setTitle(fileName);
                }

                User defaultUser = new User();
                defaultUser.setId(DEFAULT_USER_ID);
                doc.setUser(defaultUser);

                savedDocs.add(service.saveDocument(doc));
            } catch (IOException ex) {
                logger.error("Failed to store file {}", fileName, ex);
                throw new RuntimeException("Failed to store file " + fileName, ex);
            }
        }

        return savedDocs;
    }

    /**
     * Downloads a file associated with the document ID.
     *
     * @param id Document ID.
     * @return File resource to be downloaded.
     */
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
            logger.error("Failed to convert path to URL", e);
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getFilename() + "\"")
                .body(resource);
    }

    /**
     * Handles exceptions for the controller.
     *
     * @param ex Exception encountered.
     * @return Error response.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAllExceptions(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}