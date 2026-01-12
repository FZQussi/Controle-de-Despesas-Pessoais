package pt.com.despesas.importer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.com.despesas.model.Despesa;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class RelatorioCsvImporter {

    private static final Logger log =
            LoggerFactory.getLogger(RelatorioCsvImporter.class);

    // -------------------------------
    // Importação com validação rígida
    // -------------------------------
    public List<Despesa> importar(Path ficheiro) throws IOException {

        log.info("Início da importação CSV (modo rígido): {}", ficheiro);

        if (!Files.exists(ficheiro)) {
            log.error("Ficheiro CSV não encontrado: {}", ficheiro);
            throw new IllegalArgumentException("Ficheiro CSV não encontrado");
        }

        List<String> linhas = Files.readAllLines(ficheiro);

        if (linhas.size() <= 1) {
            log.error("CSV não contém dados: {}", ficheiro);
            throw new CsvValidationException("CSV não contém dados");
        }

        List<Despesa> despesas = new ArrayList<>();

        for (int i = 1; i < linhas.size(); i++) {
            String linha = linhas.get(i);

            if (linha.isBlank()) {
                log.debug("Linha {} vazia ignorada", i + 1);
                continue;
            }

            String[] campos = linha.split(",");

            if (campos.length != 4) {
                log.error("Linha {} inválida: número de colunas ({})",
                        i + 1, campos.length);
                throw new CsvValidationException(
                        "Linha " + (i + 1) + ": número inválido de colunas"
                );
            }

            String descricao = campos[0].trim();
            String categoria = campos[1].trim();
            String valorStr = campos[2].trim();
            String dataStr = campos[3].trim();

            if (descricao.isEmpty()) {
                log.error("Linha {} inválida: descrição vazia", i + 1);
                throw new CsvValidationException(
                        "Linha " + (i + 1) + ": descrição vazia"
                );
            }

            if (categoria.isEmpty()) {
                log.error("Linha {} inválida: categoria vazia", i + 1);
                throw new CsvValidationException(
                        "Linha " + (i + 1) + ": categoria vazia"
                );
            }

            double valor;
            try {
                valor = Double.parseDouble(valorStr);
            } catch (NumberFormatException e) {
                log.error("Linha {} inválida: valor '{}'", i + 1, valorStr);
                throw new CsvValidationException(
                        "Linha " + (i + 1) + ": valor inválido"
                );
            }

            if (valor <= 0) {
                log.error("Linha {} inválida: valor não positivo ({})",
                        i + 1, valor);
                throw new CsvValidationException(
                        "Linha " + (i + 1) + ": valor deve ser positivo"
                );
            }

            LocalDate data;
            try {
                data = LocalDate.parse(dataStr);
            } catch (DateTimeParseException e) {
                log.error("Linha {} inválida: data '{}'", i + 1, dataStr);
                throw new CsvValidationException(
                        "Linha " + (i + 1) + ": data inválida (yyyy-MM-dd)"
                );
            }

            despesas.add(new Despesa(descricao, categoria, valor, data));
        }

        log.info("Importação CSV concluída com sucesso: {} despesas importadas",
                despesas.size());

        return despesas;
    }

    // -------------------------------
    // Importação parcial: ignora erros
    // -------------------------------
    public List<Despesa> importarParcial(Path ficheiro) throws IOException {

        log.info("Início da importação CSV (modo parcial): {}", ficheiro);

        if (!Files.exists(ficheiro)) {
            log.error("Ficheiro CSV não encontrado: {}", ficheiro);
            throw new IllegalArgumentException("Ficheiro CSV não encontrado");
        }

        List<String> linhas = Files.readAllLines(ficheiro);

        if (linhas.size() <= 1) {
            log.warn("CSV não contém dados: {}", ficheiro);
            return List.of();
        }

        List<Despesa> despesas = new ArrayList<>();

        for (int i = 1; i < linhas.size(); i++) {
            String linha = linhas.get(i);

            if (linha.isBlank()) {
                log.debug("Linha {} vazia ignorada", i + 1);
                continue;
            }

            String[] campos = linha.split(",");

            if (campos.length != 4) {
                log.warn("Linha {} ignorada: número inválido de colunas",
                        i + 1);
                continue;
            }

            String descricao = campos[0].trim();
            String categoria = campos[1].trim();
            String valorStr = campos[2].trim();
            String dataStr = campos[3].trim();

            try {
                if (descricao.isEmpty() || categoria.isEmpty()) {
                    throw new IllegalArgumentException(
                            "Descrição ou categoria vazia"
                    );
                }

                double valor = Double.parseDouble(valorStr);
                if (valor <= 0) {
                    throw new IllegalArgumentException(
                            "Valor deve ser positivo"
                    );
                }

                LocalDate data = LocalDate.parse(dataStr);

                despesas.add(new Despesa(descricao, categoria, valor, data));

            } catch (Exception e) {
                log.warn("Linha {} ignorada: {}", i + 1, e.getMessage());
            }
        }

        log.info("Importação parcial concluída: {} despesas válidas importadas",
                despesas.size());

        return despesas;
    }
}
