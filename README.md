# API RESTful Golden Raspberry Awards <h5>[*maior e menor intervalos de premiação*]</h5>

Esta é uma API que permite a leitura da lista dos indicados e vencedores da categoria Pior Filme do Golden Raspberry Awards, a partir de um arquivo local CSV, possibilitando visualizar o maior e menor intervalo de premiação dos produtores.

## Repositório

O código-fonte da aplicação se encontra disponível no repositório Git: [texo-it-challenge](https://github.com/hudsonhenriquelopes/texo-it-challenge) .

## Como executar o projeto

1. É necessário que Git, JDK (17) e o Maven (v3.6.3+) estejam instalados e configurados corretamente em seu ambiente de desenvolvimento.
2. Clone o repositório git utilizando o comando <code>git clone https://github.com/hudsonhenriquelopes/texo-it-challenge </code>
3. Navegue até o diretório raiz do projeto.
4. Execute o seguinte comando para compilar e empacotar o projeto: <code>mvn package</code>
5. Após o empacotamento, execute o seguinte comando para iniciar a aplicação: <code>java -jar target/challenge-0.0.1-SNAPSHOT.jar</code>
6. A aplicação estará disponível no endereço `http://localhost:8080`.

## Endpoints da aplicação

* <b>POST</b> `/api/store`: permite enviar o caminho de um arquivo CSV local para armazenamento no banco H2. O caminho do arquivo deve ser passado como body da requisição. Para facilitar a criação da requisição, recomenda-se o uso do Postman.
  * Exemplo de caminho da requisição: `http://localhost:8080/api/store`
  * Exemplo de caminho do arquivo local: `C:\Users\tester\Desktop\movielist.csv`
  * Resposta de sucesso: _[STATUS 200] File content is stored._


* <b>GET</b> `/api/list-awards-interval`: permite consultar os maiores e menores intervalos de premiação que os produtores receberam na produção dos filmes vencedores. Para facilitar a criação da requisição, recomenda-se o uso do Postman.
    * Exemplo de caminho da requisição: `http://localhost:8080/api/list-awards-interval`
    * Resposta de sucesso deve ser similar a: _[STATUS OK]_
        
        ```json
        {
          "min": [
            {
              "producer": "Joel Silver",
              "interval": 1,
              "previousWin": 1990,
              "followingWin": 1991
            }
          ],
          "max": [
            {
              "producer": "Matthew Vaughn",
              "interval": 13,
              "previousWin": 2002,
              "followingWin": 2015
            }
          ]
        }
        ```

<b>OBS.:</b> _Caso não seja executado o endpoint `/api/store` primeiramente, não haverão registros para serem retornados no endpoint `/api/list-awards-interval`._ 

## Exceções tratadas das requisições

* <b>STATUS BAD_REQUEST</b>
    * _Invalid path. This should be a local CSV file path._
    * _File does not exist. (Path: "notExistentFile.csv")_
    * _File has no header or content._
    * _File has no header._
    * _File is missing column on header: producers._
    * _File has only the header._
    * _There are not enough values in the line. (Required at least 4 columns)_
    * _There are movies where producers have not being informed._
    * _Year 'x' is invalid._

## Execução dos Testes de Integração

Para executar os testes de integração:

1. Navegue até o diretório raiz do projeto.
2. Execute o seguinte comando: <code>mvn integration-test</code>