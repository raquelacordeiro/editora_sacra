# Editora Sacra

Aplicação web para gerenciamento do catálogo de livros da Editora Sacra.

## Tecnologias

- Java 17
- Spring Boot 3.5.6
- Maven
- Thymeleaf
- HTML e CSS
- PostgreSQL
- Spring Data JPA
- Flyway

## Configuração

Antes de executar o projeto, é necessário ter o PostgreSQL instalado e o banco de dados `trabalho_av2` configurado com as tabelas utilizadas pela aplicação.

Configure a variável de ambiente `DB_PASSWORD` com a senha do usuário PostgreSQL. A conexão com o banco está definida no arquivo `src/main/resources/application.properties`.

## Como executar

Na pasta do projeto, execute:

```bash
mvn spring-boot:run
```

Depois, acesse:

- Página inicial: http://localhost:8080/
- Catálogo de livros: http://localhost:8080/livros

## Funcionalidades

- Listagem e pesquisa de livros por título
- Cadastro de livros
- Edição de informações
- Exclusão de livros
- Atualização de estoque
- Associação de livros às categorias
- Validação dos dados cadastrados