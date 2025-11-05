package petstore.api.config;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class ApiHelper {

    private static final ThreadLocal<RequestSpecification> REQUEST_SPEC = ThreadLocal.withInitial(() ->
            new RequestSpecBuilder()
                    .setContentType(ContentType.JSON)
                    .setAccept(ContentType.JSON)
                    .addHeader("api_key", "special-key")
                    .addFilter(new AllureRestAssured())
                    .log(LogDetail.ALL)
                    .build()
    );

    private static final ThreadLocal<ResponseSpecification> RESPONSE_SPEC = ThreadLocal.withInitial(() ->
            new ResponseSpecBuilder()
                    .expectContentType(ContentType.JSON)
                    .log(LogDetail.ALL)
                    .build()
    );

    private ApiHelper() {
    }

    public static RequestSpecification getRequestSpec() {
        return REQUEST_SPEC.get();
    }

    public static ResponseSpecification getResponseSpec() {
        return RESPONSE_SPEC.get();
    }

    public static void clearSpecs() {
        REQUEST_SPEC.remove();
        RESPONSE_SPEC.remove();
    }

}
