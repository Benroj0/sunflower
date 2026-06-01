Feature: Shopping Cart Management
  Scenario: Add a new product to the cart
    Given the ecommerce API is up
    When I send a POST request to add a product with id 1, name "Sunflower Seed", quantity 2, and price 25.5
    Then the response status should be 200
    And the response should contain cartCount greater than 0

  Scenario: Verify cart item count after adding product
    Given the ecommerce API is up
    When I send a POST request to add a product with id 2, name "Organic Flower", quantity 1, and price 45.0
    Then the response status should be 200
