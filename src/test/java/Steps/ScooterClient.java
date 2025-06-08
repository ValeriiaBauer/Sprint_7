package Steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum.Courier;
import ru.praktikum.CourierCredentials;
import ru.praktikum.Order;
import static ru.praktikum.ApiEndPoint.*;


public class ScooterClient {

    @Step("Создание курьера")
    public Response createCourier(Courier courier) {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(COURIER_POST_CREATE);
    }

    @Step("Логин курьера {credentials.login}")
    public Response loginCourier(CourierCredentials credentials) {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(credentials)
                .when()
                .post(COURIER_POST_LOGIN);
    }

    @Step("Создание заказа")
    public Response createOrder(Order order) {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(ORDER_POST_CREATE);
    }

    @Step("Получение списка заказов")
    public Response getOrdersList() {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .when()
                .get(ORDER_GET_LIST);
    }

    @Step("Получение списка заказов с лимитом {limit}")
    public Response getOrdersListWithLimit(int limit) {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .queryParam("limit", limit)
                .when()
                .get(ORDER_GET_LIST);
    }

    @Step("Удаление курьера с ID {courierId}")
    public Response deleteCourier(int courierId) {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .pathParam("id", courierId)
                .when()
                .delete(COURIER_DELETE);
    }
}