package pt.com.despesas.integration;

import org.junit.jupiter.api.Test;
import pt.com.despesas.model.Despesa;
import pt.com.despesas.repository.DespesaRepositoryInterface;
import pt.com.despesas.repository.DespesaRepositoryJson;
import pt.com.despesas.service.DespesaService;
import pt.com.despesas.storage.JsonStorage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;

class DespesaIntegrationTest {

    @Test
    void devePersistirERecarregarDespesasEmJson() throws Exception {

        // ficheiro temporário
        Path ficheiro = Files.createTempFile("despesas", ".json");

        // primeira execução
        JsonStorage storage1 = new JsonStorage(ficheiro);
        DespesaRepositoryInterface repo1 = new DespesaRepositoryJson(storage1);
        DespesaService service1 = new DespesaService(repo1);

        service1.adicionarDespesa(
                new Despesa("Mercado", "Alimentação", 100,
                        LocalDate.of(2026, 1, 10))
        );

        service1.adicionarDespesa(
                new Despesa("Internet", "Serviços", 50,
                        LocalDate.of(2026, 1, 15))
        );

        // "reinício da aplicação"
        JsonStorage storage2 = new JsonStorage(ficheiro);
        DespesaRepositoryInterface repo2 = new DespesaRepositoryJson(storage2);
        DespesaService service2 = new DespesaService(repo2);

        assertEquals(2, service2.listarDespesas().size());

        double totalJaneiro =
                service2.totalDoMes(YearMonth.of(2026, 1));

        assertEquals(150.0, totalJaneiro);
    }
}
