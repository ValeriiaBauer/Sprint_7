package steps;

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

import static ru.praktikum.ApiEndPoint.*;

public class CourierClient {
    private static final Logger logger = LogManager.getLogger(CourierClient.class);

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

    @Step("Удаление курьера с ID {courierId}")
    public Response deleteCourier(int courierId) {
        logger.info("Удаление курьера с ID {}", courierId);
        return getBaseRequest()
                .pathParam("id", courierId)
                .when()
                .delete(COURIER_DELETE);
    }
}
