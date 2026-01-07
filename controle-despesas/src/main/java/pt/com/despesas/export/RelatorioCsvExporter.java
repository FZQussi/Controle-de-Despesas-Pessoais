package pt.com.despesas.export;

import pt.com.despesas.model.Despesa;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.YearMonth;
import java.util.List;

public class RelatorioCsvExporter {

    public Path exportar(
            List<Despesa> despesas,
            YearMonth mes,
            Path diretorio
    ) throws IOException {

        if (despesas.isEmpty()) {
            throw new IllegalArgumentException("Não há despesas para exportar");
        }

        Files.createDirectories(diretorio);

        Path ficheiro = diretorio.resolve(
                "relatorio-" + mes + ".csv"
        );

        StringBuilder csv = new StringBuilder();
        csv.append("Descrição,Categoria,Valor,Data\n");

        for (Despesa d : despesas) {
            csv.append(d.getDescricao()).append(",")
               .append(d.getCategoria()).append(",")
               .append(d.getValor()).append(",")
               .append(d.getData()).append("\n");
        }

        Files.writeString(ficheiro, csv.toString());

        return ficheiro;
    }
}
