import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.MemoryAddress;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class APITestBooks {

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "https://demoqa.com/BookStore/v1/";
    }

    @Test
    @DisplayName("Вызов всех книг в коллекции")
    public void getAllBooksFromCollection() {
        given().when()

                .get(baseURI + "Books")
                .then()
                .log().all()
                .statusCode(200)
                .body("books", everyItem(is(notNullValue())));
    }

    @Test
    @DisplayName("Создание рандомного юзера ")
    public void createRandomUser() {

        Random random = new Random();
        int fourDigitRandom = random.nextInt(9000);

        String userName = "RandomUser"+fourDigitRandom;
        String password = "Q123x!@3";

        Map<String, String> request = new HashMap<>();
        request.put("userName", userName);
        request.put("password", password);

        given().contentType("application/json")
                .body(request)
                .when()
                .post("https://demoqa.com/Account/v1/User")
                .then()
                .log().all()
                .assertThat()
                .statusCode(201)
                .body("userID", notNullValue())
                .body("username", notNullValue())
                .body("books", notNullValue());
    }
}
