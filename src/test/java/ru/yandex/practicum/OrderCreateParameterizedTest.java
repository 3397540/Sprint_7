package ru.yandex.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.practicum.constants.URLS;

@RunWith(Parameterized.class)
public class OrderCreateParameterizedTest extends BaseTest {



    private Orders order;

    //Set data for tests
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;

    public OrderCreateParameterizedTest(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, String color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = new String[]{color};
    }

    @Parameterized.Parameters(name = "color = {8}")
    public static Object[][] getTestData() {
        return new Object[][]{
                {"SomeNewFirstName", "SomeNewLastName", "Some New Address, 99", "26", "+7 800 000 00 01", 3,
                        "2024-08-02", "Some Random Comment", "BLACK"},
                {"SomeNewFirstName", "SomeNewLastName", "Some New Address, 99", "26", "+7 800 000 00 01", 3,
                        "2024-08-02", "Some Random Comment", "GREY"}
        };
    }

    @Test
    @DisplayName("Order could be created with any of 2 available colors in request body")
    @Description("Send POST request to {{baseURI}}/api/v1/orders with any of 2 available colors in request body and " +
            "check that order is created")
    public void orderCouldBeCreatedWithAnyColorTest() {
        order = new Orders(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        Response response = sendPostRequest(URLS.ORDERS_CREATE, order);
        checkResponseCode(response, 201);
        Integer track = getOrderTrackNumber(response);
        checkValueNotNullForPayloadParameter(track);
    }
}
