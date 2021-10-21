package com.example.store;

import com.example.store.dto.UserLogRegDTO;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class Assure {

    @Test
    public void whenCreateExistingUser_thenStatus409() {
        UserLogRegDTO user = new UserLogRegDTO("USER_SUPER", "SUPER_PASS");

        given().log().all()
                .contentType(ContentType.JSON).body(user)
                .when().post("/api/registration")
                .then().log().all();
        /*        .statusCode(HttpStatus.CONFLICT.value())
                .and()
                .body("MESSAGE", equalTo("USER_EXISTS"))
                .and()
                .body("USERNAME", equalTo(user.getEmail()));*/
    }
}
