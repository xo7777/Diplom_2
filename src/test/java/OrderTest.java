import com.github.javafaker.Faker;
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
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class OrderTest {
    private String email;
    private String password;
    private String name;
    private String accessToken;
    private UserSteps userSteps;
    private UserCreateRequest userCreateRequest;
    private Response createUser;
    private OrderSteps orderSteps;
    private List<String> ingredients = List.of(BUN_HASH, BEEF_METEORITE_HASH, CHEESE_HASH);

    @Before
    public void setUp() {
        userSteps = new UserSteps();
        Faker faker = new Faker();
        email = faker.bothify("??????#####@ya.ru");
        password = faker.bothify("?##?#?#?#");
        name = faker.letterify("?????");
        userCreateRequest = new UserCreateRequest(email, password, name);
        createUser = userSteps.createUser(userCreateRequest);
        accessToken = createUser.jsonPath().getString("accessToken");
        orderSteps = new OrderSteps();

    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    @Description("Успешное создание заказа")
    public void createOrderWithAuthTest() {
        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);
        userSteps.loginUser(userLoginRequest);
        orderSteps.createOrderWithAuth(orderCreateRequest, accessToken)
                .then()
                .statusCode(SC_OK)
                .and()
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("name", equalTo("Метеоритный краторный астероидный бургер"));
    }


    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Ожидаем ошибку 401 создания заказа у неавторизованного пользователя")
    public void createOrderNoAuthTest() {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);
        orderSteps.createOrderNoLogin(orderCreateRequest)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat().body("success", equalTo(false));
    }


    @After
    public void tearDown() {
        userSteps.deleteUser(accessToken);
    }

}
