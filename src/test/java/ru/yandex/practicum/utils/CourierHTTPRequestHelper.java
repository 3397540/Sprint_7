package ru.yandex.practicum.utils;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practicum.Courier;
import ru.yandex.practicum.constants.URLS;

import static io.restassured.RestAssured.given;

public class CourierHTTPRequestHelper extends BaseHTTPRequestHelper{

    //COURIER HTTP REQUEST STEPS

    @Step("Login as courier with login: \"{login}\" and password: \"{password}\"")
    public Response loginAsCourier(String login, String password) {
        Courier courier = new Courier();
        courier.setLogin(login);
        courier.setPassword(password);
        return given()
                .config(config)
                .spec(requestSpec())
                .body(courier)
                .post(URLS.COURIER_LOGIN)
                .thenReturn();
    }

    @Step("Delete courier with id: \"{id}\"")
    public Response deleteCourier(Integer id) {
        return given()
                .config(config)
                .spec(requestSpec())
                .pathParam("courier_id", id)
                .delete(URLS.COURIER_DELETE);
    }

    @Step("Delete courier created for test")
    public void deleteCourierCreatedForTest(Courier courier) {
        Response response = loginAsCourier(courier.getLogin(), courier.getPassword());
        CourierResponseValidationtHelper helper = new CourierResponseValidationtHelper();
        Integer id = helper.getCourierId(response);
        deleteCourier(id);
    }


}
