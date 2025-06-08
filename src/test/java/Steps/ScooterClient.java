package Steps;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.praktikum.Courier;
import ru.praktikum.CourierCredentials;
import ru.praktikum.Order;

import static ru.praktikum.ApiEndPoint.*;

public class ScooterClient {
    private static final Logger logger = LogManager.getLogger(ScooterClient.class);

    private RequestSpecification getBaseRequest() {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Step("Создание курьера")
    public Response createCourier(Courier courier) {
        logger.info("Создание курьера с логином: {}", courier.getLogin());
        return getBaseRequest()
                .body(courier)
                .when()
                .post(COURIER_POST_CREATE);
    }

    @Step("Логин курьера {credentials.login}")
    public Response loginCourier(CourierCredentials credentials) {
        logger.info("Авторизация курьера с логином: {}", credentials.getLogin());
        return getBaseRequest()
                .body(credentials)
                .when()
                .post(COURIER_POST_LOGIN);
    }

    @Step("Создание заказа")
    public Response createOrder(Order order) {
        logger.info("Создание заказа для клиента: {} {}", order.getFirstName(), order.getLastName());
        return getBaseRequest()
                .body(order)
                .when()
                .post(ORDER_POST_CREATE);
    }

    @Step("Получение списка заказов")
    public Response getOrdersList() {
        logger.info("Получение списка заказов");
        return getBaseRequest()
                .when()
                .get(ORDER_GET_LIST);
    }

    @Step("Получение списка заказов с лимитом {limit}")
    public Response getOrdersListWithLimit(int limit) {
        logger.info("Получение списка заказов с лимитом {}", limit);
        return getBaseRequest()
                .queryParam("limit", limit)
                .when()
                .get(ORDER_GET_LIST);
    }

    @Step("Удаление курьера с ID {courierId}")
    public Response deleteCourier(int courierId) {
        logger.info("Удаление курьера с ID {}", courierId);
        return getBaseRequest()
                .pathParam("id", courierId)
                .when()
                .delete(COURIER_DELETE);
    }
}