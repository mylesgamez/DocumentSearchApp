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

## Cloud Hosting Configuration
To host the app on a cloud provider, you'll need to update several areas in the codebase. 

### Backend - Server Configuration
- WebConfig.java: Replace the ALLOWED_ORIGIN value from http://localhost:3000 with the frontend's cloud URL, e.g., https://yourfrontendapp.cloudprovider.com.
- DocumentController.java: For file storage, rather than storing locally in the "uploads" directory, you'll want to use a cloud storage service like Amazon S3, Google Cloud Storage, etc. This requires refactoring the file upload and download functionalities to interact with the chosen cloud storage.

### Backend - Database Configuration
- Replace the local database connection URL in application.properties with the cloud database connection URL. Update the database username, password, and other required configurations according to your cloud database setup.

### Frontend Configuration
- App.js and DocumentList.js: Replace the backendURL value from http://localhost:8080 with your backend's cloud URL, e.g., https://yourbackendapi.cloudprovider.com.

### Deploying to the Cloud
- Backend: Package your application using the appropriate build tool (e.g., Gradle or Maven). Upload the generated artifact (e.g., a .jar file) to your cloud provider and set up a server instance to run it.
- Frontend: Build your React application using npm run build. The built artifacts will be in the build directory. Deploy these files to a static website hosting service or a cloud provider that supports frontend app hosting. Ensure that both the frontend and backend applications are running and can communicate with each other. Adjust security groups or firewall settings as needed.

### Environment Variables (Recommended for Security):
- Instead of hardcoding URLs and other sensitive configurations, consider using environment variables.
- On the frontend, you can access environment variables in React apps with process.env.REACT_APP_YOUR_VARIABLE_NAME.
- On the backend, in a Spring Boot application, you can use the @Value annotation to inject properties.

### Secure Your Application:
- Ensure that all your APIs and endpoints are secured, especially when deploying to the cloud.
- Use HTTPS for both frontend and backend services.
- Implement authentication and authorization mechanisms to protect your endpoints.
