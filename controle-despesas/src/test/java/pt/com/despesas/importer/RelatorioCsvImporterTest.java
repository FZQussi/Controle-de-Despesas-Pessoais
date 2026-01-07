package pt.com.despesas.importer;

import org.junit.jupiter.api.Test;
import pt.com.despesas.model.Despesa;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RelatorioCsvImporterTest {

    @Test
    void deveImportarDespesasDoCsv() throws Exception {
        Path ficheiro = Files.createTempFile("despesas", ".csv");

        String csv = """
                Descrição,Categoria,Valor,Data
                Mercado,Alimentação,100,2026-01-10
                Internet,Serviços,50,2026-01-15
                """;

        Files.writeString(ficheiro, csv);

        RelatorioCsvImporter importer = new RelatorioCsvImporter();

        List<Despesa> despesas = importer.importar(ficheiro);

        assertEquals(2, despesas.size());
        assertEquals("Mercado", despesas.get(0).getDescricao());
        assertEquals(50, despesas.get(1).getValor());
    }
}
