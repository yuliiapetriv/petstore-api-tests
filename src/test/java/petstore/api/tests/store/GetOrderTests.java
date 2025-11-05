package petstore.api.tests.store;

import io.qameta.allure.Flaky;
import io.qameta.allure.Issue;
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

@Issue("Unstable due to API inconsistencies: Intermittent 404 on GET /order/{id}")
public class GetOrderTests extends BaseTest {

    private StoreClient storeClient;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        storeClient = new StoreClient();
    }

    @Flaky
    @Test(description = "Check GET order request with valid ID")
    public void checkGetOrderRequestWithValidId() {
        OrderDto orderDto = OrderUtils.createOrderDto();

        Response createOrderResponse = storeClient.createOrder(orderDto);
        Assert.assertEquals(createOrderResponse.statusCode(), HttpStatus.SC_OK, "Expected 200 for successful order creation.");
        LOGGER.info("Order created successfully with id: {}", orderDto.getId());

        Response getOrderByIdResponse = storeClient.getOrderById(orderDto.getId());
        Assert.assertEquals(getOrderByIdResponse.statusCode(), HttpStatus.SC_OK, "Expected 200 for successful existing order fetching.");
        LOGGER.info("Order with id {} successfully fetched", orderDto.getId());

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(getOrderByIdResponse.as(OrderDto.class))
                .usingRecursiveComparison()
                .ignoringFields("shipDate")
                .isEqualTo(orderDto);
        softAssertions.assertAll();

        OrderUtils.deleteOrderWithAwait(storeClient, orderDto.getId());
    }

    @Flaky
    @Test(description = "Check GET order request with non-existing ID")
    public void checkGetOrderRequestWithNonExistingId() {
        OrderDto orderDto = OrderUtils.createOrderDto();

        Response createOrderResponse = storeClient.createOrder(orderDto);
        Assert.assertEquals(createOrderResponse.statusCode(), HttpStatus.SC_OK, "Expected 200 for successful order creation.");
        LOGGER.info("Order created successfully with id: {}", orderDto.getId());

        OrderUtils.deleteOrderWithAwait(storeClient, orderDto.getId());
        LOGGER.info("Order with id {} successfully deleted", orderDto.getId());

        Response getOrderByIdResponse = storeClient.getOrderById(orderDto.getId());
        Assert.assertEquals(getOrderByIdResponse.statusCode(), HttpStatus.SC_NOT_FOUND, "Expected 404 for non-existing order fetching.");
    }

    @AfterMethod
    public void cleanupThreadLocal() {
        ApiHelper.clearSpecs();
    }
}
