package Tests;

import Steps.ScooterClient;
import Utils.CleanupUtils;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.Courier;
import ru.praktikum.CourierCredentials;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class CourierLoginTest {

    private final ScooterClient client = new ScooterClient();
    private String testLogin;
    private final String testPassword = "1234";
    private final String testFirstName = "sheldon";
    private int courierId;

    @Before
    public void setUp() {
        testLogin = "kuper" + System.currentTimeMillis();
        Courier courier = new Courier(testLogin, testPassword, testFirstName);
        client.createCourier(courier);
        int courierId = client.loginCourier(new CourierCredentials(testLogin, testPassword))
                .then().extract().path("id");
    }

    @After
    public void tearDown() {
        CleanupUtils.deleteCourier(testLogin, testPassword);
    }

    @Test
    @DisplayName("Успешная авторизация курьера с валидными данными")
    public void successfulLoginWithValidCredentialsShouldReturn200AndId() {
        client.loginCourier(new CourierCredentials(testLogin, testPassword))
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    public void loginWithWrongPasswordShouldReturn404() {
        client.loginCourier(new CourierCredentials(testLogin, "wrong_password"))
                .then()
                .assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация без логина")
    public void loginWithoutLoginShouldReturn400() {
        client.loginCourier(new CourierCredentials(null, testPassword))
                .then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация без пароля")
    public void loginWithoutPasswordShouldReturn400() {
        client.loginCourier(new CourierCredentials(testLogin, null))
                .then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация несуществующего курьера")
    public void loginNonExistentCourierShouldReturn404() {
        client.loginCourier(new CourierCredentials("nonexistent_login", "any_password"))
                .then()
                .assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с пустым логином")
    public void loginWithEmptyLoginShouldReturn400() {
        client.loginCourier(new CourierCredentials("", testPassword))
                .then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация с пустым паролем")
    public void loginWithEmptyPasswordShouldReturn400() {
        client.loginCourier(new CourierCredentials(testLogin, ""))
                .then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }
}