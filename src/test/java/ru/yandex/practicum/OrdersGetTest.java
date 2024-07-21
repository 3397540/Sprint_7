package ru.yandex.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import ru.yandex.practicum.constants.URLS;

import java.util.List;


public class OrdersGetTest extends BaseTest {

    @Test
    @DisplayName("Get Orders response contains Orders list")
    @Description("Send GET request to {{baseURI}}/api/v1/orders and check that response contains orders list")
    public void getOrdersResponseContainsOrdersList() {
        Response response = sendGetRequest(URLS.ORDERS_GET);
        checkResponseCode(response, 200);
        List<String> orders = getOrdersList(response);
        checkValueNotNullForPayloadParameter(orders);
    }

}
