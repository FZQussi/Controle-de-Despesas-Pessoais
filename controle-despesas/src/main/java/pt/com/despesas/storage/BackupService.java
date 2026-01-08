package pt.com.despesas.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BackupService {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    private final Path diretorioBackup;

    public BackupService(Path diretorioBackup) {
        this.diretorioBackup = diretorioBackup;
    }

    public void criarBackup(Path ficheiroOriginal) throws IOException {

        if (!Files.exists(ficheiroOriginal)) {
            return; // nada para backup
        }

        Files.createDirectories(diretorioBackup);

        String nomeBackup =
                ficheiroOriginal.getFileName().toString()
                        .replace(".json", "")
                        + "_" + LocalDateTime.now().format(FORMATTER)
                        + ".json";

        Path destino = diretorioBackup.resolve(nomeBackup);

        Files.copy(ficheiroOriginal, destino);
    }
}
