package ru.yandex.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.constants.URLS;
import ru.yandex.practicum.utils.OrderHTTPRequestHelper;
import ru.yandex.practicum.utils.OrderResponseValidationHelper;


public class OrdersGetByTrackingNumberTest {

    private Orders order;

    //Set data for tests
    private final String firstName = "SomeNewFirstName";
    private final String lastName = "SomeNewLastName";
    private final String address = "Some New Address, 99";
    private final String metroStation = "26";
    private final String phone = "+7 800 000 00 01";
    private final Integer rentTime = 3;
    private final String deliveryDate = "2024-08-02";
    private final String comment = "Some Random Comment";
    private final String[] color = {"BLACK", "GREY"};

    private Integer track;

    @Before
    @DisplayName("Create Order")
    @Description("Create Order for test use")
    public void createOrderBeforeTest() {
        order = new Orders(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        OrderHTTPRequestHelper requestHelper = new OrderHTTPRequestHelper();
        OrderResponseValidationHelper responseHelper = new OrderResponseValidationHelper();
        Response response = requestHelper.sendPostRequest(URLS.ORDERS_CREATE, order);
        responseHelper.checkResponseCode(response, 201);
        track = responseHelper.getOrderTrackNumber(response);
    }

    @Test
    @DisplayName("Get Order with tracking number response contains Order")
    @Description("Send GET request to {{baseURI}}/api/v1/orders/track with tracking number and check that response " +
            "contains orders list")
    public void getOrderByTrackingNumberResponseContainsOrder() {
        OrderHTTPRequestHelper requestHelper = new OrderHTTPRequestHelper();
        OrderResponseValidationHelper responseHelper = new OrderResponseValidationHelper();
        Response response = requestHelper.sendGetRequestWithQueryParam(URLS.ORDERS_TRACK, "t", track.toString());
        responseHelper.checkResponseCode(response, 200);
        String value = responseHelper.getTextValueForPayloadParameter(response, "order");
        responseHelper.checkValueNotNullForPayloadParameter(value);
    }

    @Test
    @DisplayName("Get Order with wrong tracking number response contains Error")
    @Description("Send GET request to {{baseURI}}/api/v1/orders/track with wrong tracking number and check that response " +
            "contains Error")
    public void getOrderWithWrongTrackingNumberResponseContainsError() {
        OrderHTTPRequestHelper requestHelper = new OrderHTTPRequestHelper();
        OrderResponseValidationHelper responseHelper = new OrderResponseValidationHelper();
        Response response = requestHelper.sendGetRequestWithQueryParam(URLS.ORDERS_TRACK, "t", RandomStringUtils.randomNumeric(9));
        responseHelper.checkResponseCode(response, 404);
        responseHelper.validateResponseBody(response, "message", "Заказ не найден");
    }

    @Test
    @DisplayName("Get Order without tracking number response contains error")
    @Description("Send GET request to {{baseURI}}/api/v1/orders/track without tracking number and check that response " +
            "contains Error message")
    public void getOrderWithoutTrackingNumberResponseContainsError() {
        OrderHTTPRequestHelper requestHelper = new OrderHTTPRequestHelper();
        OrderResponseValidationHelper responseHelper = new OrderResponseValidationHelper();
        Response response = requestHelper.sendGetRequest(URLS.ORDERS_TRACK);
        responseHelper.checkResponseCode(response, 400);
        responseHelper.validateResponseBody(response, "message", "Недостаточно данных для поиска");

    }
// PLEASE DISREGARD. MADE JUST AS MY NOTES, HOW TO GET RESPONSE BODY AS OBJECT!
//
//    @Test
//    @DisplayName("EXTRACTING BODY AS OBJECT - Get Order with tracking number response contains Order")
//    @Description("Send GET request to {{baseURI}}/api/v1/orders/track with tracking number and check that response " +
//            "contains orders list")
//    public void tempGetOrderByTrackingNumberResponseContainsOrder() {
//        OrderHTTPRequestHelper requestHelper = new OrderHTTPRequestHelper();
//        OrderResponseValidationHelper responseHelper = new OrderResponseValidationHelper();
//        Response response = requestHelper.sendGetRequestWithQueryParam(URLS.ORDERS_TRACK, "t", track.toString());
//        responseHelper.checkResponseCode(response, 200);
//        Orders order = responseHelper.deserializeResponseAsOrderResponseObject(response).getOrder();
//        System.out.println("ПЕЧАТАЕМ АДРЕС ЗАКАЗА");
//        System.out.println(order.getAddress());
//    }

    @After
    @DisplayName("Cancel Order")
    @Description("Cancel Order created for test use")
    public void cancelOrderAfterTest() {
        OrderHTTPRequestHelper requestHelper = new OrderHTTPRequestHelper();
        requestHelper.cancelOrder(track);
    }

}
