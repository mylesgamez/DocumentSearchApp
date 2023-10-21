# DocumentSearchApp

DocumentSearchApp is a full-stack Java and React application for document management and retrieval. It allows users to upload documents, search for documents based on keywords, and download documents.

<img width="1416" alt="Screenshot 2023-10-21 at 12 00 51 PM" src="https://github.com/mylesgamez/DocumentSearchApp/assets/94767708/0822c96f-3537-4ffb-9f5d-d5e42c81609e">

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

## Setting Up MySQL
1. Download and install MySQL Server from the official MySQL website.
https://dev.mysql.com/downloads/mysql/

2. Start the MySQL server:
```
sudo systemctl start mysql
```

3. Secure your MySQL installation:
```
sudo mysql_secure_installation
```

4. Log in to the MySQL CLI using root:
```
mysql -u root -p
```

5. Create a database for the application:
```
CREATE DATABASE documentsdb;
```

6. Grant all privileges on the database to the user (Change 'root' and 'helloworld' as per your setup):
```
GRANT ALL ON documentsdb.* TO 'root'@'localhost' IDENTIFIED BY 'your password';
```

7. Exit the MySQL CLI:
```
EXIT; 
```

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
  ./gradlew run
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
