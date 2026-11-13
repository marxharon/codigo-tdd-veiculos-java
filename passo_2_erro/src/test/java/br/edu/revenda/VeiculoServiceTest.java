package br.edu.revenda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Passo 2 ERRO (Fase RED): Adicionado teste do CA1 antes de implementar validação no Service")
class VeiculoServiceTest {

    private VeiculoRepository repository;
    private VeiculoService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryVeiculoRepository();
        service = new VeiculoService(repository);
    }

    @Test
    @DisplayName("CA5 [PASSA]: Deve cadastrar um veículo válido atribuindo identificador e persistindo no estoque")
    void deveCadastrarVeiculoValidoGerandoIdentificadorEPersistindoNoEstoque() {
        Veiculo novoVeiculo = new Veiculo("ABC1D23", "Toyota Corolla", 2024, new BigDecimal("125000.00"));

        Veiculo cadastrado = service.cadastrar(novoVeiculo);

        assertNotNull(cadastrado.getId());
        assertEquals(1L, cadastrado.getId());

        Optional<Veiculo> persistido = repository.buscarPorId(cadastrado.getId());
        assertTrue(persistido.isPresent());
        assertEquals("ABC1D23", persistido.get().getPlaca());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"ABC1234", "ABC-1D23", "abc1d23", "AB12C34", "AAA1223", "ABCD123", "ABC1D2"})
    @DisplayName("CA1 [FALHA / RED]: Espera RegraNegocioException, mas o Service ainda aceita qualquer placa!")
    void naoDeveCadastrarVeiculoComPlacaForaDoPadraoMercosul(String placaInvalida) {
        Veiculo veiculoComPlacaInvalida = new Veiculo(placaInvalida, "Honda Civic", 2023, new BigDecimal("110000.00"));

        RegraNegocioException ex = assertThrows(
                RegraNegocioException.class,
                () -> service.cadastrar(veiculoComPlacaInvalida)
        );

        assertTrue(ex.getMessage().contains("AAA1A23"));
        assertTrue(repository.buscarPorId(1L).isEmpty(), "Veículo inválido não deve ser persistido");
    }
}
