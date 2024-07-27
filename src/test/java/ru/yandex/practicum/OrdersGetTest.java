package ru.yandex.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import ru.yandex.practicum.constants.URLS;
import ru.yandex.practicum.utils.OrderHTTPRequestHelper;
import ru.yandex.practicum.utils.OrderResponseValidationHelper;

import java.util.List;


public class OrdersGetTest {

    @Test
    @DisplayName("Get Orders response contains Orders list")
    @Description("Send GET request to {{baseURI}}/api/v1/orders and check that response contains orders list")
    public void getOrdersResponseContainsOrdersList() {
        OrderHTTPRequestHelper requestHelper = new OrderHTTPRequestHelper();
        OrderResponseValidationHelper responseHelper = new OrderResponseValidationHelper();
        Response response = requestHelper.sendGetRequest(URLS.ORDERS_GET);
        responseHelper.checkResponseCode(response, 200);
        List<String> orders = responseHelper.getOrdersList(response);
        responseHelper.checkValueNotNullForPayloadParameter(orders);
    }

}
