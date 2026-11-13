# Plano de Projeto TDD em Java: Cadastro de Veículos no Estoque

## 1. Visão Geral e Contexto Pedagógico

Este plano de projeto foi elaborado sob a ótica de Engenharia e Testes de Software para demonstrar, de forma prática e incremental, a aplicação de **Test-Driven Development (TDD)** em Java utilizando **JUnit 5**.

### Problema de Negócio
Uma revenda precisa incluir veículos no estoque. O serviço deve validar os dados de entrada, impedir a duplicidade de placa e persistir o cadastro.

### História de Usuário
> **Como** atendente,  
> **Quero** cadastrar um veículo,  
> **Para** disponibilizá-lo no estoque da revenda.

### Critérios de Aceitação (CAs)
- **CA1:** Placa deve seguir o padrão Mercosul simplificado (`AAA1A23` — 3 letras maiúsculas, 1 dígito, 1 letra maiúscula e 2 dígitos).
- **CA2:** Ano deve estar entre `1900` e o `ano atual + 1`.
- **CA3:** Preço deve ser maior que zero (`> 0`).
- **CA4:** Não pode existir outro veículo cadastrado com a mesma placa.
- **CA5:** Cadastro válido recebe um identificador (`id`) e é persistido no estoque.

---

## 2. Metodologia: Ciclo TDD e Baby Steps

Cada diretório numerado (`passo_0`, `passo_1`, ..., `passo_6`) é um **projeto Maven autocontido e independente**, representando a fotografia do código ao final daquele incremento.

### Regra de Ouro de Progressão
Nenhum passo avança para o próximo sem cumprir estritamente o ciclo:
1. **RED (Vermelho):** Escrever um pequeno teste automatizado que falha (seja por erro de compilação inicial ou por falha na asserção) para um único comportamento desejado.
2. **GREEN (Verde):** Escrever a implementação mais simples possível (mesmo que ingênua ou *hardcoded* inicialmente, evoluindo por triangulação) apenas para fazer o teste passar.
3. **REFACTOR (Refatoração):** Melhorar o design do código de produção e de testes (extrair constantes, métodos, clarear nomes, remover duplicações) mantendo todos os testes no verde.
4. **GATE DE TRANSIÇÃO:** Executar `mvn clean test` na pasta atual. Somente com `BUILD SUCCESS` e `0 failures` copia-se a base para a pasta seguinte (`passo_N+1`).

---

## 3. Tecnologias Utilizadas

- **Linguagem:** Java 17+ (compatível com `record` ou classes tradicionais)
- **Gerenciador de Build:** Apache Maven
- **Framework de Testes:** JUnit 5 (`jupiter-api` e `jupiter-params` para testes parametrizados na refatoração)
- **Dublês de Teste (Test Doubles):** Implementação `InMemoryVeiculoRepository` (Fake) para manter o foco didático no estado e comportamento sem depender de banco de dados externo.

---

## 4. Arquitetura Alvo (Ao Final da Evolução)

Ao longo dos passos, o design emergirá gradualmente até atingir a seguinte responsabilidade de classes:

| Classe / Interface | Tipo | Responsabilidade |Quando Surge |
| :--- | :--- | :--- | :--- |
| `Veiculo` | Entidade / Domínio | Representa o veículo no estoque (`id`, `placa`, `modelo`, `ano`, `preco`). | Passo 1 |
| `VeiculoRepository` | Interface (Porta) | Contrato de persistência (`salvar`, `existePorPlaca`, `buscarPorId`). | Passo 1 |
| `InMemoryVeiculoRepository` | Fake / Infra em Memória | Implementação em memória com geração sequencial de ID para testes e execução rápida. | Passo 1 |
| `VeiculoService` | Serviço de Domínio | Orquestra as regras de negócio (CA1 a CA5) e interage com o repositório. | Passo 1 |
| `RegraNegocioException` | Exceção de Domínio | Representa violações de validação e regras de negócio com mensagens claras. | Passo 2 |

---

## 5. Organização do Diretório de Pastas (Fluxo Completo RED → GREEN → REFACTOR)

Para tornar o ciclo do TDD tangível aos alunos, cada regra de negócio possui **duas pastas sequenciais**:
1. **`passo_N_erro` (Fase RED 🔴):** Contém o código de produção do passo anterior (`N-1`) + o **novo teste unitário escrito primeiro**. Ao rodar `mvn test`, o projeto compila, os testes antigos continuam passando, mas o teste novo **falha** (`BUILD FAILURE`).
2. **`passo_N` (Fases GREEN 🟢 + REFACTOR 🔵):** Contém a evolução do código de produção implementando a regra de negócio para fazer o teste passar (`BUILD SUCCESS`).

```text
codigo_tdd/
├── plano_projeto.md
├── README.md
├── passo_0/       # Estágio inicial: esqueleto Maven + JUnit 5 configurado (Sanity Check)
├── passo_1_erro/  # [RED]   CA5: Teste de persistência e ID escrito ANTES da implementação (Falha: 1 de 1)
├── passo_1/       # [GREEN] CA5: Implementação do fluxo feliz (Passa: 1 de 1)
├── passo_2_erro/  # [RED]   CA1: Testes de placa Mercosul adicionados ao código do passo_1 (Falha: 9 de 10)
├── passo_2/       # [GREEN] CA1: Validação do formato da placa Mercosul AAA1A23 (Passa: 10 de 10)
├── passo_3_erro/  # [RED]   CA2: Testes de limites de ano adicionados ao código do passo_2 (Falha: 2 de 13)
├── passo_3/       # [GREEN] CA2: Validação do intervalo de ano 1900 até anoAtual + 1 (Passa: 13 de 13)
├── passo_4_erro/  # [RED]   CA3: Testes de preço <= 0 adicionados ao código do passo_3 (Falha: 5 de 18)
├── passo_4/       # [GREEN] CA3: Validação de preço maior que zero (Passa: 18 de 18)
├── passo_5_erro/  # [RED]   CA4: Teste de placa duplicada adicionado ao código do passo_4 (Falha: 1 de 19)
├── passo_5/       # [GREEN] CA4: Impedimento de placa duplicada no estoque (Passa: 19 de 19)
└── passo_6/       # [REFACTOR FINAL] Consolidação, imutabilidade, Clean Code e Suíte @Nested (Passa: 31 de 31)
```

Cada pasta `passo_X/` possui a estrutura padrão Maven:
```text
passo_X/
├── pom.xml
└── src/
    ├── main/java/br/edu/revenda/
    │   └── (classes de produção do estágio X)
    └── test/java/br/edu/revenda/
        └── VeiculoServiceTest.java
```

---

## 6. Detalhamento Passo a Passo (Baby Steps Autocontidos)

---

### 📁 `passo_0` — Estágio Inicial: Ambiente e Sanity Check

#### Objetivo
Garantir que a estrutura do projeto, o compilador Java 17 e o runner do JUnit 5 estão operacionais antes de escrever a primeira linha de regra de negócio.

#### Estrutura de Arquivos
- `passo_0/pom.xml`
- `passo_0/src/test/java/br/edu/revenda/AmbienteTest.java`

#### O que fazer neste passo
1. Configurar o `pom.xml` com `junit-jupiter` (versão `5.10.2`) e `maven-surefire-plugin`.
2. Criar a classe `AmbienteTest` com um teste simples (`deveValidarAmbienteDeTestesConfigurado`) verificando `assertTrue(true)`.

#### Critério de Saída (Gate)
- Executar `mvn test` em `passo_0`.
- Resultado esperado: **1 teste executado, 1 passou**.

---

### 📁 `passo_1` — Baby Step 1: Cadastro Válido, ID e Persistência (CA5)

> **Por que começar pelo CA5?**  
> Em TDD, começar pelo "caminho feliz" (*happy path*) permite fazer emergir a assinatura do método principal, a estrutura da entidade e o contrato de persistência sem a complexidade prematura das validações.

#### Ciclo TDD

1. **🔴 RED (Teste que falha):**
   - Criar `VeiculoServiceTest`.
   - Escrever o teste `deveCadastrarVeiculoValidoGerandoIdentificadorEPersistindoNoEstoque()`.
   - O teste instancia `InMemoryVeiculoRepository` e `VeiculoService`, cria um `Veiculo("ABC1D23", "Onix", 2024, new BigDecimal("75000.00"))` e chama `service.cadastrar(veiculo)`.
   - Asserções do teste:
     - `assertNotNull(salvo.getId())`
     - `assertEquals(1L, salvo.getId())`
     - `assertTrue(repository.buscarPorId(salvo.getId()).isPresent())`
   - *Motivo da falha:* As classes `Veiculo`, `VeiculoRepository`, `InMemoryVeiculoRepository` e `VeiculoService` ainda não existem (ou o método `cadastrar` retorna `null`).

2. **🟢 GREEN (Implementação mínima):**
   - Criar `Veiculo` com atributos `id`, `placa`, `modelo`, `ano`, `preco`.
   - Criar interface `VeiculoRepository` com `Veiculo salvar(Veiculo veiculo)` e `Optional<Veiculo> buscarPorId(Long id)`.
   - Criar `InMemoryVeiculoRepository` usando uma `Map<Long, Veiculo>` e um contador `sequenciaId` que atribui o `id` ao salvar.
   - Criar `VeiculoService` delegando diretamente `return repository.salvar(veiculo);`.

3. **🔵 REFACTOR (Melhoria do código):**
   - Extrair a inicialização de `repository` e `service` para um método `@BeforeEach void setUp()` em `VeiculoServiceTest`.

#### Critério de Saída (Gate)
- Executar `mvn test` em `passo_1`.
- Resultado esperado: **1 teste passando (CA5 atendido)**.

---

### 📁 `passo_2` — Baby Step 2: Validação da Placa Padrão Mercosul (CA1)

> **Regra (CA1):** A placa deve seguir estritamente o formato Mercosul simplificado `AAA1A23` (Regex: `^[A-Z]{3}[0-9][A-Z][0-9]{2}$`) e não pode ser nula ou vazia.

#### Ciclo TDD

1. **🔴 RED (Testes que falham):**
   - Adicionar em `VeiculoServiceTest`:
     - `naoDeveCadastrarVeiculoComPlacaNoPadraoAntigo()` (ex.: `"ABC1234"` ou `"ABC-1234"`)
     - `naoDeveCadastrarVeiculoComPlacaMinusculaOuFormatoInvalido()` (ex.: `"abc1d23"`, `"1234567"`, `""`, `null`)
   - Asserção usando `assertThrows(RegraNegocioException.class, () -> service.cadastrar(veiculo))` e validando que o repositório continua vazio.
   - *Motivo da falha:* `VeiculoService` aceita qualquer placa e salva o veículo sem lançar exceção.

2. **🟢 GREEN (Implementação mínima):**
   - Criar a exceção `RegraNegocioException extends RuntimeException`.
   - Em `VeiculoService.cadastrar()`, adicionar a checagem:
     ```java
     if (veiculo.getPlaca() == null || !veiculo.getPlaca().matches("^[A-Z]{3}[0-9][A-Z][0-9]{2}$")) {
         throw new RegraNegocioException("Placa inválida. Deve seguir o padrão Mercosul AAA1A23.");
     }
     ```

3. **🔵 REFACTOR (Melhoria do código):**
   - Extrair a expressão regular para uma constante `private static final Pattern PADRAO_PLACA_MERCOSUL = Pattern.compile("^[A-Z]{3}[0-9][A-Z][0-9]{2}$");`.
   - No teste, criar um método auxiliar `criarVeiculoPadrao(...)` ou usar `@ParameterizedTest` / `@ValueSource` para testar múltiplas placas inválidas sem duplicar código de teste.

#### Critério de Saída (Gate)
- Executar `mvn test` em `passo_2`.
- Resultado esperado: **Todos os testes de CA5 e CA1 passando**.

---

### 📁 `passo_3` — Baby Step 3: Validação do Ano do Veículo (CA2)

> **Regra (CA2):** O ano deve estar entre `1900` e `ano atual + 1` (inclusive). Valores como `1899` ou `anoAtual + 2` devem ser rejeitados, enquanto os limites exatos (`1900` e `anoAtual + 1`) devem ser aceitos (Análise de Valor Limite).

#### Ciclo TDD

1. **🔴 RED (Testes que falham):**
   - Adicionar em `VeiculoServiceTest`:
     - `naoDeveCadastrarVeiculoComAnoMenorQue1900()` (testando `1899`)
     - `naoDeveCadastrarVeiculoComAnoMaiorQueAnoAtualMaisUm()` (testando ` Year.now().getValue() + 2`)
     - `devePermitirCadastroNosLimitesDeAnoValidos()` (testando `1900` e `Year.now().getValue() + 1`)
   - *Motivo da falha:* Veículos com ano `1899` e `anoAtual + 2` estão sendo cadastrados sem erro.

2. **🟢 GREEN (Implementação mínima):**
   - Em `VeiculoService.cadastrar()`, adicionar validação:
     ```java
     int anoMaximo = Year.now().getValue() + 1;
     if (veiculo.getAno() < 1900 || veiculo.getAno() > anoMaximo) {
         throw new RegraNegocioException("Ano do veículo deve estar entre 1900 e " + anoMaximo + ".");
     }
     ```

3. **🔵 REFACTOR (Melhoria do código):**
   - Extrair constante `ANO_MINIMO = 1900`.
   - Extrair método privado `validarAno(int ano)` em `VeiculoService` e `validarPlaca(String placa)`.

#### Critério de Saída (Gate)
- Executar `mvn test` em `passo_3`.
- Resultado esperado: **Todos os testes de CA5, CA1 e CA2 passando**.

---

### 📁 `passo_4` — Baby Step 4: Validação de Preço Maior que Zero (CA3)

> **Regra (CA3):** O preço deve ser estritamente maior que zero (`> 0`). Preço nulo, igual a zero (`0.00`) ou negativo (`-1.00`) deve lançar `RegraNegocioException`.

#### Ciclo TDD

1. **🔴 RED (Testes que falham):**
   - Adicionar em `VeiculoServiceTest`:
     - `naoDeveCadastrarVeiculoComPrecoZero()` (`BigDecimal.ZERO`)
     - `naoDeveCadastrarVeiculoComPrecoNegativo()` (`new BigDecimal("-0.01")`)
     - `naoDeveCadastrarVeiculoComPrecoNulo()` (`null`)
   - *Motivo da falha:* `VeiculoService` permite cadastrar veículos com preço zero, negativo ou nulo.

2. **🟢 GREEN (Implementação mínima):**
   - Em `VeiculoService.cadastrar()`, adicionar validação:
     ```java
     if (veiculo.getPreco() == null || veiculo.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
         throw new RegraNegocioException("Preço do veículo deve ser maior que zero.");
     }
     ```

3. **🔵 REFACTOR (Melhoria do código):**
   - Extrair método privado `validarPreco(BigDecimal preco)` em `VeiculoService`.
   - Revisar mensagens de exceção e nomes dos testes para manter legibilidade de documentação viva.

#### Critério de Saída (Gate)
- Executar `mvn test` em `passo_4`.
- Resultado esperado: **Todos os testes de CA5, CA1, CA2 e CA3 passando**.

---

### 📁 `passo_5` — Baby Step 5: Impedimento de Duplicidade de Placa (CA4)

> **Regra (CA4):** Não pode existir outro veículo no estoque com a mesma placa. Caso a placa já esteja cadastrada, deve lançar `RegraNegocioException` e não criar um novo registro.

#### Ciclo TDD

1. **🔴 RED (Teste que falha):**
   - Adicionar em `VeiculoServiceTest`:
     - `naoDeveCadastrarVeiculoComPlacaJaExistenteNoEstoque()`:
       1. Cadastra um primeiro veículo válido com placa `"BRA2E19"`.
       2. Tenta cadastrar um segundo veículo válido com a mesma placa `"BRA2E19"`.
       3. Verifica que `RegraNegocioException` é lançada na segunda chamada.
       4. Verifica que o total de veículos no repositório permanece `1`.
   - *Motivo da falha:* O segundo veículo é salvo normalmente, gerando duplicidade.

2. **🟢 GREEN (Implementação mínima):**
   - Adicionar método `boolean existePorPlaca(String placa)` na interface `VeiculoRepository` e implementá-lo em `InMemoryVeiculoRepository`.
   - Em `VeiculoService.cadastrar()`, verificar antes de salvar:
     ```java
     if (repository.existePorPlaca(veiculo.getPlaca())) {
         throw new RegraNegocioException("Já existe um veículo cadastrado com a placa informada.");
     }
     ```

3. **🔵 REFACTOR (Melhoria do código):**
   - Extrair método `validarDuplicidadePlaca(String placa)` em `VeiculoService`.
   - Garantir que as validações de formato/dados (CA1, CA2, CA3 — em memória e baratas) executem **antes** da consulta ao repositório (CA4 — potencialmente I/O).

#### Critério de Saída (Gate)
- Executar `mvn test` em `passo_5`.
- Resultado esperado: **Todos os testes de CA1 a CA5 passando**.

---

### 📁 `passo_6` — Estágio Final: Refatoração, Clean Code e Projeto Completo

#### Objetivo
Consolidar o design emergente após todos os critérios de aceitação estarem cobertos por testes, aplicando boas práticas de código limpo e organização de testes com `@DisplayName` e `@Nested`.

#### Evoluções realizadas no `passo_6`
1. **Refatoração da Entidade e Serviço:**
   - Garantir imutabilidade ou encapsulamento seguro em `Veiculo` (construtor de requisição e método `comId(Long novoId)` ou separação clara de estado).
   - Validação defensiva contra referência `veiculo == null`.
   - Em `VeiculoService`, o método `cadastrar(Veiculo veiculo)` lê-se como uma especificação executável de alto nível:
     ```java
     public Veiculo cadastrar(Veiculo veiculo) {
         Objects.requireNonNull(veiculo, "Os dados do veículo são obrigatórios.");
         validarPlaca(veiculo.getPlaca());
         validarAno(veiculo.getAno());
         validarPreco(veiculo.getPreco());
         validarDuplicidadeDePlaca(veiculo.getPlaca());
         return repository.salvar(veiculo);
     }
     ```
2. **Refatoração da Suíte de Testes (`VeiculoServiceTest`):**
   - Organização semântica com `@DisplayName` e classes internas `@Nested` agrupadas por Critério de Aceitação (`CA1`, `CA2`, `CA3`, `CA4`, `CA5`).
   - Uso de `@ParameterizedTest` para cenários de borda de placa, ano e preço.
   - Verificação completa de estado (garantindo que falhas em CA1–CA4 nunca sujam o repositório).

---

## 7. Matriz de Rastreabilidade (Critérios de Aceitação × Passos × Testes)

| Critério | Descrição | Introduzido em | Casos de Teste JUnit 5 Correspondentes |
| :--- | :--- | :--- | :--- |
| **CA5** | Cadastro válido recebe ID e é persistido | `passo_1` | `deveCadastrarVeiculoValidoGerandoIdEPersistindoNoEstoque`, `deveGerarIdsSequenciaisParaMultiplosVeiculos` |
| **CA1** | Placa no padrão Mercosul `AAA1A23` | `passo_2` | `naoDeveCadastrarComPlacaNulaOuVazia`, `naoDeveCadastrarComPlacaForaDoPadraoMercosul` (`ABC1234`, `abc1d23`, `AB12C34`, `AAA1223`) |
| **CA2** | Ano entre `1900` e `anoAtual + 1` | `passo_3` | `naoDeveCadastrarComAnoInferiorA1900` (`1899`), `naoDeveCadastrarComAnoSuperiorAoLimite` (`anoAtual + 2`), `deveAceitarAnosNosLimitesExatos` (`1900` e `anoAtual + 1`) |
| **CA3** | Preço maior que zero (`> 0`) | `passo_4` | `naoDeveCadastrarComPrecoNulo`, `naoDeveCadastrarComPrecoZero`, `naoDeveCadastrarComPrecoNegativo`, `deveAceitarPrecoMinimoPositivo` (`0.01`) |
| **CA4** | Proibir duplicidade de placa | `passo_5` | `naoDeveCadastrarVeiculoComPlacaDuplicada`, `devePermitirCadastrarVeiculosComPlacasDiferentes` |

---

## 8. Como Executar e Validar Cada Passo

Para testar qualquer estágio da evolução individualmente via terminal:

```powershell
# Exemplo para validar o passo inicial
Set-Location .\passo_0
mvn clean test

# Exemplo para validar o projeto final completo
Set-Location ..\passo_6
mvn clean test
```
