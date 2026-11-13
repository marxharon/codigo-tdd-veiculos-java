package br.edu.revenda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Passo 1 ERRO (Fase RED): Teste escrito ANTES da implementação do CA5")
class VeiculoServiceTest {

    private VeiculoRepository repository;
    private VeiculoService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryVeiculoRepository();
        service = new VeiculoService(repository);
    }

    @Test
    @DisplayName("CA5 [RED]: Deve falhar pois o serviço ainda não gera ID nem persiste o veículo")
    void deveCadastrarVeiculoValidoGerandoIdentificadorEPersistindoNoEstoque() {
        Veiculo novoVeiculo = new Veiculo("ABC1D23", "Toyota Corolla", 2024, new BigDecimal("125000.00"));

        Veiculo cadastrado = service.cadastrar(novoVeiculo);

        // Esta asserção FALHARÁ na fase RED porque cadastrado.getId() ainda é null!
        assertNotNull(cadastrado.getId(), "O veículo cadastrado deve receber um ID");
        assertEquals(1L, cadastrado.getId());

        Optional<Veiculo> persistido = repository.buscarPorId(cadastrado.getId());
        assertTrue(persistido.isPresent(), "O veículo deve estar persistido no repositório");
        assertEquals("ABC1D23", persistido.get().getPlaca());
    }
}
