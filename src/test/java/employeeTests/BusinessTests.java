package employeeTests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static employeeRestAssuredMethods.APIRequests.*;
import static employeeVariables.LoginData.url;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class BusinessTests {

    @BeforeAll
    public static void setUp() throws IOException {
        RestAssured.baseURI = url();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Получение списка сотрудников")
    public void getListOfEmployees() throws IOException {

        given()
                .queryParam("company", createNewCompanyWithEmployees())
                .basePath("employee")
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .body("lastName", hasItems("Иванов", "Ivanov"));

    }

       @Test
    @Tag("Позитивный")
    @DisplayName("Получение пустого списка сотрудников")
    public void getEmptyListOfEmployees() throws IOException {

        given()
                .queryParam("company", createNewCompany())
                .basePath("employee")
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .header("Content-Length", (String) null);

    }



}
