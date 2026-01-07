package pt.com.despesas.service;

import org.junit.jupiter.api.Test;
import pt.com.despesas.model.Despesa;

import java.time.LocalDate;
import java.util.Map;

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
    @Test
void deveCalcularTotalPorCategoria() {
    DespesaService service = new DespesaService();

    service.adicionarDespesa(
        new Despesa("Mercado", "Alimentação", 100, LocalDate.now())
    );
    service.adicionarDespesa(
        new Despesa("Restaurante", "Alimentação", 50, LocalDate.now())
    );
    service.adicionarDespesa(
        new Despesa("Internet", "Serviços", 40, LocalDate.now())
    );

    Map<String, Double> totais = service.totalPorCategoria();

    assertEquals(150.0, totais.get("Alimentação"));
    assertEquals(40.0, totais.get("Serviços"));
}
}
