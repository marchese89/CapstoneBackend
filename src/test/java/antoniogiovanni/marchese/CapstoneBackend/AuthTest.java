package antoniogiovanni.marchese.CapstoneBackend;

import antoniogiovanni.marchese.CapstoneBackend.model.enums.Role;
import antoniogiovanni.marchese.CapstoneBackend.payloads.UserDTO;
import antoniogiovanni.marchese.CapstoneBackend.payloads.UserLoginDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
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
                .post("http://localhost:3001/auth/register");
        response.then().assertThat().statusCode(201);
    }

    @Test
    @Order(2)
    void login() throws JsonProcessingException {
        String requestBody = objectMapper.writeValueAsString(
                new UserLoginDTO(email,
                        "cvoYs99iS.N987@"));

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("http://localhost:3001/auth/login");
        response.then().assertThat().statusCode(200);
        JsonNode jsonNode = objectMapper.readTree(response.body().asString());
        authToken = jsonNode.get("token").toString();
    }
    @Test
    @Order(3)
    void tokenOk() throws JsonProcessingException {
        assertNotNull(authToken);
    }
}
