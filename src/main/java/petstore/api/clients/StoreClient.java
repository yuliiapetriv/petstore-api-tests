package petstore.api.clients;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import petstore.api.config.ApiHelper;
import petstore.api.endpoints.Endpoints;

public class StoreClient {

    private static final Logger LOGGER = LogManager.getLogger(StoreClient.class);

    public Response createOrder(Object body) {
        LOGGER.info("Creating order with payload: {}", body);

        return RestAssured.given()
                .spec(ApiHelper.getRequestSpec())
                .body(body)
                .when()
                .post(Endpoints.ORDER)
                .then()
                .spec(ApiHelper.getResponseSpec())
                .extract()
                .response();
    }

    public Response getOrderById(int orderId) {
        LOGGER.info("Fetching order with ID: {}", orderId);

        return RestAssured.given()
                .spec(ApiHelper.getRequestSpec())
                .when()
                .get(Endpoints.ORDER + "/" + orderId)
                .then()
                .spec(ApiHelper.getResponseSpec())
                .extract()
                .response();
    }

    public Response deleteOrder(int orderId) {
        LOGGER.info("Deleting order with ID: {}", orderId);

        return RestAssured.given()
                .spec(ApiHelper.getRequestSpec())
                .when()
                .delete(Endpoints.ORDER + "/" + orderId)
                .then()
                .spec(ApiHelper.getResponseSpec())
                .extract()
                .response();
    }

    public Response getInventory() {
        LOGGER.info("Fetching inventory");

        return RestAssured.given()
                .spec(ApiHelper.getRequestSpec())
                .when()
                .get(Endpoints.INVENTORY)
                .then()
                .spec(ApiHelper.getResponseSpec())
                .extract()
                .response();
    }
}
