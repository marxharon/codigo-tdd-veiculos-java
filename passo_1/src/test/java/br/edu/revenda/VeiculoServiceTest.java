package br.edu.revenda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Passo 1: TDD - CA5 (Cadastro válido recebe identificador e é persistido)")
class VeiculoServiceTest {

    private VeiculoRepository repository;
    private VeiculoService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryVeiculoRepository();
        service = new VeiculoService(repository);
    }

    @Test
    @DisplayName("CA5: Deve cadastrar um veículo válido atribuindo identificador e persistindo no estoque")
    void deveCadastrarVeiculoValidoGerandoIdentificadorEPersistindoNoEstoque() {
        Veiculo novoVeiculo = new Veiculo("ABC1D23", "Toyota Corolla", 2024, new BigDecimal("125000.00"));

        Veiculo cadastrado = service.cadastrar(novoVeiculo);

        assertNotNull(cadastrado.getId(), "O veículo cadastrado deve receber um ID");
        assertEquals(1L, cadastrado.getId());

        Optional<Veiculo> persistido = repository.buscarPorId(cadastrado.getId());
        assertTrue(persistido.isPresent(), "O veículo deve estar persistido no repositório");
        assertEquals("ABC1D23", persistido.get().getPlaca());
    }
}
