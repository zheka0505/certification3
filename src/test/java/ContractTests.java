
import dataClasses.AuthResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static io.restassured.RestAssured.given;


public class ContractTests {

    APIRequests apiRequests;
    AuthResponse info = APIRequests.auth();

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://x-clients-be.onrender.com";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

    }

    @BeforeEach
    public void setUpL() {
        apiRequests = new APIRequests();
    }

    @Test
    public void status200OnCreateEmployee() throws IOException {

        given()
                .basePath("employee")
                .body(apiRequests.newEmployee())
                .header("x-client-token", info.userToken())
                .contentType(ContentType.JSON)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(201);
    }


}
