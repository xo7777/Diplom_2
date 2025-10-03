import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.UserCreateRequest;
import steps.UserSteps;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class UserTest {
    private String email;
    private String password;
    private String name;
    private String accessToken;
    private UserSteps userSteps;
    private UserCreateRequest userCreateRequest;
    private Response createUser;

    @Before
    public void setUp(){
        userSteps = new UserSteps();
        Faker faker = new Faker();
        email = faker.bothify("??????#####@ya.ru");
        password = faker.bothify("?##?#?#?#");
        name = faker.letterify("?????");
        userCreateRequest = new UserCreateRequest(email,password,name);
        createUser = userSteps.createUser(userCreateRequest);
        accessToken = createUser.jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Создание пользователя")
    @Description("Успешное создание нового пользователя")
    public void createUserTest(){
    createUser.then()
            .statusCode(SC_OK)
            .and()
            .assertThat().body("success", equalTo(true))
            .and()
            .assertThat().body("accessToken", notNullValue());
    }


    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Ожидаем ошибку при создании одинаковых пользователей")
    public void createSameUser(){
        createUser.then()
                .statusCode(SC_OK)
                .and()
                .assertThat().body("success", equalTo(true));
        createUser = userSteps.createUser(userCreateRequest);
        createUser.then()
                .statusCode(SC_FORBIDDEN)
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .assertThat().body("message", equalTo("User already exists"));

    }


@After
    public void tearDown(){
            userSteps.deleteUser(accessToken);
}

}
