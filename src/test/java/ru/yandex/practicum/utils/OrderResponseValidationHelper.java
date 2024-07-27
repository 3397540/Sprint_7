package ru.yandex.practicum.utils;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practicum.OrderResponse;

import java.util.List;

public class OrderResponseValidationHelper extends BaseResponseValidationHelper{

    //ORDER RESPONSE VALIDATION STEPS

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

    @Step("Deserialize response as OrderResponse object")
    public OrderResponse deserializeResponseAsOrderResponseObject(Response response) {
        return response
                .then()
                .extract()
                .body().as(OrderResponse.class);

    }
}
