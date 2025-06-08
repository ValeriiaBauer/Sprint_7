package Utils;

import Steps.ScooterClient;
import io.restassured.response.Response;
import ru.praktikum.CourierCredentials;


public class CleanupUtils {

    public static final ScooterClient client = new ScooterClient();

    public static void deleteCourier(String login, String password) {
        CourierCredentials credentials = new CourierCredentials(login, password);
        Response loginResponse = client.loginCourier(credentials);

        if (loginResponse.statusCode() == 200) {
            int courierId = loginResponse.body().jsonPath().getInt("id");
            client.deleteCourier(courierId);
        }
    }
}