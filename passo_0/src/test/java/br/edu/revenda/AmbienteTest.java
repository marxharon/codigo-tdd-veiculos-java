package br.edu.revenda;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Passo 0: Verificação Inicial do Ambiente de Testes (JUnit 5)")
class AmbienteTest {

    @Test
    @DisplayName("Deve confirmar que o ambiente JUnit 5 está configurado e operacional")
    void deveValidarAmbienteDeTestesConfigurado() {
        assertTrue(true, "O runner do JUnit 5 deve executar este teste com sucesso.");
    }
}
