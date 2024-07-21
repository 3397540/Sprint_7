package ru.yandex.practicum;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.Assert;
import ru.yandex.practicum.constants.URLS;

import java.util.List;

public abstract class BaseTest {

    RestAssuredConfig config = RestAssured.config()
            .httpClient(HttpClientConfig.httpClientConfig()
                    .setParam("http.socket.timeout",30000)
                    .setParam("http.connection.timeout", 30000));

    private static RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(URLS.BASE_URL)
                .addHeader("Content-Type", "application/json")
                //Off https validation
                .setRelaxedHTTPSValidation()
                .addFilter(new ErrorLoggingFilter())
                .build();
    }

    //COMMON STEPS

    @Step("Send POST request to \"{endpoint}\"")
    protected Response sendPostRequest(String endpoint, Object body) {
        return given()
                    .config(config)
                    .spec(requestSpec())
                    .body(body)
                    .post(endpoint)
                    .thenReturn();
    }

    @Step("Send GET request to \"{endpoint}\"")
    protected Response sendGetRequest(String endpoint) {
        return given()
                .config(config)
                .spec(requestSpec())
                .get(endpoint)
                .thenReturn();
    }

    @Step("Check that response code is equal to {responseCode}")
    protected void checkResponseCode(Response response, int responseCode) {
        response.then()
                    .assertThat()
                    .statusCode(responseCode);
    }

    //Overloaded method for String values
    @Step("Validate that response body contains parameter \"{parameter}\" with value equals to \"{value}\"")
    protected void validateResponseBody(Response response, String parameter, String value) {
        response.then()
                    .assertThat()
                    .body(parameter, equalTo(value));
    }

    //Overloaded method for boolean values
    @Step("Validate that response body contains parameter \"{parameter}\" with value equals to \"{value}\"")
    protected void validateResponseBody(Response response, String parameter, Boolean value) {
        response.then()
                .assertThat()
                .body(parameter, equalTo(value));
    }

    //Overloaded method for Integer values
    @Step("Validate that response body contains parameter \"{parameter}\" with value equals to \"{value}\"")
    protected void validateResponseBody(Response response, String parameter, Integer value) {
        response.then()
                .assertThat()
                .body(parameter, equalTo(value));
    }

    @Step("Get value for payload parameter {param}")
    protected Object checkValueForPayloadParameter(Response response, String param) {
        return response.then()
                .extract()
                .body()
                .path(param);
    }

    @Step("Check that value is not NULL")
    protected void checkValueNotNullForPayloadParameter(Object param) {
        String objName = param.getClass().getName();
        Assert.assertNotNull("Value for " + objName + " is not presented", param);
    }

    //COURIER STEPS

    @Step("Login as courier with login: \"{login}\" and password: \"{password}\"")
    protected Response loginAsCourier(String login, String password) {
        Courier courier = new Courier();
        courier.setLogin(login);
        courier.setPassword(password);
        return given()
                    .config(config)
                    .spec(requestSpec())
                    .body(courier)
                    .post(URLS.COURIER_LOGIN)
                    .thenReturn();
    }

    @Step("Get courier's id")
    protected Integer getCourierId(Response response) {
        return response
                .then()
                    .extract()
                    .body()
                    .path("id");
    }

    @Step("Delete courier with id: \"{id}\"")
    protected Response deleteCourier(Integer id) {
        return given()
                    .config(config)
                    .spec(requestSpec())
                    .pathParam("courier_id", id)
                    .delete(URLS.COURIER_DELETE);
    }

    @Step("Deserialize response as Courier object")
    protected Object deserializeResponseAsCourier(Response response) {
        return response
                .body()
                .as(Courier.class);
    }

    @Step("Delete courier created for test")
    protected void deleteCourierCreatedForTest(Courier courier) {
        Response response = loginAsCourier(courier.getLogin(), courier.getPassword());
        Integer id = getCourierId(response);
        deleteCourier(id);
    }

    //ORDER STEPS

    @Step("Get order's track number")
    protected Integer getOrderTrackNumber(Response response) {
        return response
                .then()
                    .extract()
                    .body()
                    .path("track");
    }

    @Step("Get orders list")
    protected List<String> getOrdersList(Response response) {
        return response
                .then()
                .extract()
                .body()
                .path("orders");
    }

    @Step("Deserialize response as Orders object")
    protected Object deserializeResponseAsOrder(Response response) {
        return response
                .body()
                .as(Orders.class);
    }




}
