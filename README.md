# Simple Banking API

<h4 align="center">
    <p>
        <a href="./README-ptbr.md">Português</a> |
        <b>English</b>
    </p>
</h4>

A RESTful API for a simple banking system, built with Spring Boot, heavily focused on best practices like **Clean Architecture**, **SOLID** principles, and **Clean Code**.

## Technologies and Tools

- **Java 17**
- **Spring Boot 3.2.5** (Web, Data JPA, Validation)
- **H2 Database** (In-memory for DEV, persistent for PROD)
- **Springdoc OpenAPI (Swagger)** for interactive documentation
- **Lombok** to reduce boilerplate
- **JUnit 5 / Mockito** for unit testing
- **TestRestTemplate / @SpringBootTest** for integration testing
- **JPA Auditing** for automatic creation/modification timestamps tracking

## Features and Business Rules

- **Clients**: Account creation associated with client registration; custom CPF validation; paginated listing (`Pageable`).
- **Accounts**: Support for Account Types (CHECKING/SAVINGS).
- **Operations**: Withdrawals, Deposits, and Transfers with atomic balance calculations.
- **Transactions**: Automatic and auditable recording of movements; paginated bank statements.
- **Error Handling**: Global Exception Handler (`@ControllerAdvice`) managing rich messages to the client, `@Valid` field validations, etc.
- **Structured Logging**: Systemic interception with SLF4J at every transactional step.

## Architectural Patterns and Design

- **Layer Segregation**: Code divided clearly into `domain`, `application`, `presentation`, and `infrastructure` layers.
- **"Tell, Don't Ask"**: Domain business logic (e.g., depositing and withdrawing balance) is cohesively encapsulated directly inside the relational `Conta` (Account) class.
- **DTOs & Records**: Protection of the transactional application layer using isolated, immutable contracts (Request/Response) via Java Records.

## How to Run (Locally - DEV)

The application has two main active profiles configured: `dev` (default) and `prod`.

1. Clone the repository
2. Run the Maven command at the root:
   ```bash
   mvn spring-boot:run
   ```
3. The API will be available at `http://localhost:8080/api`.

### Swagger UI (DEV Only)
- Access `http://localhost:8080/docs` to inspect, generate mock requests, and freely interact with the endpoints.

### H2 Database Console
- Access `http://localhost:8080/h2-console`
- Username: `sa`
- Password: *(empty)*
- JDBC URL: `jdbc:h2:mem:bancodb`

## How to Run the Tests

The tests for this API consist of:
- **Unit Tests**: Tests running with injected independent mocks from Mockito, simulating `infrastructure` contracts (repositories) to ensure logic conditions and `@Throw` Exceptions.
- **Integration Tests**: Randomized web environment that builds the full Spring container and executes tests calling endpoints directly on Controllers using `TestRestTemplate`, verifying database persistence integrity and precise `HttpStatus` behaviors.

```bash
mvn test
```

---
*This project was completed as an incremental proof of concept involving 30 meticulous steps of structural evolution and corporate backend best practices.*
