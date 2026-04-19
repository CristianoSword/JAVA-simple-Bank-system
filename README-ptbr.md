# 🏦 API Bancária Simples

<h4 align="center">
    <p>
        <b>Português</b> |
        <a href="./README.md">English</a>
    </p>
</h4>

Uma API bancária simples e robusta desenvolvida com **Spring Boot 3**, **Java 17** e banco em memória **H2**. Ela foi construída seguindo fortemente os princípios do **Clean Code** e **SOLID**, fornecendo endpoints estruturados para gestão de clientes e transações.

## Funcionalidades
- **Gerenciamento de Clientes:** Cadastro, atualização e consulta de clientes por CPF ou ID.
- **Gestão de Contas:** Vinculação de Contas Corrente e Poupança para os clientes.
- **Transações Financeiras:** Operações de depósito, saque e transferência entre contas.
- **OpenAPI / Swagger:** Documentação interativa cobrindo todos os endpoints.
- **Paginação:** Otimizada para o retorno de listas grandes.
- **H2 In-Memory DB:** Dispensa configurações complexas para testar localmente.

## Como Executar

**Requisitos:** `Java 17` e `Maven`.

1. Inicie a aplicação via terminal:
```bash
mvn spring-boot:run
```

2. Acesse a documentação interativa (Swagger UI):
`http://localhost:8080/swagger-ui/index.html`

3. Acesse o console do banco (H2 console):
`http://localhost:8080/h2-console`
*(JDBC URL: `jdbc:h2:mem:bancodb` | Usuário: `sa` | Senha: vazio)*
