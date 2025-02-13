import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class APITests {

    private final int unxestingPetId = 232323493;

    @BeforeEach
    public void setup() {
      RestAssured.baseURI = "https://petstore.swagger.io/v2/";
    }

    @Test
    public void petNotFoundTestWithAssert() {
        RestAssured.baseURI += "pet/" + unxestingPetId;
        RestAssured.requestSpecification = given();
        Response response = RestAssured.requestSpecification.get();
        System.out.println("Response: " + response.asPrettyString());
        assertEquals(404, response.statusCode(), "Не тот status code");
        assertEquals("HTTP/1.1 404 Not Found", response.statusLine(), "Не корректная status line");
        assertEquals("Pet not found", response.jsonPath().get("message"), "Не то сообщение об ошибке");
    }

    @Test
    public void petNotFoundTest() {
        RestAssured.baseURI += "pet/" + unxestingPetId;
        RestAssured.requestSpecification = given();
        Response response = RestAssured.requestSpecification.get();
        System.out.println("Response: " + response.asPrettyString());
        ValidatableResponse validatableResponse = response.then();
        validatableResponse.statusCode(404);
        validatableResponse.statusLine("HTTP/1.1 404 Not Found");
        validatableResponse.body("message", equalTo("Pet not found"));
    }

    @Test
    public void petNotFoundTest_BDD() {
        given().when()
                .get(baseURI + "pet/{id}", unxestingPetId)
                .then()
                .statusCode(404)
                .statusLine("HTTP/1.1 404 Not Found")
                .body("message", equalTo("Pet not found"))
                .body("type", equalTo("error"));
    }

    @Test
    public void newPetTest() {
        Integer id = 11;
        String name = "dogg";
        String status = "sold";

        Map<String, String> request = new HashMap<>();
        request.put("id", id.toString());
        request.put("name", name);
        request.put("status", status);

       given().contentType("application/json")
                .body(request)
                .when()
                .post(baseURI + "pet/")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("name", equalTo(name))
                .body("status", equalTo(status));
    }
}
