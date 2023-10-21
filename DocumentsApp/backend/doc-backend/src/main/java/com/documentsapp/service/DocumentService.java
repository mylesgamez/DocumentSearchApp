package com.documentsapp.service;

import com.documentsapp.model.Document;
import com.documentsapp.model.User;
import com.documentsapp.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service class responsible for managing document-related operations.
 * This includes CRUD operations, searching documents, and handling document
 * file storage.
 * 
 * @author Myles Gamez
 * @version 1.0
 */
@Service
public class DocumentService {

    private static final String UPLOAD_DIR = "uploads";
    private static final Path UPLOAD_PATH = Paths.get(UPLOAD_DIR);
    private final Long DEFAULT_USER_ID = 1L; // Default user assumed in the database

    /**
     * Initializes a new instance of the DocumentService and ensures the upload
     * directory exists.
     */
    public DocumentService() {
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

    /**
     * Searches documents by title or content.
     *
     * @param query The search query.
     * @return A list of documents matching the query.
     */
    public List<Document> searchByTitleOrContent(String query) {
        return repository.findByTitleContainingOrContentContaining(query, query);
    }

    /**
     * Saves a document entity.
     *
     * @param document The document entity to save.
     * @return The saved document entity.
     */
    public Document saveDocument(Document document) {
        return repository.save(document);
    }

    /**
     * Retrieves a document by its ID.
     *
     * @param id The ID of the document.
     * @return The retrieved document, or null if not found.
     */
    public Document getDocumentById(Long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * Deletes a document by its ID.
     *
     * @param id The ID of the document to delete.
     */
    public void deleteDocument(Long id) {
        repository.deleteById(id);
    }

    /**
     * Retrieves all documents.
     *
     * @return A list of all documents.
     */
    public List<Document> getAllDocuments() {
        return repository.findAll();
    }

    /**
     * Stores a file and creates a corresponding document entry in the database.
     *
     * @param file The file to store.
     * @return The saved document entity with metadata from the file.
     */
    public Document storeFile(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (originalFileName.contains("..")) {
            throw new RuntimeException("Invalid file path sequence " + originalFileName);
        }
        Path targetLocation = Paths.get(UPLOAD_DIR).resolve(originalFileName);
        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            Document document = new Document();
            document.setFilename(originalFileName);
            document.setFiletype(file.getContentType());
            document.setFileUrl(targetLocation.toString());
            return repository.save(document);
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + originalFileName + ". Please try again!", ex);
        }
    }

    /**
     * Stores multiple files and creates corresponding document entries in the
     * database.
     *
     * @param files The files to store.
     * @return A list of saved document entities with metadata from the files.
     */
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
                doc.setContent("File uploaded on " + java.time.LocalDateTime.now());
                doc.setTitle(fileName);
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
