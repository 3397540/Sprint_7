package ru.yandex.practicum.utils;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practicum.Courier;

public class CourierResponseValidationtHelper extends BaseResponseValidationHelper{

    //COURIER RESPONSE VALIDATION STEPS

    @Step("Get courier's id")
    public Integer getCourierId(Response response) {
        return response
                .then()
                .extract()
                .body()
                .path("id");
    }

    @Step("Deserialize response as Courier object")
    public Courier deserializeResponseAsCourier(Response response) {
        return response
                .body()
                .as(Courier.class);
    }
}
