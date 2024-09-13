package employeeVariables;

import model.AuthRequest;

import java.io.FileInputStream;
import java.util.Objects;
import java.util.Properties;

public class LoginData {


    public static AuthRequest loginData() {
        return new AuthRequest(Objects.requireNonNull(getProperties()).getProperty("user"), getProperties().getProperty("password"));
    }

    public static String url() {

        return Objects.requireNonNull(getProperties()).getProperty("url");
    }


    public static Properties getProperties() {

        try {
            String appConfigPath = "src/test/resources/env.properties";
            Properties properties = new Properties();
            properties.load(new FileInputStream(appConfigPath));

            return properties;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }
}
