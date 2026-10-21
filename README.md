# 🎮 API GamesReview - Parte 2 (Integração com PostgreSQL)

API REST desenvolvida em **Java** com **Spring Boot** para gerenciamento e avaliação de jogos digitais. Nesta versão, o projeto evoluiu de um armazenamento temporário em memória para uma integração completa com o banco de dados **PostgreSQL** utilizando **Spring Data JPA**.

---

## 🚀 O que mudou nesta versão?

* **Banco de Dados Relacional:** Substituição da lista em memória pela persistência de dados no **PostgreSQL**[cite: 6].
* **Mapeamento Objeto-Relacional (ORM):** Uso de anotações JPA (`@Entity`, `@Table`, `@Id`, `@GeneratedValue`) e relacionamentos entre as tabelas de Jogos e Avaliações[cite: 6].
* **Geração Automática de IDs:** Os IDs das entidades agora são gerados automaticamente pelo banco[cite: 6].
* **Busca por ID e Filtros Combináveis:** Adicionadas rotas de consulta por ID e suporte a filtros dinâmicos (busca por nome, gênero e nota Metacritic)[cite: 6].
* **Coleção do Postman:** Arquivo `GamesReview.postman_collection.json` incluído para facilitar o teste das rotas.

---

## 🛠️ Tecnologias Utilizadas

* **Java 17** (ou superior)
* **Spring Boot 3**
* **Spring Data JPA**
* **PostgreSQL**
* **Maven**
* **Postman** (para testes das requisições)

---

## 📌 Endpoints da API

### 🕹️ Jogos (`/review`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/review` | Lista todos os jogos cadastrados no banco. |
| `GET` | `/review/{id}` | Busca um jogo específico pelo seu ID. |
| `GET` | `/review/buscar` | Busca combinada por `nome`, `genero` ou `metacritic`. |
| `POST` | `/review/gravar-jogo` | Cadastra um novo jogo no banco de dados. |
| `PUT` | `/review/{nome}/editar-jogo` | Atualiza as informações de um jogo existente. |
| `DELETE` | `/review/{id}/deletar-jogo` | Remove um jogo e suas avaliações do banco. |

### 📝 Avaliações

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/review/{nome}/avaliacoes` | Lista todas as avaliações de um jogo específico. |
| `POST` | `/review/{nome}/gravar-avaliacao` | Adiciona uma nova avaliação para um jogo. |

---

## 🗄️ Configuração do Banco de Dados

Para rodar a aplicação localmente, certifique-se de ter o PostgreSQL instalado e configurado no arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gamesreview
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true