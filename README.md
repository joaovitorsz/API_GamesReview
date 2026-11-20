# 🎮 GamesReview

> 📅 **Última atualização:** 20/11/2026

Plataforma para gerenciamento e avaliação de jogos digitais. O projeto conta com uma **API REST completa em Java com Spring Boot**, evoluída de um armazenamento em memória até uma arquitetura em camadas com persistência em **PostgreSQL**, autenticação **JWT**, controle de acesso por perfis (`USER` e `ADMIN`) e configuração de CORS pronta para integração com o frontend em **Angular**.

---

## 📌 Evolução do Projeto

### 🟢 Parte 1: Arquitetura Inicial & Armazenamento em Memória
- **Estrutura Base:** Criação da API REST estruturada no padrão MVC (Controllers, Models e Services).
- **CRUD em Memória:** Operações de cadastro, listagem, edição e exclusão de jogos e avaliações armazenadas em listas locais (`List/ArrayList`).
- **Regras de Negócio:** Manipulação inicial do relacionamento entre Jogos e suas respectivas Avaliações.

### 🔵 Parte 2: Persistência com PostgreSQL & JPA
- **Banco de Dados Relacional:** Substituição das listas em memória pelo **PostgreSQL**.
- **Mapeamento Objeto-Relacional (ORM):** Implementação de entidades JPA (`@Entity`, `@Table`, `@Id`, `@GeneratedValue`) e relacionamento entre jogos e avaliações.
- **Persistência com Spring Data JPA:** Criação das interfaces Repository para abstração das consultas SQL.
- **Busca e Filtros Dinâmicos:** Implementação de buscas e filtros combináveis (por nome, gênero e nota Metacritic) com suporte a expressões regulares (Regex) no PostgreSQL.
- **Automação e Evidências:** Inclusão da coleção do Postman (`GamesReview.postman_collection.json`) para testes das rotas e pasta com evidências em execução.

### 🟣 Parte 3: Segurança com Spring Security, JWT & Gestão de Usuários
- **Camada de Services & DTOs:** Refatoração com separação de responsabilidades entre Controllers, Services (`@Transactional`), Repositories e DTOs (`record`).
- **Autenticação Stateless com JWT:** Geração e validação de tokens assinados com **HMAC-SHA256** (`JwtService`) e interceptação de requisições via `JwtAuthenticationFilter` (`OncePerRequestFilter`).
- **Criptografia de Senhas:** Armazenamento seguro de senhas com **BCrypt** (`BCryptPasswordEncoder`).
- **Controle de Acesso por Perfil (RBAC):** Entidade `Usuario` implementando `UserDetails` e enum `Role` (`USER` e `ADMIN`), liberando o cadastro de jogos e avaliações para usuários autenticados e restringindo a edição e exclusão de jogos no catálogo a administradores.
- **Gestão do Próprio Perfil (`/usuarios/me`):** Rotas autenticadas via `@AuthenticationPrincipal` para visualização, edição de dados (com renovação automática do token JWT) e exclusão da própria conta.
- **CORS para Frontend:** Configuração de `CorsConfigurationSource` liberando requisições a partir do Angular (`http://localhost:4200`).

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 17
- **Framework:** Spring Boot 3.2.1
- **Segurança:** Spring Security & JWT (`jjwt 0.11.5`)
- **Persistência:** Spring Data JPA / Hibernate
- **Banco de Dados:** PostgreSQL
- **Utilitários & Validação:** Spring Boot Validation & Lombok
- **Gerenciador de Dependências:** Maven
- **Ferramentas de Teste:** Postman

---

## 🔐 Controle de Acesso (Perfis)

| Perfil | Permissões |
| :--- | :--- |
| **Público (sem login)** | Cadastro (`/auth/register`), login (`/auth/login`) e consultas (`GET`) de jogos e avaliações. |
| **`USER` (autenticado)** | Todas as permissões públicas + cadastrar novos jogos + criar, editar e excluir avaliações + gerenciar a própria conta (`/usuarios/me`). |
| **`ADMIN` (autenticado)** | Todas as permissões de `USER` + editar e excluir jogos no catálogo. |

> As rotas protegidas exigem o envio do token JWT no header HTTP:
> `Authorization: Bearer <seu_token_jwt>`

---

## 🚀 Endpoints da API

### 🔑 Autenticação (`/auth`)

| Método | Endpoint | Acesso | Descrição |
| :--- | :--- | :--- | :--- |
| `POST` | `/auth/register` | Público | Cadastra um novo usuário (`USER` por padrão) e retorna o token JWT. |
| `POST` | `/auth/login` | Público | Autentica com email e senha e retorna o token JWT e dados do perfil. |

### 👤 Gestão de Conta (`/usuarios`)

| Método | Endpoint | Acesso | Descrição |
| :--- | :--- | :--- | :--- |
| `GET` | `/usuarios/me` | Autenticado | Retorna os dados do usuário logado (`nome`, `email` e `role`). |
| `PUT` | `/usuarios/me` | Autenticado | Atualiza `nome`, `email` e/ou `senha` do usuário logado e retorna um novo token JWT. |
| `DELETE` | `/usuarios/me` | Autenticado | Exclui permanentemente a conta do usuário logado. |

### 🕹️ Gestão de Jogos (`/game/review`)

| Método | Endpoint | Acesso | Descrição |
| :--- | :--- | :--- | :--- |
| `GET` | `/game/review/listar-jogos` | Público | Lista os jogos cadastrados no banco de dados (com suporte a filtros). |
| `POST` | `/game/review/gravar-jogo` | Autenticado | Cadastra um novo jogo na base de dados. |
| `PUT` | `/game/review/editar-jogo/{id}` | `ADMIN` | Atualiza as informações de um jogo existente. |
| `DELETE` | `/game/review/deletar-jogo/{id}` | `ADMIN` | Remove um jogo e suas avaliações vinculadas do banco. |

### 📝 Gestão de Avaliações (`/game/review`)

| Método | Endpoint | Acesso | Descrição |
| :--- | :--- | :--- | :--- |
| `GET` | `/game/review/listar-avaliacoes/{id}` | Público | Lista as avaliações cadastradas para um jogo específico. |
| `POST` | `/game/review/gravar-avaliacao/{id}` | Autenticado | Registra uma nova avaliação atrelada a um jogo específico. |
| `PUT` | `/game/review/editar-avaliacao/{id}` | Autenticado | Atualiza uma avaliação existente. |
| `DELETE` | `/game/review/deletar-avaliacao/{id}` | Autenticado | Remove uma avaliação do banco de dados. |

---

## 🗄️ Configuração do Ambiente

Para rodar a aplicação localmente, certifique-se de ter o **Java 17+**, **Maven** e o **PostgreSQL** rodando. Em seguida, crie o banco `games_review_db` e configure o arquivo `src/main/resources/application.properties`:

```properties
server.port=7979

spring.application.name=GamesReview

# Configurações do PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/games_review_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

# Configurações do Hibernate / JPA
spring.jpa.hibernate.ddl-auto=update
server.servlet.session.persistent=false

# Configurações do JWT (mínimo de 32 caracteres para HS256)
jwt.secret=sua-chave-secreta-com-pelo-menos-32-caracteres
jwt.expiration=86400000
```

### ▶️ Executando o Projeto

```bash
# Clonar o repositório
git clone https://github.com/joaovitorsz/GamesReview.git
cd GamesReview

# Rodar a aplicação com Maven Wrapper
./mvnw spring-boot:run
```

A API ficará disponível em `http://localhost:7979`.

---

## 🧪 Testes com Postman

A coleção completa de requisições está disponível no arquivo `GamesReview.postman_collection.json` na raiz do projeto, organizada nas pastas **`Usuario`**, **`Games`** e **`Avaliações`**.

1. Importe o arquivo no Postman.
2. Execute **`Registrar-usuario`** ou **`Login-usuario`** na pasta `Usuario`.
3. O script de teste da coleção captura e salva automaticamente o JWT retornado na variável `{{token}}` (inclusive quando o token é renovado em `Editar-usuario`).
4. Ajuste `{{gameId}}` e `{{avaliacaoId}}` nas variáveis da coleção conforme os registros do seu banco para testar as rotas de consulta, edição e exclusão.

---

*Documentação publicada e atualizada em **20/11/2026**.*