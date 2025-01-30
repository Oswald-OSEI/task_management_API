# Task Management API

A Spring Boot REST API for managing personal tasks with JWT authentication.

## Project Overview

This application provides a secure task management system where users can create, update, delete, and filter tasks. Each task includes properties such as title, priority, status, and deadline. The system ensures that users can only access and modify their own tasks through JWT-based authentication.

## Technical Stack

- Java Spring Boot
- Spring Security with JWT Authentication
- Spring Data JPA
- Lombok
- JSON Web Token (JWT)

## Project Structure

```
taskmanagement/
├── common/
│   ├── dto/
│   │   ├── ApiResponseDto.java         # Generic API response wrapper
│   │   └── ErrorResponseDto.java       # Error response structure
│   ├── exceptions/
│   │   ├── GlobalExceptionHandler.java # Central exception handling
│   │   ├── InvalidAuthorizationHeaderException.java
│   │   └── TokenValidationExceptionWrapper.java
│   └── security/
│       ├── jwt/                        # JWT authentication components
│       │   ├── CustomUserDetails.java
│       │   ├── CustomUserDetailsService.java
│       │   ├── JwtAuthenticationEntryPoint.java
│       │   ├── JwtAuthFilter.java
│       │   └── TokenService.java
├── taskmanagementservice/
│   ├── controller/
│   │   ├── TaskController.java        # Main task CRUD operations
│   │   └── TaskFilterController.java   # Task filtering endpoints
│   ├── dto/
│   │   └── TaskDto.java               # Task data transfer object
│   ├── entity/
│   │   └── TaskEntity.java            # Task database entity
│   ├── enums/
│   │   ├── Priority.java              # Task priority levels
│   │   └── Status.java                # Task status options
│   ├── service/
│   │   ├── TaskService.java           # Task business logic
│   │   └── TaskFilterService.java     # Task filtering logic
│   └── repository/
│       └── TaskRepository.java        # Task data access layer
└── userservice/
    ├── controller/
    │   ├── AuthenticationController.java
    │   └── RegistrationController.java
    ├── dto/
    │   ├── AccessTokenRequest.java
    │   ├── LoginRequestDto.java
    │   ├── LoginResponseDto.java
    │   ├── SignUpRequestDto.java
    │   └── UserDetailDto.java
    ├── entity/
    │   └── UserEntity.java
    ├── exceptions/
    │   ├── InvalidUserCredentialException.java
    │   └── SignUpException.java
    ├── repository/
    │   └── UserRepository.java
    └── service/
        ├── AuthenticationService.java
        └── SignUpService.java
```

## Test File Locations

### Security Tests
Located under `src/test/java/taskmanagement/taskmanagementapi/common/security/jwt/`:
- `CustomUserDetailServiceTest.java` - Tests for user detail loading and validation
- `JwtAuthenticationEntryPointTest.java` - Tests for unauthorized access handling
- `JwtAuthFilterTest.java` - Tests for JWT token filtering and validation
- `TokenServiceTest.java` - Tests for JWT token generation and validation

### Task Management Tests
Located under `src/test/java/taskmanagement/taskmanagementapi/taskmanagementservice/`:
- Controller Tests:
  - `TaskControllerTest.java` - Tests for task CRUD operations
  - `TaskFilterControllerTest.java` - Tests for task filtering functionality
- Service Tests:
  - `TaskServiceTest.java` - Tests for task management business logic
  - `TaskFilterServiceTest.java` - Tests for task filtering logic

### User Service Tests
Located under `src/test/java/taskmanagement/taskmanagementapi/userservice/`:
- Controller Tests:
  - `AuthenticationControllerTest.java` - Tests for login and token refresh
  - `RegistrationControllerTest.java` - Tests for user registration
- Service Tests:
  - `AuthenticationServiceTest.java` - Tests for authentication logic
  - `SignUpServiceTest.java` - Tests for user registration logic

## Data Models

### Task
```json
{
  "id": "long",
  "title": "string",
  "priority": "enum(HIGH, MEDIUM, LOW)",
  "status": "enum(PENDING, IN_PROGRESS, COMPLETED)",
  "deadline": "LocalDate",
  "creatorId": "long"
}
```

### User
```json
{
  "id": "long",
  "username": "string",
  "password": "string",
  "tasks": "List<TaskEntity>"
}
```

## API Endpoints

### Task Management Endpoints

#### Create Tasks
```
POST /v1/task-management/create
Authorization: Bearer {jwt-token}
```
- Creates 1-5 tasks in a single request
- Request body: List of TaskDto objects

### User Management Endpoints

#### Sign Up
```
POST /v1/task-management/user/sign-up
```
- Creates a new user account
- Request body: SignUpRequestDto

#### Login
```
POST /v1/task-management/auth/login
```
- Authenticates user and returns tokens
- Request body: LoginRequestDto

#### Refresh Token
```
POST /v1/task-management/auth/token/refresh
```
- Generates new access token using refresh token
- Request body: AccessTokenRequest

## Security

The application implements JWT-based authentication with the following features:
- Access tokens valid for 30 minutes
- Refresh tokens valid for 1 day
- Token-based user validation for all task operations
- Task operation authorization ensures users can only access their own tasks

## Response Format

### Success Response
```json
{
  "statusCode": "int",
  "message": "string",
  "data": "object"
}
```

### Error Response
```json
{
  "statusCode": "int",
  "status": "HttpStatus",
  "message": "string"
}
```

## Getting Started

1. Configure application.properties:
   ```properties
   jwt.secret=your_jwt_secret_key
   jwt.refresh.secret=your_refresh_token_secret
   ```

2. Set up your database configuration

3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

4. Run tests:
   ```bash
   ./mvnw test
   ```

## Pagination

The API supports pagination for list endpoints with the following parameters:
- `page`: Page number (0-based)
- `size`: Number of items per page
- `sort`: Sort criteria

Example:
```
GET /v1/task-management/all-tasks?page=0&size=10&sort=deadline,desc
```