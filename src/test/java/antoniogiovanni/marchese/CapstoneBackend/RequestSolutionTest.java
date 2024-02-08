package antoniogiovanni.marchese.CapstoneBackend;

import antoniogiovanni.marchese.CapstoneBackend.model.enums.Role;
import antoniogiovanni.marchese.CapstoneBackend.payloads.SubjectDTO;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application.properties")
@Component
public class RequestSolutionTest {
    @Autowired
    private ObjectMapper objectMapper;
    static Faker faker = new Faker();

    private static String email;

    private static String emailTeacher;

    private static String emailTeacher2;
    private static String authTokenTeacher;

    private static String authTokenTeacher2;

    private static String authTokenStudent;

    private static Long subjectId;

    private static Long registeredTeacher;

    private static Long requestId;

    private static Long solutionId;

    private static String password = "cvoYs99iS.N987@";

    @Value("${email.student}")
    public void setEmailStudent(String emailStudent){
        RequestSolutionTest.email = emailStudent;
    }

    @Value("${email.teacher}")
    public void setEmailTeacher(String email){
        RequestSolutionTest.emailTeacher = email;
    }

    @Value("${email.teacher2}")
    public void setEmailTeacher2(String email){
        RequestSolutionTest.emailTeacher2 = email;
    }

    @BeforeAll
    public static void setUpEmail() {
//        email = faker.name().username()+"@"+faker.internet().domainName();
//        emailTeacher = faker.name().username()+"@"+faker.internet().domainName();
//        emailTeacher2 = faker.name().username()+"@"+faker.internet().domainName();
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
        registeredTeacher = Long.parseLong(jsonNode.get("id").toString());
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
    //create new teacher and add same subject
    @Test
    @Order(4)
    void registerTeacher2() throws JsonProcessingException {

        String requestBody = objectMapper.writeValueAsString(
                new UserRegisterDTO(faker.name().firstName(),
                        faker.name().lastName(),emailTeacher2,
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
//        JsonNode jsonNode = objectMapper.readTree(response.body().asString());
//        registeredTeacher = Long.parseLong(jsonNode.get("id").toString());
    }

    @Test
    @Order(5)
    void loginTeacher2() throws JsonProcessingException {
        String requestBody = objectMapper.writeValueAsString(
                new UserLoginDTO(emailTeacher2,
                        password));

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/auth/login");
        response.then().assertThat().statusCode(200);
        JsonNode jsonNode = objectMapper.readTree(response.body().asString());
        authTokenTeacher2 = jsonNode.get("token").toString();
        authTokenTeacher2 = authTokenTeacher2.substring(1, authTokenTeacher2.length() - 1);
    }

    @Test
    @Order(6)
    void connectSubject() throws JsonProcessingException {
        String requestBody = objectMapper.writeValueAsString(
                new SubjectDTO("Subject "+Math.random()
                ));

        Response response = given()
                .header("Authorization", "Bearer " + authTokenTeacher2)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/subjects/add/"+subjectId);
        response.then().assertThat().statusCode(201);

    }

    @Test
    @Order(7)
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
                        "76539",
                        null
                ));

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/auth/register");
        response.then().assertThat().statusCode(201);

    }

    @Test
    @Order(8)
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
        authTokenStudent = jsonNode.get("token").toString();
        authTokenStudent = authTokenStudent.substring(1, authTokenStudent.length() - 1);
    }

    @Test
    @Order(9)
    void createRequest() throws JsonProcessingException {

        File file = new File("./files_to_upload/a.txt");

        Response response = given()
                .header("Authorization", "Bearer " + authTokenStudent)
                .multiPart("file", file)
                .when()
                .post("/requests/"+subjectId+"/"+faker.name().fullName());
        response.then().assertThat().statusCode(201);
        JsonNode jsonNode = objectMapper.readTree(response.body().asString());
        requestId = Long.parseLong(jsonNode.get("id").toString());
    }

    @Test
    @Order(10)
    void getRequestByTeacher() throws JsonProcessingException {

        Response response = given()
                .header("Authorization", "Bearer " + authTokenTeacher)
                .when()
                .get("/requests/byTeacher");
        response.then().assertThat().statusCode(200);
        JsonNode jsonNode = objectMapper.readTree(response.body().asString());
        Long requestID = Long.parseLong(jsonNode.get(0).get("id").toString());
        assertEquals(requestId,requestID);
    }
    @Test
    @Order(11)
    void addSolutionToRequest() throws JsonProcessingException {
        File file = new File("./files_to_upload/a.txt");

        Response response = given()
                .header("Authorization", "Bearer " + authTokenTeacher)
                .multiPart("file", file)
                .when()
                .post("/solutions/"+requestId+"/"+1000);
        response.then().assertThat().statusCode(201);
        JsonNode jsonNode = objectMapper.readTree(response.body().asString());
        solutionId = Long.parseLong(jsonNode.get("id").toString());
    }
    @Test
    @Order(12)
    void addSolutionToRequest2() throws JsonProcessingException {
        File file = new File("./files_to_upload/a.txt");

        Response response = given()
                .header("Authorization", "Bearer " + authTokenTeacher2)
                .multiPart("file", file)
                .when()
                .post("/solutions/"+requestId+"/"+1000);
        response.then().assertThat().statusCode(201);
    }

    @Test
    @Order(13)
    void acceptSolution(){

        Response response = given()
                .header("Authorization", "Bearer " + authTokenStudent)
                .when()
                .put("/solutions/acceptSolution/"+solutionId);
        response.then().assertThat().statusCode(200);
    }
}
