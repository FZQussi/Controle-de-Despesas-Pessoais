package pt.com.despesas.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream input = Config.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input != null) {
                PROPS.load(input);
            }

        } catch (IOException e) {
            System.out.println("Não foi possível carregar config.properties: " + e.getMessage());
        }
    }

    public static String get(String chave, String valorPadrao) {
        return PROPS.getProperty(chave, valorPadrao);
    }
}
