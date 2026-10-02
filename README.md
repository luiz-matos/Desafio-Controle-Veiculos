# 🚗 Desafio Controle Veículos

<div align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk" alt="Java 17">
  <img src="https://img.shields.io/badge/Spring%20Boot-4.1.1-brightgreen?style=for-the-badge&logo=springboot" alt="Spring Boot 4.1.1">
  <img src="https://img.shields.io/badge/PostgreSQL-17-blue?style=for-the-badge&logo=postgresql" alt="PostgreSQL 17">
  <img src="https://img.shields.io/badge/Redis-Cache-red?style=for-the-badge&logo=redis" alt="Redis">
  <img src="https://img.shields.io/badge/JWT-Autentica%C3%A7%C3%A3o-black?style=for-the-badge&logo=jsonwebtokens" alt="JWT">
  <img src="https://img.shields.io/badge/Liquibase-Migrations-2962FF?style=for-the-badge&logo=liquibase" alt="Liquibase">
  <img src="https://img.shields.io/badge/Swagger-OpenAPI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black" alt="Swagger">
  <img src="https://img.shields.io/badge/Licen%C3%A7a-MIT-yellow?style=for-the-badge" alt="Licença MIT">
</div>

<br>

> 🎯 **API REST em Java 17 e Spring Boot 4 para cadastrar veículos e consultar o valor deles em dólar**, com login JWT, perfis de acesso, cotação vinda de duas APIs públicas e cache no Redis.

Fiz o projeto em 2025, para estudar autenticação JWT, cache e integração com APIs externas no Spring Boot. Em 2026 voltei a ele para ver se funcionava de verdade: a conversão para dólar saía 25 vezes maior, os filtros da listagem não filtravam e quase todo erro chegava ao cliente como um 403 vazio.

## 📋 Índice

- [🎓 O que aprendi](#-o-que-aprendi)
- [🚀 Como rodar](#-como-rodar)
- [🧠 Decisões técnicas](#-decisões-técnicas)
- [🔄 Revisitando o projeto em 2026](#-revisitando-o-projeto-em-2026)
- [📄 Licença](#-licença)

## 🎓 O que aprendi

- **Teste com mock pode confirmar o bug.** Os testes antigos passavam porque eram unitários com mocks, e um deles conferia justamente a conta errada do dólar. Passei a escrever o teste pela API primeiro, ver o bug acontecer e só então corrigir.
- **Um detalhe do framework pode desligar uma funcionalidade inteira.** Um `@Query` fixo no `findAll(Specification, Pageable)` descartava a Specification, e os filtros e a ordenação da listagem não faziam nada.
- **Segurança mal configurada esconde todos os erros.** Sem o `authenticationEntryPoint` e com o `/error` exigindo login, dado inválido, rota inexistente e falta de token chegavam iguais, como 403 vazio.
- **Unicidade se garante no banco.** Dez cadastros simultâneos da mesma placa passavam todos pela conferência da aplicação. Com exclusão lógica, o índice único precisou de uma coluna gerada, `placa_ativa`, que funciona igual no PostgreSQL e no H2 dos testes.
- **Dependência externa precisa de plano B e de limite de tempo.** A cotação vem da AwesomeAPI e, se ela falhar ou passar de 30 segundos, da Frankfurter. O Redis fora do ar vira aviso no log, não erro na rota.
- **Refatorar com prova.** Gravei as respostas de 57 chamadas antes de reorganizar o código e comparei depois de cada commit: ficaram idênticas.

## 🚀 Como rodar

Precisa do JDK 17 ou mais novo e do Docker.

```bash
docker compose up -d     # PostgreSQL 17 e Redis
./mvnw spring-boot:run   # API em http://localhost:8080 (no Windows, mvnw.cmd)
```

| Usuário | Senha | Pode |
|---|---|---|
| `user` | `user123` | Consultar |
| `admin` | `admin123` | Consultar, cadastrar, alterar e excluir |

| Rotas | O que fazem |
|---|---|
| `POST /auth/login` | Gera o token, que vai no cabeçalho `Authorization: Bearer <token>` |
| `GET /api/veiculos`, `/api/veiculos/{id}`, `/api/veiculos/relatorios/por-marca` | Consultas, com filtros por marca, ano e preço, paginação e ordenação |
| `POST`, `PUT`, `PATCH` e `DELETE` em `/admin/veiculos` | Cadastro, só para ADMIN |

A documentação fica no Swagger, em `http://localhost:8080/swagger-ui/index.html`. Os 66 testes rodam com `./mvnw test`, sem Docker.

## 🧠 Decisões técnicas

| Decisão | Alternativa | Por quê |
|---|---|---|
| Valor guardado em reais e convertido na resposta | Guardar em dólar | O dado do banco fica estável, e o dólar sai sempre com a cotação do momento |
| Coluna gerada `placa_ativa` com índice único | Índice parcial do PostgreSQL | O H2 dos testes não tem índice parcial; a coluna gerada funciona igual nos dois |
| Envelope próprio de resposta (`message` e `data`) | Problem Details (RFC 9457) | Já existia no projeto original e cobre também o sucesso; o custo é ferramenta pronta não entender o formato |
| Entidade sem setters, que muda por métodos de negócio | Service alterando campo a campo | O `PUT` e o `PATCH` repetiam os seis campos; agora a regra fica na entidade |
| Testes pela API, com H2 e cotação fixa | Testes unitários com mocks | Foram os testes pela API que mostraram os bugs |
| Configuração por variável de ambiente | Valores fixos no código | Banco, Redis e segredo do JWT estavam no código e no `application.properties` |

## 🔄 Revisitando o projeto em 2026

O README prometia mais do que a API entregava. Dos 14 bugs corrigidos, os principais:

| O que estava errado | O que mudou |
|---|---|
| Valor em dólar 25 vezes maior | O mapper multiplicava pela cotação; agora divide |
| Filtros e ordenação da listagem não faziam nada | Filtro de excluídos dentro da Specification e ordenação no `PageRequest` |
| Quase todo erro chegava como 403 vazio | `/error` público e erros no envelope, com 401 para token ausente ou inválido |
| Dez cadastros simultâneos da mesma placa passavam | Índice único no banco |
| Com o Redis fora, nenhuma rota de veículo respondia | O erro do cache vira aviso no log |

## 📄 Licença

[MIT](LICENSE)

---

<div align="center">
  <p>Desenvolvido por <strong>Luiz Matos</strong></p>
  <p>
    <a href="https://github.com/luiz-matos">GitHub</a> •
    <a href="https://www.linkedin.com/in/luizeduardomatos/">LinkedIn</a>
  </p>
</div>
