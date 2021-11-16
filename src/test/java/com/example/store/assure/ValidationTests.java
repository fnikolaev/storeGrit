package com.example.store.assure;

import com.example.store.dto.CartAdditionDTO;
import com.example.store.dto.UserLogRegDTO;
import com.example.store.entity.User;
import com.example.store.service.GoodsService;
import com.example.store.service.UserService;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext
public class ValidationTests {

    @LocalServerPort
    int port;

    @Autowired
    UserService userService;

    @Autowired
    GoodsService goodsService;

    @BeforeEach
    public void init(){
        userService.deleteAllUsers();
        userService.addUser(new User("existing1@mail.ru", "123"));
        userService.addUser(new User("existing2@mail.ru", "123"));
    }

    protected SessionFilter sessionFilterCustomerOne = new SessionFilter();

    @Test
    public void emailValidationBlankEmail() {
        UserLogRegDTO userLogRegDTO = new UserLogRegDTO("", "123");

        given().port(port).log().all()
                .contentType(ContentType.JSON).body(userLogRegDTO)
                .when().post("/api/registration")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .and()
                .body("message", equalTo("Check your request body"),
                        "errors[0]", equalTo("email must not be blank"));
    }

    @Test
    public void emailValidationBlankPassword() {
        UserLogRegDTO userLogRegDTO = new UserLogRegDTO("my@bk.ru", "");

        given().port(port).log().all()
                .contentType(ContentType.JSON).body(userLogRegDTO)
                .when().post("/api/registration")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .and()
                .body("message", equalTo("Check your request body"),
                        "errors[0]", equalTo("password must not be blank"));
    }

    @Test
    public void emailValidationEmail() {
        UserLogRegDTO userLogRegDTO = new UserLogRegDTO("@mail.ru", "123");

        given().port(port).log().all()
                .contentType(ContentType.JSON).body(userLogRegDTO)
                .when().post("/api/registration")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .and()
                .body("message", equalTo("Check your request body"),
                        "errors[0]", equalTo("email must be a well-formed email address"));
    }

    @Test
    protected void lowQuantity() {
        UserLogRegDTO userLogRegDTO = new UserLogRegDTO("existing1@mail.ru", "123");
        CartAdditionDTO cartAdditionDTO = new CartAdditionDTO(goodsService.goodsByTitle("charger").getId(),0L);

        given().port(port).log().all()
                .filter(sessionFilterCustomerOne)
                .contentType(ContentType.JSON).body(userLogRegDTO)
                .when().post("/api/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        given().port(port).log().all()
                .filter(sessionFilterCustomerOne)
                .contentType(ContentType.JSON).body(cartAdditionDTO)
                .when().post("/api/cart/add")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .and()
                .body("message", equalTo("Check your request body"),
                        "errors[0]", equalTo("quantity must be greater than or equal to 1"));
    }
}
