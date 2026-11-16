# Academia API

API REST desenvolvida com Java e Spring Boot para gerenciamento de uma academia. O sistema permite administrar alunos, planos, mensalidades, professores, treinos e avaliações físicas, realizando operações de cadastro, consulta, atualização e exclusão.

## Tecnologias utilizadas

- Java 21
- Spring Boot 4.1.2
- Spring Data JPA
- Hibernate
- Banco de dados H2
- Maven
- Jakarta Validation
- JUnit 5
- Spring Boot Test
- MockMvc

## Estrutura do projeto

```text
src/
├── main/
│   ├── java/com/academia/api/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── exception/
│   │   ├── mapper/
│   │   ├── model/
│   │   ├── repository/
│   │   ├── service/
│   │   └── AcademiaApiApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/academia/api/
        └── controller/
```

### Organização dos pacotes

- **controller:** recebe e processa as requisições HTTP.
- **dto:** contém os objetos de transferência de dados.
- **exception:** contém as exceções personalizadas e o tratamento global de erros.
- **mapper:** realiza a conversão entre entidades e DTOs.
- **model:** representa as entidades do sistema.
- **repository:** realiza o acesso e a comunicação com o banco de dados.
- **service:** concentra as regras de negócio.

## Funcionalidades

### Alunos
- Cadastrar, listar, consultar, atualizar e excluir alunos.
- Validar campos obrigatórios.
- Impedir CPF e e-mail duplicados.

### Planos
- Cadastrar, listar, consultar, atualizar e excluir planos.
- Validar nome, duração e valor.

### Mensalidades
- Cadastrar mensalidades vinculadas a alunos e planos.
- Listar, consultar, atualizar e excluir mensalidades.
- Validar valores e campos obrigatórios.
- Verificar a existência dos registros associados.

Status disponíveis:
- `PENDENTE`
- `PAGO`
- `ATRASADO`

### Professores
- Cadastrar, listar, consultar, atualizar e excluir professores.
- Registrar especialidade e informações de contato.
- Validar CPF e e-mail duplicados.

### Treinos
- Cadastrar, listar, consultar, atualizar e excluir treinos.
- Associar treinos a alunos e professores.
- Consultar treinos por aluno.
- Registrar data de criação e situação do treino.

### Avaliações físicas
- Cadastrar, listar, consultar, atualizar e excluir avaliações físicas.
- Associar avaliações aos alunos.
- Registrar peso, altura, percentual de gordura e observações.
- Consultar avaliações por aluno.
- Validar os dados informados.

## Endpoints da API

Todos os endpoints utilizam o endereço base `http://localhost:8080`.

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/alunos` | Cadastrar aluno |
| GET | `/alunos` | Listar alunos |
| GET | `/alunos/{id}` | Buscar aluno |
| PUT | `/alunos/{id}` | Atualizar aluno |
| DELETE | `/alunos/{id}` | Excluir aluno |
| POST | `/planos` | Cadastrar plano |
| GET | `/planos` | Listar planos |
| GET | `/planos/{id}` | Buscar plano |
| PUT | `/planos/{id}` | Atualizar plano |
| DELETE | `/planos/{id}` | Excluir plano |
| POST | `/mensalidades` | Cadastrar mensalidade |
| GET | `/mensalidades` | Listar mensalidades |
| GET | `/mensalidades/{id}` | Buscar mensalidade |
| PUT | `/mensalidades/{id}` | Atualizar mensalidade |
| DELETE | `/mensalidades/{id}` | Excluir mensalidade |
| POST | `/professores` | Cadastrar professor |
| GET | `/professores` | Listar professores |
| GET | `/professores/{id}` | Buscar professor |
| PUT | `/professores/{id}` | Atualizar professor |
| DELETE | `/professores/{id}` | Excluir professor |
| POST | `/treinos` | Cadastrar treino |
| GET | `/treinos` | Listar treinos |
| GET | `/treinos/{id}` | Buscar treino |
| GET | `/treinos/aluno/{alunoId}` | Listar treinos por aluno |
| PUT | `/treinos/{id}` | Atualizar treino |
| DELETE | `/treinos/{id}` | Excluir treino |
| POST | `/avaliacoes-fisicas` | Cadastrar avaliação |
| GET | `/avaliacoes-fisicas` | Listar avaliações |
| GET | `/avaliacoes-fisicas/{id}` | Buscar avaliação |
| GET | `/avaliacoes-fisicas/aluno/{alunoId}` | Listar avaliações por aluno |
| PUT | `/avaliacoes-fisicas/{id}` | Atualizar avaliação |
| DELETE | `/avaliacoes-fisicas/{id}` | Excluir avaliação |

## Banco de dados

O projeto utiliza o H2 Database, configurado em modo persistente por arquivo.

Configurações principais:

- Banco: H2
- Estratégia de atualização: `update`
- Console: `/h2-console`

Acesse o console durante a execução:

`http://localhost:8080/h2-console`

Configurações de conexão:

- JDBC URL: `jdbc:h2:file:./academia-db`
- User Name: `sa`
- Password: deixar vazio.

## Como executar o projeto

### Pré-requisitos

- JDK 21 ou superior.
- IntelliJ IDEA ou outra IDE compatível.
- Git (opcional).

### Clonar o repositório

```bash
git clone URL_DO_REPOSITORIO
```

Entre na pasta:

```bash
cd academia-api
```

### Executar a aplicação

**Windows:**

```powershell
.\mvnw.cmd spring-boot:run
```

**Linux/macOS:**

```bash
./mvnw spring-boot:run
```

Após a inicialização, a API estará disponível em:

`http://localhost:8080`

## Executando os testes

**Windows:**

```powershell
.\mvnw.cmd clean test
```

**Linux/macOS:**

```bash
./mvnw clean test
```

Os testes utilizam JUnit 5, Spring Boot Test e MockMvc para verificar o funcionamento dos endpoints, validações e tratamento de erros.

### Resultado dos testes

Na última execução de `mvnw clean test`, foram obtidos os seguintes resultados:

- 57 testes executados.
- 0 falhas.
- 0 erros.
- 0 testes ignorados.
- Build concluído com sucesso.

## Tratamento de erros

A aplicação possui tratamento global de exceções para padronizar as respostas da API.

Principais códigos HTTP utilizados:

| Código | Significado |
|---|---|
| 200 | Requisição realizada com sucesso |
| 201 | Recurso criado com sucesso |
| 204 | Recurso excluído com sucesso |
| 400 | Dados inválidos ou campos obrigatórios ausentes |
| 404 | Recurso não encontrado |
| 409 | Conflito de dados, como CPF ou e-mail duplicado |

---

**Projeto:** Academia API  
**Finalidade:** Sistema de gerenciamento de uma academia.
