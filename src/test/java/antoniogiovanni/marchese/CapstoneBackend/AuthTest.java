package antoniogiovanni.marchese.CapstoneBackend;

import antoniogiovanni.marchese.CapstoneBackend.model.enums.Role;
import antoniogiovanni.marchese.CapstoneBackend.payloads.UserDTO;
import antoniogiovanni.marchese.CapstoneBackend.payloads.UserLoginDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthTest {
    @Autowired
    private ObjectMapper objectMapper;
    static Faker faker = new Faker();

    private static String authToken;

    private  static String email;

    @BeforeAll
    public static void setUpEmail() {
        email = faker.name().username()+"@"+faker.internet().domainName();
    }

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost:3001";
    }
    @Test
    @Order(1)
    void register() throws JsonProcessingException {
        

        String requestBody = objectMapper.writeValueAsString(
                new UserDTO(email,
                        "cvoYs99iS.N987@",
                        Role.ADMIN));

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/auth/register");
        response.then().assertThat().statusCode(201);
    }
    @Test
    @Order(2)
    void registerNo() throws JsonProcessingException {

        String requestBody = objectMapper.writeValueAsString(
                new UserDTO(email,
                        "cvoYs99iS0N987",
                        Role.ADMIN));

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/auth/register");
        response.then().assertThat().statusCode(400);
    }

    @Test
    @Order(3)
    void login() throws JsonProcessingException {
        String requestBody = objectMapper.writeValueAsString(
                new UserLoginDTO(email,
                        "cvoYs99iS.N987@"));

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/auth/login");
        response.then().assertThat().statusCode(200);
        JsonNode jsonNode = objectMapper.readTree(response.body().asString());
        authToken = jsonNode.get("token").toString();
    }
    @Test
    @Order(4)
    void tokenOk() throws JsonProcessingException {
        assertNotNull(authToken);
    }
    @Test
    @Order(5)
    void loginNo() throws JsonProcessingException {
        String requestBody = objectMapper.writeValueAsString(
                new UserLoginDTO(email,
                        "cvoYs99iS.N987@e"));

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/auth/login");
        response.then().assertThat().statusCode(401);

    }

}
