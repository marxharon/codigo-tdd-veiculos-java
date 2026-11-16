# Diagrama de Classes — Cadastro de Veículos

- [`diagrama_classes.png`](./diagrama_classes.png) — Imagem em Alta Definição.
- [`diagrama_classes.svg`](./diagrama_classes.svg) — Diagrama vetorial escalável (SVG).

![Diagrama de Classes UML](./diagrama_classes.png)

## Mermaid

```mermaid
classDiagram
    direction LR

    class VeiculoService {
        -PADRAO_PLACA_MERCOSUL : Pattern$
        -ANO_MINIMO : int$
        -repository : VeiculoRepository
        +VeiculoService(repository: VeiculoRepository)
        +cadastrar(veiculo: Veiculo) Veiculo
        -validarObjetoVeiculo(veiculo: Veiculo) void
        -validarPlacaMercosul(placa: String) void
        -validarIntervaloDeAno(ano: int) void
        -validarPrecoPositivo(preco: BigDecimal) void
        -validarUnicidadeDaPlaca(placa: String) void
    }

    class VeiculoRepository {
        <<interface>>
        +salvar(veiculo: Veiculo) Veiculo
        +buscarPorId(id: Long) Optional~Veiculo~
        +buscarPorPlaca(placa: String) Optional~Veiculo~
        +existePorPlaca(placa: String) boolean
        +listarTodos() List~Veiculo~
    }

    class InMemoryVeiculoRepository {
        -bancoEmMemoria : Map~Long, Veiculo~
        -sequenciaId : long
        +salvar(veiculo: Veiculo) Veiculo
        +buscarPorId(id: Long) Optional~Veiculo~
        +buscarPorPlaca(placa: String) Optional~Veiculo~
        +existePorPlaca(placa: String) boolean
        +listarTodos() List~Veiculo~
    }

    class Veiculo {
        -id : Long
        -placa : String
        -modelo : String
        -ano : int
        -preco : BigDecimal
        +Veiculo(placa: String, modelo: String, ano: int, preco: BigDecimal)
        +Veiculo(id: Long, placa: String, modelo: String, ano: int, preco: BigDecimal)
        +comId(novoId: Long) Veiculo
        +getId() Long
        +getPlaca() String
        +getModelo() String
        +getAno() int
        +getPreco() BigDecimal
        +equals(o: Object) boolean
        +hashCode() int
    }

    VeiculoService "1" --> "1" VeiculoRepository : -repository
    InMemoryVeiculoRepository ..|> VeiculoRepository
    InMemoryVeiculoRepository "1" o--> "0..*" Veiculo : -bancoEmMemoria
    VeiculoService ..> Veiculo
    VeiculoRepository ..> Veiculo
```

## PlantUML

```plantuml
@startuml diagrama_classes
skinparam dpi 300
skinparam classAttributeIconSize 0
skinparam shadowing true
skinparam roundcorner 8
skinparam defaultFontName "Segoe UI"

class VeiculoService {
    - {static} PADRAO_PLACA_MERCOSUL : Pattern
    - {static} ANO_MINIMO : int
    - repository : VeiculoRepository
    --
    + VeiculoService(repository: VeiculoRepository)
    + cadastrar(veiculo: Veiculo) : Veiculo
    - validarObjetoVeiculo(veiculo: Veiculo) : void
    - validarPlacaMercosul(placa: String) : void
    - validarIntervaloDeAno(ano: int) : void
    - validarPrecoPositivo(preco: BigDecimal) : void
    - validarUnicidadeDaPlaca(placa: String) : void
}

interface VeiculoRepository <<interface>> {
    + salvar(veiculo: Veiculo) : Veiculo
    + buscarPorId(id: Long) : Optional<Veiculo>
    + buscarPorPlaca(placa: String) : Optional<Veiculo>
    + existePorPlaca(placa: String) : boolean
    + listarTodos() : List<Veiculo>
}

class InMemoryVeiculoRepository {
    - bancoEmMemoria : Map<Long, Veiculo>
    - sequenciaId : long
    --
    + salvar(veiculo: Veiculo) : Veiculo
    + buscarPorId(id: Long) : Optional<Veiculo>
    + buscarPorPlaca(placa: String) : Optional<Veiculo>
    + existePorPlaca(placa: String) : boolean
    + listarTodos() : List<Veiculo>
}

class Veiculo {
    - id : Long
    - placa : String
    - modelo : String
    - ano : int
    - preco : BigDecimal
    --
    + Veiculo(placa: String, modelo: String, ano: int, preco: BigDecimal)
    + Veiculo(id: Long, placa: String, modelo: String, ano: int, preco: BigDecimal)
    + comId(novoId: Long) : Veiculo
    + getId() : Long
    + getPlaca() : String
    + getModelo() : String
    + getAno() : int
    + getPreco() : BigDecimal
    + equals(o: Object) : boolean
    + hashCode() : int
}

VeiculoService "1" -right-> "1" VeiculoRepository : -repository
InMemoryVeiculoRepository .up.|> VeiculoRepository
InMemoryVeiculoRepository "1" o-right-> "0..*" Veiculo : -bancoEmMemoria
VeiculoService .up.> Veiculo
VeiculoRepository .right.> Veiculo
@enduml
```

