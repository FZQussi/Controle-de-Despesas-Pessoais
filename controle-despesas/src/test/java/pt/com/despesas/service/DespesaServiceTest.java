package pt.com.despesas.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pt.com.despesas.model.Despesa;
import pt.com.despesas.repository.DespesaRepositoryInterface;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DespesaServiceTest {

    @Mock
    private DespesaRepositoryInterface repository;

    private DespesaService service;

    @BeforeEach
    void setup() {
        service = new DespesaService(repository);
    }

    // ------------------------
    // Validação
    // ------------------------

    @Test
    void deveLancarExcecaoQuandoValorForZeroOuNegativo() {
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

    // ------------------------
    // Relatório por categoria
    // ------------------------

    @Test
    void deveCalcularTotalPorCategoria() {
        when(repository.listar()).thenReturn(List.of(
                new Despesa("Mercado", "Alimentação", 100, LocalDate.now()),
                new Despesa("Restaurante", "Alimentação", 50, LocalDate.now()),
                new Despesa("Internet", "Serviços", 40, LocalDate.now())
        ));

        Map<String, Double> totais = service.totalPorCategoria();

        assertEquals(150.0, totais.get("Alimentação"));
        assertEquals(40.0, totais.get("Serviços"));
    }

    // ------------------------
    // Relatório mensal
    // ------------------------

    @Test
    void deveCalcularTotalDoMes() {
        when(repository.listar()).thenReturn(List.of(
                new Despesa("Mercado", "Alimentação", 100,
                        LocalDate.of(2026, 1, 10)),
                new Despesa("Internet", "Serviços", 50,
                        LocalDate.of(2026, 1, 15)),
                new Despesa("Cinema", "Lazer", 30,
                        LocalDate.of(2026, 2, 5))
        ));

        double total = service.totalDoMes(YearMonth.of(2026, 1));

        assertEquals(150.0, total);
    }
}
