package petstore.api.tests.store;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import petstore.api.clients.StoreClient;
import petstore.api.config.ApiHelper;
import petstore.api.dto.OrderDto;
import petstore.api.tests.BaseTest;
import petstore.api.utils.OrderUtils;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class CreateOrderTests extends BaseTest {

    private StoreClient storeClient;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        storeClient = new StoreClient();
    }

    @Test(description = "Check create order POST request with valid data")
    public void checkCreateOrderRequestWithValidData() {
        OrderDto orderDto = OrderUtils.createOrderDto();

        Response actualResponse = storeClient.createOrder(orderDto);
        Assert.assertEquals(actualResponse.statusCode(), HttpStatus.SC_OK, "Expected 200 for successful order creation.");
        LOGGER.info("Order created successfully with id: {}", orderDto.getId());

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(actualResponse.as(OrderDto.class))
                .usingRecursiveComparison()
                .ignoringFields("shipDate")
                .isEqualTo(orderDto);
        softAssertions.assertAll();

        OrderUtils.deleteOrderWithAwait(storeClient, orderDto.getId());
    }

    @Test(description = "Check create order POST request with empty body")
    public void checkCreateOrderRequestWithEmptyBody() {
        Response actualResponse = storeClient.createOrder("");
        Assert.assertEquals(actualResponse.statusCode(), HttpStatus.SC_BAD_REQUEST, "Expected 400 for empty body request.");
        Assert.assertEquals(actualResponse.jsonPath().getString("message"), "No data");
    }

    @Test(description = "Check create order POST request Json schema")
    public void checkCreateOrderRequestJsonSchema() {
        OrderDto orderDto = OrderUtils.createOrderDto();

        Response actualResponse = storeClient.createOrder(orderDto);
        actualResponse.then().assertThat().body(matchesJsonSchemaInClasspath("api/store_order_response.json"));
    }

    @AfterMethod
    public void cleanupThreadLocal() {
        ApiHelper.clearSpecs();
    }
}
