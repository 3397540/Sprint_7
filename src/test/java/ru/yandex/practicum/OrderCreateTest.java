package ru.yandex.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import ru.yandex.practicum.constants.URLS;
import ru.yandex.practicum.utils.OrderHTTPRequestHelper;
import ru.yandex.practicum.utils.OrderResponseValidationHelper;


public class OrderCreateTest {

    private Orders order;

    //Set data for tests
    private String firstName = "SomeNewFirstName";
    private String lastName = "SomeNewLastName";
    private String address = "Some New Address, 99";
    private String metroStation = "26";
    private String phone = "+7 800 000 00 01";
    private Integer rentTime = 3;
    private String deliveryDate = "2024-08-02";
    private String comment = "Some Random Comment";
    private String[] color = {"BLACK", "GREY"};


    @Test
    @DisplayName("Order could be created with 2 colors in request body")
    @Description("Send POST request to {{baseURI}}/api/v1/orders with 2 colors in request body and check that " +
            "order is created")
    public void orderCouldBeCreatedWith2ColorsTest() {
        order = new Orders(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        OrderHTTPRequestHelper requestHelper = new OrderHTTPRequestHelper();
        OrderResponseValidationHelper responseHelper = new OrderResponseValidationHelper();
        Response response = requestHelper.sendPostRequest(URLS.ORDERS_CREATE, order);
        responseHelper.checkResponseCode(response, 201);
        Integer track = responseHelper.getOrderTrackNumber(response);
        responseHelper.checkValueNotNullForPayloadParameter(track);
    }

    @Test
    @DisplayName("Order could be created without color in request body")
    @Description("Send POST request to {{baseURI}}/api/v1/orders without color in request body and check that " +
            "order is created")
    public void orderCouldBeCreatedWithoutColorTest() {
        order = new Orders(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment);
        OrderHTTPRequestHelper requestHelper = new OrderHTTPRequestHelper();
        OrderResponseValidationHelper responseHelper = new OrderResponseValidationHelper();
        Response response = requestHelper.sendPostRequest(URLS.ORDERS_CREATE, order);
        responseHelper.checkResponseCode(response, 201);
        Integer track = responseHelper.getOrderTrackNumber(response);
        responseHelper.checkValueNotNullForPayloadParameter(track);
    }


}
