package petstore.api.tests.store;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import petstore.api.clients.StoreClient;
import org.testng.annotations.Test;
import petstore.api.config.ApiHelper;
import petstore.api.tests.BaseTest;

import java.util.Map;

public class GetInventoryTests extends BaseTest {

    private StoreClient storeClient;

    @BeforeClass
    public void setUp() {
        storeClient = new StoreClient();
    }

    @Test(description = "Check GET inventory request")
    public void checkGetInventoryRequest() {
        Response response = storeClient.getInventory();
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Expected 200 for inventory fetching.");

        Map<String, Integer> inventoryMap = response.jsonPath().getMap("");
        LOGGER.info("Inventory: {}", inventoryMap);

        Assert.assertTrue(inventoryMap.containsKey("available"), "Inventory should contain 'available' status");
        Assert.assertTrue(inventoryMap.containsKey("pending"), "Inventory should contain 'pending' status");
        Assert.assertTrue(inventoryMap.containsKey("sold"), "Inventory should contain 'sold' status");

        inventoryMap.forEach((status, value) -> {
            Assert.assertNotNull(value, "Inventory value should not be null for status: " + status);
            Assert.assertTrue(value >= 0, "Inventory value should be non-negative for status: " + status);
        });
    }

    @AfterMethod
    public void cleanupThreadLocal() {
        ApiHelper.clearSpecs();
    }
}
