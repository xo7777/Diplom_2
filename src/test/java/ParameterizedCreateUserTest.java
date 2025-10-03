import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.UserCreateRequest;
import steps.UserSteps;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.hamcrest.CoreMatchers.equalTo;


@RunWith(Parameterized.class)
public class ParameterizedCreateUserTest {
    private String email;
    private String password;
    private String name;
    private String expected;

    public ParameterizedCreateUserTest(String email, String password, String name, String expected) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.expected = expected;
    }
    @Parameterized.Parameters (name = "email - {0}, password - {1}, name - {2}, expected - {3}")
    public static Object[][] testData(){
        Faker faker = new Faker();
        String email = faker.bothify("??????#####@ya.ru");
        String password = faker.bothify("?##?#?#?#");
        String name = faker.letterify("?????");
        return new Object[][] {
                {"", password, name, "Email, password and name are required fields"},
                {email, "", name, "Email, password and name are required fields"},
                {email, password, "", "Email, password and name are required fields"},

        };
    }

    @Test
    @DisplayName("Создание пользователя без обязательных полей")
    @Description("Ошибка при создании пользователя без email, password и name")
            public void createTestWithoutRequiredFields(){
        UserSteps userSteps = new UserSteps();
        UserCreateRequest userCreateRequest = new UserCreateRequest(email,password,name);
        userSteps.createUser(userCreateRequest)
                .then()
                .statusCode(SC_FORBIDDEN)
                .and()
                .assertThat().body("message", equalTo(expected));
    }
}
