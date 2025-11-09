________________________________________
# 🧩 API — Gestão de Estoque e Caixa

Aplicação **Spring Boot 3** desenvolvida para gerenciar **estoque** e **caixa** de uma empresa fictícia.

---

## ⚙️ Tecnologias Utilizadas

- **Java 17+**
- **Spring Boot 3**  
  - Módulos: Web, Data JPA, Validation
- **Banco H2** (em memória, para testes e desenvolvimento)
- **Jackson** → serialização e desserialização JSON
- **OpenAPI / Swagger UI** → documentação automática e interativa

---

## 🚀 Executando o Projeto

### 1. Pré-requisitos
- **Java 17** ou superior instalado  
- **Maven 3.8+**

### 2. Clonar o repositório
```bash
git clone https://github.com/luisfernandoosiqueira/gestao-estoque-caixa-back.git
3. Entrar na pasta e executar
cd gestao-estoque-caixa-back
mvn spring-boot:run
4. Acessar a aplicação
•	Swagger UI: http://localhost:8080/swagger-ui.html
•	Banco H2 Console: http://localhost:8080/h2-console
________________________________________
📦 Estrutura do Projeto
src/
 ├── main/
 │   ├── java/app/
 │   │   ├── controller/      → Endpoints REST
 │   │   ├── dto/             → Objetos de transferência de dados
 │   │   ├── entity/          → Entidades JPA
 │   │   ├── mapper/          → Conversão entre DTOs e Entidades
 │   │   ├── repository/      → Interfaces do Spring Data JPA
 │   │   ├── service/         → Regras de negócio
 │   │   └── exceptions/      → Tratamento de erros personalizados
 │   └── resources/
 │       ├── application.properties
 │       └── static/          → Arquivos estáticos
 └── test/                    → Testes automatizados
________________________________________
🧠 Padrões de Projeto e Boas Práticas
•	Arquitetura em camadas (Controller → Service → Repository)
•	Uso de DTOs para isolamento entre camadas
•	Tratamento centralizado de exceções (GlobalExceptionHandler)
•	Documentação automática com Swagger
•	Persistência via Spring Data JPA
________________________________________
---

