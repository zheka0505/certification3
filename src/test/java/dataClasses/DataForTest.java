package dataClasses;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;



public class DataForTest {

    public static final CreateCompanyRequest COMPANY= new CreateCompanyRequest("ООО Цветочек","Новая компания");

    public static final AuthRequest LOGINDATA = new AuthRequest("zheka", "zheka");

    public static CreateEmployeeRequest employeeData() throws IOException {

        Path filePath = Path.of("employee.json");
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(filePath.toFile(), new TypeReference<>() {
        });
    }

}


