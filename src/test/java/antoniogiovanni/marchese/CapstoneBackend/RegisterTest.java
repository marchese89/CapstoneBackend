package antoniogiovanni.marchese.CapstoneBackend;

import antoniogiovanni.marchese.CapstoneBackend.model.enums.Role;
import antoniogiovanni.marchese.CapstoneBackend.payloads.StudentRegisterDTO;
import antoniogiovanni.marchese.CapstoneBackend.payloads.UserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegisterTest {

    @Autowired
    private ObjectMapper objectMapper;
    static Faker faker = new Faker();

    private  static String email;

    @BeforeAll
    public static void setUpEmail() {
        email = faker.name().username()+"@"+faker.internet().domainName();
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
                .post("http://localhost:3001/auth/registerStudent");
        response.then().assertThat().statusCode(201);
    }
}
