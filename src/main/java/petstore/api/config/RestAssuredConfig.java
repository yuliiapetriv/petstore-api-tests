package petstore.api.config;

import io.restassured.RestAssured;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeSuite;
import petstore.api.endpoints.Endpoints;

public class RestAssuredConfig {

    private static final Logger LOGGER = LogManager.getLogger(RestAssuredConfig.class);
    private static volatile boolean initialized = false;

    private RestAssuredConfig() {
    }

    @BeforeSuite(alwaysRun = true)
    public static void init() {
        if (!initialized) {
            synchronized (RestAssuredConfig.class) {
                if (!initialized) {
                    RestAssured.baseURI = Endpoints.BASE_URL;
                    LOGGER.info("RestAssured initialized. Base URI = {}", RestAssured.baseURI);
                    initialized = true;
                }
            }
        }
    }

}
