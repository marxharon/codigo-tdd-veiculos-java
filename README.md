# Guia Prático de TDD em Java com JUnit 5: Cadastro de Veículos

Bem-vindo ao laboratório prático de **Test-Driven Development (TDD)**! Este repositório demonstra, passo a passo (*baby steps*), como desenvolver uma funcionalidade de negócio real em Java guiada inteiramente por testes unitários automatizados com **JUnit 5**.

---

## 1. O Problema e a História de Usuário

Uma revenda de automóveis precisa de um serviço para incluir veículos em seu estoque garantindo a integridade dos dados cadastrados.

> **História de Usuário:**  
> **Como** atendente da revenda,  
> **Quero** cadastrar um veículo informando placa, modelo, ano e preço,  
> **Para** disponibilizá-lo para venda no estoque da loja.

### Critérios de Aceitação (Regras de Negócio)
- **CA1:** A placa deve seguir o padrão Mercosul simplificado `AAA1A23` (3 letras maiúsculas, 1 número, 1 letra maiúscula e 2 números).
- **CA2:** O ano do veículo deve estar entre `1900` e o `ano atual + 1`.
- **CA3:** O preço deve ser estritamente maior que zero (`> 0`).
- **CA4:** Não pode existir outro veículo no estoque com a mesma placa (proibir duplicidade).
- **CA5:** Um cadastro válido deve receber um identificador único (`id`) e ficar persistido no estoque.

---

## 2. Entendendo a Dinâmica do TDD (Ciclo Red-Green-Refactor)

O desenvolvimento neste repositório foi dividido em **7 pastas independentes (`passo_0` a `passo_6`)**. Cada pasta representa uma fotografia da evolução do software após completar um ciclo do TDD:

1. 🔴 **RED (Vermelho):** Escrevemos primeiro um teste automatizado para um pequeno comportamento que ainda não existe. O teste falha (ou nem compila).
2. 🟢 **GREEN (Verde):** Escrevemos a quantidade mínima de código de produção necessária apenas para fazer o teste passar.
3. 🔵 **REFACTOR (Refatoração):** Com a segurança dos testes passando, limpamos o código (extraímos métodos, constantes, melhoramos nomes e eliminamos duplicações) sem alterar o comportamento externo.

> **Regra de Progressão (Baby Steps):** Só avançamos do `passo_N` para o `passo_N+1` quando todos os testes do passo atual estão passando (`100% verdes`).

---

## 3. Explicação Didática de Cada Passo

### 📁 `passo_0` — Estágio Inicial: Preparação e *Sanity Check*
- **O que este passo trata:** Antes de começar a programar regras de negócio, precisamos garantir que o projeto compila e que o motor de testes (JUnit 5) está funcionando.
- **O que existe no código:** Apenas o arquivo de configuração do Maven (`pom.xml`) e uma classe de teste mínima (`AmbienteTest.java`) com um `assertTrue(true)`.
- **Objetivo pedagógico:** Separar problemas de infraestrutura/configuração de problemas de lógica de programação. Se o `passo_0` roda, o ambiente está pronto para o TDD.

---

### 📁 `passo_1` — Baby Step 1: O Caminho Feliz — Persistência e ID (CA5)
- **O que este passo trata:** Atende ao **CA5** (cadastrar um veículo válido, gerar um `id` e guardá-lo no estoque).
- **Por que começamos pelo CA5 e não pelo CA1?** Em TDD, começar pelo "caminho feliz" (*happy path*) ajuda a desenhar a estrutura básica da solução: quais classes precisamos? Qual é o nome do método principal? Quais parâmetros ele recebe e o que ele retorna?
- **Evolução das classes:**
  - `VeiculoServiceTest`: Testa o cadastro de um veículo válido e verifica se o `id` foi preenchido (`1L`) e se o veículo pode ser recuperado do repositório.
  - `Veiculo`: Nasce com os atributos básicos (`id`, `placa`, `modelo`, `ano`, `preco`).
  - `VeiculoRepository` e `InMemoryVeiculoRepository`: Surgem para armazenar os veículos em memória (simulando um banco de dados de forma rápida e isolada).
  - `VeiculoService`: Possui apenas uma linha no método `cadastrar`: repassa o veículo diretamente para `repository.salvar(veiculo)`. Ainda não há nenhuma validação!

---

### 📁 `passo_2` — Baby Step 2: Validação da Placa Mercosul (CA1)
- **O que este passo trata:** Adiciona a primeira barreira de validação: rejeitar placas que não sigam o formato `AAA1A23` (ex.: placas no padrão antigo `ABC1234`, letras minúsculas `abc1d23`, placas com traço `ABC-1D23`, vazias ou nulas).
- **Evolução das classes:**
  - `RegraNegocioException`: Criada para representar erros de validação de domínio quando o atendente informa dados inválidos.
  - `VeiculoServiceTest`: Mantém o teste do Passo 1 (garantindo que não quebramos o que já funcionava) e adiciona um teste parametrizado testando diversas placas inválidas.
  - `VeiculoService`: Ganha o método privado `validarPlaca(String placa)` utilizando uma Expressão Regular (`^[A-Z]{3}[0-9][A-Z][0-9]{2}$`). Se a placa for inválida, lança `RegraNegocioException` antes de chamar o repositório.

---

### 📁 `passo_3` — Baby Step 3: Validação do Ano do Veículo (CA2)
- **O que este passo trata:** Garante que o veículo tenha um ano coerente: no mínimo `1900` e no máximo `ano atual + 1` (ex.: modelos do próximo ano já lançados).
- **Conceito de Teste Aplicado:** **Análise de Valor Limite (Boundary Value Analysis)**. Testamos exatamente nas fronteiras:
  - Fora do limite inferior (`1899` → deve falhar)
  - Exatamente no limite inferior (`1900` → deve passar)
  - Exatamente no limite superior (`anoAtual + 1` → deve passar)
  - Fora do limite superior (`anoAtual + 2` → deve falhar)
- **Evolução das classes:**
  - `VeiculoService`: Ganha a constante `ANO_MINIMO = 1900` e o método `validarAno(int ano)`, utilizando `Year.now().getValue() + 1` para calcular dinamicamente o teto permitido.

---

### 📁 `passo_4` — Baby Step 4: Validação de Preço Positivo (CA3)
- **O que este passo trata:** Impede o cadastro de veículos sem preço (`null`), com preço zerado (`0.00`) ou negativo (`-0.01`, `-50000.00`).
- **Evolução das classes:**
  - `VeiculoServiceTest`: Adiciona testes específicos para preço `null`, zero e valores negativos.
  - `VeiculoService`: Ganha o método `validarPreco(BigDecimal preco)`, que utiliza `preco.compareTo(BigDecimal.ZERO) <= 0` (forma correta em Java para comparar valores monetários com `BigDecimal`, evitando problemas de escala como `0` vs `0.00`).

---

### 📁 `passo_5` — Baby Step 5: Impedimento de Placa Duplicada (CA4)
- **O que este passo trata:** Impede que dois veículos com a mesma placa sejam cadastrados no estoque.
- **Evolução das classes:**
  - `VeiculoServiceTest`: Cria um cenário onde cadastra um primeiro veículo com placa `"BRA2E19"` (com sucesso) e, em seguida, tenta cadastrar outro veículo com a mesma placa `"BRA2E19"`, esperando que o serviço lance `RegraNegocioException` e não salve o segundo registro.
  - `VeiculoRepository` / `InMemoryVeiculoRepository`: Evoluem apenas agora (quando a necessidade real surgiu!) ganhando o método `boolean existePorPlaca(String placa)`.
  - `VeiculoService`: Ganha o método `validarDuplicidadePlaca(String placa)`, posicionado estrategicamente **após** as validações de formato, ano e preço (evitando consultar o repositório caso os dados básicos já estejam inválidos).

---

### 📁 `passo_6` — Estágio Final: Refatoração Limpa (Clean Code) e Suíte Completa
- **O que este passo trata:** Com todos os critérios de aceitação (**CA1 a CA5**) atendidos e protegidos por testes, realizamos uma refatoração de consolidação tanto no código de produção quanto na suíte de testes.
- **Evolução das classes:**
  - `Veiculo`: Torna-se uma classe **imutável** (todos os campos `final`, sem `setId` aberto), utilizando o método `comId(Long novoId)` para gerar a instância persistida com segurança.
  - `VeiculoRepository` / `InMemoryVeiculoRepository`: Ganham métodos de consulta complementares (`buscarPorPlaca`, `listarTodos`) para facilitar a inspeção completa do estoque.
  - `VeiculoService`: Código altamente legível e autoexplicativo, incluindo validação contra objeto `veiculo` nulo.
  - `VeiculoServiceTest`: Organizado em classes internas com `@Nested` (uma seção para cada Critério de Aceitação), servindo como uma **documentação viva** do sistema.

---

## 4. Anotações e Estruturas do JUnit 5 Utilizadas no Projeto

Se você está estudando as classes `*Test.java` deste projeto, abaixo está o guia de referência de todas as anotações e métodos do **JUnit 5 (Jupiter)** empregados:

### 4.1. Anotações de Ciclo de Vida e Organização

| Anotação | Onde é usada | Para que serve? |
| :--- | :--- | :--- |
| `@Test` | Em métodos | Marca um método comum como um caso de teste executável pelo JUnit. |
| `@DisplayName("...")` | Em classes e métodos | Define um nome descritivo em português (com espaços e acentos) que aparece no relatório de execução, facilitando entender qual regra de negócio está sendo testada. |
| `@BeforeEach` | No método `setUp()` | Indica que aquele método deve ser executado **antes de cada teste** individual. Usamos para criar um `new InMemoryVeiculoRepository()` e um `new VeiculoService()` limpos a cada teste, garantindo que um teste nunca interfira no outro (**isolamento**). |
| `@Nested` | Em classes internas (`passo_6`) | Permite agrupar testes relacionados dentro de subclasses organizadas por contexto (ex.: uma subclasse para `CA1`, outra para `CA2`, etc.), criando uma hierarquia visual clara no relatório de testes. |

### 4.2. Anotações de Testes Parametrizados (`junit-jupiter-params`)

Em vez de copiar e colar 8 métodos `@Test` quase idênticos para testar 8 placas inválidas diferentes, usamos testes parametrizados:

| Anotação | Para que serve? |
| :--- | :--- |
| `@ParameterizedTest` | Substitui o `@Test` quando queremos rodar o **mesmo método de teste várias vezes**, injetando um valor diferente a cada execução. |
| `@ValueSource(strings = {...})` ou `(ints = {...})` | Fornece a lista de valores (ex.: `"ABC1234"`, `"abc1d23"`, `"AB12C34"`) que serão passados, um por vez, para o parâmetro do método de teste. |
| `@NullAndEmptySource` | Injeta automaticamente um valor `null` e uma String vazia `""` no teste parametrizado, garantindo que o código sabe lidar com entradas ausentes. |

### 4.3. Métodos de Asserção (`org.junit.jupiter.api.Assertions`)

As asserções são o coração do teste: elas verificam se o resultado obtido do código é igual ao resultado esperado.

- **`assertEquals(esperado, atual)`**: Verifica se dois valores são iguais (ex.: `assertEquals(1L, cadastrado.getId())`).
- **`assertNotNull(objeto)`**: Garante que a referência retornada não é nula.
- **`assertTrue(condicao)`**: Garante que uma expressão booleana é verdadeira (ex.: verificar se `persistido.isPresent()` é `true` ou se a mensagem de erro contém determinado texto).
- **`assertThrows(ClasseDaExcecao.class, () -> chamadaDoMetodo())`**:
  - Verifica se a execução do bloco lambda `() -> service.cadastrar(veiculo)` **lança obrigatoriamente** a exceção esperada (`RegraNegocioException.class`).
  - Se o método não lançar a exceção (ou lançar outra diferente), o teste **falha**.
  - Além disso, o `assertThrows` retorna a instância da exceção capturada, permitindo inspecionar a mensagem de erro:
    ```java
    RegraNegocioException ex = assertThrows(
        RegraNegocioException.class,
        () -> service.cadastrar(veiculoInvalido)
    );
    assertTrue(ex.getMessage().contains("AAA1A23"));
    ```

### 4.4. Estrutura AAA (Arrange, Act, Assert) nos Métodos de Teste

Todos os testes do projeto seguem a estrutura clássica de 3 blocos separados por uma linha em branco:
```java
@Test
void exemploDeEstruturaAAA() {
    // 1. ARRANGE (Preparação / Cenário): montamos os objetos de entrada
    Veiculo novoVeiculo = new Veiculo("ABC1D23", "Toyota Corolla", 2024, new BigDecimal("125000.00"));

    // 2. ACT (Ação / Execução): chamamos o método que queremos testar
    Veiculo cadastrado = service.cadastrar(novoVeiculo);

    // 3. ASSERT (Verificação): conferimos se o resultado e o estado estão corretos
    assertNotNull(cadastrado.getId());
    assertEquals(1L, cadastrado.getId());
}
```

---

## 5. Como Executar o Código em Cada Passo

### Pré-requisitos
- **Java JDK 17** ou superior instalado (`java -version`).
- **Apache Maven** instalado e configurado no `PATH` (`mvn -version`) ou execução via IDE (VS Code com *Extension Pack for Java*, IntelliJ IDEA ou Eclipse).

### Executando via Terminal (PowerShell / CMD / Bash)

O projeto já inclui o **Maven Wrapper (`mvnw` / `mvnw.cmd`)** tanto na raiz quanto dentro de cada pasta `passo_X`, além de um `pom.xml` agregador na raiz. Você pode usar tanto `mvn` (caso esteja no seu `PATH`) quanto `.\mvnw`.

#### Opção 1: Executar um passo entrando na pasta dele

```powershell
# Testando o Passo 0 (Sanity Check)
cd passo_0
mvn clean test
# (ou .\mvnw clean test)

# Testando o Passo 1 (Apenas CA5)
cd ..\passo_1
mvn clean test

# Testando o Passo 2 (CA5 + CA1)
cd ..\passo_2
mvn clean test

# Testando o Passo 3 (CA5 + CA1 + CA2)
cd ..\passo_3
mvn clean test

# Testando o Passo 4 (CA5 + CA1 + CA2 + CA3)
cd ..\passo_4
mvn clean test

# Testando o Passo 5 (CA5 + CA1 + CA2 + CA3 + CA4)
cd ..\passo_5
mvn clean test

# Testando o Passo 6 (Projeto Final Completo e Refatorado)
cd ..\passo_6
mvn clean test
```

#### Opção 2: Executar a partir da raiz (`codigo_tdd`) sem mudar de diretório

```powershell
# Executar um passo específico usando -pl (Project List)
mvn test -pl passo_6
# ou com o Maven Wrapper:
.\mvnw test -pl passo_6

# Executar TODOS os passos (passo_0 até passo_6) de uma só vez
mvn clean test
# ou com o Maven Wrapper:
.\mvnw clean test
```

---

## 6. Como Interpretar o Resultado da Execução

Ao rodar `mvn clean test`, o plugin **Maven Surefire** compilará as classes de produção (`src/main/java`), compilará as classes de teste (`src/test/java`) e executará todos os testes encontrados.

### 6.1. Cenário de Sucesso (Sinal Verde 🟢)

Você verá no terminal uma saída semelhante a esta (exemplo do `passo_6`):

```text
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running br.edu.revenda.VeiculoServiceTest
[INFO] Tests run: 31, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.145 s -- in br.edu.revenda.VeiculoServiceTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 31, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

### 6.2. O que cada indicador significa?

- **`Tests run` (Testes executados):** Quantidade total de execuções de teste. *(Nota: em testes com `@ParameterizedTest`, cada valor da lista conta como 1 execução individual!)*
- **`Failures: 0` (Falhas de Asserção):** Indica quantos testes rodaram, mas o resultado do código foi **diferente** do esperado pela asserção (`assertEquals`, `assertThrows`, etc.). No ciclo TDD, quando estamos na fase **RED**, veremos `Failures: 1` (ou mais).
- **`Errors: 0` (Erros Inesperados):** Indica quantos testes quebraram por uma exceção não tratada que o teste não esperava (ex.: um `NullPointerException` acidental).
- **`Skipped: 0` (Ignorados):** Testes desabilitados temporariamente (com `@Disabled`).
- **`BUILD SUCCESS` vs `BUILD FAILURE`:**
  - `BUILD SUCCESS`: Todos os testes passaram (`Failures: 0, Errors: 0`). Você está no **GREEN** e autorizado a refatorar ou avançar para o próximo passo!
  - `BUILD FAILURE`: Ao menos um teste falhou ou houve erro de compilação. O Maven exibirá logo acima exatamente qual método falhou, a linha do teste e a diferença entre o valor esperado e o valor obtido.

### 6.3. Evolução do Número de Execuções de Teste por Passo

Ao executar do `passo_0` ao `passo_6`, você observará o crescimento progressivo da rede de proteção da suíte de testes:

| Diretório | Critérios Cobertos | Total de Execuções (`Tests run`) | Status Esperado |
| :--- | :--- | :---: | :---: |
| `passo_0` | Verificação de Ambiente | `1` | `BUILD SUCCESS` |
| `passo_1` | CA5 | `1` | `BUILD SUCCESS` |
| `passo_2` | CA5 + CA1 | `10` | `BUILD SUCCESS` |
| `passo_3` | CA5 + CA1 + CA2 | `13` | `BUILD SUCCESS` |
| `passo_4` | CA5 + CA1 + CA2 + CA3 | `18` | `BUILD SUCCESS` |
| `passo_5` | CA5 + CA1 + CA2 + CA3 + CA4 | `19` | `BUILD SUCCESS` |
| `passo_6` | CA1 a CA5 (Suíte Final Expandida com `@Nested`) | `31` | `BUILD SUCCESS` |
