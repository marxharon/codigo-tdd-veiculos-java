package br.edu.revenda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Passo 5: TDD - CA5 + CA1 + CA2 + CA3 + CA4 (Impedir duplicidade de placa)")
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

        assertNotNull(cadastrado.getId());
        assertEquals(1L, cadastrado.getId());

        Optional<Veiculo> persistido = repository.buscarPorId(cadastrado.getId());
        assertTrue(persistido.isPresent());
        assertEquals("ABC1D23", persistido.get().getPlaca());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"ABC1234", "ABC-1D23", "abc1d23", "AB12C34", "AAA1223", "ABCD123", "ABC1D2"})
    @DisplayName("CA1: Deve rejeitar placas nulas, vazias ou fora do padrão Mercosul AAA1A23")
    void naoDeveCadastrarVeiculoComPlacaForaDoPadraoMercosul(String placaInvalida) {
        Veiculo veiculoComPlacaInvalida = new Veiculo(placaInvalida, "Honda Civic", 2023, new BigDecimal("110000.00"));

        assertThrows(RegraNegocioException.class, () -> service.cadastrar(veiculoComPlacaInvalida));
        assertTrue(repository.buscarPorId(1L).isEmpty());
    }

    @Test
    @DisplayName("CA2: Deve rejeitar veículo com ano inferior a 1900")
    void naoDeveCadastrarVeiculoComAnoMenorQue1900() {
        Veiculo veiculoAnoAntigo = new Veiculo("ABC1D23", "Ford Modelo Antigo", 1899, new BigDecimal("50000.00"));

        assertThrows(RegraNegocioException.class, () -> service.cadastrar(veiculoAnoAntigo));
    }

    @Test
    @DisplayName("CA2: Deve rejeitar veículo com ano superior ao ano atual + 1")
    void naoDeveCadastrarVeiculoComAnoMaiorQueAnoAtualMaisUm() {
        int anoAcimaDoLimite = Year.now().getValue() + 2;
        Veiculo veiculoAnoFuturo = new Veiculo("ABC1D23", "Conceito Futuro", anoAcimaDoLimite, new BigDecimal("90000.00"));

        assertThrows(RegraNegocioException.class, () -> service.cadastrar(veiculoAnoFuturo));
    }

    @Test
    @DisplayName("CA2: Deve aceitar anos exatamente nos limites inferior (1900) e superior (anoAtual + 1)")
    void deveAceitarCadastroNosLimitesDeAnoValidos() {
        int anoMaximoValido = Year.now().getValue() + 1;

        Veiculo limiteInferior = service.cadastrar(new Veiculo("AAA1A00", "Clássico", 1900, new BigDecimal("150000.00")));
        Veiculo limiteSuperior = service.cadastrar(new Veiculo("BBB2B99", "Lançamento", anoMaximoValido, new BigDecimal("95000.00")));

        assertNotNull(limiteInferior.getId());
        assertNotNull(limiteSuperior.getId());
    }

    @Test
    @DisplayName("CA3: Deve rejeitar veículo com preço nulo")
    void naoDeveCadastrarVeiculoComPrecoNulo() {
        Veiculo veiculoSemPreco = new Veiculo("ABC1D23", "VW Polo", 2024, null);

        assertThrows(RegraNegocioException.class, () -> service.cadastrar(veiculoSemPreco));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "0.00", "-0.01", "-50000.00"})
    @DisplayName("CA3: Deve rejeitar veículo com preço igual ou menor que zero")
    void naoDeveCadastrarVeiculoComPrecoZeroOuNegativo(String valorPreco) {
        Veiculo veiculoPrecoInvalido = new Veiculo("ABC1D23", "VW Polo", 2024, new BigDecimal(valorPreco));

        assertThrows(RegraNegocioException.class, () -> service.cadastrar(veiculoPrecoInvalido));
    }

    @Test
    @DisplayName("CA4: Deve impedir o cadastro de segundo veículo com a mesma placa")
    void naoDeveCadastrarVeiculoComPlacaDuplicada() {
        Veiculo primeiro = new Veiculo("BRA2E19", "Fiat Pulse", 2023, new BigDecimal("98000.00"));
        Veiculo duplicado = new Veiculo("BRA2E19", "Jeep Compass", 2024, new BigDecimal("165000.00"));

        service.cadastrar(primeiro);

        RegraNegocioException ex = assertThrows(
                RegraNegocioException.class,
                () -> service.cadastrar(duplicado)
        );

        assertTrue(ex.getMessage().contains("BRA2E19"));
        assertTrue(repository.buscarPorId(2L).isEmpty(), "O segundo veículo duplicado não deve ser persistido");
    }
}
