package pt.com.despesas.export;

import org.junit.jupiter.api.Test;
import pt.com.despesas.model.Despesa;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RelatorioCsvExporterTest {

    @Test
    void deveExportarRelatorioParaCsv() throws Exception {
        RelatorioCsvExporter exporter = new RelatorioCsvExporter();

        List<Despesa> despesas = List.of(
                new Despesa("Mercado", "Alimentação", 100,
                        LocalDate.of(2026, 1, 10)),
                new Despesa("Internet", "Serviços", 50,
                        LocalDate.of(2026, 1, 15))
        );

        Path diretorio = Files.createTempDirectory("relatorios");

        Path ficheiro = exporter.exportar(
                despesas,
                YearMonth.of(2026, 1),
                diretorio
        );

        assertTrue(Files.exists(ficheiro));

        String conteudo = Files.readString(ficheiro);
        assertTrue(conteudo.contains("Mercado"));
        assertTrue(conteudo.contains("Alimentação"));
    }
}
