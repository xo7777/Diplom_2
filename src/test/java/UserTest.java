import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.UserCreateRequest;
import steps.UserSteps;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserTest {
    private String email = "kukururu8979878@gmail.com";
    private String password = "password6548";
    private String name = "kukuruzina99";
    private String accessToken;
    private UserSteps userSteps;
    private UserCreateRequest userCreateRequest;
    private Response createUser;

    @Before
    public void setUp(){
        userSteps = new UserSteps();
        userCreateRequest = new UserCreateRequest(email,password,name);
        createUser = userSteps.createUser(userCreateRequest);
        accessToken = createUser.jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Создание пользователя")
    @Description("Успешное создание нового пользователя")
    public void createUserTest(){
    createUser.then()
            .assertThat().body("success", equalTo(true))
            .and()
            .statusCode(200);
    }


    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Ожидаем ошибку при создании одинаковых пользователей")
    public void createSameUser(){
        createUser.then()
                .assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
        createUser = userSteps.createUser(userCreateRequest);
        createUser.then()
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Ожидаем ошибку при создании пользователя без email")
    public void createUserWithoutEmail(){

        userCreateRequest.setEmail("");
        createUser = userSteps.createUser(userCreateRequest);
        createUser.then()
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(403);
    }

@After
    public void tearDown(){
            userSteps.deleteUser(accessToken);
}

}
