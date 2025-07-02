O React roda na porta 3000 (com npm start).

O Spring Boot roda na porta 8080.

# Pass-Hub: Documentação Técnica

## 1. Introdução

O Pass-Hub é um sistema de gerenciamento de senhas projetado para fornecer uma solução segura e centralizada para armazenamento e acesso a credenciais. Esta documentação técnica detalha a arquitetura, os componentes, a configuração e o processo de implantação do projeto.

## 2. Arquitetura da Solução

O Pass-Hub segue uma arquitetura cliente-servidor desacoplada, consistindo em dois componentes principais:

- **Frontend:** Uma aplicação de página única (SPA) desenvolvida em React, responsável pela interface do usuário e interação.
- **Backend:** Uma API RESTful desenvolvida com Spring Boot, que lida com a lógica de negócios, autenticação, autorização e persistência de dados.
- **Banco de Dados:** O MySQL é utilizado como sistema de gerenciamento de banco de dados relacional para armazenar os dados da aplicação.

## 3. Backend (Spring Boot)

O backend é construído com o framework Spring Boot e utiliza as seguintes dependências principais:

- **Spring Boot Starter Data JPA:** Para persistência de dados com a Java Persistence API (JPA).
- **Spring Boot Starter Security:** Para autenticação e autorização. A segurança é reforçada com JSON Web Tokens (JWT).
- **Spring Boot Starter Web:** Para a criação de APIs RESTful.
- **MySQL Connector/J:** Driver JDBC para comunicação com o banco de dados MySQL.
- **Lombok:** Para reduzir a verbosidade do código Java.
- **SpringDoc OpenAPI:** Para a geração automática de documentação da API no padrão OpenAPI 3 (Swagger).
- **JSON Web Token (jjwt):** Para a criação e validação de tokens JWT.
- **Google Authenticator:** Para a implementação de autenticação de dois fatores (2FA).

### 3.1. Configuração do Ambiente de Backend

1.  **Java 17:** Certifique-se de ter o JDK 17 instalado.
2.  **Maven:** Utilize o Maven para gerenciamento de dependências e build do projeto.
3.  **MySQL:** Configure uma instância do MySQL e crie um banco de dados para a aplicação.
4.  **Configuração da Aplicação:** As propriedades da aplicação, incluindo a conexão com o banco de dados, são definidas em `src/main/resources/application.yml`. É crucial configurar corretamente `spring.datasource.url`, `spring.datasource.username`, e `spring.datasource.password`.

### 3.2. Documentação da API (Swagger)

Com a dependência `springdoc-openapi-starter-webmvc-ui`, a documentação da API é gerada automaticamente e pode ser acessada em:

`http://localhost:8080/swagger-ui.html`

Esta interface permite visualizar e interagir com todos os endpoints da API.

## 4. Frontend (React)

O frontend é uma aplicação React que consome a API do backend. A estrutura do projeto segue o padrão do Create React App.

### 4.1. Configuração do Ambiente de Frontend

1.  **Node.js e npm:** Instale o Node.js e o gerenciador de pacotes npm.
2.  **Instalação de Dependências:** Navegue até o diretório `passhub-frontend` e execute `npm install` para instalar todas as dependências listadas no `package.json`.

## 5. Build e Implantação

### 5.1. Build

- **Backend:** Para gerar o arquivo JAR do backend, execute `mvn clean package` no diretório `back`.
- **Frontend:** Para gerar os arquivos estáticos otimizados para produção, execute `npm run build` no diretório `passhub-frontend`.




# Pass-Hub: Documentação Técnica

## 1. Introdução

O Pass-Hub é um sistema de gerenciamento de senhas projetado para fornecer uma solução segura e centralizada para armazenamento e acesso a credenciais. Esta documentação técnica detalha a arquitetura, os componentes, a configuração e o processo de implantação do projeto.

## 2. Arquitetura da Solução

O Pass-Hub segue uma arquitetura cliente-servidor desacoplada, consistindo em dois componentes principais:

- **Frontend:** Uma aplicação de página única (SPA) desenvolvida em React, responsável pela interface do usuário e interação.
- **Backend:** Uma API RESTful desenvolvida com Spring Boot, que lida com a lógica de negócios, autenticação, autorização e persistência de dados.
- **Banco de Dados:** O MySQL é utilizado como sistema de gerenciamento de banco de dados relacional para armazenar os dados da aplicação.

## 3. Backend (Spring Boot)

O backend é construído com o framework Spring Boot e utiliza as seguintes dependências principais (conforme `pom.xml`):

- **Spring Boot Starter Data JPA:** Para persistência de dados com a Java Persistence API (JPA).
- **Spring Boot Starter Security:** Para autenticação e autorização. A segurança é reforçada com JSON Web Tokens (JWT).
- **Spring Boot Starter Web:** Para a criação de APIs RESTful.
- **MySQL Connector/J:** Driver JDBC para comunicação com o banco de dados MySQL.
- **Lombok:** Para reduzir a verbosidade do código Java.
- **SpringDoc OpenAPI:** Para a geração automática de documentação da API no padrão OpenAPI 3 (Swagger).
- **JSON Web Token (jjwt):** Para a criação e validação de tokens JWT.
- **Google Authenticator:** Para a implementação de autenticação de dois fatores (2FA).

### 3.1. Configuração do Ambiente de Backend

1.  **Java 17:** Certifique-se de ter o JDK 17 instalado.
2.  **Maven:** Utilize o Maven para gerenciamento de dependências e build do projeto.
3.  **MySQL:** Configure uma instância do MySQL e crie um banco de dados para a aplicação.
4.  **Configuração da Aplicação:** As propriedades da aplicação, incluindo a conexão com o banco de dados, são definidas em `src/main/resources/application.yml`. É crucial configurar corretamente `spring.datasource.url`, `spring.datasource.username`, e `spring.datasource.password`.

### 3.2. Documentação da API (Swagger)

Com a dependência `springdoc-openapi-starter-webmvc-ui`, a documentação da API é gerada automaticamente e pode ser acessada em:

`http://localhost:8080/swagger-ui.html`

Esta interface permite visualizar e interagir com todos os endpoints da API.

## 4. Frontend (React)

O frontend é uma aplicação React que consome a API do backend. A estrutura do projeto segue o padrão do Create React App.

### 4.1. Configuração do Ambiente de Frontend

1.  **Node.js e npm:** Instale o Node.js e o gerenciador de pacotes npm.
2.  **Instalação de Dependências:** Navegue até o diretório `passhub-frontend` e execute `npm install` para instalar todas as dependências listadas no `package.json`.

## 5. Executando a Aplicação

Para iniciar os componentes do Pass-Hub, siga as instruções abaixo:

### 5.1. Iniciando o Backend (Spring Boot)

No diretório `back` do projeto, execute o seguinte comando para iniciar o servidor Spring Boot:

```bash
mvn spring-boot:run
```

O backend estará acessível em `http://localhost:8080`. Este é o ponto de entrada para todas as requisições da API.

### 5.2. Iniciando o Frontend (React)

No diretório `passhub-frontend` do projeto, execute o comando para iniciar a aplicação React:

```bash
npm start
```

O frontend será iniciado e estará disponível em `http://localhost:3000`. A aplicação React se conectará automaticamente ao backend em `http://localhost:8080`.
