package upeu.edu.pe.ecommerce.bdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ProductSteps {

    @LocalServerPort
    private int port;

    private ResponseEntity<String> response;
    private RestTemplate restTemplate = new RestTemplate();

    @Given("the products API is available")
    public void the_products_api_is_available() {
        // Verificación básica
    }

    @When("I request the product catalog")
    public void i_request_the_product_catalog() {
        String url = "http://localhost:" + port + "/home";
        response = restTemplate.getForEntity(url, String.class);
    }

    @Then("the response should be successful")
    public void the_response_should_be_successful() {
        assertEquals(200, response.getStatusCode().value());
    }

    @Then("the product response status should be {int}")
    public void the_product_response_status_should_be(Integer statusCode) {
        assertEquals(statusCode, response.getStatusCode().value());
    }

    @Then("the response should not be null")
    public void the_response_should_not_be_null() {
        assertNotNull(response.getBody());
    }
}
