package com.example.store.assure;

import com.example.store.dto.CartAdditionDTO;
import com.example.store.dto.UserLogRegDTO;
import com.example.store.entity.Goods;
import com.example.store.entity.User;
import com.example.store.repository.OrderRepository;
import com.example.store.service.GoodsService;
import com.example.store.service.UserService;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.equalTo;
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
public class OrderTests {

    @LocalServerPort
    int port;

    @Autowired
    UserService userService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderRepository orderRepository;

    @BeforeEach
    public void init(){
        userService.deleteAllUsers();
        userService.addUser(new User("existing1@mail.ru", "123"));
        userService.addUser(new User("existing2@mail.ru", "123"));

        goodsService.deleteAllGoods();
        goodsService.addGoods(new Goods("pen", 10L,25L));
        goodsService.addGoods(new Goods("charger", 32L,240L));
        goodsService.addGoods(new Goods("cup", 40L,50L));

        orderRepository.deleteAll();
    }

    protected SessionFilter sessionFilterCustomerOne = new SessionFilter();
    protected SessionFilter sessionFilterCustomerTwo = new SessionFilter();

    @Test
    protected void createOrderOk() {
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
                .when().post("/api/cart/order")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        given().port(port).log().all()
                .filter(sessionFilterCustomerOne)
                .when().get("/api/order")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .and().body("[0].status", equalTo(true),
                        "[0].total", equalTo(480));
    }

    @Test
    protected void createOrderBadRequest() {
        UserLogRegDTO userLogRegDTO = new UserLogRegDTO("existing1@mail.ru", "123");
        CartAdditionDTO cartAdditionDTO = new CartAdditionDTO(goodsService.goodsByTitle("charger").getId(),2L);
        CartAdditionDTO cartAdditionDTO2 = new CartAdditionDTO(goodsService.goodsByTitle("charger").getId(),32L);

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
                .filter(sessionFilterCustomerTwo)
                .contentType(ContentType.JSON).body(userLogRegDTO)
                .when().post("/api/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        given().port(port).log().all()
                .filter(sessionFilterCustomerTwo)
                .contentType(ContentType.JSON).body(cartAdditionDTO2)
                .when().post("/api/cart/add")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        given().port(port).log().all()
                .filter(sessionFilterCustomerTwo)
                .when().post("/api/cart/order")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        given().port(port).log().all()
                .filter(sessionFilterCustomerOne)
                .when().post("/api/cart/order")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    protected void deleteOrderOk() {
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
                .when().post("/api/cart/order")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        Long id = orderRepository.findAllByOrderByIdDesc().get(0).getId();

        given().port(port).log().all()
                .filter(sessionFilterCustomerOne)
                .when().delete("/api/order/delete?id=" + id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        given().port(port).log().all()
                .filter(sessionFilterCustomerOne)
                .when().get("/api/order")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .and().body("[0].status", equalTo(false));

        given().port(port).log().all()
                .filter(sessionFilterCustomerOne)
                .when().get("/api/goods")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .and().body("[1].available", equalTo(32));
    }
}