# PetStore API Automated Tests

This project contains automated API tests for the PetStore application, implemented with Java, TestNG, RestAssured, and Allure for reporting.

# Project Structure
The project is structured according to a modular and scalable architecture, separating configuration, API clients, data models, and test logic.

src/

├ main/java/petstore/api/config      (Configuration classes)

├ main/java/petstore/api/clients     (API clients)

├ main/java/petstore/api/dto         (Data Transfer Objects for request/response mapping)

├ main/java/petstore/api/endpoints   (Endpoint constants)

├ main/java/petstore/api/utils       (API-level utility classes)

├ main/java/utils                    (Common utility classes shared across all modules)

├ main/resources/api                 (JSON schema files for response validation)

├ test/java/petstore/api/tests       (Test classes)

└ test/resources                     (TestNG XML configurations and environment files)

This approach ensures:

* Ease of maintenance: updates to endpoints, DTOs, or specifications can be made in isolation.
* Reusability: shared configurations and helpers (e.g., ApiHelper, OrderUtils) reduce code duplication.
* Scalability: new modules, clients, or test types can be added without refactoring existing code.
* Clear separation of concerns: each package has a single responsibility, which simplifies onboarding and debugging.

# Prerequisites

Java 17+

Maven installed

Allure command-line tool installed

# Dependencies and Tooling

`RestAssured`: for HTTP request/response handling.

`TestNG`: for test execution and suite organization.

`Allure`: for test reporting.

`Log4j2`: for structured logging.

`Lombok`: to reduce boilerplate code in DTOs and utility classes (make sure your IDE has the Lombok plugin enabled and annotation processing turned on; otherwise the project won’t compile).

`Awaitility`: for handling delayed or asynchronous API responses.


# How to Run Tests

Run all tests using TestNG XML suite:

`mvn clean test`

Test suite file: `src/test/resources/petstore-api.xml`

# Generate Allure Report
Serve Allure report locally:

`allure serve target/allure-results`

This will open a browser window showing a detailed HTML report

# Running Specific Tests

Run a single test class:

`mvn clean test -Dtest=CreateOrderTests`

Run a specific test method:

`mvn clean test -Dtest=CreateOrderTests#checkCreateOrderRequestWithValidData`

# Project Configuration
**RestAssuredConfig**: Initializes base URI.

**ApiHelper**: Provides reusable request and response specifications with configurations and filters for logging.

**StoreClient**: Contains methods to interact with PetStore API endpoints.

**Thread-safe Logging**: Each test class logs independently using Log4j.

# Testing Approach
The testing approach focused on achieving functional coverage and API reliability validation rather than exhaustive negative testing, due to known backend issues (detailed below).

**Key principles:**

* Each test runs independently, with dynamic test data generation (DataGenerator).
* DTOs ensure strict type consistency for request and response payloads.
* Schema validation is used to verify response structure where applicable.
* Allure annotations provide detailed reporting for every test case.
* Awaitility is used to handle delayed backend processing (e.g., eventual consistency after POST or DELETE).
* Limited negative testing was applied to avoid false negatives caused by API inconsistencies.

# Notes About API Behavior & Vulnerabilities
**While working with the Swagger PetStore API, several instabilities and inconsistencies were discovered:**

* Unstable responses: identical GET requests occasionally return inconsistent status codes (200 or 404).
* Performance issues: endpoints (especially DELETE) respond with noticeable delay before data is updated.
* Idempotency violations: repeated identical requests may produce different results.
* Documentation mismatch: Swagger definitions don’t match actual endpoint behavior.
* Data integrity flaws: the API allows creation of multiple orders with identical IDs, violating uniqueness and RESTful design principles.
* Weak backend validation: many input checks appear to occur only on the client side, making the API overly permissive and prone to invalid data acceptance.
  
**Examples**:

* DELETE /store/order/{orderId}
  * Expected: returns 400 “Invalid ID supplied” for negative or non-integer IDs. 
  * Actual: returns 404 “Order not found” instead.
  * Additional issue: repeated DELETE calls are not idempotent
    * 1st call: returns 200 OK (order deleted successfully)
    * 2nd call: still returns 200 OK, even though the order no longer exists
    * 3rd call: finally returns 404 Not Found

* GET /store/order/{orderId}
  * Expected: returns 400 “Invalid ID supplied” for IDs <1, >10, or non-numeric.
  * Actual: returns 404 “Order not found”, or sometimes 200 “OK” if an order with that ID happens to exist.
  * Additional issue: repeated GET calls are not idempotent (occasionally return inconsistent status codes (200 or 404))

* POST /store/order
  * Expected: validate data types and reject invalid input.
  * Actual: can trigger 500 Internal Server Error when sending string values for numeric fields like id, petId, or quantity.
  
**Impact on Testing**

Because of these inconsistencies, automated tests may occasionally fail even when the test logic itself is correct - effectively exposing bugs in the API, not in the tests.
For example, transient 404 responses immediately after valid operations (especially after POST or DELETE) were observed in:

* checkGetOrderRequestWithValidId
* checkGetOrderRequestWithNonExistingId
* checkDeleteOrderRequestWithValidId

**Mitigation Measures**

To ensure test suite stability and avoid false negatives, only a limited number of negative test cases were implemented.

Additional validation or error-handling tests (e.g., for invalid data, negative IDs, or boundary conditions) were intentionally omitted because the API does not return correct or consistent status codes in such scenarios - making those tests unreliable and not meaningful for result verification.

Due to backend delays and asynchronous behavior, some tests required the use of Awaitility for controlled waiting and retry logic.
This approach was used to handle situations where the API takes several seconds to process entity creation or deletion before it becomes available or confirmed on subsequent requests (e.g., a DELETE request needing retries before the resource is fully removed).
