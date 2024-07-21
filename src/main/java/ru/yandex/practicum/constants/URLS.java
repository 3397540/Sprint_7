package ru.yandex.practicum.constants;

public class URLS {

    //Base URL
    public static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    //Courier endpoints
    public static final String COURIER_LOGIN = "/api/v1/courier/login";
    public static final String COURIER_CREATE = "/api/v1/courier";
    public static final String COURIER_DELETE = "/api/v1/courier/{courier_id}";
    //Couriers endpoint
    public static final String COURIER_ORDERS_COUNT = "/api/v1/courier/{courier_id}/ordersCount";

    //Orders endpoints
    public static final String ORDERS_GET = "/api/v1/orders";
    public static final String ORDERS_TRACK = "/api/v1/orders/track";
    public static final String ORDERS_CREATE = "/api/v1/orders";
    public static final String ORDERS_ACCEPT = "/api/v1/orders/accept/{order_id}";
    public static final String ORDERS_FINISH = "/api/v1/orders/finish/{order_id}";
    public static final String ORDERS_CANCEL = "/api/v1/orders/cancel";

    //Utils endpoints
    public static final String SERVER_PING = "/api/v1/ping";
    public static final String STATION_SEARCH = "/api/v1/stations/search";

}
