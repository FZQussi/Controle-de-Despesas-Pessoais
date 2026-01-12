package pt.com.despesas.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final Logger log =
            LoggerFactory.getLogger(Config.class);

    private static final Properties PROPS = new Properties();

    static {
        log.debug("A iniciar carregamento de config.properties");

        try (InputStream input = Config.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input != null) {
                PROPS.load(input);
                log.info("config.properties carregado com sucesso");
            } else {
                log.warn("config.properties não encontrado no classpath");
            }

        } catch (IOException e) {
            log.error("Erro ao carregar config.properties", e);
        }
    }

    public static String get(String chave, String valorPadrao) {
        String valor = PROPS.getProperty(chave, valorPadrao);

        log.debug("Config lida: {} = {}", chave, valor);

        return valor;
    }
}

