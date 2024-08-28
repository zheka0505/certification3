import dataClasses.*;
import io.restassured.http.ContentType;

import java.io.IOException;

import static dataClasses.DataForTest.*;
import static io.restassured.RestAssured.given;

public class APIRequests {

    public static AuthResponse auth() {


        return given()
                .basePath("/auth/login")
                .body(LOGINDATA)
                .contentType(ContentType.JSON)
                .when()
                .post()
                .as(AuthResponse.class);
    }

    public CreateEmployeeResponse createNewEmployee() throws IOException {
        AuthResponse info = auth();

        return given()
                .basePath("employee")
                .body(newEmployee())
                .header("x-client-token", info.userToken())
                .contentType(ContentType.JSON)
                .when()
                .post()
                .as(CreateEmployeeResponse.class);

    }

    public CreateEmployeeRequest newEmployee() throws IOException {

        int newCompanyId = createNewCompany().id();
        CreateEmployeeRequest employee = employeeData();
        employee.setCompanyId(newCompanyId);

        return employee;
    }


    public CreateCompanyResponse createNewCompany() {
        AuthResponse info = auth();

        return given()
                .basePath("company")
                .body(COMPANY)
                .header("x-client-token", info.userToken())
                .contentType(ContentType.JSON)
                .when()
                .post()
                .as(CreateCompanyResponse.class);

    }

}
