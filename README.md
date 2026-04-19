# 🏦 Simple Banking API

<h4 align="center">
    <p>
        <a href="./README-ptbr.md">Português</a> |
        <b>English</b>
    </p>
</h4>

A simple and robust Banking API built with **Spring Boot 3**, **Java 17**, and **H2 Database**. It follows **Clean Code** and **SOLID** principles, offering structured endpoints for managing clients and performing banking transactions.

## Features
- **Client Management:** Register, update, and search clients by CPF or ID.
- **Account Management:** Checking and savings accounts linked to clients.
- **Transactions:** Deposits, withdrawals, and transfers between accounts.
- **OpenAPI / Swagger:** Fully documented RESTful endpoints and schema validations.
- **Pagination:** Supported for efficient large data retrieval.
- **H2 In-Memory DB:** Fast setup and development testing.

## How to Run

**Requirements:** `Java 17` and `Maven`.

1. Run the application via terminal:
```bash
mvn spring-boot:run
```

2. Access the API documentation (Swagger UI):
`http://localhost:8080/swagger-ui/index.html`

3. Access the database (H2 console):
`http://localhost:8080/h2-console`
*(JDBC URL: `jdbc:h2:mem:bancodb` | User: `sa` | Password: empty)*
