package tests;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import steps.OrderClient;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNull.notNullValue;

public class OrderListTest {

    private final OrderClient client = new OrderClient();

    @Test
    @DisplayName("Получение списка заказов должен возвращать непустой список")
    public void getOrdersListShouldReturnNonEmptyList() {
        client.getOrdersList()
                .then()
                .assertThat()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders", hasSize(greaterThan(0)))
                .body("orders.id", everyItem(notNullValue()))
                .body("orders.track", everyItem(notNullValue()))
                .body("orders.createdAt", everyItem(notNullValue()));
    }

    @Test
    @DisplayName("Получение списка заказов с лимитом")
    public void getOrdersListWithLimitShouldReturnCorrectCount() {
        int limit = 5;

        client.getOrdersListWithLimit(limit)
                .then()
                .assertThat()
                .statusCode(200)
                .body("orders", hasSize(lessThanOrEqualTo(limit)))
                .body("orders", everyItem(hasKey("id")));
    }

    @Test
    @DisplayName("Получение списка заказов проверка структуры заказа")
    public void getOrdersListShouldHaveCorrectOrderStructure() {
        client.getOrdersList()
                .then()
                .assertThat()
                .statusCode(200)
                .body("orders[0]", hasKey("id"))
                .body("orders[0]", hasKey("track"))
                .body("orders[0]", hasKey("createdAt"))
                .body("orders[0]", hasKey("status"))
                .body("orders[0]", hasKey("color"))
                .body("orders[0]", hasKey("comment"));
    }
}