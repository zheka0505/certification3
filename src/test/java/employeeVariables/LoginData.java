package employeeVariables;

import model.AuthRequest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class LoginData {


    public static AuthRequest loginData() throws IOException {
        return new AuthRequest(getProperties().getProperty("user"), getProperties().getProperty("password"));
    }

    public static String url() throws IOException {
        return getProperties().getProperty("url");
    }

    public static Properties getProperties() throws IOException {
        String appConfigPath = "src/test/resources/env.properties";

        Properties properties = new Properties();
        properties.load(new FileInputStream(appConfigPath));
        return properties;
    }
}
