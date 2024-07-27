package ru.yandex.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import ru.yandex.practicum.constants.URLS;
import ru.yandex.practicum.utils.CourierHTTPRequestHelper;
import ru.yandex.practicum.utils.CourierResponseValidationtHelper;


public class CourierCreateTest {

    private Courier courier;

    //Set data for tests
    private final String login = "SomeNewCourier";
    private final String password = "SomeNewPassword";
    private final String firstName = "SomeFirstName";

    @Test
    @DisplayName("Courier could be created")
    @Description("Send POST request to {{baseURI}}/api/v1/courier with login, password and firstName and check that " +
            "courier is created")
    public void courierCouldBeCreatedTest() {
        courier = new Courier(login, password, firstName);
        CourierHTTPRequestHelper requestHelper = new CourierHTTPRequestHelper();
        CourierResponseValidationtHelper responseHelper = new CourierResponseValidationtHelper();
        Response response = requestHelper.sendPostRequest(URLS.COURIER_CREATE, courier);
        responseHelper.checkResponseCode(response, SC_CREATED);
        responseHelper.validateResponseBody(response, "ok", true);
    }

    @Test
    @DisplayName("Courier could be created without firstName")
    @Description("Send POST request to {{baseURI}}/api/v1/courier with login and password but without firstName and " +
            "check that courier is created")
    public void courierCouldBeCreatedWithoutFirstNameTest() {
        courier = new Courier();
        courier.setLogin(login);
        courier.setPassword(password);
        CourierHTTPRequestHelper requestHelper = new CourierHTTPRequestHelper();
        CourierResponseValidationtHelper responseHelper = new CourierResponseValidationtHelper();
        Response response = requestHelper.sendPostRequest(URLS.COURIER_CREATE, courier);
        responseHelper.checkResponseCode(response, SC_CREATED);
    }

    @Test
    @DisplayName("Courier could not be created without password")
    @Description("Send POST request to {{baseURI}}/api/v1/courier with login and firstName but without password and " +
            "check that courier is not created")
    public void courierCouldNotBeCreatedWithoutPasswordTest() {
        courier = new Courier();
        courier.setLogin(login);
        courier.setFirstName(firstName);
        CourierHTTPRequestHelper requestHelper = new CourierHTTPRequestHelper();
        CourierResponseValidationtHelper responseHelper = new CourierResponseValidationtHelper();
        Response response = requestHelper.sendPostRequest(URLS.COURIER_CREATE, courier);
        responseHelper.checkResponseCode(response, SC_BAD_REQUEST);
        responseHelper.validateResponseBody(response, "message", "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Courier could not be created without login")
    @Description("Send POST request to {{baseURI}}/api/v1/courier with password and firstName but without login and " +
            "check that courier is not created")
    public void courierCouldNotBeCreatedWithoutLoginTest() {
        courier = new Courier();
        courier.setPassword(password);
        courier.setFirstName(firstName);
        CourierHTTPRequestHelper requestHelper = new CourierHTTPRequestHelper();
        CourierResponseValidationtHelper responseHelper = new CourierResponseValidationtHelper();
        Response response = requestHelper.sendPostRequest(URLS.COURIER_CREATE, courier);
        responseHelper.checkResponseCode(response, SC_BAD_REQUEST);
        responseHelper.validateResponseBody(response, "message", "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Courier could not be created with duplicated login value")
    @Description("Send POST request to {{baseURI}}/api/v1/courier with login that duplicates any other existent " +
            "courier record and  with any password and firstName values and check that courier is not created")
    public void courierCouldNotBeCreatedWithDuplicateLoginTest() {
        //Need to create precondition courier, since we do not have any get courier method in api to find any existent courier
        courier = new Courier(login, password, firstName);
        CourierHTTPRequestHelper requestHelper = new CourierHTTPRequestHelper();
        CourierResponseValidationtHelper responseHelper = new CourierResponseValidationtHelper();
        requestHelper.sendPostRequest(URLS.COURIER_CREATE, courier);
        Courier newCourier = new Courier(login, RandomStringUtils
                .random(7, true, true), RandomStringUtils.randomAlphabetic(7));
        Response response =  requestHelper.sendPostRequest(URLS.COURIER_CREATE, newCourier);
        responseHelper.checkResponseCode(response, SC_CONFLICT);
        responseHelper.validateResponseBody(response, "message", "Этот логин уже используется. Попробуйте другой.");
    }

    @After
    @DisplayName("Deleting created courier")
    @Description("Deleting created for test courier")
    public void deleteCreatedCourier() {
        try {
            CourierHTTPRequestHelper requestHelper = new CourierHTTPRequestHelper();
            requestHelper.deleteCourierCreatedForTest(courier);
        } catch (Exception e){
            System.out.println("Courier could not be deleted");
        }
    }
}
