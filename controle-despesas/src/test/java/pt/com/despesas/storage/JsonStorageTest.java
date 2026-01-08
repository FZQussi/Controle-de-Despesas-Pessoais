package pt.com.despesas.storage;

import org.junit.jupiter.api.Test;
import pt.com.despesas.model.Despesa;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonStorageTest {

    @Test
    void deveGuardarECarregarDespesas() throws Exception {
        Path ficheiro = Files.createTempFile("despesas", ".json");

        JsonStorage storage = new JsonStorage(ficheiro);

        List<Despesa> despesas = List.of(
                new Despesa("Mercado", "Alimentação", 100, LocalDate.of(2026, 1, 10)),
                new Despesa("Internet", "Serviços", 50, LocalDate.of(2026, 1, 15))
        );

        storage.guardar(despesas);

        List<Despesa> carregadas = storage.carregar();

        assertEquals(2, carregadas.size());
        assertEquals("Mercado", carregadas.get(0).getDescricao());
        assertEquals(50, carregadas.get(1).getValor());
    }

    @Test
    void deveRetornarListaVaziaQuandoFicheiroNaoExiste() throws Exception {
        Path ficheiro = Files.createTempFile("despesas", ".json");
        Files.deleteIfExists(ficheiro);

        JsonStorage storage = new JsonStorage(ficheiro);

        List<Despesa> despesas = storage.carregar();

        assertNotNull(despesas);
        assertTrue(despesas.isEmpty());
    }

    @Test
    void deveCriarDiretorioQuandoNaoExiste() throws Exception {
        Path diretorio = Files.createTempDirectory("dados");
        Path ficheiro = diretorio.resolve("subdir/despesas.json");

        JsonStorage storage = new JsonStorage(ficheiro);

        List<Despesa> despesas = List.of(
                new Despesa("Renda", "Moradia", 800, LocalDate.now())
        );

        storage.guardar(despesas);

        assertTrue(Files.exists(ficheiro));
    }
}
