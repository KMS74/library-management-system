# Library Management System

A RESTful Web Service built with **Spring Boot**, **Spring Data JPA**, and **H2 In-Memory Database** for managing books, library patrons, and book borrowing/return workflows.

---

## 🛠️ Tech Stack

- **Java**: 17
- **Framework**: Spring Boot 4.x
- **Persistence**: Spring Data JPA / Hibernate
- **Database**: H2 In-Memory Database (`jdbc:h2:mem:testdb`)
- **Validation**: Jakarta Bean Validation (`spring-boot-starter-validation`)
- **Boilerplate Reduction**: Project Lombok
- **Build Tool**: Apache Maven

---

## 📋 Features & Business Logic

- **Book Catalog Management**: Create, view, update, delete, and filter books by genre.
- **Member Directory**: Register and manage library patrons and their membership periods.
- **Circulation & Inventory Control**:
  - **Borrowing Books**: Automatically verifies availability (`availableCopies > 0`), decreases available inventory, and issues a borrowing record.
  - **Returning Books**: Validates active loan status, updates the return date, and increments book availability.
  - **Active Loans Querying**: Quickly view active checkouts (`returnDate IS NULL`), or trace loans by member or book ID.
- **Validation & Exception Handling**: Centralized global exception handler mapping business errors (e.g. out-of-stock, double-return) to `400 Bad Request` and missing entities to `404 Not Found`.

---

## 🚀 Getting Started

### Prerequisites
- JDK 17 or later installed
- Git

### Build and Run

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd rest-demo
   ```

2. **Run tests:**
   - **Linux / macOS:**
     ```bash
     ./mvnw test
     ```
   - **Windows:**
     ```cmd
     mvnw.cmd test
     ```

3. **Start the application:**
   - **Linux / macOS:**
     ```bash
     ./mvnw spring-boot:run
     ```
   - **Windows:**
     ```cmd
     mvnw.cmd spring-boot:run
     ```

The application will start on `http://localhost:8080`.

---

## 📖 API Documentation

### 1. Books (`/api/books`)

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/books` | Get all books (supports optional `?genre=` filter) |
| `GET` | `/api/books/{id}` | Get book by ID |
| `POST` | `/api/books` | Create a new book |
| `PUT` | `/api/books/{id}` | Update an existing book |
| `DELETE` | `/api/books/{id}` | Delete a book |

#### Example: Create Book Request (`POST /api/books`)
```json
{
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "publicationYear": 2008,
  "genre": "Software Engineering",
  "availableCopies": 5
}
```

---

### 2. Members (`/api/members`)

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/members` | Get all members |
| `GET` | `/api/members/{id}` | Get member by ID |
| `POST` | `/api/members` | Register a new member |
| `PUT` | `/api/members/{id}` | Update member details |
| `DELETE` | `/api/members/{id}` | Delete a member |

#### Example: Create Member Request (`POST /api/members`)
```json
{
  "name": "Jane Doe",
  "email": "jane.doe@example.com",
  "phoneNumber": "1234567890",
  "startDate": "2026-01-01",
  "endDate": "2027-01-01"
}
```

---

### 3. Borrowing Records (`/api/borrowings`)

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/borrowings` | Retrieve all borrowing records |
| `GET` | `/api/borrowings/{id}` | Retrieve borrowing record by ID |
| `GET` | `/api/borrowings/active` | Retrieve all unreturned (active) borrowings |
| `GET` | `/api/borrowings/member/{memberId}` | Get all borrowings by member |
| `GET` | `/api/borrowings/book/{bookId}` | Get circulation history for a book |
| `POST` | `/api/borrowings/borrow` | Borrow a book (decrements available copies) |
| `POST` | `/api/borrowings/{id}/return` | Return a book (increments available copies) |
| `DELETE` | `/api/borrowings/{id}` | Delete a borrowing record |

#### Example: Borrow Book Request (`POST /api/borrowings/borrow`)
```json
{
  "bookId": 1,
  "memberId": 1,
  "dueDate": "2026-10-20"
}
```

---

## 🗄️ Project Structure

```text
src/
├── main/
│   ├── java/com/example/restdemo/
│   │   ├── LibraryManagementApplication.java  # Main application entry point
│   │   ├── controller/                         # REST controllers
│   │   ├── dto/                                # Data transfer objects & request payloads
│   │   ├── exception/                          # Custom exceptions & global handler
│   │   ├── model/                              # JPA entity models
│   │   ├── repository/                         # Spring Data JPA repositories
│   │   └── service/                            # Core business logic services
│   └── resources/
│       └── application.properties             # App & datasource configuration
└── test/
    └── java/com/example/restdemo/             # Integration & unit test suites
```
