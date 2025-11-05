package petstore.api.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeSuite;
import petstore.api.config.RestAssuredConfig;

public class BaseTest {

    protected final Logger LOGGER = LogManager.getLogger(getClass());

    @BeforeSuite
    public void setup() {
        RestAssuredConfig.init();
    }
}
