# CDMS API Testing Guide - Postman

This guide provides comprehensive instructions for testing the CDMS API using Postman, including authentication, error handling, and validation testing.

## Table of Contents
- [Setup](#setup)
- [Authentication Flow](#authentication-flow)
- [Testing All Endpoints](#testing-all-endpoints)
- [Error Handling & Validation](#error-handling--validation)
- [Postman Collection Setup](#postman-collection-setup)

---

## Setup

### Prerequisites
1. Start the CDMS application in IntelliJ
2. Ensure PostgreSQL and Redis are running (via Docker Compose or locally)
3. Application should be running on `http://localhost:8080`

### Initial Database Setup

Before testing, you need to create at least one role in the database:

```sql
-- Connect to your PostgreSQL database and run:
INSERT INTO roles (role_name, permissions) VALUES 
('ADMIN', '{"read": true, "write": true, "delete": true}'::jsonb),
('USER', '{"read": true, "write": false, "delete": false}'::jsonb),
('OFFICER', '{"read": true, "write": true, "delete": false}'::jsonb);
```

---

## Authentication Flow

### Step 1: Register a New User

**Endpoint:** `POST http://localhost:8080/api/auth/register`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "username": "admin",
  "password": "admin123",
  "email": "admin@cdms.com",
  "role": "ADMIN"
}
```

**Expected Response (201 Created):**
```
User registered successfully
```

**Validation Errors to Test:**

1. **Missing Username:**
```json
{
  "password": "admin123",
  "email": "admin@cdms.com",
  "role": "ADMIN"
}
```
Expected: `400 Bad Request` - "Username is required"

2. **Short Password:**
```json
{
  "username": "admin",
  "password": "123",
  "email": "admin@cdms.com",
  "role": "ADMIN"
}
```
Expected: `400 Bad Request` - "Password must be at least 6 characters"

3. **Invalid Email:**
```json
{
  "username": "admin",
  "password": "admin123",
  "email": "invalid-email",
  "role": "ADMIN"
}
```
Expected: `400 Bad Request` - "Email should be valid"

4. **Duplicate Username:**
```json
{
  "username": "admin",
  "password": "admin123",
  "email": "another@cdms.com",
  "role": "ADMIN"
}
```
Expected: `400 Bad Request` - "Username already exists"

---

### Step 2: Login to Get JWT Token

**Endpoint:** `POST http://localhost:8080/api/auth/login`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Expected Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcwMTM1MDAwMCwiZXhwIjoxNzAxNDM2NDAwfQ...",
  "type": "Bearer",
  "username": "admin",
  "role": "ADMIN"
}
```

**Copy the `token` value** - you'll need it for all subsequent requests!

**Login Errors to Test:**

1. **Wrong Password:**
```json
{
  "username": "admin",
  "password": "wrongpassword"
}
```
Expected: `401 Unauthorized` - "Invalid username or password"

2. **Non-existent User:**
```json
{
  "username": "nonexistent",
  "password": "password123"
}
```
Expected: `401 Unauthorized`

---

### Step 3: Using JWT Token in Requests

For all protected endpoints, add this header:

**Headers:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcwMTM1MDAwMCwiZXhwIjoxNzAxNDM2NDAwfQ...
Content-Type: application/json
```

Replace the token with your actual JWT token from the login response.

---

## Testing All Endpoints

### Case Management

#### 1. Create a Case

**Endpoint:** `POST http://localhost:8080/api/cases`

**Headers:**
```
Authorization: Bearer <your-jwt-token>
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "title": "Bank Robbery Case #001",
  "description": "Armed robbery at First National Bank",
  "caseType": "ROBBERY",
  "status": "open",
  "severity": "HIGH",
  "location": "123 Main Street, Downtown",
  "reportedAt": "2024-01-15T10:30:00Z",
  "leadOfficerId": null
}
```

**Expected Response (201 Created):**
```json
{
  "caseId": 1,
  "title": "Bank Robbery Case #001",
  "description": "Armed robbery at First National Bank",
  "caseType": "ROBBERY",
  "status": "open",
  "severity": "HIGH",
  "location": "123 Main Street, Downtown",
  "reportedAt": "2024-01-15T10:30:00Z",
  "leadOfficerId": null,
  "createdAt": "2024-01-15T10:35:00Z"
}
```

**Validation Errors to Test:**

1. **Missing Title:**
```json
{
  "description": "Test case",
  "caseType": "ROBBERY"
}
```
Expected: `400 Bad Request` - "Title is required"

2. **Without Authentication:**
Remove the `Authorization` header
Expected: `401 Unauthorized`

---

#### 2. Get All Cases

**Endpoint:** `GET http://localhost:8080/api/cases`

**Headers:**
```
Authorization: Bearer <your-jwt-token>
```

**Expected Response (200 OK):**
```json
[
  {
    "caseId": 1,
    "title": "Bank Robbery Case #001",
    "description": "Armed robbery at First National Bank",
    "caseType": "ROBBERY",
    "status": "open",
    "severity": "HIGH",
    "location": "123 Main Street, Downtown",
    "reportedAt": "2024-01-15T10:30:00Z",
    "leadOfficerId": null,
    "createdAt": "2024-01-15T10:35:00Z"
  }
]
```

---

#### 3. Get Case by ID

**Endpoint:** `GET http://localhost:8080/api/cases/1`

**Headers:**
```
Authorization: Bearer <your-jwt-token>
```

**Expected Response (200 OK):**
```json
{
  "caseId": 1,
  "title": "Bank Robbery Case #001",
  "description": "Armed robbery at First National Bank",
  "caseType": "ROBBERY",
  "status": "open",
  "severity": "HIGH",
  "location": "123 Main Street, Downtown",
  "reportedAt": "2024-01-15T10:30:00Z",
  "leadOfficerId": null,
  "createdAt": "2024-01-15T10:35:00Z"
}
```

**Error to Test:**

**Non-existent Case:**
`GET http://localhost:8080/api/cases/9999`

Expected: `404 Not Found`
```json
{
  "timestamp": "2024-01-15T10:40:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Case not found with id: '9999'",
  "path": "/api/cases/9999"
}
```

---

#### 4. Update a Case

**Endpoint:** `PUT http://localhost:8080/api/cases/1`

**Headers:**
```
Authorization: Bearer <your-jwt-token>
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "status": "under_investigation",
  "description": "Armed robbery at First National Bank. Suspect identified from CCTV footage.",
  "severity": "CRITICAL"
}
```

**Expected Response (200 OK):**
```json
{
  "caseId": 1,
  "title": "Bank Robbery Case #001",
  "description": "Armed robbery at First National Bank. Suspect identified from CCTV footage.",
  "caseType": "ROBBERY",
  "status": "under_investigation",
  "severity": "CRITICAL",
  "location": "123 Main Street, Downtown",
  "reportedAt": "2024-01-15T10:30:00Z",
  "leadOfficerId": null,
  "createdAt": "2024-01-15T10:35:00Z"
}
```

---

#### 5. Delete a Case

**Endpoint:** `DELETE http://localhost:8080/api/cases/1`

**Headers:**
```
Authorization: Bearer <your-jwt-token>
```

**Expected Response (204 No Content):**
```
Case deleted successfully
```

**Error to Test:**

**Delete Non-existent Case:**
`DELETE http://localhost:8080/api/cases/9999`

Expected: `404 Not Found` - "Case not found with id: '9999'"

---

## Error Handling & Validation

### Common HTTP Status Codes

| Status Code | Meaning | When It Occurs |
|-------------|---------|----------------|
| 200 OK | Success | GET, PUT requests successful |
| 201 Created | Resource created | POST requests successful |
| 204 No Content | Success, no content | DELETE requests successful |
| 400 Bad Request | Validation error | Invalid input data |
| 401 Unauthorized | Authentication failed | Missing/invalid JWT token |
| 404 Not Found | Resource not found | Requested entity doesn't exist |
| 500 Internal Server Error | Server error | Unexpected server issue |

### Testing Validation Errors

#### 1. Test Missing Required Fields

**Create Case Without Title:**
```json
{
  "description": "Test case",
  "caseType": "ROBBERY"
}
```

**Expected Response (400 Bad Request):**
```json
{
  "timestamp": "2024-01-15T10:45:00Z",
  "status": 400,
  "error": "Validation Error",
  "message": "title: Title is required",
  "path": "/api/cases"
}
```

#### 2. Test Invalid Data Types

**Invalid Date Format:**
```json
{
  "title": "Test Case",
  "reportedAt": "invalid-date"
}
```

Expected: `400 Bad Request` with validation error

#### 3. Test Authentication Errors

**No Authorization Header:**
Make any request without the `Authorization` header

**Expected Response (401 Unauthorized):**
```json
{
  "timestamp": "2024-01-15T10:50:00Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/cases"
}
```

**Invalid/Expired Token:**
Use an invalid or expired JWT token

Expected: `401 Unauthorized`

#### 4. Test Resource Not Found

**Get Non-existent Resource:**
`GET http://localhost:8080/api/cases/99999`

**Expected Response (404 Not Found):**
```json
{
  "timestamp": "2024-01-15T10:55:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Case not found with id: '99999'",
  "path": "/api/cases/99999"
}
```

---

## Postman Collection Setup

### Creating a Postman Collection

1. **Create a New Collection:**
   - Click "New" → "Collection"
   - Name it "CDMS API"

2. **Add Environment Variables:**
   - Click "Environments" → "Create Environment"
   - Name it "CDMS Local"
   - Add variables:
     ```
     base_url: http://localhost:8080
     jwt_token: (leave empty, will be set automatically)
     ```

3. **Create Folders:**
   - Authentication
   - Cases
   - Officers
   - Persons
   - Evidence

### Setting Up Auto-Token Management

#### In the Login Request:

**Tests Tab (JavaScript):**
```javascript
// Parse the response
var jsonData = pm.response.json();

// Save the token to environment variable
if (jsonData.token) {
    pm.environment.set("jwt_token", jsonData.token);
    console.log("JWT Token saved:", jsonData.token);
}

// Test assertions
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has token", function () {
    pm.expect(jsonData.token).to.exist;
});
```

#### In All Other Requests:

**Authorization Tab:**
- Type: Bearer Token
- Token: `{{jwt_token}}`

Or in **Headers:**
```
Authorization: Bearer {{jwt_token}}
```

### Sample Request Structure

```
CDMS API/
├── Authentication/
│   ├── Register User
│   └── Login
├── Cases/
│   ├── Create Case
│   ├── Get All Cases
│   ├── Get Case by ID
│   ├── Update Case
│   └── Delete Case
└── Error Testing/
    ├── Missing Auth Token
    ├── Invalid Token
    ├── Validation Errors
    └── Resource Not Found
```

---

## Complete Testing Workflow

### 1. Initial Setup
```
1. Start CDMS application in IntelliJ
2. Ensure database is running
3. Insert roles into database (see Setup section)
4. Open Postman
```

### 2. Authentication Flow
```
1. POST /api/auth/register → Create admin user
2. POST /api/auth/login → Get JWT token
3. Token is automatically saved to environment variable
```

### 3. Test CRUD Operations
```
1. POST /api/cases → Create a case
2. GET /api/cases → List all cases
3. GET /api/cases/{id} → Get specific case
4. PUT /api/cases/{id} → Update case
5. DELETE /api/cases/{id} → Delete case
```

### 4. Test Error Scenarios
```
1. Remove Authorization header → Test 401
2. Use invalid case ID → Test 404
3. Send incomplete data → Test 400 validation
4. Use expired token → Test 401
```

---

## Quick Reference: All Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/auth/register` | Register new user | No |
| POST | `/api/auth/login` | Login and get JWT | No |
| POST | `/api/cases` | Create case | Yes |
| GET | `/api/cases` | Get all cases | Yes |
| GET | `/api/cases/{id}` | Get case by ID | Yes |
| PUT | `/api/cases/{id}` | Update case | Yes |
| DELETE | `/api/cases/{id}` | Delete case | Yes |

---

## Troubleshooting

### Issue: "401 Unauthorized" on all requests
**Solution:** 
- Ensure you've logged in and copied the JWT token
- Check that the Authorization header is set correctly
- Token format: `Bearer <token>` (note the space after "Bearer")

### Issue: "404 Not Found" on valid endpoints
**Solution:**
- Check the application is running on port 8080
- Verify the endpoint URL is correct
- Check application logs in IntelliJ

### Issue: "400 Bad Request" with validation errors
**Solution:**
- Check all required fields are present
- Verify data types match the expected format
- Review the error message for specific field issues

### Issue: Token expired
**Solution:**
- JWT tokens expire after 24 hours (default)
- Simply login again to get a new token
- The new token will automatically replace the old one in Postman

---

## Advanced Testing Tips

### 1. Pre-request Scripts
Add to collection level for automatic token refresh:

```javascript
// Check if token exists
if (!pm.environment.get("jwt_token")) {
    console.log("No token found. Please login first.");
}
```

### 2. Collection-level Tests
Add common assertions:

```javascript
pm.test("Response time is less than 500ms", function () {
    pm.expect(pm.response.responseTime).to.be.below(500);
});

pm.test("Content-Type is JSON", function () {
    pm.response.to.have.header("Content-Type", /application\/json/);
});
```

### 3. Environment Switching
Create multiple environments:
- `CDMS Local` → http://localhost:8080
- `CDMS Dev` → http://dev-server:8080
- `CDMS Prod` → https://api.cdms.com

Switch between them easily in Postman.

---

## Sample Postman Collection JSON

Save this as `CDMS-API.postman_collection.json`:

```json
{
  "info": {
    "name": "CDMS API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Authentication",
      "item": [
        {
          "name": "Register User",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"username\": \"admin\",\n  \"password\": \"admin123\",\n  \"email\": \"admin@cdms.com\",\n  \"role\": \"ADMIN\"\n}"
            },
            "url": {
              "raw": "{{base_url}}/api/auth/register",
              "host": ["{{base_url}}"],
              "path": ["api", "auth", "register"]
            }
          }
        },
        {
          "name": "Login",
          "event": [
            {
              "listen": "test",
              "script": {
                "exec": [
                  "var jsonData = pm.response.json();",
                  "if (jsonData.token) {",
                  "    pm.environment.set(\"jwt_token\", jsonData.token);",
                  "}"
                ]
              }
            }
          ],
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"username\": \"admin\",\n  \"password\": \"admin123\"\n}"
            },
            "url": {
              "raw": "{{base_url}}/api/auth/login",
              "host": ["{{base_url}}"],
              "path": ["api", "auth", "login"]
            }
          }
        }
      ]
    },
    {
      "name": "Cases",
      "item": [
        {
          "name": "Create Case",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{jwt_token}}"
              },
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"title\": \"Bank Robbery Case #001\",\n  \"description\": \"Armed robbery at First National Bank\",\n  \"caseType\": \"ROBBERY\",\n  \"status\": \"open\",\n  \"severity\": \"HIGH\",\n  \"location\": \"123 Main Street, Downtown\"\n}"
            },
            "url": {
              "raw": "{{base_url}}/api/cases",
              "host": ["{{base_url}}"],
              "path": ["api", "cases"]
            }
          }
        },
        {
          "name": "Get All Cases",
          "request": {
            "method": "GET",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{jwt_token}}"
              }
            ],
            "url": {
              "raw": "{{base_url}}/api/cases",
              "host": ["{{base_url}}"],
              "path": ["api", "cases"]
            }
          }
        }
      ]
    }
  ]
}
```

---

## Summary

This guide covers:
- ✅ Complete authentication flow with JWT
- ✅ All CRUD operations for cases
- ✅ Validation error testing
- ✅ Authentication error testing
- ✅ Resource not found testing
- ✅ Postman collection setup with auto-token management
- ✅ Troubleshooting common issues

**Next Steps:**
1. Start your application in IntelliJ
2. Insert roles into the database
3. Import the Postman collection
4. Follow the testing workflow
5. Expand testing to other entities (Officers, Persons, Evidence, etc.)

Happy Testing! 🚀
