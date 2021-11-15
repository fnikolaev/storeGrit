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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static io.restassured.RestAssured.given;

// Only one test should throw "1 expectation failed."

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
public class JointReqTest {

    @LocalServerPort
    int port;

    @Autowired
    UserService userService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderRepository orderRepository;

    @BeforeAll
    public void init(){
        orderRepository.deleteAll();
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


   //@Test
    public void temp() throws ExecutionException, InterruptedException {

        final ExecutorService service = Executors.newFixedThreadPool(2);

        final List<Future<Long>> results = new ArrayList<>(2);

        final Future<Long> result = service.submit(new Callable1());
        results.add(result);
        final Future<Long> result2 = service.submit(new Callable2());
        results.add(result2);


        for(Future<Long> fut : results) {
            Long val = fut.get();
        }
    }

    public class Callable1 implements Callable {
        UserLogRegDTO userLogRegDTO = new UserLogRegDTO("existing1@mail.ru", "123");
        CartAdditionDTO cartAdditionDTO = new CartAdditionDTO(goodsService.goodsByTitle("charger").getId(),32L);
        @Override
        public Object call() throws Exception {
            System.out.println(1);
            given().port(port).log().all()
                    .filter(sessionFilterCustomerOne)
                    .contentType(ContentType.JSON).body(userLogRegDTO)
                    .when().post("/api/login")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value());
            System.out.println(1);
            given().port(port).log().all()
                    .filter(sessionFilterCustomerOne)
                    .contentType(ContentType.JSON).body(cartAdditionDTO)
                    .when().post("/api/cart/add")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value());
            System.out.println(1);
            given().port(port).log().all()
                    .filter(sessionFilterCustomerOne)
                    .when().post("/api/cart/order")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value());

/*            given().port(port).log().all()
                    .filter(sessionFilterCustomerOne)
                    .when().get("/api/goods")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value());*/
            return null;
        }
    }

    public class Callable2 implements Callable {
        UserLogRegDTO userLogRegDTO2 = new UserLogRegDTO("existing2@mail.ru", "123");
        CartAdditionDTO cartAdditionDTO = new CartAdditionDTO(goodsService.goodsByTitle("charger").getId(),32L);
        @Override
        public Object call() throws Exception {
            System.out.println(2);
            given().port(port).log().all()
                    .filter(sessionFilterCustomerTwo)
                    .contentType(ContentType.JSON).body(userLogRegDTO2)
                    .when().post("/api/login")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value());

            given().port(port).log().all()
                    .filter(sessionFilterCustomerTwo)
                    .contentType(ContentType.JSON).body(cartAdditionDTO)
                    .when().post("/api/cart/add")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value());

            given().port(port).log().all()
                    .filter(sessionFilterCustomerTwo)
                    .when().post("/api/cart/order")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value());

/*            given().port(port).log().all()
                    .filter(sessionFilterCustomerTwo)
                    .when().get("/api/goods")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value());*/
            return null;
        }
    }
}