# 🚗 Desafio Veículos API

<div align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java" alt="Java 17">
  <img src="https://img.shields.io/badge/Spring%20Boot-4.0.0-brightgreen?style=for-the-badge&logo=spring" alt="Spring Boot">
  <img src="https://img.shields.io/badge/PostgreSQL-Database-blue?style=for-the-badge&logo=postgresql" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/JWT-Authentication-red?style=for-the-badge&logo=jsonwebtokens" alt="JWT">
  <img src="https://img.shields.io/badge/Redis-Cache-red?style=for-the-badge&logo=redis" alt="Redis">
</div>

<br>

> 🎯 **API REST robusta para gerenciamento de veículos** com autenticação JWT, cache Redis, conversão de moedas em tempo real e documentação Swagger.

## 📋 Índice

- [✨ Funcionalidades](#-funcionalidades)
- [🛠️ Tecnologias](#️-tecnologias)
- [🚀 Como Executar](#-como-executar)
- [🔐 Autenticação](#-autenticação)
- [📚 Endpoints](#-endpoints)
- [💾 Banco de Dados](#-banco-de-dados)
- [🧪 Testes](#-testes)
- [📖 Documentação](#-documentação)
- [🤝 Contribuição](#-contribuição)

## ✨ Funcionalidades

- 🔐 **Autenticação JWT** com diferentes níveis de acesso
- 🚗 **CRUD completo** de veículos
- 💰 **Conversão automática** de valores para dólar (APIs externas)
- ⚡ **Cache Redis** para otimização de performance
- 📊 **Relatórios** de quantidade por marca
- 🔍 **Filtros avançados** e paginação
- 📝 **Documentação Swagger** interativa
- 🧪 **Testes unitários** abrangentes
- 🗄️ **Migrations** com Liquibase

## 🛠️ Tecnologias

### Backend
- **Java 17** - Linguagem de programação
- **Spring Boot 4.0.0** - Framework principal
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **JWT** - Tokens de autenticação
- **Liquibase** - Controle de versão do banco

### Banco de Dados
- **PostgreSQL** - Banco principal
- **H2** - Banco para testes
- **Redis** - Cache em memória

### Ferramentas
- **Maven** - Gerenciamento de dependências
- **Docker Compose** - Orquestração de containers
- **Swagger/OpenAPI** - Documentação da API
- **Lombok** - Redução de boilerplate

## 🚀 Como Executar

### Pré-requisitos

- ☕ Java 17+
- 🐘 PostgreSQL 13+
- 🔴 Redis 6+
- 🐳 Docker & Docker Compose (opcional)

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/desafio-veiculos.git
cd desafio-veiculos
```

### 2. Configure o banco de dados

**Docker Compose (Obrigatório)**

```bash
docker-compose up -d
```

### 3. Execute a aplicação

```bash
./mvnw spring-boot:run
```

🎉 **Aplicação rodando em:** http://localhost:8080

## 🔐 Autenticação

### 🎫 Gerar Token JWT

**Endpoint:** `POST /auth/login`

**👥 Usuários disponíveis:**

| 🏷️ Tipo | 👤 Username | 🔑 Password | 🛡️ Permissões |
|---------|-------------|-------------|----------------|
| 👤 USER | `user` | `user123` | 📖 Leitura de veículos |
| 👑 ADMIN | `admin` | `admin123` | 📖 Leitura + ✏️ Escrita |

**📝 Exemplo de requisição:**

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

**✅ Resposta:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIkFETUlOIl0sImlhdCI6MTYzOTU4NzYwMCwiZXhwIjoxNjM5NTkxMjAwfQ.signature",
  "type": "Bearer",
  "expiresIn": 3600
}
```

### 🔑 Usar o Token

Adicione o token no header `Authorization` com prefixo `Bearer`:

```bash
curl -X GET http://localhost:8080/api/veiculos \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

## 📚 Endpoints

### 🌐 Públicos

| Método | Endpoint | Descrição |
|--------|----------|----------|
| `POST` | `/auth/login` | 🎫 Gerar token JWT |

### 👤 USER e 👑 ADMIN

| Método | Endpoint | Descrição | Parâmetros |
|--------|----------|-----------|------------|
| `GET` | `/api/veiculos` | 📋 Listar veículos | `page`, `size`, `marca`, `ano` |
| `GET` | `/api/veiculos/{id}` | 🔍 Buscar por ID | - |
| `GET` | `/api/veiculos/estatisticas/marcas` | 📊 Quantidade por marca | - |

### 👑 Apenas ADMIN

| Método | Endpoint | Descrição |
|--------|----------|----------|
| `POST` | `/admin/veiculos` | ➕ Criar veículo |
| `PUT` | `/admin/veiculos/{id}` | ✏️ Atualizar veículo |
| `PATCH` | `/admin/veiculos/{id}` | 🔧 Atualizar parcialmente |
| `DELETE` | `/admin/veiculos/{id}` | 🗑️ Deletar veículo |

### 📝 Exemplo de Payload

**Criar/Atualizar Veículo:**

```json
{
  "veiculo": "Civic",
  "marca": "Honda",
  "ano": 2023,
  "descricao": "Sedan executivo com tecnologia híbrida",
  "valor": 12000.00,
  "placa": "ABC1234"
}
```

**Resposta:**

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "veiculo": "Civic",
  "marca": "Honda",
  "ano": 2023,
  "descricao": "Sedan executivo com tecnologia híbrida",
  "valor": 21818.18,
  "placa": "ABC1234"
}
```

> 💡 **Nota:** O valor é automaticamente convertido para dólar usando APIs externas em tempo real.

## 💾 Banco de Dados

### 📊 Modelo de Dados

```sql
CREATE TABLE veiculos (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    veiculo VARCHAR(100) NOT NULL,
    marca VARCHAR(100) NOT NULL,
    ano INTEGER NOT NULL,
    descricao TEXT,
    valor DECIMAL(15,2),
    placa VARCHAR(8) UNIQUE NOT NULL,
    deletado BOOLEAN DEFAULT FALSE,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 🔄 Migrations

As migrations são gerenciadas pelo **Liquibase** e executadas automaticamente:

- `01-create-table-veiculos.yaml` - Criação da tabela principal
- `02-add-placa-column.yaml` - Adição da coluna placa

## 🧪 Testes

### ▶️ Executar Testes

```bash
# Todos os testes
./mvnw test

# Apenas testes unitários
./mvnw test -Dtest="*Test"

# Com relatório de cobertura
./mvnw test jacoco:report
```

### 📊 Cobertura

- ✅ **Testes Unitários** - Services e Controllers
- ✅ **Testes de Integração** - Endpoints completos
- ✅ **Mocks** - APIs externas e dependências

## 📖 Documentação

### 🔗 Swagger UI (Não funcional)

Acesse a documentação interativa em: http://localhost:8080/swagger-ui.html

### 📋 OpenAPI Spec (Não funcional)

Especificação completa em: http://localhost:8080/v3/api-docs

---

<div align="center">
  <p>Desenvolvido com ❤️ por <strong>Luiz Matos</strong></p>
  <p>
    <a href="https://github.com/luizmatosdev">GitHub</a> •
    <a href="https://linkedin.com/in/luizmatosdev">LinkedIn</a>
  </p>
</div>
