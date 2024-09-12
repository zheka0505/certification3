package employeeTests;

import DBConnection.EmployeeEntity;
import model.AuthResponse;
import model.CreateEmployeeRequest;
import model.CreateEmployeeResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;

import static services.DBQuery.*;
import static services.RestAssuredRequests.*;

import static employeeVariables.LoginData.url;
import static employeeVariables.VariablesForEmployeeTests.*;
import static employeeVariables.VariablesOfResponses.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class BusinessTests {

    AuthResponse info = auth();

    public BusinessTests() throws IOException {
    }

    @BeforeAll
    public static void setUp() throws IOException {
        RestAssured.baseURI = url();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    //тест не пройдет, бага емейл не сохраняется в базу
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
                .body("lastName", hasItems(RUSSIAN_LASTNAME, LATIN_LASTNAME))
                .body("firstName", hasItems(RUSSIAN_NAME, LATIN_NAME))
                .body("middleName", hasItems(RUSSIAN_MIDDLE_NAME, null))
                .body("birthdate", hasItems(EMPLOYEE_BIRTHDAY, null))
                .body("phone", hasItem(EMPLOYEE_PHONE))
                .body("isActive", hasItem(true))
                .body("avatar_url", hasItems(EMPLOYEE_URL, null))
                .body("email", hasItems(EMPLOYEE_EMAIL, null));

    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Получение пустого списка сотрудников")
    public void getEmptyListOfEmployees() throws IOException {

        given()
                .queryParam("company", createNewCompanyDB())
                .basePath("employee")
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .header("Content-Length", (String) null);

    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Создание сотрудника, данные на русском")
    public void createEmployeeRussian() throws IOException {

        int newEmployeeId = given()
                .basePath("employee")
                .body(createEmployeeRequest(new CreateEmployeeRequest(), russian))
                .header(TOKEN_TYPE, info.userToken())
                .contentType(ContentType.JSON)
                .when()
                .post()
                .as(CreateEmployeeResponse.class).id();

        EmployeeEntity employeeFromDB = getEmployeeByIdDB(newEmployeeId);

        assertEquals(RUSSIAN_NAME, employeeFromDB.getFirstName());
        assertEquals(RUSSIAN_LASTNAME, employeeFromDB.getLastName());
        assertEquals(EMPLOYEE_PHONE, employeeFromDB.getPhone());
        assertTrue(employeeFromDB.isActive());
    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Создание сотрудника, данные латиницей")
    public void createEmployeeLatin() throws IOException {

        int newEmployeeId = given()
                .basePath("employee")
                .body(createEmployeeRequest(new CreateEmployeeRequest(), latin))
                .header(TOKEN_TYPE, info.userToken())
                .contentType(ContentType.JSON)
                .when()
                .post()
                .as(CreateEmployeeResponse.class).id();

        EmployeeEntity employeeFromDB = getEmployeeByIdDB(newEmployeeId);

        assertEquals(LATIN_NAME, employeeFromDB.getFirstName());
        assertEquals(LATIN_LASTNAME, employeeFromDB.getLastName());
    }

    //тест не пройдет, бага емейл не сохраняется в базу
    @Test
    @Tag("Позитивный")
    @DisplayName("Создание сотрудника, заполнены все поля")
    public void createEmployeeFullData() throws IOException {

        int newEmployeeId = given()
                .basePath("employee")
                .body(createEmployeeRequest(new CreateEmployeeRequest(), fullFieldsRussian))
                .header(TOKEN_TYPE, info.userToken())
                .contentType(ContentType.JSON)
                .when()
                .post()
                .as(CreateEmployeeResponse.class).id();

        EmployeeEntity employeeFromDB = getEmployeeByIdDB(newEmployeeId);

        String toStringBirthday = new SimpleDateFormat("yyyy-MM-dd").format(employeeFromDB.getBirthdate());

        assertEquals(RUSSIAN_MIDDLE_NAME, employeeFromDB.getMiddleName());
        assertEquals(EMPLOYEE_BIRTHDAY, toStringBirthday);
        assertEquals(EMPLOYEE_URL, employeeFromDB.getAvatar_url());
        assertEquals(EMPLOYEE_EMAIL, employeeFromDB.getEmail());


    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Создание сотрудника, данные спецсимволы")
    public void createEmployeeSpecialCharacters() throws IOException {

        int newEmployeeId = given()
                .basePath("employee")
                .body(createEmployeeRequest(new CreateEmployeeRequest(), specialCharacters))
                .header(TOKEN_TYPE, info.userToken())
                .contentType(ContentType.JSON)
                .when()
                .post()
                .as(CreateEmployeeResponse.class).id();

        EmployeeEntity employeeFromDB = getEmployeeByIdDB(newEmployeeId);

        assertEquals(SPECIAL_CHARACTERS, employeeFromDB.getFirstName());
        assertEquals(SPECIAL_CHARACTERS, employeeFromDB.getLastName());
        assertEquals(SPECIAL_CHARACTERS, employeeFromDB.getMiddleName());
    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Получение сотрудника с полными данными")
    public void getEmployeeFullData() throws IOException {

        given()
                .basePath("employee")
                .when()
                .get("{id}", createEmployeeDB(new EmployeeEntity(), fullFieldsRussianDB).getId())
                .then()
                .body("lastName", equalTo(RUSSIAN_LASTNAME))
                .body("firstName", equalTo(RUSSIAN_NAME))
                .body("middleName", equalTo(RUSSIAN_MIDDLE_NAME))
                .body("birthdate", equalTo(EMPLOYEE_BIRTHDAY))
                .body("phone", equalTo(EMPLOYEE_PHONE))
                .body("isActive", equalTo(true))
                .body("avatar_url", equalTo(EMPLOYEE_URL))
                .body("email", equalTo(EMPLOYEE_EMAIL));

    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Получение сотрудника с данными латиницей")
    public void getEmployeeLatinData() throws IOException {

        given()
                .basePath("employee")
                .when()
                .get("{id}", createEmployeeDB(new EmployeeEntity(), latinDB).getId())
                .then()
                .body("lastName", equalTo(LATIN_LASTNAME))
                .body("firstName", equalTo(LATIN_NAME))
                .body("phone", equalTo(EMPLOYEE_PHONE))
                .body("isActive", equalTo(true));

    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Получение сотрудника со спецсимволами")
    public void getEmployeeSpecialCharacters() throws IOException {

        given()
                .basePath("employee")
                .when()
                .get("{id}", createEmployeeDB(new EmployeeEntity(), specialCharactersDB).getId())
                .then()
                .body("lastName", equalTo(SPECIAL_CHARACTERS))
                .body("firstName", equalTo(SPECIAL_CHARACTERS))
                .body("middleName", equalTo(SPECIAL_CHARACTERS))
                .body("phone", equalTo(EMPLOYEE_PHONE))
                .body("isActive", equalTo(true));

    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Изменение информации о сотруднике")
    public void changeEmployeeData() throws IOException {

        CreateEmployeeRequest s = createEmployeeRequest(new CreateEmployeeRequest(), employeeChange);
        System.out.println(s);
        given()
                .basePath("employee")
                .body(s)
                .header(TOKEN_TYPE, info.userToken())
                .contentType(ContentType.JSON)
                .when()
                .patch("{id}", createEmployeeDB(new EmployeeEntity(), fullFieldsRussianDB).getId())
                .then()
                .body("isActive", equalTo(false))
                .body("url", equalTo(EMPLOYEE_CHANGED_URL))
                .body("email", equalTo(EMPLOYEE_CHANGED_EMAIL));




    }


}
