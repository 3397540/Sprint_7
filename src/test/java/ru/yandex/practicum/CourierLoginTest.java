package ru.yandex.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import ru.yandex.practicum.constants.URLS;
import ru.yandex.practicum.utils.CourierHTTPRequestHelper;
import ru.yandex.practicum.utils.CourierResponseValidationtHelper;


public class CourierLoginTest {

    private Courier courier;

    //Set data for tests
    private final String login = "SomeNewCourier";
    private final String password = "SomeNewPassword";
    private final String firstName = "SomeFirstName";

    @Before
    @DisplayName("Create a courier")
    @Description("Create a courier for test use")
    public void createCourier() {
        courier = new Courier(login, password, firstName);
        CourierHTTPRequestHelper requestHelper = new CourierHTTPRequestHelper();
        requestHelper.sendPostRequest(URLS.COURIER_CREATE, courier);
    }


    @Test
    @DisplayName("Courier could log in")
    @Description("Send POST request to {{baseURI}}/api/v1/courier/login with login and password and check that " +
            "courier is logged in")
    public void courierCouldLogInTest() {
        CourierHTTPRequestHelper requestHelper = new CourierHTTPRequestHelper();
        CourierResponseValidationtHelper responseHelper = new CourierResponseValidationtHelper();
        Response response = requestHelper.loginAsCourier(login, password);
        responseHelper.checkResponseCode(response, SC_OK);
        Integer id = responseHelper.getCourierId(response);
        responseHelper.checkValueNotNullForPayloadParameter(id);
    }

    @Test
    @DisplayName("Courier could not log in without password")
    @Description("Send POST request to {{baseURI}}/api/v1/courier with login but without password and " +
            "check that error response is received")
    public void courierCouldNotLogInWithoutPasswordTest() {
        CourierHTTPRequestHelper requestHelper = new CourierHTTPRequestHelper();
        CourierResponseValidationtHelper responseHelper = new CourierResponseValidationtHelper();
        Response response = requestHelper.loginAsCourier(login, null);
        responseHelper.checkResponseCode(response, SC_BAD_REQUEST);
        responseHelper.validateResponseBody(response, "message", "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Courier could not log in without login")
    @Description("Send POST request to {{baseURI}}/api/v1/courier with password but without login and " +
            "check that error response is received")
    public void courierCouldNotLogInWithoutLoginTest() {
        CourierHTTPRequestHelper requestHelper = new CourierHTTPRequestHelper();
        CourierResponseValidationtHelper responseHelper = new CourierResponseValidationtHelper();
        Response response = requestHelper.loginAsCourier(null, password);
        responseHelper.checkResponseCode(response, SC_BAD_REQUEST);
        responseHelper.validateResponseBody(response, "message", "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Courier could not log in without login and password")
    @Description("Send POST request to {{baseURI}}/api/v1/courier without login and password and " +
            "check that error response is received")
    public void courierCouldNotLogInWithoutLoginAndPasswordTest() {
        CourierHTTPRequestHelper requestHelper = new CourierHTTPRequestHelper();
        CourierResponseValidationtHelper responseHelper = new CourierResponseValidationtHelper();
        Response response = requestHelper.loginAsCourier(null, null);
        responseHelper.checkResponseCode(response, SC_BAD_REQUEST);
        responseHelper.validateResponseBody(response, "message", "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Courier could not log in with wrong login")
    @Description("Send POST request to {{baseURI}}/api/v1/courier with wrong login and " +
            "check that error response is received")
    public void courierCouldNotLogInWithWrongLoginTest() {
        CourierHTTPRequestHelper requestHelper = new CourierHTTPRequestHelper();
        CourierResponseValidationtHelper responseHelper = new CourierResponseValidationtHelper();
        Response response = requestHelper.loginAsCourier(RandomStringUtils.randomAlphabetic(7), password);
        responseHelper.checkResponseCode(response, SC_NOT_FOUND);
        responseHelper.validateResponseBody(response, "message", "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Courier could not log in with wrong password")
    @Description("Send POST request to {{baseURI}}/api/v1/courier with wrong password and " +
            "check that error response is received")
    public void courierCouldNotLogInWithWrongPasswordTest() {
        CourierHTTPRequestHelper requestHelper = new CourierHTTPRequestHelper();
        CourierResponseValidationtHelper responseHelper = new CourierResponseValidationtHelper();
        Response response = requestHelper.loginAsCourier(login, RandomStringUtils.randomAlphabetic(7));
        responseHelper.checkResponseCode(response, SC_NOT_FOUND);
        responseHelper.validateResponseBody(response, "message", "Учетная запись не найдена");
    }

    @After
    @DisplayName("Deleting created courier")
    @Description("Deleting created for test courier")
    public void deleteCreatedCourier() {
        try {
            CourierHTTPRequestHelper requestHelper = new CourierHTTPRequestHelper();
            CourierResponseValidationtHelper responseHelper = new CourierResponseValidationtHelper();
            requestHelper.deleteCourierCreatedForTest(courier);
        } catch (Exception e){
            System.out.println("Courier could not be deleted");
        }
    }


}
