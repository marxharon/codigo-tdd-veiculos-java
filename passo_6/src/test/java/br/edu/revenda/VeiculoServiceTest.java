package br.edu.revenda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Passo 6 (Final): Suíte Completa de Testes TDD - Cadastro de Veículos no Estoque")
class VeiculoServiceTest {

    private VeiculoRepository repository;
    private VeiculoService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryVeiculoRepository();
        service = new VeiculoService(repository);
    }

    @Nested
    @DisplayName("CA1: Placa deve seguir o padrão Mercosul simplificado AAA1A23")
    class ValidacaoDePlacaTests {

        @ParameterizedTest
        @ValueSource(strings = {"AAA1A23", "ABC1D23", "BRA2E19", "XYZ9Z99"})
        @DisplayName("Deve aceitar placas válidas no padrão Mercosul AAA1A23")
        void deveAceitarPlacasNoPadraoMercosul(String placaValida) {
            Veiculo veiculo = new Veiculo(placaValida, "Hyundai HB20", 2024, new BigDecimal("85000.00"));

            Veiculo salvo = service.cadastrar(veiculo);

            assertNotNull(salvo.getId());
            assertEquals(placaValida, salvo.getPlaca());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {
                "ABC1234",   // Padrão antigo (4 números no final)
                "ABC-1D23",  // Com hífen
                "abc1d23",   // Letras minúsculas
                "AB12C34",   // Apenas 2 letras no início
                "AAA1223",   // Número na 5ª posição em vez de letra
                "AAAA123",   // 4 letras no início
                "ABC1D2",    // Menos de 7 caracteres
                "ABC1D234",  // Mais de 7 caracteres
                "   "        // Apenas espaços
        })
        @DisplayName("Deve rejeitar placas nulas, vazias ou fora do formato AAA1A23")
        void deveRejeitarPlacasInvalidas(String placaInvalida) {
            Veiculo veiculo = new Veiculo(placaInvalida, "Hyundai HB20", 2024, new BigDecimal("85000.00"));

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> service.cadastrar(veiculo)
            );

            assertTrue(ex.getMessage().contains("AAA1A23"));
            assertTrue(repository.listarTodos().isEmpty(), "Nenhum veículo inválido deve ser persistido");
        }
    }

    @Nested
    @DisplayName("CA2: Ano deve estar entre 1900 e o ano atual + 1")
    class ValidacaoDeAnoTests {

        @Test
        @DisplayName("Deve aceitar veículo no limite inferior exato (1900)")
        void deveAceitarAnoNoLimiteInferior1900() {
            Veiculo veiculo = new Veiculo("AAA1A00", "Ford Clássico", 1900, new BigDecimal("200000.00"));

            Veiculo salvo = service.cadastrar(veiculo);

            assertEquals(1900, salvo.getAno());
        }

        @Test
        @DisplayName("Deve aceitar veículo no limite superior exato (anoAtual + 1)")
        void deveAceitarAnoNoLimiteSuperiorAnoAtualMaisUm() {
            int anoLimiteSuperior = Year.now().getValue() + 1;
            Veiculo veiculo = new Veiculo("AAA1A01", "Modelo Próximo Ano", anoLimiteSuperior, new BigDecimal("130000.00"));

            Veiculo salvo = service.cadastrar(veiculo);

            assertEquals(anoLimiteSuperior, salvo.getAno());
        }

        @ParameterizedTest
        @ValueSource(ints = {1899, 1800, 0, -2024})
        @DisplayName("Deve rejeitar veículo com ano inferior a 1900")
        void deveRejeitarAnoMenorQue1900(int anoInvalido) {
            Veiculo veiculo = new Veiculo("ABC1D23", "Veículo Inválido", anoInvalido, new BigDecimal("50000.00"));

            assertThrows(RegraNegocioException.class, () -> service.cadastrar(veiculo));
            assertTrue(repository.listarTodos().isEmpty());
        }

        @Test
        @DisplayName("Deve rejeitar veículo com ano superior ao ano atual + 1")
        void deveRejeitarAnoMaiorQueAnoAtualMaisUm() {
            int anoAcimaDoPermitido = Year.now().getValue() + 2;
            Veiculo veiculo = new Veiculo("ABC1D23", "Veículo Futuro", anoAcimaDoPermitido, new BigDecimal("95000.00"));

            assertThrows(RegraNegocioException.class, () -> service.cadastrar(veiculo));
            assertTrue(repository.listarTodos().isEmpty());
        }
    }

    @Nested
    @DisplayName("CA3: Preço deve ser maior que zero")
    class ValidacaoDePrecoTests {

        @Test
        @DisplayName("Deve aceitar veículo com preço mínimo positivo (0.01)")
        void deveAceitarPrecoMinimoPositivo() {
            Veiculo veiculo = new Veiculo("ABC1D23", "Oferta Simbólica", 2020, new BigDecimal("0.01"));

            Veiculo salvo = service.cadastrar(veiculo);

            assertEquals(new BigDecimal("0.01"), salvo.getPreco());
        }

        @Test
        @DisplayName("Deve rejeitar veículo com preço nulo")
        void deveRejeitarPrecoNulo() {
            Veiculo veiculo = new Veiculo("ABC1D23", "Sem Preço", 2022, null);

            assertThrows(RegraNegocioException.class, () -> service.cadastrar(veiculo));
            assertTrue(repository.listarTodos().isEmpty());
        }

        @ParameterizedTest
        @ValueSource(strings = {"0", "0.00", "-0.01", "-1.00", "-99999.99"})
        @DisplayName("Deve rejeitar veículo com preço igual a zero ou negativo")
        void deveRejeitarPrecoZeroOuNegativo(String precoInvalido) {
            Veiculo veiculo = new Veiculo("ABC1D23", "Preço Inválido", 2022, new BigDecimal(precoInvalido));

            assertThrows(RegraNegocioException.class, () -> service.cadastrar(veiculo));
            assertTrue(repository.listarTodos().isEmpty());
        }
    }

    @Nested
    @DisplayName("CA4: Não pode existir outro veículo com a mesma placa")
    class ValidacaoDeDuplicidadeTests {

        @Test
        @DisplayName("Deve impedir cadastro de veículo quando a placa já existir no estoque")
        void deveImpedirCadastroDePlacaDuplicada() {
            Veiculo original = new Veiculo("BRA2E19", "Renault Kwid", 2023, new BigDecimal("68000.00"));
            Veiculo duplicado = new Veiculo("BRA2E19", "Fiat Mobi", 2024, new BigDecimal("72000.00"));

            service.cadastrar(original);

            RegraNegocioException ex = assertThrows(
                    RegraNegocioException.class,
                    () -> service.cadastrar(duplicado)
            );

            assertTrue(ex.getMessage().contains("BRA2E19"));
            assertEquals(1, repository.listarTodos().size(), "O estoque deve manter apenas o primeiro veículo");
        }

        @Test
        @DisplayName("Deve permitir cadastro de múltiplos veículos com placas diferentes")
        void devePermitirCadastroComPlacasDiferentes() {
            service.cadastrar(new Veiculo("BRA2E19", "Renault Kwid", 2023, new BigDecimal("68000.00")));
            service.cadastrar(new Veiculo("MER3C45", "Fiat Mobi", 2024, new BigDecimal("72000.00")));

            assertEquals(2, repository.listarTodos().size());
        }
    }

    @Nested
    @DisplayName("CA5: Cadastro válido recebe identificador e é persistido")
    class PersistenciaEIdentificadorTests {

        @Test
        @DisplayName("Deve atribuir identificador sequencial e persistir todos os dados corretamente")
        void deveGerarIdentificadorEPersistirDadosDoVeiculo() {
            Veiculo primeiro = service.cadastrar(
                    new Veiculo("ABC1D23", "Chevrolet Onix", 2024, new BigDecimal("89900.00"))
            );
            Veiculo segundo = service.cadastrar(
                    new Veiculo("DEF4G56", "Volkswagen T-Cross", 2025, new BigDecimal("142000.00"))
            );

            assertEquals(1L, primeiro.getId());
            assertEquals(2L, segundo.getId());

            Optional<Veiculo> encontrado = repository.buscarPorId(1L);
            assertTrue(encontrado.isPresent());
            assertEquals("ABC1D23", encontrado.get().getPlaca());
            assertEquals("Chevrolet Onix", encontrado.get().getModelo());
            assertEquals(2024, encontrado.get().getAno());
            assertEquals(new BigDecimal("89900.00"), encontrado.get().getPreco());
        }
    }
}
