package upeu.edu.pe.ecommerce.bdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Map;

public class CartSteps {

    @LocalServerPort
    private int port;

    // Compartido de forma estática para que OrderSteps pueda actualizarlo al hacer Checkout
    public static ResponseEntity<Map> sharedResponse;
    private RestTemplate restTemplate = new RestTemplate();

    @Given("the ecommerce API is up")
    public void the_ecommerce_api_is_up() {
        // Verificación de disponibilidad de la API
    }

    @When("I send a POST request to add a product with id {int}, name {string}, quantity {int}, and price {double}")
    public void i_send_post_request_to_add_product(Integer id, String name, Integer quantity, Double price) {
        String url = "http://localhost:" + port + "/user/cart/add-product?idProduct=" + id 
                     + "&nameProduct=" + name + "&quantity=" + quantity + "&price=" + price;
        sharedResponse = restTemplate.postForEntity(url, null, Map.class);
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(Integer statusCode) {
        assertEquals(statusCode, sharedResponse.getStatusCode().value());
    }

    @Then("the response should contain cartCount greater than {int}")
    public void the_response_should_contain_cart_count(Integer minCount) {
        Map<String, Object> body = sharedResponse.getBody();
        if (body != null && body.containsKey("cartCount")) {
            Integer cartCount = (Integer) body.get("cartCount");
            assertEquals(true, cartCount > minCount);
        }
    }
}