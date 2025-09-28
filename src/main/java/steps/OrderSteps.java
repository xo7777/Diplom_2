package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.OrderCreateRequest;

import static constants.Url.*;
import static io.restassured.RestAssured.given;

public class OrderSteps {


    @Step("Создание заказа авторизованным пользователем")
    public Response createOrderWithAuth(OrderCreateRequest orderCreateRequest, String accessToken) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(orderCreateRequest)
                .when()
                .post(CRAETE_ORDER_URL);
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderNoLogin(OrderCreateRequest orderCreateRequest) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(orderCreateRequest)
                .when()
                .post(CRAETE_ORDER_URL);
    }

}