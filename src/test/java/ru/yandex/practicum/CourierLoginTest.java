package ru.yandex.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.constants.URLS;


public class CourierLoginTest extends BaseTest {

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
        sendPostRequest(URLS.COURIER_CREATE, courier);
    }


    @Test
    @DisplayName("Courier could log in")
    @Description("Send POST request to {{baseURI}}/api/v1/courier/login with login and password and check that " +
            "courier is logged in")
    public void courierCouldLogInTest() {
        Response response = loginAsCourier(login, password);
        checkResponseCode(response, 200);
        Integer id = getCourierId(response);
        checkValueNotNullForPayloadParameter(id);
    }

    @Test
    @DisplayName("Courier could not log in without password")
    @Description("Send POST request to {{baseURI}}/api/v1/courier with login but without password and " +
            "check that error response is received")
    public void courierCouldNotLogInWithoutPasswordTest() {
        Response response = loginAsCourier(login, null);
        checkResponseCode(response, 400);
        validateResponseBody(response, "message", "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Courier could not log in without login")
    @Description("Send POST request to {{baseURI}}/api/v1/courier with password but without login and " +
            "check that error response is received")
    public void courierCouldNotLogInWithoutLoginTest() {
        Response response = loginAsCourier(null, password);
        checkResponseCode(response, 400);
        validateResponseBody(response, "message", "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Courier could not log in without login and password")
    @Description("Send POST request to {{baseURI}}/api/v1/courier without login and password and " +
            "check that error response is received")
    public void courierCouldNotLogInWithoutLoginAndPasswordTest() {
        Response response = loginAsCourier(null, null);
        checkResponseCode(response, 400);
        validateResponseBody(response, "message", "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Courier could not log in with wrong login")
    @Description("Send POST request to {{baseURI}}/api/v1/courier with wrong login and " +
            "check that error response is received")
    public void courierCouldNotLogInWithWrongLoginTest() {
        Response response = loginAsCourier(RandomStringUtils.randomAlphabetic(7), password);
        checkResponseCode(response, 404);
        validateResponseBody(response, "message", "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Courier could not log in with wrong password")
    @Description("Send POST request to {{baseURI}}/api/v1/courier with wrong password and " +
            "check that error response is received")
    public void courierCouldNotLogInWithWrongPasswordTest() {
        Response response = loginAsCourier(login, RandomStringUtils.randomAlphabetic(7));
        checkResponseCode(response, 404);
        validateResponseBody(response, "message", "Учетная запись не найдена");
    }

    @After
    @DisplayName("Deleting created courier")
    @Description("Deleting created for test courier")
    public void deleteCreatedCourier() {
        try {
            deleteCourierCreatedForTest(courier);
        } catch (Exception e){
            System.out.println("Courier could not be deleted");
        }
    }


}
