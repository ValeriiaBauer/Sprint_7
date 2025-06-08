package Utils;

import Steps.ScooterClient;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.praktikum.CourierCredentials;

public class CleanupUtils {
    private static final Logger logger = LogManager.getLogger(CleanupUtils.class);
    private static final ScooterClient client = new ScooterClient();

    public static void deleteCourier(String login, String password) {
        try {
            Allure.step("Удаление курьера с логином: " + login);
            logger.info("Попытка удаления курьера: {}", login);

            if (login == null || password == null) {
                String warning = "Не удалось удалить курьера - логин или пароль null";
                logger.warn(warning);
                Allure.addAttachment("Предупреждение", "text/plain", warning);
                return;
            }

            CourierCredentials credentials = new CourierCredentials(login, password);
            Response loginResponse = client.loginCourier(credentials);

            if (loginResponse.statusCode() == 200) {
                int courierId = loginResponse.body().jsonPath().getInt("id");
                Response deleteResponse = client.deleteCourier(courierId);

                if (deleteResponse.statusCode() == 200) {
                    String success = String.format("Курьер %s успешно удален", login);
                    logger.info(success);
                    Allure.step(success);
                } else {
                    String error = String.format("Не удалось удалить курьера %s. Код ответа: %d",
                            login, deleteResponse.statusCode());
                    logger.error(error);
                    Allure.addAttachment("Ошибка удаления", "text/plain", error);
                }
            } else {
                String warning = String.format("Курьер %s не найден. Код ответа: %d",
                        login, loginResponse.statusCode());
                logger.warn(warning);
                Allure.addAttachment("Предупреждение", "text/plain", warning);
            }
        } catch (Exception e) {
            String error = String.format("Ошибка при удалении курьера %s: %s", login, e.getMessage());
            logger.error(error, e);
            Allure.addAttachment("Критическая ошибка", "text/plain", error);
        }
    }
}