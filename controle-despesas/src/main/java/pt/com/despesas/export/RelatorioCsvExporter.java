package pt.com.despesas.export;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.com.despesas.model.Despesa;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.YearMonth;
import java.util.List;

public class RelatorioCsvExporter {

    private static final Logger log =
            LoggerFactory.getLogger(RelatorioCsvExporter.class);

    public Path exportar(
            List<Despesa> despesas,
            YearMonth mes,
            Path diretorio
    ) throws IOException {

        log.info("A exportar relatório CSV para o mês {}", mes);

        if (despesas.isEmpty()) {
            log.warn("Tentativa de exportar CSV sem despesas ({})", mes);
            throw new IllegalArgumentException("Não há despesas para exportar");
        }

        Files.createDirectories(diretorio);
        log.debug("Diretório garantido: {}", diretorio.toAbsolutePath());

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

        log.info(
                "Relatório CSV exportado com sucesso: {} ({} despesas)",
                ficheiro.toAbsolutePath(),
                despesas.size()
        );

        return ficheiro;
    }
}
