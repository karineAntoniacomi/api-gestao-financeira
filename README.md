# API de Gestão Financeira
API de gestão financeira que permite que os usuários gerenciem suas finanças pessoais, com operações CRUD e perfis de usuário. 

## Resursos 💰
A API possui validações de entrada, segurança com Spring Security, testes unitários, documentação OpenAPI, integração com  e comunicação com a api de transação via Kafka:

- Spring Boot
- Docker
- PostgreeSQL
- FlyWay
- Spring Data JPA
- APache POI
- OpenAPI
- Exchangerate API
- Mock API
- Kafka
- Criptografia de Senhas com BCrypt

## Melhorias futuras
- Clean Archtecture
- Mais testes unitários
- Saldo bancário recebendo alteração de valor de transações de Retirada e Tranferência e verificando se há saldo sufficiente.
- Aprovar/Recusar transações no Microsserviço Processor (que atualmente está somente recebendo as mensagens na fila)
- Salvar logs de erro do Microsserviço Processor na tabela DLQ.

## Documentação Swagger:
Com os microsserviços rodando, acessar o link:
- usuarios: http://localhost:8081/swagger-ui/index.html#/
- transações: http://localhost:8082/swagger-ui/index.html#/


## Como executar 🔨
- Clone o projeto no github;

## MockAPI
1. Crie conta na Mockapi:
- https://mockapi.io/

2. Crie alguns dados fictícios para dados de saldo bancário, os usuarioId deveráo ser os mesmos dos usuários registrados no banco de dados para refletirem no saldo:
- Exemplo de formato dos dados:
[
  {
    "usuarioId": 10,
    "saldo": 1500.75,
    "moeda": "BRL",
    "ativa": true,
    "ultimaAtualizacao": 1769184723,
    "id": "1"
  },
  {
    "usuarioId": 9,
    "saldo": 1500,
    "moeda": "BRL",
    "ativa": true,
    "ultimaAtualizacao": 1769184663,
    "id": "2"
  }
]
3. Seu link da MockAPi deve ser algo como: http://123.mockapi.io/conta
4. Configure a variável de ambiente com o nome API_CONTA_URL e o link como valor.

## Exchangerate API
1. Crie uma conta na exchangerate-api: https://www.exchangerate-api.com/
2. Configure a variável de ambiente com o nome EXCHANGE_RATE_API_URL e o link como valor (Exemplo de como ficará o link: https://v6.exchangerate-api.com/v6/sua-chave-numerica-aqui/latest).

## Executar localmente/dev:
1. Subir apenas banco (executar comando via terminal dentro da pasta raiz dos microsserviços): 
- docker-compose up -d 

## Banco de dados
1. Após subir o postgres, fazer login http://localhost:5050/browser/ com usuario (email admin@admin.com) e senha configurada nas variáveis de ambiente do sistema operacional.

2. Criar os respectivos bancos dos Microsserviços no postgres (que estão listados no docker-compose.yml):
- ms-service-usuario
- ms-service-transacao
- ms-processor-consumer

2. Subir apenas banco (executar comando via terminal dentro da pasta raiz de cada Microsserviço): 
- mvn spring-boot:run -Dspring-boot.run.profiles=local 


## Executar via Docker
1. Para rodar tudo com Docker (executar comandos via terminal dentro da pasta raiz de cada Microsserviço). Garantir que os JARs existem. Para cada microsserviço executar os comandos via terminal:
- mvn clean package -DskipTests

2. Subir tudo (executar comando via terminal dentro da pasta raiz dos microsserviços):
- docker-compose up --build


Ou, se preferir, para <ins>executar via Docker</ins> utilize o script **build-and-run.bat**: Execute clicando duas vezes ou via terminal build-and-run.bat.


## Quando estiver tudo rodando, é possível testar a API no Swagger:
1. Importe usuários do arquivo usuarios.xlsx.
2. Efetue login com algum dos usuários salvos no banco de dados, todos tem a senha padrão 1234.

Teste os demais endpoints!
Os de transação são privados, podendo ser acessados somente estando autenticado.
Entre os endpoints de usuário, são públicos: Importação de usuário e Login. A listagem de usuários precisa de autenticação porém está liberada para qualquer usuário (apenas para fins de facilitar a visualização dos dados, sendo o mais indicado a configuração de holes especíicas para listagens gerais).