package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.UserCreateRequest;

import static constants.Url.*;
import static io.restassured.RestAssured.given;

public class UserSteps {

    @Step("Создание пользователя")
public Response createUser(UserCreateRequest userCreateRequest){
        return given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(userCreateRequest)
                .when()
                .post(CREATE_USER_URL);
    }
    @Step("Удаление пользователя")
    public void deleteUser(String accessToken){
        given()
                .baseUri(BASE_URL)
                .header("Authorization", accessToken)
                .delete(DELETE_USER_URL);
    }


}
