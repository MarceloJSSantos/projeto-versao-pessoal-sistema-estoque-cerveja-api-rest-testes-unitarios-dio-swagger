# Projeto Sistema de Estoque de Cervejas (VERSÃO PESSOAL) - versão c/ testes unitários e documentação das APIs c/ Swagger

repositório com a VERSÃO PESSOAL do projeto prático "Controle de Estoque de Cervejas" do Bootcamp GFT START #2 Java da [DIO](https://digitalinnovation.one/).

--------------------

Neste projeto foi desenvolvido um sistema backend (APIs) de controle de estoque de cervejas, c/ os testes unitários nas camadas de "Service" e "Controller" para validar as APIs.

--------------------



### Desenvolvimento

Foram desenvolvidas as seguintes tarefas:

* Configurações iniciais de um projeto com o Spring Boot Initialzr para a criação do sistema

* Criação de modelo de dados para o mapeamento da entidade Cerveja

* Desenvolvimento de algumas operações c/ seus respectivos testes:
  * Inserir Cerveja

    No Service

    * Ao inserir, retorna a cerveja inserida
      * controller: status Created e json
      * controller: Se faltou campo obrigatório, retorna status "Bad Request"
    * Se cerveja de mesmo nome existir, retorna uma exceção
      * não tratado no controller

  * Listar Cervejas

    No Service

    * Ao listar, se existir cervejas, uma lista de cervejas é retornada
      * controller: status Ok e json
    * Ao listar, se não houver cervejas, uma lista vazia é retornada
      * controller: status Ok e json

  * Encontrar Cerveja por nome

    No Service

    - Ao procurar uma cerveja pelo nome e encontrado é retornado uma cerveja
      - controller: status Ok e json

    * Ao procurar uma cerveja pelo nome e NÃO encontrado é retornado uma exceção
      * controller: status "Not Found"

  * Remover Cerveja por id

    No Service

    * Ao excluir uma cerveja, sendo um id válido, é excluída
      * controller: status No Content
    * Ao excluir uma cerveja, sendo um id inválido, não é excluída
      * controller: status "Not Found"

  * Alterar quantidade de Cerveja por id e quantidade (incremento e decremento)

    No Service

    * Ao alterar, se quantidade resultante, for maior que zero, quantidade máxima for menor ou igual ao cadastrado, é alterado
      * controller: status Ok e json
    * Ao alterar, se quantidade for maior que a quantidade máxima, é retornada uma exceção
      * controller: status "Bad Request"
    * Ao alterar, se quantidade resultante for maior que a quantidade máxima, é retornada uma exceção
      * controller: status "Bad Request"
    * Ao alterar, se o id não for válido, é retornada uma exceção
      * controller: status "Not Found"
    * Ao alterar, se quantidade resultante for menor que zero, é retornada uma exceção
      * controller: status "Bad Request"

  

### Tecnologias e Dependências utilizadas

- **Java 11**
- **Maven** p/ gerenciamento de dependências
  - **H2 Database SQL**
  - **Lombok**
  - **MapStruct**
- **Spring Boot (2.4.6)**
  - **Spring Web**
  - **Spring Boot Actuator**
  - **Spring Boot DevTools**
  - **Spring Data JPA**
  - **Spring Boot Validation**
- **Git/GitHub** para versionamento do código



### Instalação e Execução

Baixar e importar todos os módulos em sua IDE preferida

Após executar todos os módulos, basta apenas abrir os endpoints abaixo:

#### APIs Cervejas

| VERBO  | URL                                                      | CORPO                                                        |
| ------ | -------------------------------------------------------- | ------------------------------------------------------------ |
| GET    | http://localhost:8080/api/v1/cervejas                    | NÃO É NECESSÁRIO                                             |
| POST   | http://localhost:8080/api/v1/cervejas                    | {<br/>    "nome":"Brahma 1",<br/>    "marca":"Brahma",<br/>    "quantMax":100,<br/>    "quantidade":50,<br/>    "tipo": "LAGER"<br/>} |
| GET    | http://localhost:8080/api/v1/cervejas/{nome}             | NÃO É NECESSÁRIO                                             |
| DELETE | http://localhost:8080/api/v1/cervejas/{id}               | NÃO É NECESSÁRIO                                             |
| PATCH  | http://localhost:8080/api/v1/cervejas/{id}/alteraestoque | {<br/>    "quantidade":-10<br/>}                             |

**nota**: a API "".../alteraestoque" faz tanto o incremento, quanto o decremento da quantidade , para isso fiz uma readequação do projeto original para tal.

#### Documentação das APIs c/ Swagger

##### Passos:
1) Incluir as dependências do Swagger 2.0 no pom.xml;
  ````xml
    <dependency>
      <groupId>io.springfox</groupId>
      <artifactId>springfox-boot-starter</artifactId>
      <version>3.0.0</version>
    </dependency>
    <dependency>
      <groupId>io.springfox</groupId>
      <artifactId>springfox-swagger-ui</artifactId>
      <version>3.0.0</version>
    </dependency>
  ````
2) Após é só executar o projeto e a documentação no formato JSON estará em:
  ````http://localhost:8080/v3/api-docs/````
3) E a versão da documentação com a interface de usuário do Swagger:
  ````http://localhost:8080/swagger-ui/index.html````
4) (opcional) Criar o arquivo de configuração do Swagger ("com.marcelojssantos.dio.estoquecerveja.config.SwaggerConfig")

**nota**: [Collection do Postman]('./collection Postman/"APIs Cerveja".postman_collection.json')
