# Enterprise Backend Application

A production-ready enterprise-level backend application built with Java 21, Spring Boot 3, and complete security features including JWT authentication, RBAC, and dynamic menu management.

## ✨ Features

### Authentication & Security
- ✅ JWT Access Token & Refresh Token implementation
- ✅ Secure password hashing with BCrypt
- ✅ Login/Logout functionality
- ✅ Forgot Password & Reset Password
- ✅ Change Password
- ✅ Email Verification
- ✅ Account Locking (after 5 failed attempts)
- ✅ Token expiration handling
- ✅ Session invalidation
- ✅ Multi-device login support
- ✅ Remember Me functionality
- ✅ Spring Security with stateless authentication

### User & Role Management
- ✅ Complete User Management (CRUD)
- ✅ Role-Based Access Control (RBAC)
- ✅ Permission-Based Authorization
- ✅ Dynamic Role & Permission assignment
- ✅ Role hierarchy support

### Authorization & Access Control
- ✅ Permission-based API access
- ✅ Method-level security with @PreAuthorize
- ✅ Fine-grained permission control
- ✅ Custom authorization handlers

### Dynamic Menu System
- ✅ Hierarchical menu structure
- ✅ Permission-based menu visibility
- ✅ Dynamic menu rendering for React frontend
- ✅ Menu assignment to roles

### Audit & Logging
- ✅ Complete audit trail tracking
- ✅ JPA Entity auditing
- ✅ Action logging (CREATE, UPDATE, DELETE, LOGIN, LOGOUT)
- ✅ IP address and User-Agent tracking
- ✅ Failed login attempt tracking

### API Features
- ✅ Pagination & Sorting
- ✅ Search functionality
- ✅ Global exception handling
- ✅ Standardized API responses
- ✅ Swagger/OpenAPI documentation
- ✅ API versioning (v1)
- ✅ CORS configuration
- ✅ Comprehensive logging with SLF4J

### Database & Persistence
- ✅ PostgreSQL support
- ✅ Spring Data JPA
- ✅ Hibernate ORM
- ✅ Flyway database migrations
- ✅ Database indexes for performance
- ✅ Relationships and constraints

### Infrastructure
- ✅ Docker support (Dockerfile & Docker Compose)
- ✅ Redis caching for tokens/sessions
- ✅ Environment-based configuration (dev, test, prod)
- ✅ Spring Actuator for monitoring
- ✅ Health checks
- ✅ Scheduled tasks support

### Clean Architecture
- ✅ Layered architecture
- ✅ DTO pattern for data transfer
- ✅ Service & Repository layers
- ✅ Mapper classes for entity conversion
- ✅ Constructor injection
- ✅ SOLID principles
- ✅ Production-ready error handling

## 🏗️ Architecture Overview

```
src/main/java/com/enterprise/
├── EnterpriseBackendApplication.java (Main class)
├── config/                           (Configuration classes)
├── security/                         (JWT, Security filters)
├── auth/                             (Authentication logic)
├── controller/                       (REST endpoints)
├── service/                          (Business logic)
├── service/impl/                     (Service implementations)
├── repository/                       (Data access layer)
├── entity/                           (JPA entities)
├── dto/                              (Data transfer objects)
├── mapper/                           (Entity mappers)
├── exception/                        (Custom exceptions)
├── util/                             (Utility classes)
└── validator/                        (Custom validators)
```

## 🚀 Quick Start

### Prerequisites
- Java 21
- Maven 3.9+
- PostgreSQL 13+
- Redis 6+ (optional for caching)
- Docker & Docker Compose (optional)

### Installation

#### Option 1: Using Docker Compose (Recommended)
```bash
# Clone the repository
git clone https://github.com/achalturkar/backend-base.git
cd backend-base

# Start all services
docker-compose up -d

# Check service status
docker-compose ps

# View application logs
docker-compose logs -f app
```

#### Option 2: Local Development
```bash
# Clone the repository
git clone https://github.com/achalturkar/backend-base.git
cd backend-base

# Update application-dev.yml with your database credentials
vim src/main/resources/application-dev.yml

# Build the project
mvn clean package

# Run the application
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

#### Option 3: Build and Run JAR
```bash
# Build JAR
mvn clean package -DskipTests

# Run JAR
java -jar target/enterprise-backend-1.0.0.jar --spring.profiles.active=dev
```

## 📡 Access the Application

- **API Base URL**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Documentation**: http://localhost:8080/v3/api-docs
- **Health Check**: http://localhost:8080/actuator/health

## 🔐 Default Credentials

```
Email: admin@example.com
Password: Admin@123
```

## 📚 API Documentation

### Authentication Endpoints

#### Login
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@example.com",
    "password": "Admin@123",
    "rememberMe": false
  }'
```

**Response:**
```json
{
  "success": true,
  "statusCode": 200,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
    "tokenType": "Bearer",
    "expiresIn": 900,
    "user": {
      "id": "user-admin-001",
      "username": "admin",
      "email": "admin@example.com",
      "firstName": "Admin",
      "lastName": "User",
      "isActive": true,
      "isVerified": true,
      "lastLoginAt": "2026-05-14T13:45:00",
      "roles": ["ADMIN"],
      "permissions": ["USER_CREATE", "USER_READ", "USER_UPDATE", "USER_DELETE", ...]
    }
  },
  "timestamp": "2026-05-14T13:45:00"
}
```

#### Refresh Access Token
```bash
curl -X POST http://localhost:8080/api/v1/auth/refresh-token \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9..."
  }'
```

#### Logout
```bash
curl -X POST http://localhost:8080/api/v1/auth/logout \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

#### Forgot Password
```bash
curl -X POST http://localhost:8080/api/v1/auth/forgot-password \
  -H "Content-Type: application/json" \
  -d '{"email": "admin@example.com"}'
```

#### Change Password
```bash
curl -X POST http://localhost:8080/api/v1/auth/change-password \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..." \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user-admin-001",
    "oldPassword": "Admin@123",
    "newPassword": "NewPassword@123"
  }'
```

### User Management Endpoints

#### Create User
```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..." \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john.doe",
    "email": "john.doe@example.com",
    "password": "SecurePass@123",
    "firstName": "John",
    "lastName": "Doe",
    "phone": "+1234567890",
    "roleIds": ["role-user-001"]
  }'
```

#### Get All Users (with Pagination)
```bash
curl -X GET "http://localhost:8080/api/v1/users?page=0&size=20&sort=createdAt,desc" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

#### Search Users
```bash
curl -X GET "http://localhost:8080/api/v1/users/search?search=john&page=0&size=20" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

#### Get User by ID
```bash
curl -X GET http://localhost:8080/api/v1/users/{userId} \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

#### Update User
```bash
curl -X PUT http://localhost:8080/api/v1/users/{userId} \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..." \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john.doe",
    "email": "john.doe@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "phone": "+1234567890",
    "isActive": true
  }'
```

#### Delete User
```bash
curl -X DELETE http://localhost:8080/api/v1/users/{userId} \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

#### Assign Roles to User
```bash
curl -X POST http://localhost:8080/api/v1/users/{userId}/roles \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..." \
  -H "Content-Type: application/json" \
  -d '["role-admin-001", "role-user-001"]'
```

#### Get User Roles
```bash
curl -X GET http://localhost:8080/api/v1/users/{userId}/roles \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

#### Get User Permissions
```bash
curl -X GET http://localhost:8080/api/v1/users/{userId}/permissions \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

## 🔑 Authorization

All protected endpoints require a valid JWT token in the Authorization header:

```
Authorization: Bearer <access_token>
```

Permissions are checked at the method level using `@PreAuthorize` annotations:

```java
@PreAuthorize("hasAuthority('USER_CREATE')")
@PostMapping
public ResponseEntity<ApiResponse<UserDto>> createUser(...) { }
```

## 🗄️ Database Schema

### Tables
- **users** - User accounts with authentication details
- **roles** - User roles
- **permissions** - System permissions
- **menus** - Navigation menus
- **user_roles** - User-Role relationships
- **role_permissions** - Role-Permission relationships
- **role_menus** - Role-Menu relationships
- **refresh_tokens** - Token management
- **audit_logs** - Activity audit trail

### Indexes
- email, username (users)
- role_code, role_name (roles)
- permission_code, module (permissions)
- menu_code, parent_menu_id, sequence (menus)
- token, user_id, is_revoked (refresh_tokens)
- user_id, action, entity_type, created_at (audit_logs)

## 🔒 Security Features

- **JWT Authentication**: Secure token-based authentication
- **Password Security**: BCrypt hashing with salt
- **CORS Configuration**: Allowed origins can be configured
- **CSRF Protection**: Stateless authentication
- **SQL Injection Prevention**: Parameterized queries via JPA
- **XSS Prevention**: Input validation and output encoding
- **Rate Limiting Ready**: Extensible for rate limiting
- **Audit Logging**: Complete activity tracking
- **Account Locking**: After failed login attempts
- **Token Expiration**: Access tokens expire in 15 minutes, refresh tokens in 7 days

## 📊 Monitoring & Logging

### Actuator Endpoints
- `/actuator/health` - Application health
- `/actuator/metrics` - Application metrics
- `/actuator/info` - Application info
- `/actuator/prometheus` - Prometheus metrics

### Log Levels
```yaml
root: INFO
com.enterprise: DEBUG
org.springframework.security: DEBUG
org.springframework.web: INFO
org.hibernate.SQL: DEBUG
```

## 🔧 Configuration

### Environment Files

**Development (application-dev.yml)**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/enterprise_db_dev
    username: postgres
    password: postgres
```

**Production (application-prod.yml)**
```yaml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

### Required Environment Variables (Production)
- `DB_URL` - Database connection URL
- `DB_USERNAME` - Database username
- `DB_PASSWORD` - Database password
- `JWT_SECRET_KEY` - JWT secret key (minimum 32 characters)
- `REDIS_HOST` - Redis host
- `REDIS_PORT` - Redis port
- `REDIS_PASSWORD` - Redis password (if required)

## 📦 Deployment

### Docker Deployment
```bash
# Build Docker image
docker build -f docker/Dockerfile -t enterprise-backend:1.0.0 .

# Run container
docker run -d \
  --name enterprise-backend \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_URL=jdbc:postgresql://postgres:5432/enterprise_db \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=postgres \
  -e JWT_SECRET_KEY=your-secret-key-here \
  enterprise-backend:1.0.0
```

### Kubernetes Deployment
The application is Kubernetes-ready. Modify the deployment as needed:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: enterprise-backend
spec:
  replicas: 3
  template:
    spec:
      containers:
      - name: app
        image: enterprise-backend:1.0.0
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: prod
        - name: DB_URL
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: url
```

## 🧪 Testing

```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify

# Run specific test class
mvn test -Dtest=AuthControllerTest

# Generate test coverage report
mvn jacoco:report
```

## 📝 Database Migration

Migrations are handled automatically by Flyway on application startup.

### Manual Migration
```bash
# Apply migrations
mvn flyway:migrate

# Reset database
mvn flyway:clean
```

### Migration Files Location
```
src/main/resources/db/migration/
├── V1__Initial_Schema.sql
├── V2__Seed_Initial_Data.sql
├── V3__Add_New_Feature.sql
└── ...
```

## 🛠️ Development

### Build
```bash
mvn clean package
```

### Run Tests
```bash
mvn test
```

### Generate Code Coverage
```bash
mvn jacoco:report
```

### Check Dependencies
```bash
mvn dependency:tree
```

### Format Code
```bash
mvn spotless:apply
```

## 📋 Checklist for Production

- [ ] Update `application-prod.yml` with production database
- [ ] Set strong JWT secret key (minimum 32 characters)
- [ ] Configure Redis connection
- [ ] Set up SSL/TLS certificates
- [ ] Configure email service for password reset
- [ ] Update CORS allowed origins
- [ ] Enable HTTPS only
- [ ] Configure rate limiting
- [ ] Set up log aggregation
- [ ] Configure monitoring and alerting
- [ ] Run security scan (OWASP ZAP)
- [ ] Load test the application
- [ ] Set up backup strategy
- [ ] Document API endpoints
- [ ] Create incident response plan

## 🐛 Troubleshooting

### Common Issues

#### Port Already in Use
```bash
# Find process using port 8080
lsof -i :8080

# Kill process
kill -9 <PID>
```

#### Database Connection Failed
```bash
# Check PostgreSQL is running
docker-compose ps

# Check logs
docker-compose logs postgres
```

#### JWT Token Expired
- Get a new access token using the refresh token
- Refresh tokens are valid for 7 days

#### User Account Locked
- Account locks for 1 hour after 5 failed login attempts
- Wait or reset password

## 📞 Support & Contribution

For issues, questions, or contributions:
1. Check existing issues on GitHub
2. Create a new issue with detailed description
3. Submit pull requests for improvements

## 📄 License

This project is licensed under the MIT License - see LICENSE file for details.

## 🙏 Acknowledgments

- Spring Boot & Spring Framework
- JWT (JSON Web Tokens)
- PostgreSQL
- Redis
- Docker

---

**Last Updated**: May 14, 2026
**Version**: 1.0.0
**Status**: Production Ready ✅
