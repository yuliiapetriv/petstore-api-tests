package petstore.api.endpoints;

public interface Endpoints {

    String BASE_URL = "https://petstore.swagger.io/v2";
    String STORE_BASE = BASE_URL + "/store";
    String ORDER = STORE_BASE + "/order";
    String INVENTORY = STORE_BASE + "/inventory";
}
