import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.OrderCreateRequest;
import pojo.UserCreateRequest;
import pojo.UserLoginRequest;
import steps.OrderSteps;
import steps.UserSteps;

import java.util.List;

import static constants.HashIngredients.*;

@RunWith(Parameterized.class)
public class ParameterizedOrderTest {
    private String email = "kukururu8979878@gmail.com";
    private String password = "password6548";
    private String name = "kukuruzina99";
    private String accessToken;
    private UserSteps userSteps;
    private UserCreateRequest userCreateRequest;
    private UserLoginRequest userLoginRequest;
    private OrderCreateRequest orderCreateRequest;
    private Response createUser;
    private OrderSteps orderSteps;
    private List<String> ingredients;
    private int statusCode;

    public ParameterizedOrderTest(List<String> ingredients, int statusCode){
        this.ingredients = ingredients;
        this.statusCode = statusCode;
    }


    @Parameterized.Parameters (name = "list of ingredients - {0}, status code - {1}")
    public static Object[][] testData(){
        return new Object[][] {
                {List.of(BUN_HASH,BEEF_METEORITE_HASH,CHEESE_HASH), 200},
                {List.of(INCORRECT_HASH), 500},
                {List.of(), 400},

        };
    }

    @Before
    public void setUp(){
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();
        userCreateRequest = new UserCreateRequest(email, password, name);
        userLoginRequest = new UserLoginRequest(email, password);
        orderCreateRequest = new OrderCreateRequest(ingredients);
        createUser = userSteps.createUser(userCreateRequest);
        accessToken = createUser.jsonPath().getString("accessToken");
        userSteps.loginUser(userLoginRequest);
    }


@Test
@DisplayName("Создание заказа с хешем, без хеша, с некорректным хешем")
@Description("Успешное создание заказа с валидными значениями и ошибка при создании заказа без хеша и с некорректным хешем")
public void createOrderTest(){
        orderSteps.createOrderWithAuth(orderCreateRequest, accessToken)
                .then().statusCode(statusCode);

}






    @After
    public void tearDown(){
        userSteps.deleteUser(accessToken);
    }
}
