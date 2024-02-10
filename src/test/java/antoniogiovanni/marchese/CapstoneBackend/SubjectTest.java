package antoniogiovanni.marchese.CapstoneBackend;

import antoniogiovanni.marchese.CapstoneBackend.model.enums.Role;
import antoniogiovanni.marchese.CapstoneBackend.payloads.SubjectDTO;
import antoniogiovanni.marchese.CapstoneBackend.payloads.UserLoginDTO;
import antoniogiovanni.marchese.CapstoneBackend.payloads.UserRegisterDTO;
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

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubjectTest {

    @Autowired
    private ObjectMapper objectMapper;
    private static String emailTeacher;
    private static String password = "cvoYs99iS.N987@";
    static Faker faker = new Faker();

    private static String authTokenTeacher;
    private static Long subjectId;

    @BeforeAll
    public static void setUpEmail() {
        emailTeacher = faker.name().username()+"@"+faker.internet().domainName();
    }
    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost:3001";
    }

    @Test
    @Order(1)
    void registerTeacher() throws JsonProcessingException {


        String requestBody = objectMapper.writeValueAsString(
                new UserRegisterDTO(faker.name().firstName(),
                        faker.name().lastName(),emailTeacher,
                        password,
                        "ERGITH76L23I763W",
                        Role.TEACHER,
                        faker.address().streetAddress(),
                        faker.address().buildingNumber(),
                        faker.address().city(),
                        "VV",
                        "76539",
                        "26789087654"
                ));

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/auth/register");
        response.then().assertThat().statusCode(201);
        JsonNode jsonNode = objectMapper.readTree(response.body().asString());
    }

    @Test
    @Order(2)
    void loginTeacher() throws JsonProcessingException {
        String requestBody = objectMapper.writeValueAsString(
                new UserLoginDTO(emailTeacher,
                        password));

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/auth/login");
        response.then().assertThat().statusCode(200);
        JsonNode jsonNode = objectMapper.readTree(response.body().asString());
        authTokenTeacher = jsonNode.get("token").toString();
        authTokenTeacher = authTokenTeacher.substring(1, authTokenTeacher.length() - 1);
    }

    @Test
    @Order(3)
    void addSubject() throws JsonProcessingException {
        String requestBody = objectMapper.writeValueAsString(
                new SubjectDTO("Subject "+Math.random()
                ));

        Response response = given()
                .header("Authorization", "Bearer " + authTokenTeacher)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/subjects");
        response.then().assertThat().statusCode(201);
        JsonNode jsonNode = objectMapper.readTree(response.body().asString());
        subjectId = Long.parseLong(jsonNode.get("id").toString());
    }
    @Test
    @Order(4)
    void getSubjectsByTeacher() throws JsonProcessingException {
        Response response = given()
                .header("Authorization", "Bearer " + authTokenTeacher)
                .contentType("application/json")
                .when()
                .get("/subjects/byTeacher");
        response.then().assertThat().statusCode(200);
        //JsonNode jsonNode = objectMapper.readTree(response.body().asString());
        System.out.println(response.body().asString());
    }

}
