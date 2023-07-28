# VoucherPoolManager

Aplicação Java/Spring para gerenciamento de vouchers.

## Desenvolvimento

Quando você inicia a aplicação, é chamado o `docker compose up` e o app irá se conectar aos serviços contidos. O [Docker](https://www.docker.com/get-started/) deve estar disponível no seu sistema atual.

Durante o desenvolvimento, é recomendado usar o perfil `local`. No IntelliJ, adicione `-Dspring.profiles.active=local` nas opções da VM da Configuração de Execução, após habilitar essa propriedade em "Modificar opções". Crie seu próprio arquivo `application-local.yml` para sobrescrever as configurações para o desenvolvimento.

O Lombok deve ser suportado pelo seu IDE. Para o IntelliJ, instale o plugin Lombok e habilite o processamento de anotações.

Após iniciar a aplicação, ela estará acessível em `localhost:8080`.

## Dependências

Certifique-se de ter todas as dependências necessárias para executar a aplicação:

* Java 11
* Docker
* MongoDB

## Uso da API

A seguir estão as etapas passo a passo para utilizar a API:

1. **Cadastro de Cliente Destinatário**:
- Endpoint: `/api/v1/destinatarios`
- Método: POST
- Exemplo de Corpo da Requisição:
  ```json
  {
    "nome": "Nome do Destinatario",
    "email": "destinatario1@email.com"
  }
  ```

2. **Criar Oferta Especial**:
- Endpoint: `/api/v1/ofertas`
- Método: POST
- Exemplo de Corpo da Requisição:
  ```json
  {
    "nome": "Oferta de Inverno",
    "desconto": 15
  }
  ```

3. **Criar Voucher**:
- Endpoint: `/api/v1/vouchers`
- Método: POST
- Exemplo de Corpo da Requisição:
  ```json
  {
    "ofertaEspecialId": "64c0613c7f0aa22f02f059b2",
    "destinatarioEmail": "destinatario1@email.com",
    "validade": "30/12/2023"
  }
  ```


4. **Criar Vouchers para Vários Destinatários**:
  - Endpoint: `/api/v1/vouchers/pool`
  - Método: POST
  - Descrição: Cria vouchers para vários destinatários usando uma oferta especial específica e a data de validade fornecida.
  - Corpo da Requisição (Request Body):

    ```json
    {
        "ofertaEspecialId": "64c0613c7f0aa22f02f059b2",
        "destinatariosEmails": ["destinatario@email.com", "destinatario1@email.com"],
        "validade": "2023-12-31"
    }
    ```

    **Campos:**
    - `ofertaEspecialId` (String): O ID da oferta especial à qual os vouchers serão vinculados.
      - **Formato**: Uma string alfanumérica que identifica exclusivamente a oferta especial. Exemplo: "64c0613c7f0aa22f02f059b2".
    - `destinatariosEmails` (Array de Strings): Uma lista de endereços de e-mail dos destinatários para os quais os vouchers serão criados.
      - **Formato do E-mail**: Cada endereço de e-mail deve ser uma string válida de endereço de e-mail.
    - `validade` (String): A data de validade para os vouchers criados.
      - **Formato da Data**: A data fornecida deve seguir o formato "yyyy-MM-dd" (ano-mês-dia). Por exemplo: "2023-12-31".

  - Resposta de Sucesso:
    - Status: 200 OK
    - Corpo da Resposta (Response Body):
      A resposta conterá informações sobre os vouchers criados, como IDs, códigos, detalhes das ofertas especiais e a data de validade comum a todos os vouchers.

  - Resposta de Erro:
    - Status: 400 Bad Request
    - Descrição: Em caso de erro, a resposta conterá detalhes sobre o problema encontrado.

Observações:
- Certifique-se de que a oferta especial com o ID especificado em `ofertaEspecialId` exista e esteja ativa.
- Verifique se todos os endereços de e-mail em `destinatariosEmails` são válidos e existentes na sua base de dados ou sistema.
- Assegure-se de que a data de validade (`validade`) esteja no futuro e seja uma data válida.
- O sistema pode gerar códigos únicos para cada voucher criado, ou você pode fornecer esses códigos no corpo da requisição, se aplicável.
- Utilize autenticação e autorização adequadas para proteger este endpoint de uso indevido.

5. **Validar Voucher**:
- Endpoint: `/api/v1/vouchers/{codigo}`
- Método: PUT
- Exemplo de Parâmetro de Rota:
  ```json
  "25339E72"
  ```

6. **Busca todos os vouchers válidos para um determinado e-mail**:
  - Endpoint: `/api/v1/vouchers/valid`
  - Método: GET
  - Parâmetro de Consulta (Query Parameter):
    - `email`: O e-mail do destinatário para o qual os vouchers válidos serão retornados.
      - **Formato do E-mail**: O e-mail fornecido deve ser uma string válida de endereço de e-mail, por exemplo, "destinatario@email.com".

  - Corpo da Resposta (Response Body):
    A resposta conterá uma lista de objetos representando os vouchers válidos encontrados para o e-mail especificado. Cada objeto terá os seguintes campos:

    ```json
    [
      {
        "id": "64c063f1e854a346a8ac459c",
        "destinatario": {
          "id": "64c0628ac58ef5516633c952",
          "nome": "Nome do Destinatario",
          "email": "destinatario1@email.com"
        },
        "ofertaEspecial": {
          "id": "64c0613c7f0aa22f02f059b2",
          "nome": "Oferta de Inverno",
          "desconto": 15
        },
        "codigo": "f6c53970",
        "validade": "dd/MM/yyyy",
        "status": "ATV"
      }
    ]
    ```

    **Formato da Data de Validade (validade)**: A data de validade de cada voucher seguirá o formato "dd/MM/yyyy" (dia/mês/ano). Por exemplo, "28/07/2023".  

Para quaisquer dúvidas ou problemas, entre em contato com Thiago M Oliveira (thg.mnzs@gmail.com) ou consulte o repositório GitHub no [github.com/thimico/voucher-pool-manager](https://github.com/thimico/voucher-pool-manager).

---

Certifique-se de substituir os IDs e os códigos de voucher reais nos exemplos acima.


## Documentação da API

Para uma visão mais detalhada da API, você pode acessar a documentação da API em `http://localhost:8080/swagger-ui.html` quando a aplicação estiver rodando.

## Testes

Para executar os testes unitários da aplicação, você pode executar o seguinte comando:

```bash
./mvnw test
```

Os resultados dos testes serão exibidos no terminal.
