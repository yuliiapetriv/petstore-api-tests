package petstore.api.tests.store;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import petstore.api.clients.StoreClient;
import petstore.api.config.ApiHelper;
import petstore.api.dto.OrderDto;
import petstore.api.tests.BaseTest;
import petstore.api.utils.OrderUtils;
import utils.DataGenerator;

import java.time.Duration;

import static org.awaitility.Awaitility.await;

public class DeleteOrderTests extends BaseTest {

    private StoreClient storeClient;

    @BeforeClass
    public void setUp() {
        storeClient = new StoreClient();
    }


    @Test(description = "Check DELETE order request with valid order ID")
    public void checkDeleteOrderRequestWithValidId() {
        OrderDto orderDto = OrderUtils.createOrderDto();

        Response createOrderResponse = storeClient.createOrder(orderDto);
        Assert.assertEquals(createOrderResponse.statusCode(), HttpStatus.SC_OK, "Expected 200 for successful order creation.");
        LOGGER.info("Order created successfully with id: {}", orderDto.getId());

        await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofSeconds(3))
                .ignoreExceptions()
                .untilAsserted(() -> {
                    Response deleteOrderResponse = storeClient.deleteOrder(orderDto.getId());
                    Assert.assertEquals(deleteOrderResponse.statusCode(), HttpStatus.SC_OK,
                            "Expected 200 for successful order deletion.");
                    Assert.assertEquals(deleteOrderResponse.jsonPath().getString("message"),
                            orderDto.getId().toString(), "Response message should match order ID");
                });
    }

    @Test(description = "Check DELETE order request with with negative order Id")
    public void checkDeleteOrderRequestWithNegativeId() {
        int negativeId = -DataGenerator.generateRandomId();

        Response deleteOrderResponse = storeClient.deleteOrder(negativeId);
        Assert.assertEquals(deleteOrderResponse.statusCode(), HttpStatus.SC_NOT_FOUND, "Expected 404 for negative order Id.");
        Assert.assertEquals(deleteOrderResponse.jsonPath().getString("message"), "Order Not Found");
    }

    @AfterMethod
    public void cleanupThreadLocal() {
        ApiHelper.clearSpecs();
    }
}
