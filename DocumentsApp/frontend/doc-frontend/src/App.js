import React, { useState } from 'react';
import SearchBox from './components/SearchBox';
import DocumentList from './components/DocumentList';

function App() {
  const [documents, setDocuments] = useState([]);
  const backendURL = "http://localhost:8080"; // assuming backend is running here

  const handleSearch = async query => {
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
      setDocuments(docs);
    } else {
      console.error("Error fetching documents:", response.statusText);
    }
  };

  const uploadFiles = async (files) => {
    const formData = new FormData();
    for (let i = 0; i < files.length; i++) {
      formData.append('files', files[i]);
    }

    try {
      const response = await fetch(`${backendURL}/api/documents/upload`, {
        method: 'POST',
        body: formData,
        mode: 'cors'
      });

      if (!response.ok) {
        const errorData = await response.json();
        console.error("Error uploading files:", errorData.message || response.statusText);
        return;
      }

      const newDocs = await response.json();
      setDocuments(prevDocs => [...prevDocs, ...newDocs]);
    } catch (error) {
      console.error("Error uploading files:", error.message);
    }
  }


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