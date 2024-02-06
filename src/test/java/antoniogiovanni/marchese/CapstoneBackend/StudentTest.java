package antoniogiovanni.marchese.CapstoneBackend;

import antoniogiovanni.marchese.CapstoneBackend.model.enums.Role;
import antoniogiovanni.marchese.CapstoneBackend.payloads.AddressModifyDTO;
import antoniogiovanni.marchese.CapstoneBackend.payloads.StudentModifyDTO;
import antoniogiovanni.marchese.CapstoneBackend.payloads.StudentRegisterDTO;
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

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StudentTest {

    @Autowired
    private ObjectMapper objectMapper;
    static Faker faker = new Faker();

    private  static String email;

    private static String authToken;

    private static Long registeredStudent;

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
                new StudentRegisterDTO(faker.name().firstName(),
                        faker.name().lastName(),email,
                        "cvoYs99iS.N987@",
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
                .post("/auth/registerStudent");
        response.then().assertThat().statusCode(201);
        JsonNode jsonNode = objectMapper.readTree(response.body().asString());

        registeredStudent = Long.parseLong(jsonNode.get("id").toString());
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
                .post("/auth/login");
        response.then().assertThat().statusCode(200);
        JsonNode jsonNode = objectMapper.readTree(response.body().asString());
        authToken = jsonNode.get("token").toString();
        authToken = authToken.substring(1, authToken.length() - 1);
    }

    @Test
    @Order(3)
    void modifyStudent() throws JsonProcessingException {

        String requestBody = objectMapper.writeValueAsString(
                new StudentModifyDTO("Modified Name",
                        "Modified Surname",email,
                        "HHHHHHH76L23I763"
                ));

        Response response = given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put("/users/"+registeredStudent);
        response.then().assertThat().statusCode(200);

    }

    @Test
    @Order(4)
    void modifyStudentNo() throws JsonProcessingException {
        String requestBody = objectMapper.writeValueAsString(
                new StudentModifyDTO(faker.name().firstName(),
                        faker.name().lastName(),email,
                        "ERGITH76L23I763"
                ));

        Response response = given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put("/users/"+registeredStudent);
        response.then().assertThat().statusCode(400);

    }

    @Test
    @Order(5)
    void modifyAddress() throws JsonProcessingException {
        //we need to get address id from student
        Response response0 = given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .when()
                .get("/users/"+registeredStudent);
        response0.then().assertThat().statusCode(200);
        JsonNode student = objectMapper.readTree(response0.body().asString());
        JsonNode address = student.get("address");
        String id = address.get("id").toString();

        String requestBody = objectMapper.writeValueAsString(
                new AddressModifyDTO("New Street",
                        "11",
                        "New City",
                        "TT",
                        "55555"
                ));

        Response response = given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put("/addresses/"+id);
        response.then().assertThat().statusCode(200);

    }

    @Test
    @Order(6)
    void modifyAddressNo() throws JsonProcessingException {
        String requestBody = objectMapper.writeValueAsString(
                new AddressModifyDTO("New Street",
                        "11",
                        "New City",
                        "T",
                        "55555"
                ));

        Response response = given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put("/users/"+registeredStudent);
        response.then().assertThat().statusCode(400);

    }
}
