# MCP Farma — Etapa 1

Base de desenvolvimento: Java 21, Spring Boot 3.5.7, PostgreSQL 16, Flyway.

## Pré-requisitos
- JDK 21
- Maven 3.9+
- Docker + Docker Compose

## Executar
```bash
docker compose up -d db
cd backend
mvn spring-boot:run
```

## Verificar
```bash
curl http://localhost:8080/actuator/health
curl 'http://localhost:8080/api/v1/produtos?nome=dipirona'
```

O endpoint retorna `[]` até existir catálogo importado.

## Estado atual
- Banco e tabelas criados automaticamente pelo Flyway na inicialização.
- Entidade Produto e consulta REST de produtos implementadas.
- **Ainda não há importação CSV/XLSX**, cadastro administrativo, autenticação, serviços de preço/promoção, MCP ou IA.
- O endpoint de busca é uma interface técnica inicial, não uma API pública de produção.
- `DB_PASSWORD` é um valor de desenvolvimento; altere antes de qualquer implantação.
- `publicavel` começa `false` por segurança regulatória.

## Próxima etapa
Criar DTOs, validação de GTIN, importação CSV com transações por lote, relatórios de erros e testes de integração. Depois adicionar XLSX via Apache POI, controle de acesso e ferramentas MCP somente leitura.
