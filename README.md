# Spring Security Role-Based Authentication

A comprehensive Spring Boot application demonstrating JWT-based authentication with role-based access control (RBAC). This project provides a secure, stateless authentication system with support for multiple user roles.

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-Enabled-blue.svg)](https://spring.io/projects/spring-security)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 🚀 Features

- **JWT Authentication** - Stateless authentication using JSON Web Tokens
- **Role-Based Access Control** - Support for Admin, User, and Cashier roles
- **Password Encryption** - BCrypt password hashing for secure credential storage
- **Database Migrations** - Automated schema management with Flyway
- **API Documentation** - Interactive API docs with Swagger/OpenAPI
- **Stateless Security** - No server-side sessions for improved scalability

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Programming Language |
| Spring Boot | 4.0.3 | Application Framework |
| Spring Security | - | Authentication & Authorization |
| Spring Data JDBC | - | Database Operations |
| MySQL | - | Relational Database |
| Flyway | 12.0.3 | Database Migrations |
| JWT (jjwt) | 0.13.0 | Token Generation & Validation |
| Lombok | 1.18.42 | Boilerplate Reduction |
| SpringDoc OpenAPI | 3.0.2 | API Documentation |

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

- **Java 17** or higher
- **Maven 3.6+**
- **MySQL 8.0+**
- **Git** (for cloning the repository)

## ⚙️ Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/amilasuranjith-dev/spring-security-role-based-authentication.git
cd spring-security-role-based-authentication
```

### 2. Configure Environment Variables

Copy the example environment file and configure your settings:

```bash
cp .env.example .env
```

Edit the `.env` file with your database and JWT configuration:

```properties
# Database Configuration
DB_URL=jdbc:mysql://localhost:3306/spring_security_db?createDatabaseIfNotExist=true
DB_USERNAME=root
DB_PASSWORD=your_secure_password

# JWT Configuration
JWT_SECRET=your-very-secret-jwt-key-must-be-at-least-32-characters-long
JWT_EXPIRATION_MS=86400000
```

> ⚠️ **Important:** Never commit the `.env` file to version control. It contains sensitive credentials.

### 3. Create MySQL Database

The application will automatically create the database if it doesn't exist (using `createDatabaseIfNotExist=true`). Alternatively, you can create it manually:

```sql
CREATE DATABASE spring_security_db;
```

### 4. Build and Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

### Swagger UI

Once the application is running, access the interactive API documentation at:

- **Swagger UI:** `http://localhost:8080/swagger-ui.html`

### Authentication Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/auth/register` | Register a new user | Public |
| POST | `/auth/login` | Authenticate user and get JWT token | Public |

### Register New User

**Request:**
```http
POST /auth/register
Content-Type: application/json

{
    "username": "johndoe",
    "email": "john@example.com",
    "password": "securePassword123"
}
```

**Response:**
```json
{
    "message": "User registered successfully"
}
```

### Login

**Request:**
```http
POST /auth/login
Content-Type: application/json

{
    "username": "johndoe",
    "password": "securePassword123"
}
```

**Response:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "username": "johndoe",
    "role": "ROLE_USER"
}
```

### Using the JWT Token

Include the JWT token in the `Authorization` header for protected endpoints:

```http
GET /user/profile
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

## 🔐 Security Configuration

### Available Roles

| Role | Description |
|------|-------------|
| `ROLE_ADMIN` | Administrator with full access |
| `ROLE_USER` | Standard user (default for new registrations) |
| `ROLE_CASHIER` | Cashier role for specific operations |

### Endpoint Authorization

| Endpoint Pattern | Required Role |
|------------------|---------------|
| `/auth/**` | Public (no authentication required) |
| `/swagger-ui/**` | Public |
| `/admin/**` | `ROLE_ADMIN` |
| `/user/**` | `ROLE_USER` |
| `/cashier/**` | `ROLE_CASHIER` |
| All other endpoints | Authenticated users |

### JWT Token Details

- **Algorithm:** HMAC SHA256
- **Default Expiration:** 24 hours (configurable via `JWT_EXPIRATION_MS`)
- **Claims:** username (subject), role

## 📁 Project Structure

```
spring-security-role-based-authentication/
├── pom.xml                                    # Maven configuration
├── .env.example                               # Environment variables template
├── .gitignore                                 # Git ignore rules
└── src/
    └── main/
        ├── java/edu/psy/
        │   ├── Main.java                      # Application entry point
        │   ├── config/
        │   │   └── SecurityConfig.java        # Spring Security configuration
        │   ├── controller/
        │   │   └── AuthController.java        # Authentication REST endpoints
        │   ├── dto/
        │   │   ├── LoginRequest.java          # Login request DTO
        │   │   ├── LoginResponse.java         # Login response DTO
        │   │   ├── RegisterRequest.java       # Registration request DTO
        │   │   └── RegisterResponse.java      # Registration response DTO
        │   ├── model/
        │   │   └── User.java                  # User entity
        │   ├── repository/
        │   │   ├── UserRepository.java        # User repository interface
        │   │   └── impl/
        │   │       └── UserRepositoryImpl.java # Repository implementation
        │   ├── security/
        │   │   ├── JwtAuthenticationFilter.java # JWT filter
        │   │   └── JwtUtil.java               # JWT utilities
        │   ├── service/
        │   │   ├── AuthService.java           # Auth service interface
        │   │   └── impl/
        │   │       └── AuthServiceImpl.java   # Auth service implementation
        │   └── util/
        │       └── Role.java                  # Role enum
        └── resources/
            ├── application.yml                # Application configuration
            └── db.migration/
                └── V1__psy.sql                # Flyway database migration
```

## 🗄️ Database Schema

The application uses Flyway for database migrations. The initial schema creates a `users` table:

```sql
CREATE TABLE users(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ROLE_USER', 'ROLE_ADMIN', 'ROLE_CASHIER') NOT NULL DEFAULT 'ROLE_USER',
    isActive BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 🔧 Configuration Reference

### Application Properties

| Property | Default Value | Description |
|----------|---------------|-------------|
| `DB_URL` | `jdbc:mysql://localhost:3306/spring_security_db` | Database connection URL |
| `DB_USERNAME` | `root` | Database username |
| `DB_PASSWORD` | - | Database password |
| `JWT_SECRET` | - | Secret key for JWT signing (min 32 chars) |
| `JWT_EXPIRATION_MS` | `86400000` | JWT expiration time in milliseconds |

## 🧪 Testing the API

### Using cURL

**Register a new user:**
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@example.com","password":"password123"}'
```

**Login:**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}'
```

**Access protected endpoint:**
```bash
curl -X GET http://localhost:8080/user/profile \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 📞 Contact

For questions or support, please open an issue in the GitHub repository.

---

⭐ **If you find this project helpful, please consider giving it a star!**
