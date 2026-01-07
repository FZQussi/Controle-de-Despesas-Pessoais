package pt.com.despesas.importer;

import org.junit.jupiter.api.Test;
import pt.com.despesas.model.Despesa;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RelatorioCsvImporterTest {

    // ------------------------------
    // Testes para importar() rigoroso
    // ------------------------------

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

    @Test
    void deveFalharQuandoValorForNegativo() throws Exception {
        Path ficheiro = Files.createTempFile("despesas", ".csv");

        String csv = """
                Descrição,Categoria,Valor,Data
                Mercado,Alimentação,-10,2026-01-10
                """;

        Files.writeString(ficheiro, csv);

        RelatorioCsvImporter importer = new RelatorioCsvImporter();

        assertThrows(
                CsvValidationException.class,
                () -> importer.importar(ficheiro)
        );
    }

    @Test
    void deveFalharQuandoDataForInvalida() throws Exception {
        Path ficheiro = Files.createTempFile("despesas", ".csv");

        String csv = """
                Descrição,Categoria,Valor,Data
                Mercado,Alimentação,100,10-01-2026
                """;

        Files.writeString(ficheiro, csv);

        RelatorioCsvImporter importer = new RelatorioCsvImporter();

        assertThrows(
                CsvValidationException.class,
                () -> importer.importar(ficheiro)
        );
    }

    @Test
    void deveFalharQuandoDescricaoOuCategoriaVazias() throws Exception {
        Path ficheiro = Files.createTempFile("despesas", ".csv");

        String csv = """
                Descrição,Categoria,Valor,Data
                ,Alimentação,100,2026-01-10
                Mercado,,50,2026-01-15
                """;

        Files.writeString(ficheiro, csv);

        RelatorioCsvImporter importer = new RelatorioCsvImporter();

        assertThrows(CsvValidationException.class, () -> importer.importar(ficheiro));
    }

    // --------------------------------
    // Testes para importarParcial()
    // --------------------------------

    @Test
    void deveImportarApenasLinhasValidas() throws Exception {
        Path ficheiro = Files.createTempFile("despesas", ".csv");

        String csv = """
                Descrição,Categoria,Valor,Data
                Mercado,Alimentação,100,2026-01-10
                ,Alimentação,50,2026-01-15
                Cinema,Lazer,-20,2026-01-20
                Internet,Serviços,50,2026-01-12
                """;

        Files.writeString(ficheiro, csv);

        RelatorioCsvImporter importer = new RelatorioCsvImporter();
        List<Despesa> despesas = importer.importarParcial(ficheiro);

        // Apenas linhas válidas foram importadas
        assertEquals(2, despesas.size());
        assertEquals("Mercado", despesas.get(0).getDescricao());
        assertEquals("Internet", despesas.get(1).getDescricao());
    }

    @Test
    void importarParcialNaoFalhaMesmoComErros() throws Exception {
        Path ficheiro = Files.createTempFile("despesas", ".csv");

        String csv = """
                Descrição,Categoria,Valor,Data
                ,Alimentação,50,2026-01-15
                Cinema,Lazer,-20,2026-01-20
                """;

        Files.writeString(ficheiro, csv);

        RelatorioCsvImporter importer = new RelatorioCsvImporter();
        List<Despesa> despesas = importer.importarParcial(ficheiro);

        // Nenhuma linha válida, retorna lista vazia
        assertTrue(despesas.isEmpty());
    }
}
