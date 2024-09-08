package employeeRestAssuredMethods;

import employeeDataClasses.*;
import io.restassured.http.ContentType;

import java.io.IOException;

import static employeeVariables.LoginData.loginData;
import static employeeVariables.VariablesForEmployeeTests.*;
import static io.restassured.RestAssured.given;


public class APIRequests {

    public static AuthResponse auth() throws IOException {

        return given()
                .basePath("/auth/login")
                .body(loginData())
                .contentType(ContentType.JSON)
                .when()
                .post()
                .as(AuthResponse.class);
    }

    public static int createNewCompany() throws IOException {
        AuthResponse info = auth();

        return given()
                .basePath("company")
                .body(NEW_COMPANY_DATA)
                .header("x-client-token", info.userToken())
                .contentType(ContentType.JSON)
                .when()
                .post()
                .as(CreateCompanyResponse.class).id();
    }

    public static int createNewEmployee() throws IOException {
        AuthResponse info = auth();

        return given()
                .basePath("employee")
                .body(NEW_EMPLOYEE_RUSSIAN)
                .header("x-client-token", info.userToken())
                .contentType(ContentType.JSON)
                .when()
                .post()
                .as(CreateEmployeeResponse.class).id();

    }

    public static int createNewCompanyWithEmployees () throws IOException {
        AuthResponse info = auth();

        int newCompanyId = createNewCompany();
        NEW_EMPLOYEE_RUSSIAN.setCompanyId(newCompanyId);
        NEW_EMPLOYEE_LATIN.setCompanyId(newCompanyId);

        given()
                .basePath("employee")
                .body(NEW_EMPLOYEE_RUSSIAN)
                .header("x-client-token", info.userToken())
                .contentType(ContentType.JSON)
                .when()
                .post();

        given()
                .basePath("employee")
                .body(NEW_EMPLOYEE_LATIN)
                .header("x-client-token", info.userToken())
                .contentType(ContentType.JSON)
                .when()
                .post();

        return newCompanyId;

    }
}
