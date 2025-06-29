package tests;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.Order;
import steps.CourierClient;
import steps.OrderClient;

import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreationTest {
    private final OrderClient client = new OrderClient();
    private final String[] colors;

    public OrderCreationTest(String[] colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters(name = "Тестовые данные: цвета - {0}")
    public static Object[][] getTestData() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {null},
                {new String[]{}}
        };
    }

    @Test
    @DisplayName("Создание заказа с различными вариантами цветов")
    public void shouldCreateOrderWithDifferentColorOptions() {
        Order order = new Order(
                "Говард",
                "Воловец",
                "Москва, ул. Тверская, 1",
                "4",
                "+79998887766",
                5,
                "2025-06-06",
                "Комментарий",
                colors
        );

        client.createOrder(order)
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}
