package antoniogiovanni.marchese.CapstoneBackend;

import antoniogiovanni.marchese.CapstoneBackend.model.enums.Role;
import antoniogiovanni.marchese.CapstoneBackend.payloads.RequestDTO;
import antoniogiovanni.marchese.CapstoneBackend.payloads.UserRegisterDTO;
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

import java.io.File;

import static io.restassured.RestAssured.given;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RequestTest {
    @Autowired
    private ObjectMapper objectMapper;
    static Faker faker = new Faker();

    private  static String email;

    private static String authToken;

    private static String password = "cvoYs99iS.N987@";

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
    void registerStudent() throws JsonProcessingException {


        String requestBody = objectMapper.writeValueAsString(
                new UserRegisterDTO(faker.name().firstName(),
                        faker.name().lastName(),email,
                        password,
                        "ERGITH76L23I763W",
                        Role.STUDENT,
                        faker.address().streetAddress(),
                        faker.address().buildingNumber(),
                        faker.address().city(),
                        "VV",
                        "76539"
                ));

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/auth/register");
        response.then().assertThat().statusCode(201);

    }

    @Test
    @Order(2)
    void login() throws JsonProcessingException {
        String requestBody = objectMapper.writeValueAsString(
                new UserLoginDTO(email,
                        password));

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/auth/login");
        response.then().assertThat().statusCode(200);
        JsonNode jsonNode = objectMapper.readTree(response.body().asString());
        authToken = jsonNode.get("token").toString();
        authToken = authToken.substring(1, authToken.length() - 1);
    }

    @Test
    @Order(3)
    void createRequest() throws JsonProcessingException {

        File file = new File("./files_to_upload/a.txt");

        Response response = given()
                .header("Authorization", "Bearer " + authToken)
                .multiPart("file", file)
                .when()
                .post("/requests/"+1+"/"+faker.name().fullName());
        response.then().assertThat().statusCode(201);
    }
}
