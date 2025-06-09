package tests;

import steps.CourierClient;
import utils.CleanupUtils;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.Courier;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class CourierCreationTests {

        private final CourierClient client = new CourierClient();
        private String testLogin;
        private final String testPassword = "1234";
        private final String testFirstName = "sheldon";
        private Courier testCourier;

        @Before
        public void setUp() {
            testLogin = "kuper" + System.currentTimeMillis();
            testCourier = new Courier(testLogin, testPassword, testFirstName);
        }

        @After
        public void tearDown() {
            CleanupUtils.deleteCourier(testLogin, testPassword);
        }

        @Test
        @DisplayName("Успешное создание курьера с валидными данными")
        public void successfulCourierCreationShouldReturn201AndOkTrue() {
            client.createCourier(testCourier)
                    .then()
                    .assertThat()
                    .statusCode(201)
                    .body("ok", is(true));
        }

        @Test
        @DisplayName("Попытка создания дубликата курьера")
        public void duplicateCourierCreationShouldReturn409() {

            client.createCourier(testCourier);

            client.createCourier(testCourier)
                    .then()
                    .assertThat()
                    .statusCode(409)
                    .body("message", equalTo("Этот логин уже используется"));
        }

        @Test
        @DisplayName("Создание курьера без логина")
        public void courierCreationWithoutLoginShouldReturn400() {
            Courier courierWithoutLogin = new Courier(null, testPassword, testFirstName);

            client.createCourier(courierWithoutLogin)
                    .then()
                    .assertThat()
                    .statusCode(400)
                    .body("message", equalTo("Недостаточно данных для создания учетной записи"));
        }

        @Test
        @DisplayName("Создание курьера без пароля")
        public void courierCreationWithoutPasswordShouldReturn400() {
            Courier courierWithoutPassword = new Courier(testLogin, null, testFirstName);

            client.createCourier(courierWithoutPassword)
                    .then()
                    .assertThat()
                    .statusCode(400)
                    .body("message", equalTo("Недостаточно данных для создания учетной записи"));
        }

        @Test
        @DisplayName("Создание курьера без имени (должно быть успешным)")
        public void courierCreationWithoutFirstNameShouldBeSuccessful() {
            Courier courierWithoutFirstName = new Courier(testLogin, testPassword, null);

            client.createCourier(courierWithoutFirstName)
                    .then()
                    .assertThat()
                    .statusCode(201)
                    .body("ok", is(true));
        }

        @Test
        @DisplayName("Создание курьера с пустым логином")
        public void courierCreationWithEmptyLoginShouldReturn400() {
            Courier courierWithEmptyLogin = new Courier("", testPassword, testFirstName);

            client.createCourier(courierWithEmptyLogin)
                    .then()
                    .assertThat()
                    .statusCode(400)
                    .body("message", equalTo("Недостаточно данных для создания учетной записи"));
        }

        @Test
        @DisplayName("Создание курьера с пустым паролем")
        public void courierCreationWithEmptyPasswordShouldReturn400() {
            Courier courierWithEmptyPassword = new Courier(testLogin, "", testFirstName);

            client.createCourier(courierWithEmptyPassword)
                    .then()
                    .assertThat()
                    .statusCode(400)
                    .body("message", equalTo("Недостаточно данных для создания учетной записи"));
        }
}