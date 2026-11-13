# Sistema de Cargas - API

API REST para registrar serviços de carga, consultar cargas disponíveis para faturamento, gerar faturas e registrar pagamentos. Este repositório contém o backend Java; o código da interface frontend não faz parte dele.

## Funcionalidades

- Cadastro e consulta de serviços de carga.
- Consulta de cargas ainda não vinculadas a uma fatura.
- Exclusão de serviços de carga.
- Geração de faturas a partir dos IDs das cargas.
- Consulta de todas as faturas e de uma fatura pelo ID.
- Registro de pagamento de uma fatura e atualização do status de suas cargas.
- Persistência em MySQL, com criação e versionamento do esquema por Flyway.

## Tecnologias

- Java 21
- Spring Boot 4.1.2
- Spring Web MVC
- Spring Data JPA / Hibernate
- MySQL
- Flyway
- Maven Wrapper
- Lombok

## Estrutura do projeto

```text
src/main/java/sistema_de_cargas/api/
  Controller/   Rotas HTTP de cargas e faturas
  DTO/          Dados de entrada e saída da API
  Model/        Entidades JPA e relacionamentos
  Repository/   Acesso aos dados com Spring Data
  Service/      Regras de cadastro, faturamento e pagamento

src/main/resources/
  application.properties
  db/migration/ Scripts SQL versionados do Flyway
```

## Requisitos

- JDK 21
- MySQL em execução e acessível pela aplicação

## Configuração do banco

As configurações ficam em `src/main/resources/application.properties` e podem ser sobrescritas por variáveis de ambiente:

| Variável | Padrão/uso |
| --- | --- |
| `DB_URL` | `jdbc:mysql://localhost:3306/cnc-transporte?createDatabaseIfNotExist=true` |
| `DB_USERNAME` | `root` |
| `DB_PASSWORD` | Senha do usuário do MySQL |

No PowerShell, configure os valores antes de iniciar a API:

```powershell
$env:DB_URL = "jdbc:mysql://localhost:3306/cnc-transporte?createDatabaseIfNotExist=true"
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "sua-senha"
```

Não grave senhas reais no repositório. Em outros ambientes, configure as mesmas variáveis no sistema de deploy.

## Executar

Na raiz do projeto, no Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

A API fica disponível, por padrão, em `http://localhost:8081`.

Para compilar, testar e empacotar:

```powershell
.\mvnw.cmd -DskipTests compile
.\mvnw.cmd test
.\mvnw.cmd package
```

O teste existente inicializa o contexto da aplicação e usa as configurações de banco atuais. Configure `DB_URL`, `DB_USERNAME` e `DB_PASSWORD` para apontar a um banco de teste antes de executá-lo; não use um banco de produção para testes.

## Banco de dados e migrações

O Flyway executa as migrações no início da aplicação. Os scripts ficam em `src/main/resources/db/migration`:

| Migração | Alteração |
| --- | --- |
| `V1__create_fatura_and_servico_carga.sql` | Cria `tb_fatura` e `tb_servico_carga`, incluindo o relacionamento entre cargas e faturas. |
| `V2__add_fatura_bank_details.sql` | Adiciona agência, conta e chave PIX a `tb_fatura`. |

O Hibernate está configurado com `ddl-auto=validate`: valida se as tabelas correspondem aos models, mas não cria nem altera o esquema. Para novas alterações no banco, crie uma nova migração (`V3__...sql`, por exemplo); não edite uma migração que já foi aplicada.

## API

As rotas aceitam e retornam JSON. As datas usam o formato ISO `AAAA-MM-DD`. No estado atual do projeto, os endpoints estão liberados para CORS de qualquer origem; restrinja essa configuração antes de publicar a API em produção.

### Cargas

| Método | Rota | Descrição |
| --- | --- | --- |
| `POST` | `/api/cargas` | Registra uma carga. O backend calcula `valorTotal` como quantidade x valor unitário e inicia o status como `PENDENTE`. |
| `GET` | `/api/cargas` | Lista todas as cargas. |
| `GET` | `/api/cargas/disponiveis` | Lista cargas sem fatura associada. |
| `DELETE` | `/api/cargas/{id}` | Exclui uma carga pelo ID. |

Exemplo de corpo para `POST /api/cargas`:

```json
{
  "material": "Rachão",
  "placa": "ABC1D23",
  "dataServico": "2026-11-13",
  "ticket": "715",
  "unidade": "t",
  "quantidade": 1,
  "valorUnitario": 1300.00,
  "cnpjCliente": "00.000.000/0001-00",
  "nomeCliente": "Cliente Exemplo"
}
```

`material` é recebido como texto livre pelo backend. As opções exibidas em uma lista suspensa pertencem ao frontend e devem ser mantidas nele.

### Faturas

| Método | Rota | Descrição |
| --- | --- | --- |
| `POST` | `/api/faturas` | Gera uma fatura com as cargas indicadas em `idsCargas`. |
| `GET` | `/api/faturas` | Lista faturas com seus dados e cargas associadas. |
| `GET` | `/api/faturas/{id}` | Consulta os detalhes de uma fatura. Retorna `404` quando o ID não existe. |
| `PUT` | `/api/faturas/{id}/pagar?dataPagamento=AAAA-MM-DD` | Registra o pagamento da fatura e atualiza o status e a data de pagamento das cargas associadas. |

Exemplo de corpo para `POST /api/faturas`:

```json
{
  "numeroFatura": "FAT-2026-001",
  "cnpjCliente": "00.000.000/0001-00",
  "nomeCliente": "Cliente Exemplo",
  "enderecoCliente": "Rua Exemplo, 100",
  "emailCliente": "financeiro@example.com",
  "dataFatura": "2026-11-13",
  "dataVencimento": "2026-11-20",
  "desconto": 0.00,
  "dadosBancarios": "Banco Exemplo",
  "agencia": "0001",
  "conta": "12345-6",
  "pix": "financeiro@example.com",
  "idsCargas": [1, 2]
}
```

O subtotal é calculado pela soma dos totais das cargas encontradas; o total faturado é o subtotal menos o desconto. A resposta da fatura inclui, entre outros campos:

- `id`, `numeroFatura` e dados do cliente;
- `dataFatura`, `dataVencimento` e `dataPagamento`;
- `subtotal`, `desconto` e `totalFaturado`;
- `dadosBancarios`, `agencia`, `conta` e `pix`;
- `statusPagamento` e a lista `cargas`.

Os status usados atualmente são `PENDENTE` e `PAGO`. A API fornece os dados da fatura, mas não gera arquivo PDF nem implementa os botões de impressão; essa parte deve ser feita na interface frontend.

## Observações

- Cargas vinculadas a uma fatura deixam de aparecer em `/api/cargas/disponiveis`.
- O frontend não está incluído neste repositório; suas telas devem consumir as rotas documentadas acima.
- Os endpoints de criação e pagamento retornam `200 OK` com o recurso atualizado.
