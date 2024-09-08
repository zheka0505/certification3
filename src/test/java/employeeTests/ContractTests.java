package employeeTests;

import employeeDataClasses.AuthResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static employeeRestAssuredMethods.APIRequests.*;
import static employeeVariables.LoginData.url;
import static io.restassured.RestAssured.given;
import static employeeVariables.VariablesForEmployeeTests.*;
import static employeeVariables.VariablesOfResponses.*;


public class ContractTests {

    AuthResponse info = auth();

    public ContractTests() throws IOException {
    }

    @BeforeAll
    public static void setUp() throws IOException {
        RestAssured.baseURI = url();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Получение списка сотрудников. Проверяем статус-код 200")
    public void getListOfEmployees() throws IOException {

        given()
                .queryParam("company", createNewCompany())
                .basePath("employee")
                .when()
                .get()
                .then()
                .assertThat()
                .statusCode(STATUS_CODE_OK)
                .header("Content-Type", JSON);
    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Создание сотрудника. Проверяем статус-код 201 и Content-Type")
    public void createEmployee() {

        given()
                .basePath("employee")
                .body(NEW_EMPLOYEE_RUSSIAN)
                .header(TOKEN_TYPE, info.userToken())
                .contentType(ContentType.JSON)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(STATUS_CODE_CREATED)
                .header("Content-Type", JSON);
    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Получение сотрудника по id. Проверяем статус-код 200")
    public void getEmployeeById() throws IOException {

        given()
                .basePath("employee")
                .when()
                .get("{id}", createNewEmployee())
                .then()
                .assertThat()
                .statusCode(STATUS_CODE_OK)
                .header("Content-Type", JSON);
    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Изменение информации о сотруднике. Проверяем статус-код 200 и Content-Type")
    public void changeEmployeeData() throws IOException {

        given()
                .basePath("employee")
                .body(CHANGED_DATA_EMPLOYEE_RUSSIAN)
                .header(TOKEN_TYPE, info.userToken())
                .contentType(ContentType.JSON)
                .when()
                .patch("{id}", createNewEmployee())
                .then()
                .assertThat()
                .statusCode(STATUS_CODE_OK)
                .header("Content-Type", JSON);
    }

    @Test
    @Tag("Негативный")
    @DisplayName("Получение списка сотрудников для несуществующей компании")
    public void getListOfEmployeesOfNotExistedCompany() {

        given()
                .basePath("employee")
                .when()
                .get("{company}", NEGATIVE_ID)
                .then()
                .assertThat()
                .statusCode(STATUS_CODE_OK);
    }

    @Test
    @Tag("Негативный")
    @DisplayName("Получение списка сотрудников id компании строка")
    public void getListOfEmployeesIdCompanyString() {

        given()
                .basePath("employee")
                .when()
                .get("{company}", "NEW")
                .then()
                .assertThat()
                .statusCode(STATUS_CODE_INTERNAL_SERVER_ERROR);
    }

    @Test
    @Tag("Негативный")
    @DisplayName("Создание сотрудника в несуществующей компании")
    public void createEmployeeNotExistedCompany() {

        given()
                .basePath("employee")
                .body(NEGATIVE_EMPLOYEE_COMPANY_DOES_NT_EXIST)
                .header(TOKEN_TYPE, info.userToken())
                .contentType(ContentType.JSON)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(STATUS_CODE_INTERNAL_SERVER_ERROR);
    }

    @Test
    @Tag("Негативный")
    @DisplayName("Получение сотрудника по несуществующему id, статус 404")
    public void getEmployeeByNotExistedId() {

        given()
                .basePath("employee")
                .when()
                .get("{id}", NEGATIVE_ID)
                .then()
                .assertThat()
                .statusCode(STATUS_CODE_NOT_FOUND);
    }

    @Test
    @Tag("Негативный")
    @DisplayName("Изменение информации о сотруднике с несуществующим id, статус 404")
    public void changeEmployeeDataWithNotExistedId() {

        given()
                .basePath("employee")
                .body(CHANGED_DATA_EMPLOYEE_RUSSIAN)
                .header(TOKEN_TYPE, info.userToken())
                .contentType(ContentType.JSON)
                .when()
                .patch("{id}", NEGATIVE_ID)
                .then()
                .assertThat()
                .statusCode(STATUS_CODE_NOT_FOUND);
    }

    @Test
    @Tag("Негативный")
    @DisplayName("Изменение всей информации о сотруднике")
    public void changeAllEmployeeData() throws IOException {

        given()
                .basePath("employee")
                .body(NEW_EMPLOYEE_LATIN)
                .header(TOKEN_TYPE, info.userToken())
                .contentType(ContentType.JSON)
                .when()
                .patch("{id}", createNewEmployee())
                .then()
                .assertThat()
                .statusCode(STATUS_CODE_OK)
                .header("Content-Type", JSON);
    }

}



