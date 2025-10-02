import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.UserCreateRequest;
import pojo.UserLoginRequest;
import steps.UserSteps;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;

public class LoginUserTest {
    private String email;
    private String password;
    private String name;
    private String accessToken;
    private UserSteps userSteps;
    private UserCreateRequest userCreateRequest;
    private UserLoginRequest userLoginRequest;
    private Response createUser;


    @Before
    public void setUp() {
        Faker faker = new Faker();
        email = faker.bothify("??????#####@ya.ru");
        password = faker.bothify("?##?#?#?#");
        name = faker.letterify("?????");
        userSteps = new UserSteps();
        userCreateRequest = new UserCreateRequest(email, password, name);
        userLoginRequest = new UserLoginRequest(email, password);
        createUser = userSteps.createUser(userCreateRequest);
        accessToken = createUser.jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Успешная авторизация пользователя и ошибки авторизации с неверным логином и паролем")
    public void loginUserTest() {
        userSteps.loginUser(userLoginRequest)
                .then()
                .statusCode(SC_OK)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Ошибка авторизации с неверным паролем")
    public void loginUserWithIncorrectPassword() {
        userLoginRequest.setPassword("123456");
        userSteps.loginUser(userLoginRequest)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat().body("success", equalTo(false));
    }

    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Ошибка авторизации с неверным email")
    public void loginUserWithIncorrectEmail() {
        userLoginRequest.setEmail("fdhfjih@yandex.ru");
        userSteps.loginUser(userLoginRequest)
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