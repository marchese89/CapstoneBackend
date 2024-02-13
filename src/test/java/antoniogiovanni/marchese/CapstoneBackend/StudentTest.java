package antoniogiovanni.marchese.CapstoneBackend;

import antoniogiovanni.marchese.CapstoneBackend.model.enums.Role;
import antoniogiovanni.marchese.CapstoneBackend.payloads.AddressDTO;
import antoniogiovanni.marchese.CapstoneBackend.payloads.UserModifyDTO;
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

    private static String password = "cvoYs99iS.N987@";

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
                        "76539",
                        null
                ));

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/auth/register");
        response.then().assertThat().statusCode(201);
        JsonNode jsonNode = objectMapper.readTree(response.body().asString());

        registeredStudent = Long.parseLong(jsonNode.get("id").toString());
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

//    @Test
//    @Order(3)
//    void modifyStudent() throws JsonProcessingException {
//
//        String requestBody = objectMapper.writeValueAsString(
//                new UserModifyDTO("Modified Name",
//                        "Modified Surname",email,
//                        "HHHHHHH76L23I763"
//                ));
//
//        Response response = given()
//                .header("Authorization", "Bearer " + authToken)
//                .contentType("application/json")
//                .body(requestBody)
//                .when()
//                .put("/users");
//        response.then().assertThat().statusCode(200);
//
//    }

//    @Test
//    @Order(4)
//    void modifyStudentNo() throws JsonProcessingException {
//        String requestBody = objectMapper.writeValueAsString(
//                new UserModifyDTO(faker.name().firstName(),
//                        faker.name().lastName(),email,
//                        "ERGITH76L23I763"
//                ));
//
//        Response response = given()
//                .header("Authorization", "Bearer " + authToken)
//                .contentType("application/json")
//                .body(requestBody)
//                .when()
//                .put("/users");
//        response.then().assertThat().statusCode(400);
//
//    }

    @Test
    @Order(5)
    void modifyAddress() throws JsonProcessingException {

        String requestBody = objectMapper.writeValueAsString(
                new AddressDTO("New Street",
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
                .put("/addresses");
        response.then().assertThat().statusCode(200);

    }

    @Test
    @Order(6)
    void modifyAddressNo() throws JsonProcessingException {
        String requestBody = objectMapper.writeValueAsString(
                new AddressDTO("New Street",
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
                .put("/addresses");
        response.then().assertThat().statusCode(400);

    }
}
