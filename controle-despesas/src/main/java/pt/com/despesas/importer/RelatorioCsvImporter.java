package pt.com.despesas.importer;

import pt.com.despesas.model.Despesa;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RelatorioCsvImporter {

    public List<Despesa> importar(Path ficheiro) throws IOException {

        if (!Files.exists(ficheiro)) {
            throw new IllegalArgumentException("Ficheiro CSV não encontrado");
        }

        List<String> linhas = Files.readAllLines(ficheiro);
        List<Despesa> despesas = new ArrayList<>();

        // ignora cabeçalho
        for (int i = 1; i < linhas.size(); i++) {
            String linha = linhas.get(i);

            if (linha.isBlank()) continue;

            String[] campos = linha.split(",");

            String descricao = campos[0];
            String categoria = campos[1];
            double valor = Double.parseDouble(campos[2]);
            LocalDate data = LocalDate.parse(campos[3]);

            despesas.add(new Despesa(
                    descricao,
                    categoria,
                    valor,
                    data
            ));
        }

        return despesas;
    }
}
