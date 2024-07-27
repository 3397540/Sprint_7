package ru.yandex.practicum.utils;

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

import static io.restassured.RestAssured.given;

import ru.yandex.practicum.constants.URLS;

public abstract class BaseHTTPRequestHelper {

    RestAssuredConfig config = RestAssured.config()
            .httpClient(HttpClientConfig.httpClientConfig()
                    .setParam("http.socket.timeout",30000)
                    .setParam("http.connection.timeout", 30000));

    protected static RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(URLS.BASE_URL)
                .addHeader("Content-Type", "application/json")
                //Off https validation
                .setRelaxedHTTPSValidation()
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new ErrorLoggingFilter())
                .build();
    }

    //COMMON HTTP REQUEST STEPS

    @Step("Send POST request to \"{endpoint}\"")
    public Response sendPostRequest(String endpoint, Object body) {
        return given()
                .config(config)
                .spec(requestSpec())
                .body(body)
                .post(endpoint)
                .thenReturn();
    }

    @Step("Send GET request to \"{endpoint}\"")
    public Response sendGetRequest(String endpoint) {
        return given()
                .config(config)
                .spec(requestSpec())
                .get(endpoint)
                .thenReturn();
    }

    @Step("Send GET request to \"{endpoint}\" with query parameter {param} and value {value}")
    public Response sendGetRequestWithQueryParam(String endpoint, String param, String value) {
        return given()
                .config(config)
                .spec(requestSpec())
                .queryParam(param, value)
                .get(endpoint)
                .thenReturn();
    }
}
