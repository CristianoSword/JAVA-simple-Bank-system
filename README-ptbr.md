# Sistema Bancário API

<h4 align="center">
    <p>
        <b>Português</b> |
        <a href="./README.md">English</a>
    </p>
</h4>

Uma API RESTful para um sistema bancário simples, construída com Spring Boot, focada em boas práticas como **Clean Architecture**, **SOLID** e **Clean Code**.

## Tecnologias e Ferramentas

- **Java 17**
- **Spring Boot 3.2.5** (Web, Data JPA, Validation)
- **H2 Database** (em memória para DEV e persistente para PROD)
- **Springdoc OpenAPI (Swagger)** para documentação interativa
- **Lombok** para redução de boilerplate
- **JUnit 5 / Mockito** para testes unitários
- **TestRestTemplate / @SpringBootTest** para testes de integração
- **JPA Auditing** para auditoria automática de criação e modificação

## Funcionalidades e Regras de Negócio

- **Clientes**: Abertura associada com Conta; validação customizada de CPF; listagem paginada (`Pageable`).
- **Contas**: Suporte a Tipos de Conta (CORRENTE/POUPANCA).
- **Operações**: Saque, Depósito e Transferência com cálculo atomico de saldos.
- **Transações**: Registro automático e auditável das movimentações; extrato paginado.
- **Tratamento de Erros**: Handler global (`@ControllerAdvice`) gerenciando mensagens ricas para o cliente, validações `@Valid` de campos, etc.
- **Logging Estruturado**: Interceptação sistêmica com SLF4J em cada passo transacional.

## Padrões Arquiteturais e Design

- **Segregação por Camadas**: Divisão em `domain`, `application`, `presentation` e `infrastructure`.
- **"Tell, Don't Ask"**: As lógicas de negócio do domínio (ex: depósito e saque de saldo) estão englobadas de forma coesa diretamente na classe relacional `Conta`.
- **DTOs & Record**: Proteção da camada de aplicação transacional usando contratos isolados (Request/Response) imutáveis via Records no Java.

## Como Executar (Localmente - DEV)

A aplicação conta com dois perfis ativos principais configurados: `dev` (padrão) e `prod`.

1. Clone o repositório
2. Execute o comando Maven na raiz:
   ```bash
   mvn spring-boot:run
   ```
3. A API estará disponível em `http://localhost:8080/api`.

### Swagger UI (Apenas DEV)
- Acesse `http://localhost:8080/docs` para inspecionar, gerar mocks e interagir com os endpoints livremente.

### Console do Banco H2
- Acesse `http://localhost:8080/h2-console`
- Usuário: `sa`
- Senha: *(vazio)*
- JDBC URL: `jdbc:h2:mem:bancodb`

## Como Executar os Testes

Os testes desta API são compostos por:
- **Unitários**: Testes com mocks independentes injetados do Mockito simulando os contratos das camadas `infrastructure` (repositories), assegurando as condicionais lógicas de Exceções `@Throw`.
- **Integração**: Ambiente web randomizado que constroi o container Spring e executa os testes chamando endpoints diretamente nos Controllers usando `TestRestTemplate`, atestando a integridade das persistências e comportamentos de `HttpStatus`.

```bash
mvn test
```

---
*Este projeto foi concluído como prova de conceito incremental envolvendo 30 etapas minuciosas de evolução estrutural e boas práticas corporativas de backend.*
