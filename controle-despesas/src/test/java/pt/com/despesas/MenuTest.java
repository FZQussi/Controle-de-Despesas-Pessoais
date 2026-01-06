package pt.com.despesas;

import org.junit.jupiter.api.Test;
import pt.com.despesas.model.Despesa;
import pt.com.despesas.service.DespesaServiceInterface;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

class MenuTest {

    @Test
    void testarAdicionarDespesa() {
        // Mock da interface
        DespesaServiceInterface serviceMock = mock(DespesaServiceInterface.class);

        // Simula uma chamada
        Despesa despesa = new Despesa("Teste", "Teste", 100, LocalDate.now());
        serviceMock.adicionarDespesa(despesa);

        // Verifica que o método foi chamado
        verify(serviceMock).adicionarDespesa(despesa);
    }
}
