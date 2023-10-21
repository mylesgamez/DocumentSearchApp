// Imports
import React, { useState, useEffect } from 'react';
import SearchBox from './components/SearchBox';
import DocumentList from './components/DocumentList';
import './App.css';

/**
 * App Component - main entry for the application.
 * It handles the fetching, searching, and uploading of documents.
 */
function App() {
  const [documents, setDocuments] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
  const backendURL = "http://localhost:8080";

  // Fetch or search documents when searchQuery changes
  useEffect(() => {
    if (searchQuery === '') fetchDocuments();
    else searchDocuments(searchQuery);
  }, [searchQuery]);

  /**
   * Fetch all documents from the backend
   */
  const fetchDocuments = async () => {
    try {
      const response = await fetch(`${backendURL}/api/documents`, {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        mode: 'cors'
      });

      if (response.status === 200) {
        const docs = await response.json();
        console.log(docs);
        setDocuments(docs);
      } else {
        console.error("Error fetching documents:", response.statusText);
      }
    } catch (error) {
      console.error("Error fetching documents:", error);
    }
  };

  /**
   * Search documents using the provided query
   * @param {string} query - Search query
   */
  const searchDocuments = async (query) => {
    try {
      const response = await fetch(`${backendURL}/api/documents/search?query=${query}`, {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        mode: 'cors'
      });

      if (response.status === 200) {
        const docs = await response.json();
        console.log(docs);
        setDocuments(docs);
      } else {
        console.error("Error searching documents:", response.statusText);
      }
    } catch (error) {
      console.error("Error searching documents:", error);
    }
  };

  /**
   * Handle search input changes
   * @param {string} query - Search query
   */
  const handleSearch = query => {
    setSearchQuery(query);
  };

  /**
   * Upload files to the backend
   * @param {FileList} files - List of files to upload
   */
  const uploadFiles = async (files) => {
    const formData = new FormData();
    for (let i = 0; i < files.length; i++) {
      formData.append('files', files[i]);
    }

    try {
      const response = await fetch(`${backendURL}/api/documents/uploadFiles`, {
        method: 'POST',
        body: formData,
        mode: 'cors',
        credentials: 'include'
      });

      if (!response.ok) {
        const errorData = await response.json();
        console.error("Error uploading files:", errorData.error || response.statusText);
        return;
      }

      const newDocs = await response.json();
      setDocuments(prevDocs => [...prevDocs, ...newDocs]);
    } catch (error) {
      console.error("Error uploading files:", error.message);
    }
  };

  /**
   * Handle file input changes and initiate file upload
   * @param {Event} e - Event object
   */
  const handleFileChange = (e) => {
    uploadFiles(e.target.files);
  }

  return (
    <div className="App">
      <header className="App-header">
        <h1>Document Search</h1>
      </header>
      <div className="App-content">
        <button className="upload-btn" onClick={() => document.querySelector('.file-input').click()}>
          Upload Document
        </button>
        <input type="file" className="file-input" multiple onChange={handleFileChange} />
        <SearchBox onSearch={handleSearch} />
        <DocumentList documents={documents} />
      </div>
    </div>
  );
}

export default App;
