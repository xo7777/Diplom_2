import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.UserCreateRequest;
import pojo.UserLoginRequest;
import steps.UserSteps;

import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class LoginUserTest {
    private String email;
    private String password;
    private String name = "kukuruzina99";
    private String accessToken;
    private int statusCode;
    private boolean successMessage;
    private UserSteps userSteps;
    private UserCreateRequest userCreateRequest;
    private UserLoginRequest userLoginRequest;
    private Response createUser;



public LoginUserTest (String email, String password, int statusCode, boolean successMessage){
    this.email = email;
    this.password = password;
    this.statusCode = statusCode;
    this.successMessage = successMessage;
}

    @Parameterized.Parameters (name = "email - {0}, password - {1}, statusCode - {2}, successMessage - {3}")
    public static Object[][] testData(){
        return new Object[][] {
                {"kukurirere1516@mail.ru", "password123", 200, true},
                {"kukur@mail.ru", "password123", 401, false},
                {"kukurirere1516@mail.ru", "passw123", 401, false},
                {"kukur6@mail.ru", "pas23", 401, false},

        };
    }


    @Before
    public void setUp(){
        userSteps = new UserSteps();
        userCreateRequest = new UserCreateRequest("kukurirere1516@mail.ru","password123",name);
        userLoginRequest = new UserLoginRequest(email, password);
        createUser = userSteps.createUser(userCreateRequest);
        accessToken = createUser.jsonPath().getString("accessToken");
    }

    @Test
    public void loginUserTest(){
    userSteps.loginUser(userLoginRequest)
            .then()
            .assertThat().body("success", equalTo(successMessage))
            .and()
            .statusCode(statusCode);
    }

    @After
    public void tearDown(){
        userSteps.deleteUser(accessToken);
    }












}
