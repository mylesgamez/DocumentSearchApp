# DocumentSearchApp

DocumentSearchApp is a full-stack Java and React application for document management and retrieval. It allows users to upload documents, search for documents based on keywords, and download documents.

![Uploading Screenshot 2023-10-21 at 12.00.03 PM.png…]()

## Table of Contents

- [Getting Started](#getting-started)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Contributing](#contributing)
- [License](#license)

## Getting Started

To get started with DocumentSearchApp, follow these instructions.

## Prerequisites

Before running the application, you need to have the following software installed on your system:

- Java Development Kit (JDK)
- Node.js and npm (Node Package Manager)
- MySQL (or another relational database of your choice)

## Installation

### Backend

1. Clone this repository:

   ```bash
   git clone https://github.com/your-username/DocumentSearchApp.git
   ```

2. Navigate to the backend/doc-backend directory:
```cd backend/doc-backend```

3. Configure your database settings in src/main/resources/application.properties.

4. Build and run the Spring Boot backend:
  ```
  ./gradlew bootRun
  ```

5. Navigate to the frontend/doc-frontend directory:
```
npm install
npm start
```
## Usage
Open your web browser and go to http://localhost:3000.

You can now use the DocumentSearchApp to upload, search, and download documents.

## API Endpoints
DocumentSearchApp provides the following RESTful API endpoints:

- GET /api/documents: Get a list of all documents.
- GET /api/documents/search?query={searchQuery}: Search for documents based on a query.
- POST /api/documents: Upload a new document.
- GET /api/documents/{id}: Get details of a specific document.
- DELETE /api/documents/{id}: Delete a document.
- POST /api/documents/upload: Upload one or more documents.
- GET /api/documents/download/{id}: Download a specific document.
