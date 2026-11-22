package br.com.mcpfarma.importacao;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GtinValidatorTest {

    @Test
    void deveAceitarGtin13Valido() {
        assertTrue(GtinValidator.isValid("7891234567895"));
    }

    @Test
    void deveRejeitarDigitoVerificadorIncorreto() {
        assertFalse(GtinValidator.isValid("7891234567890"));
    }

    @Test
    void deveAceitarGtin8Valido() {
        assertTrue(GtinValidator.isValid("96385074"));
    }

    @Test
    void deveRejeitarCodigoComLetras() {
        assertFalse(GtinValidator.isValid("78912345ABC95"));
    }

    @Test
    void deveRejeitarCodigoNulo() {
        assertFalse(GtinValidator.isValid(null));
    }

    @Test
    void deveRejeitarCodigoComTamanhoInvalido() {
        assertFalse(GtinValidator.isValid("123456"));
    }
}