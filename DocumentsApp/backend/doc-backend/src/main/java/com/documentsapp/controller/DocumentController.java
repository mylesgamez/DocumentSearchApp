package com.documentsapp.controller;

import com.documentsapp.model.Document;
import com.documentsapp.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000") // assuming frontend runs on localhost:3000
@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService service;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public List<Document> getAllDocuments() {
        return service.getAllDocuments();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/search")
    public List<Document> searchDocuments(@RequestParam String query) {
        return service.searchByTitleOrContent(query);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping
    public Document createDocument(@RequestBody Document document) {
        return service.saveDocument(document);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{id}")
    public Document getDocumentById(@PathVariable Long id) {
        return service.getDocumentById(id);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/{id}")
    public void deleteDocument(@PathVariable Long id) {
        service.deleteDocument(id);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/upload")
    public List<String> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw new RuntimeException("No files provided!");
        }
        List<Document> savedDocs = service.storeFiles(files);
        return savedDocs.stream().map(Document::getFilename).collect(Collectors.toList());
    }

    @CrossOrigin(origins = "http://localhost:3000")
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

}
