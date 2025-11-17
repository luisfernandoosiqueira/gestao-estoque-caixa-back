# 🧩 API — Gestão de Caixa e Estoque

Aplicação **Spring Boot 3** desenvolvida para gerenciar **estoque**, **caixa (vendas)** e **usuários** de uma empresa fictícia, servindo como backend para o frontend em **Angular 19**.

---

## ⚙️ Tecnologias Utilizadas

- **Java 17+**
- **Spring Boot 3**
  - Spring Web
  - Spring Data JPA
  - Bean Validation
- **Banco H2** (em memória, para testes e desenvolvimento)
- **Maven 3.8+**
- **Jackson** (JSON)
- **springdoc-openapi / Swagger UI** para documentação

---

## 🚀 Executando o Projeto

### 1. Pré-requisitos

- Java **17+**
- Maven **3.8+**

### 2. Clonar o repositório

```bash
git clone https://github.com/luisfernandoosiqueira/gestao-estoque-caixa-back.git
3. Entrar na pasta e executar
cd gestao-estoque-caixa-back
mvn spring-boot:run
4. Acessar a aplicação
•	Swagger UI: http://localhost:8080/swagger-ui.html
•	Console H2: http://localhost:8080/h2-console
As credenciais de acesso ao H2 (URL, usuário, senha) estão definidas em application.properties.
________________________________________
📦 Estrutura do Projeto
src/
 ├── main/
 │   ├── java/app/
 │   │   ├── controller/          → Endpoints REST
 │   │   ├── dto/                 → DTOs de entrada e saída
 │   │   ├── entity/              → Entidades JPA (Produto, Usuario, Venda, ItemVenda, Movimentacao)
 │   │   ├── enums/               → Enums (PerfilUsuario, TipoMovimentacao, etc.)
 │   │   ├── mapper/              → Conversão entre DTOs e Entidades (MapStruct ou manual)
 │   │   ├── repository/          → Repositórios Spring Data JPA
 │   │   ├── service/             → Regras de negócio e validações
 │   │   └── exceptions/          → Exceções de negócio e handler global
 │   │
 │   └── resources/
 │       ├── application.properties
 │       └── data.sql / schema.sql (se utilizados)
 │
 └── test/                        → Testes automatizados (quando aplicável)
________________________________________
🧱 Domínio e Módulos da API
👤 Usuários & Autenticação
•	Entidade: Usuario
o	Campos principais: nomeCompleto, email (único), senha, perfil, ativo
o	Enum PerfilUsuario: ADMINISTRADOR | OPERADOR
•	Endpoints típicos (resumidos):
o	POST /api/usuarios → criar usuário
o	PUT /api/usuarios/{id} → atualizar usuário
o	GET /api/usuarios → listar todos
o	GET /api/usuarios/ativos → listar apenas ativos
o	GET /api/usuarios/perfil/{perfil} → listar por perfil
o	GET /api/usuarios/{id} → buscar por id
o	PATCH /api/usuarios/{id}/inativar → inativar
o	PATCH /api/usuarios/{id}/ativar (se configurado) → reativar
•	Regras:
o	E-mail único.
o	Senha com no mínimo 8 caracteres.
o	Usuário inativo não pode autenticar nem registrar vendas.
o	Usuário não é excluído fisicamente; é marcado como ativo = false.
•	Autenticação:
o	DTOs: LoginRequestDTO, LoginResponseDTO.
o	Endpoint (exemplo): POST /api/login
	Valida e-mail/senha.
	Falha se o usuário estiver inativo.
	Retorna nome, e-mail e perfil do usuário logado.
o	(Não há JWT nesta versão — autenticação simples por sessão front + perfil.)
________________________________________
📦 Produtos
•	Entidade: Produto
o	Campos principais: codigo, nome, categoria, quantidadeEstoque, precoUnitario, ativo
•	Endpoints (resumo):
o	GET /api/produtos
o	GET /api/produtos/{id}
o	POST /api/produtos
o	PUT /api/produtos/{id}
o	(Opcional) PATCH /api/produtos/{id}/inativar
•	Regras:
o	codigo único.
o	quantidadeEstoque não pode ser negativa.
o	precoUnitario ≥ 0.
o	Produtos inativos não devem ser utilizados em novas vendas ou movimentações (regra de negócio aplicada no service).
________________________________________
🧾 Vendas / Caixa
•	Entidades: Venda e ItemVenda
o	Venda: data/hora, usuário responsável, valor total, valor recebido, troco.
o	ItemVenda: produto, quantidade, preço unitário, subtotal.
•	Endpoints típicos:
o	POST /api/vendas → registrar venda
o	GET /api/vendas → listar todas (com ordenação por data desc)
o	GET /api/vendas/{id} → detalhes de uma venda
o	(Opcional) filtros por período, usuário, faixa de valores
•	Regras principais:
o	Usuário da venda deve estar ativo.
o	Produto deve existir e possuir estoque suficiente.
o	valorRecebido ≥ valorTotal.
o	Estoque dos produtos é atualizado conforme os itens da venda.
o	Venda é registrada de forma imutável (sem edição após concluída, na abordagem padrão).
________________________________________
📊 Movimentações de Estoque
•	Entidade: Movimentacao
o	Campos: produto, tipo, quantidade, motivo, data/hora.
o	Enum TipoMovimentacao: ENTRADA, SAIDA, AJUSTE.
•	Endpoints (resumo):
o	POST /api/movimentacoes → registrar movimentação
o	GET /api/movimentacoes → listar todas
o	GET /api/movimentacoes/periodo?inicio=...&fim=... → busca por intervalo de datas
o	(Opcional) filtros por tipo e produto
•	Regras:
o	ENTRADA aumenta estoque.
o	SAIDA reduz estoque (não pode deixar negativo).
o	AJUSTE corrige estoque; a regra de +/− é tratada no service.
o	Campo motivo é opcional, mas limitado para evitar textos muito grandes.
________________________________________
🧠 Padrões de Projeto e Boas Práticas
•	Arquitetura em camadas: Controller → Service → Repository
•	Uso consistente de DTOs para separar modelo de domínio de entrada/saída HTTP.
•	Validações com Bean Validation (@NotBlank, @Size, @Email, etc.).
•	Tratamento centralizado de exceções, com classes como:
o	NegocioException
o	RecursoNaoEncontradoException
o	GlobalExceptionHandler (ou similar)
•	Respostas de erro padronizadas com mensagem legível ao cliente.
•	Documentação automática com Swagger / OpenAPI.
•	Persistência com Spring Data JPA sobre banco H2 em ambiente de desenvolvimento.
________________________________________
🌐 CORS e Integração com o Frontend
•	O projeto está preparado para ser consumido pelo frontend Gestão de Caixa e Estoque (Angular).
•	CORS liberado via @CrossOrigin("*") nos controllers ou configuração global de CORS.
•	Recomenda-se rodar:
o	Backend em http://localhost:8080
o	Frontend em http://localhost:4200
o	Proxy do Angular apontando /api → http://localhost:8080.
________________________________________

