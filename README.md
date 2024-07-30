### Pix Case

#### Decisões de gerais
1. **Tecnologias**:
   - Java 17
   - Spring 3x
   - PostgreSQL
   - Maven 3.8.4
   - JUnit, Mockito
   - Libs de log, validação (Jakarta), DB
   - Hibernate, JPA
2. **Chaves pix aceitas**:
   - CPF, CNPJ, Email, Celular 
3. **Consultas implementadas**:
   - Inclusão de chave
      - POST /key/add
   - Alteração de chave
      - PUT
   - "Deleção"/inativação de chave
      - PATCH /key/deactivate
   - GET (considerando a inativação)
      - Busca por ID
      - Busca por tipo de chave
      - Busca por agência e conta
      - Busca por nome do correntista**
4. **Banco**:
   - Duas tabelas, TB_CORRENTISTA e TB_CHAVE_PIX, numa relação de 1:N
5. **Deploy**:
   - Azure App Services - pela simplicidade de deploy focado em 1 container já com URL e geração de pipeline rápida.
   - Github como versionador.
   - Postgre no RDS
   - Docker
5. **Arquitetura**:
    - MVC por ser mais simples pro case

#### Decisões de implementação
1. **Camada comum de validação inicial**:
   - Usei a biblioteca Jakarta Validation, @Size, @NotEmpty, @Pattern (para tipo-conta e tipo-chave por exemplo. Não adicionei enum pois eu quis habilitar case insensitive para esses campos) pois não gostaria de bloquear o usuário caso ele escrevesse "Celular" ao invés de "celular"
2. **Validação de máximo de chaves para correntista PF ou PJ**:
   - O enunciado não menciona um identificador de PF ou PJ para os inputs. Então se eu quisesse incluir como PF 5 chaves de email, e fosse adicionar a 6a, não teria como validar se sou PF. Por isso, no banco eu adicionei a coluna "TIPO_PESSOA" (PF ou PJ) para a tabela TB_CORRENTISTA.
       - E para comportar a inclusão de chave para uma conta que existe, bem como essa validação de máximo de chaves:
         - Criei um endpoint POST /holder para simular a criação do correntista, que possui um tipo-pessoa.
         - Poderia adicionar o parâmetro tipo-pessoa nos inputs, não fiz isso pois não acho que faria sentido criar uma conta logo ao incluir uma chave. Embora provavelmente o endpoint POST /holder não ficaria no mesmo serviço de criação de chave na vida real
3. **Validações de chaves**:
    - A validação completa é feita com um Facade (ValidationFacadeService), que valida a regra de duplicidade e regra de máximo de chaves (classe KeyValidationService), bem como valida cada tipo de chave, num switch (não quis usar Strategy, pois o escopo da validação está bem definido e teríamos muitas classes). Certamente Strategy faria sentido num projeto real, já que poderiamos fazer reuso de validações de CPF e CNPJ por exemplo
4. **Consultas de banco**:
    - A maioria delas retorna View/DTO ao invés da entidade, pra não retornar tantos dados em algumas consultas
5. **Logs**:
    - Criei business rules no Enum ErrorMessage
    - Usei Logback pra formatar os logs como chave (LogstashEncoder, usado como layout no logback), mas isso foi sóo preferência pessoal pra leitura de logs
    - Tenho um Exception Handler Global para BusinessException e Exceptions de validação de campo, bem como um geral pra exceptions nao tratadas (padrão que costumo usar no trabalho)
    - Uso Lombok para alguns boilerplates

#### Endpoints e Estruturas

1. **POST `/clientes`**:
    - Request:
      ```json
      {
        "tipo": "PF", // PF para pessoa física, PJ para pessoa jurídica
        "nome": "string",
        "email": "string",
        "numeroCliente": "string", // para pessoa jurídica
        "dataNascimento": "YYYY-MM-DD", // para pessoa física
        "banco": 30 // Código do banco
      }
      ```
    - Response:
      ```json
      {
        "id": "string",
        "status": "PENDING"
      }
      ```

2. **GET `/clientes/{id}`**:
    - Response:
      ```json
      {
        "numeroCliente": "string",
        "tipo": "PF",
        "nome": "string",
        "email": "string",
        "dataNascimento": "YYYY-MM-DD",
        "status": "PENDING/COMPLETED/ERROR"
      }
      ```

3. **PATCH `/clientes/{id}`**:
    - Request:
      ```json
      {
        "nome": "string",
        "email": "string"
      }
      ```
    - Response:
      ```json
      {
        "numeroCliente": "string",
        "tipo": "PF",
        "nome": "string",
        "email": "string",
        "dataNascimento": "YYYY-MM-DD",
        "status": "string"
      }
      ```
    
### Local setup
- H2 db
- Java 17
- Maven 3.8.4
### DB conn
- http://localhost:8080/h2-console