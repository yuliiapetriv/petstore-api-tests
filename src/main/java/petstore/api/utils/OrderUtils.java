package petstore.api.utils;

import io.qameta.allure.Step;
import org.apache.http.HttpStatus;
import petstore.api.clients.StoreClient;
import petstore.api.dto.OrderDto;

import java.time.Duration;

import static org.awaitility.Awaitility.await;

public class OrderUtils {

    @Step("Create order DTO")
    public static OrderDto createOrderDto() {
        return OrderDto.builder()
                .id(utils.DataGenerator.generateRandomId())
                .petId(utils.DataGenerator.generateRandomId())
                .quantity(1)
                .shipDate(utils.DataGenerator.generateCurrentIsoDate())
                .status("placed")
                .complete(false)
                .build();
    }

    @Step("Delete order")
    public static void deleteOrderWithAwait(StoreClient storeClient, int orderId) {
        await()
                .atMost(Duration.ofSeconds(20))
                .pollInterval(Duration.ofSeconds(3))
                .ignoreExceptions()
                .until(() -> storeClient.deleteOrder(orderId).statusCode() == HttpStatus.SC_OK);
    }
}
