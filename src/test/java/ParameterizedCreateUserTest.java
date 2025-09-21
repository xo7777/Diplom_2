import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.UserCreateRequest;
import steps.UserSteps;

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
        return new Object[][] {
                {"", "password123", "kukuruz99", "Email, password and name are required fields"},
                {"kukurirere1516@mail.ru", "", "kukuruz99", "Email, password and name are required fields"},
                {"kukurirere1516@mail.ru", "password123", "", "Email, password and name are required fields"},

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
                .assertThat().body("message", equalTo(expected))
                .and()
                .statusCode(403);
    }
}
