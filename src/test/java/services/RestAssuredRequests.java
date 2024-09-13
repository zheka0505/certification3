package services;

import model.*;
import io.restassured.http.ContentType;

import java.io.IOException;

import static services.DBQuery.createNewCompanyDB;
import static employeeVariables.LoginData.loginData;
import static employeeVariables.VariablesForEmployeeTests.*;
import static io.restassured.RestAssured.given;


public class RestAssuredRequests {

    public static AuthResponse auth(){

        return given()
                .basePath("/auth/login")
                .body(loginData())
                .contentType(ContentType.JSON)
                .when()
                .post()
                .as(AuthResponse.class);
    }


    public static int createNewCompanyWithEmployees() throws IOException {
        AuthResponse info = auth();

        int newCompanyId = createNewCompanyDB();

        CreateEmployeeRequest russianEmployeeName = createEmployeeRequest(new CreateEmployeeRequest(), fullFieldsRussian);
        russianEmployeeName.setCompanyId(newCompanyId);

        CreateEmployeeRequest latinEmployeeName = createEmployeeRequest(new CreateEmployeeRequest(), latin);
        latinEmployeeName.setCompanyId(newCompanyId);


        given()
                .basePath("employee")
                .body(russianEmployeeName)
                .header("x-client-token", info.userToken())
                .contentType(ContentType.JSON)
                .when()
                .post();

        given()
                .basePath("employee")
                .body(latinEmployeeName)
                .header("x-client-token", info.userToken())
                .contentType(ContentType.JSON)
                .when()
                .post();

        return newCompanyId;

    }
}
