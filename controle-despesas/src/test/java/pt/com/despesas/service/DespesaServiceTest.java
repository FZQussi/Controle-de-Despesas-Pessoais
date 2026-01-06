package pt.com.despesas.service;

import org.junit.jupiter.api.Test;
import pt.com.despesas.model.Despesa;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DespesaServiceTest {

    @Test
    void deveLancarExcecaoQuandoValorForZeroOuNegativo() {
        DespesaService service = new DespesaService();

        Despesa despesaInvalida = new Despesa(
                "Teste",
                "Teste",
                0,
                LocalDate.now()
        );

        assertThrows(IllegalArgumentException.class, () ->
                service.adicionarDespesa(despesaInvalida)
        );
    }

    @Test
    void deveAceitarDespesaComValorPositivo() {
        DespesaService service = new DespesaService();

        Despesa despesaValida = new Despesa(
                "Teste",
                "Teste",
                10.0,
                LocalDate.now()
        );

        assertDoesNotThrow(() ->
                service.adicionarDespesa(despesaValida)
        );
    }
}
