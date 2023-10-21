package com.documentsapp.repository;

import com.documentsapp.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for {@link Document} entities.
 * Provides CRUD operations and custom queries for handling documents.
 * 
 * @author Myles Gamez
 * @version 1.0
 */
public interface DocumentRepository extends JpaRepository<Document, Long> {

    /**
     * Retrieves a list of documents where the title or content contains the given
     * strings.
     *
     * @param title   The string to match against document titles.
     * @param content The string to match against document content.
     * @return A list of matching documents.
     */
    List<Document> findByTitleContainingOrContentContaining(String title, String content);
}
