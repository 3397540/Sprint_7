package ru.yandex.practicum.utils;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practicum.constants.URLS;

import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderHTTPRequestHelper extends BaseHTTPRequestHelper{

    //ORDER HTTP REQUEST STEPS

    @Step("Get order's track number")
    public Integer getOrderTrackNumber(Response response) {
        return response
                .then()
                .extract()
                .body()
                .path("track");
    }

    @Step("Get orders list")
    public List<String> getOrdersList(Response response) {
        return response
                .then()
                .extract()
                .body()
                .path("orders");
    }

// PLEASE DISREGARD. MADE JUST AS MY NOTES, HOW TO GET RESPONSE BODY AS OBJECT!
//
//    @Step("Deserialize response as OrderResponse object")
//    protected OrderResponse deserializeResponseAsOrderResponseObject(Response response) {
//        return response
//                .then()
//                .extract()
//                .body().as(OrderResponse.class);
//
//    }

    @Step("Cancel created order")
    public Response cancelOrder(Integer track) {
        return given()
                .config(config)
                .spec(requestSpec())
                .param("track", track)
                .put(URLS.ORDERS_CANCEL);
    }
}
