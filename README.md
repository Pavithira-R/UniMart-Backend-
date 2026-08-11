# UniMart Backend API

This is the backend service for the **UniMart** web application, built with **Spring Boot** and **Java 21**. It provides APIs for user authentication, product management, and wishlist functionality.

---

## 🛠️ Tech Stack & Features

- **Core Framework**: [Spring Boot 4.1.0](https://spring.io/projects/spring-boot)
- **Language**: Java 21
- **Database Access**: Spring Data JPA (Hibernate)
- **Database Migrations**: Flyway Migration
- **Security**: Spring Security with JWT (JSON Web Token) OAuth2 Resource Server
- **Database**: MySQL
- **Build System**: Maven

---

## 🚀 Getting Started

### Prerequisites

To run this project locally, ensure you have the following installed:
- [Java Development Kit (JDK) 21](https://oracle.com/java/technologies/downloads/)
- [Maven 3.8+](https://maven.apache.org/download.cgi)
- [MySQL Database](https://dev.mysql.com/downloads/installer/)

### 1. Environment Configuration

The backend application expects configuration details via environment variables. Create a `.env` file in the root of the `backend/` directory (you can copy [.env.example](.env.example) to start):

```ini
DB_URL=jdbc:mysql://localhost:3306/unimart
DB_USERNAME=your_database_username
DB_PASSWORD=your_database_password
JWT_SECRET=your_super_secret_jwt_key_at_least_256_bits_long
JWT_ACCESS_MINUTES=15
APP_ALLOWED_ORIGINS=http://localhost:5173
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=local
```

### 2. Database Setup

Ensure MySQL is running, and create a database named `unimart`:
```sql
CREATE DATABASE unimart;
```
*Note: Flyway will automatically run database migrations from `src/main/resources/db/migration` when the application starts up.*

### 3. Run the Application

Navigate to the backend directory and execute the following command to start the development server:

#### On Windows (PowerShell):
```powershell
./mvnw.cmd spring-boot:run
```

#### On Linux/macOS:
```bash
chmod +x mvnw
./mvnw spring-boot:run
```

The application will start on port **`8080`** by default (or the custom port configured in `.env`).

---

## 📦 Building for Production

To compile and package the application into a runnable JAR file, execute:

```bash
./mvnw clean package
```

The compiled JAR file will be located inside the `target/` directory.

---

## 📈 Monitoring and Actuator Endpoints

The application exposes monitoring endpoints via **Spring Boot Actuator**:
- **Health check**: `http://localhost:8080/actuator/health`
- **Info**: `http://localhost:8080/actuator/info`

---

## 📁 Project Structure

```
backend/
├── .mvn/                  # Maven Wrapper directory
├── src/
│   ├── main/
│   │   ├── java/         # Java source code
│   │   │   └── lk/ac/kln/unimart/
│   │   │       ├── common/     # Core common resources and controllers
│   │   │       ├── config/     # Security and CORS configurations
│   │   │       └── UniMartBackendApplication.java  # Main entrypoint
│   │   └── resources/
│   │       ├── db/migration/  # Flyway schema migration SQL files
│   │       └── application.yml # Base Spring configurations
│   └── test/              # Unit and integration tests
├── .env.example           # Reference environment template
├── pom.xml                # Maven configuration & dependency manager
└── README.md              # This documentation file
```
