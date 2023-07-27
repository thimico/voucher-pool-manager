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

4. **Validar Voucher**:
- Endpoint: `/api/v1/vouchers/{codigo}`
- Método: PUT
- Exemplo de Parâmetro de Rota:
  ```json
  "25339E72"
  ```

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
