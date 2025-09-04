# Simple Notes API

A RESTful API for managing notes built with Spring Boot. This application provides basic CRUD operations for notes with title and content.

## 📋 Table of Contents

- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Configuration](#configuration)
- [Project Structure](#project-structure)

## 🔧 Prerequisites

Before running this application, make sure you have the following installed:

- **Java 11** or higher
- **Maven 3.6+**
- **Git** (for cloning the repository)

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/qybbs/astrapay-spring-boot-external.git
cd astrapay-spring-boot-external
```

### 2. Build the Application
```bash
mvn clean compile
```

### 3. Run the Application
```bash
mvn spring-boot:run
```

**Alternative**: Run using JAR
```bash
mvn clean package
java -jar target/astrapay-spring-boot-external-1.0-SNAPSHOT.jar
```

### 4. Verify Application is Running
- Application will start on: `http://localhost:8000/simple-notes`
- Health check: `GET http://localhost:8000/simple-notes/api/v1/notes`

## 🌐 API Endpoints

Base URL: `http://localhost:8000/simple-notes/api/v1/notes`

### 📝 Notes API

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| `GET` | `/api/v1/notes` | Get all notes | - | List of notes |
| `POST` | `/api/v1/notes` | Create a new note | NoteRequest | Created note |
| `PUT` | `/api/v1/notes/{id}` | Update existing note | NoteRequest | Updated note |
| `DELETE` | `/api/v1/notes/{id}` | Delete a note | - | Success message |

### 📋 Request/Response Format

#### NoteRequest (POST/PUT)
```json
{
  "title": "My Note Title",
  "content": "This is the content of my note"
}
```

#### NoteResponse
```json
{
  "success": true,
  "message": "Note created successfully",
  "data": {
    "id": "b91b6a3e-1234-5678-9abc-def123456789",
    "title": "My Note Title",
    "content": "This is the content of my note",
    "createdAt": "2025-09-04T10:30:00.000+00:00",
    "updatedAt": "2025-09-04T10:30:00.000+00:00"
  }
}
```

#### Error Response
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Title must not be empty"
}
```

## 🧪 Testing

### Run All Tests
```bash
mvn test
```

### Run Specific Test Classes
```bash
# Unit Tests
mvn test -Dtest=NoteServiceImplTest
mvn test -Dtest=NoteControllerTest

# Integration Tests
mvn test -Dtest=NoteControllerIntegrationTest

# Validation Tests
mvn test -Dtest=NoteControllerValidationTest
```

### Test Coverage
- **Unit Tests**: Service and Controller layers
- **Integration Tests**: Full application workflow
- **Validation Tests**: Input validation and error handling

## 📋 Example Usage

### Using cURL

#### Create a Note
```bash
curl -X POST http://localhost:8000/simple-notes/api/v1/notes \
  -H "Content-Type: application/json" \
  -d '{
    "title": "My First Note",
    "content": "This is a test note created via API"
  }'
```

#### Get All Notes
```bash
curl -X GET http://localhost:8000/simple-notes/api/v1/notes
```

#### Update a Note
```bash
curl -X PUT http://localhost:8000/simple-notes/api/v1/notes/{note-id} \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Updated Note Title",
    "content": "Updated content"
  }'
```

#### Delete a Note
```bash
curl -X DELETE http://localhost:8000/simple-notes/api/v1/notes/{note-id}
```

### Using Postman

1. Import the following base URL: `http://localhost:8000/simple-notes`
2. Set `Content-Type: application/json` header for POST/PUT requests
3. Use the endpoints listed above with appropriate HTTP methods

## ⚙️ Configuration

### Application Properties
```properties
# Server Configuration
server.port=8000
server.servlet.context-path=/simple-notes

# CORS Configuration (if needed)
app.cors.allowed-origins=http://localhost:4200
```

### Environment Variables
You can override configuration using environment variables:
```bash
export SERVER_PORT=9000
export CONTEXT_PATH=/notes-api
mvn spring-boot:run
```

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/astrapay/
│   │   ├── controller/          # REST Controllers
│   │   ├── service/             # Business Logic
│   │   ├── model/               # Domain Models
│   │   ├── dto/                 # Data Transfer Objects
│   │   ├── exception/           # Custom Exceptions
│   │   └── constant/            # Constants
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/astrapay/
        ├── controller/          # Controller Tests
        └── service/             # Service Tests
```

## 🛠️ Development

### Hot Reload (Development Mode)
```bash
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=dev"
```

### Build for Production
```bash
mvn clean package -Dmaven.test.skip=true
```

## 🐛 Troubleshooting

### Common Issues

#### Port Already in Use
```bash
# Check what's using port 8000
lsof -i :8000

# Kill the process (replace PID)
kill -9 <PID>

# Or use a different port
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8080
```

#### Java Version Issues
```bash
# Check Java version
java -version

# Should be Java 11 or higher
```

#### Maven Issues
```bash
# Clean and reinstall dependencies
mvn clean install -U
```

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Web MVC](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html)
- [Maven Documentation](https://maven.apache.org/guides/)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is part of the Astrapay Spring Boot base project.

---

**Happy Coding! 🚀**

For questions or issues, please check the troubleshooting section or create an issue in the repository.