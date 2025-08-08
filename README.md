# API Vendedores

Sistema de gestão de vendedores desenvolvido em Spring Boot 3.2.0 com Java 17.

## Executar

```bash
./mvnw spring-boot:run
```

**URL:** http://localhost:8080

## Tecnologias

- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database
- MapStruct

## Endpoints

- `POST /vendedores` - Criar vendedor
- `GET /vendedores/{id}` - Buscar por ID  
- `PUT /vendedores/{id}` - Atualizar
- `DELETE /vendedores/{id}` - Deletar
- `GET /vendedores` - Listar

## Documentação

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **H2 Console:** http://localhost:8080/h2-console (user: sa, password: vazio)
