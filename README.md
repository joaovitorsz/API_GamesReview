# 🎮 API GamesReview

API REST desenvolvida em **Java** com **Spring Boot** para o gerenciamento e avaliação de jogos digitais. O projeto foi construído em duas etapas principais, evoluindo de uma estrutura de armazenamento temporário em memória para a persistência completa em banco de dados relacional **PostgreSQL** com **Spring Data JPA**[cite: 6].

---

## 📌 Evolução do Projeto

### 🟢 Parte 1: Arquitetura Inicial & Armazenamento em Memória
* **Estrutura Base:** Criação da API REST estruturada no padrão MVC (Controllers, Models e Services).
* **CRUD em Memória:** As operações de cadastro, listagem, edição e exclusão de jogos e avaliações eram armazenadas em listas locais (`List/ArrayList`).
* **Regras de Negócio:** Manipulação básica de relacionamentos entre Jogos e suas respectivas Avaliações.

### 🔵 Parte 2: Persistência com PostgreSQL & JPA
* **Banco de Dados Relacional:** Substituição da lista em memória pelo banco **PostgreSQL**[cite: 6].
* **Mapeamento Objeto-Relacional (ORM):** Implementação de entidades JPA (`@Entity`, `@Table`, `@Id`, `@GeneratedValue`) e mapeamento do relacionamento entre a tabela de jogos e de avaliações[cite: 6].
* **Persistência com Spring Data JPA:** Criação das interfaces Repository para abstrair as consultas e rotas SQL.
* **Busca e Filtros Dinâmicos:** Implementação de rotas para busca por ID e filtros combináveis (por nome, gênero e nota Metacritic)[cite: 6].
* **Automação e Evidências:** Inclusão de coleção do Postman para testes das rotas e pasta de evidências com prints do sistema em execução.

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java 17+
* **Framework:** Spring Boot 3
* **Persistência:** Spring Data JPA / Hibernate
* **Banco de Dados:** PostgreSQL
* **Gerenciador de Dependências:** Maven
* **Ferramentas de Teste:** Postman

---

## 🚀 Endpoints da API

### 🕹️ Gestão de Jogos (`/review`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/review` | Lista todos os jogos cadastrados no banco de dados. |
| `GET` | `/review/{id}` | Busca os detalhes de um jogo específico pelo seu ID. |
| `GET` | `/review/buscar` | Busca filtrada por `nome`, `genero` ou `metacritic` (parâmetros combináveis). |
| `POST` | `/review/gravar-jogo` | Cadastra um novo jogo na base de dados. |
| `PUT` | `/review/{nome}/editar-jogo` | Atualiza as informações de um jogo existente. |
| `DELETE` | `/review/{id}/deletar-jogo` | Remove um jogo e suas avaliações vinculadas do banco. |

### 📝 Gestão de Avaliações

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/review/{nome}/avaliacoes` | Lista todas as avaliações cadastradas para um determinado jogo. |
| `POST` | `/review/{nome}/gravar-avaliacao` | Registra uma nova avaliação atrelada a um jogo específico. |

---

## 🗄️ Configuração do Ambiente

Para rodar a aplicação na sua máquina, certifique-se de ter o **PostgreSQL** rodando e configure as credenciais no arquivo `src/main/resources/application.properties`:

```properties
spring.application.name=GamesReview

# Configurações do PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/gamesreview
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

# Configurações do Hibernate / JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true