package com.example.store.assure;

import com.example.store.dto.CartAdditionDTO;
import com.example.store.dto.UserLogRegDTO;
import com.example.store.entity.Goods;
import com.example.store.entity.User;
import com.example.store.service.GoodsService;
import com.example.store.service.UserService;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext
public class CartTests {

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

        goodsService.deleteAllGoods();
        goodsService.addGoods(new Goods("pen", 10L,25L));
        goodsService.addGoods(new Goods("charger", 32L,240L));
        goodsService.addGoods(new Goods("cup", 40L,50L));
    }

    protected SessionFilter sessionFilterCustomerOne = new SessionFilter();
    protected SessionFilter sessionFilterCustomerTwo = new SessionFilter();

    @Test
    protected void okAddToCart() {
        UserLogRegDTO userLogRegDTO = new UserLogRegDTO("existing1@mail.ru", "123");
        CartAdditionDTO cartAdditionDTO = new CartAdditionDTO(goodsService.goodsByTitle("charger").getId(),2L);

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
                .statusCode(HttpStatus.OK.value());

        given().port(port).log().all()
                .filter(sessionFilterCustomerOne)
                .when().get("/api/cart")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .and().body("Sum", equalTo(480));
    }

    @Test
    protected void badReqAddToCart() {
        UserLogRegDTO userLogRegDTO = new UserLogRegDTO("existing1@mail.ru", "123");
        CartAdditionDTO cartAdditionDTO = new CartAdditionDTO(goodsService.goodsByTitle("charger").getId(),100L);

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
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    protected void deleteFromCart() {
        UserLogRegDTO userLogRegDTO = new UserLogRegDTO("existing1@mail.ru", "123");
        CartAdditionDTO cartAdditionDTO = new CartAdditionDTO(goodsService.goodsByTitle("charger").getId(),2L);

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
                .statusCode(HttpStatus.OK.value());

        given().port(port).log().all()
                .filter(sessionFilterCustomerOne)
                .when().delete("api/cart/delete?title=charger")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        given().port(port).log().all()
                .filter(sessionFilterCustomerOne)
                .when().get("/api/cart")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .and().body("$", hasKey("Cart is empty"));
    }

    @Test
    protected void updateCart() {
        UserLogRegDTO userLogRegDTO = new UserLogRegDTO("existing1@mail.ru", "123");
        CartAdditionDTO cartAdditionDTO = new CartAdditionDTO(goodsService.goodsByTitle("charger").getId(),2L);

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
                .statusCode(HttpStatus.OK.value());

        given().port(port).log().all()
                .filter(sessionFilterCustomerOne)
                .contentType(ContentType.JSON).body(cartAdditionDTO)
                .when().patch("api/cart/")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        given().port(port).log().all()
                .filter(sessionFilterCustomerOne)
                .when().get("/api/cart")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .and().body("$", hasKey("Cart is empty"));
    }

}