import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.OrderCreateRequest;
import pojo.UserCreateRequest;
import pojo.UserLoginRequest;
import steps.OrderSteps;
import steps.UserSteps;

import java.util.List;

import static constants.HashIngredients.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class OrderTest {
    private String email = "kukururu8979878@gmail.com";
    private String password = "password6548";
    private String name = "kukuruzina99";
    private String accessToken;
    private UserSteps userSteps;
    private UserCreateRequest userCreateRequest;
    private Response createUser;
    private OrderSteps orderSteps;
    private List<String> ingredients = List.of(BUN_HASH,BEEF_METEORITE_HASH,CHEESE_HASH);

    @Before
    public void setUp(){
        userSteps = new UserSteps();
        userCreateRequest = new UserCreateRequest(email,password,name);
        createUser = userSteps.createUser(userCreateRequest);
        accessToken = createUser.jsonPath().getString("accessToken");
        orderSteps = new OrderSteps();

    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    @Description("Успешное создание заказа")
    public void createOrderWithAuthTest(){
        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);
        userSteps.loginUser(userLoginRequest);
        orderSteps.createOrderWithAuth(orderCreateRequest, accessToken)
                .then().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }


    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Ожидаем успешное создание заказа")
    public void createOrderNoAuthTest(){
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);
        orderSteps.createOrderNoLogin(orderCreateRequest)
                .then().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }


    @After
    public void tearDown(){
        userSteps.deleteUser(accessToken);
    }

}
