Feature: Product Catalog
  Scenario: Access the product catalog
    Given the products API is available
    When I request the product catalog
    Then the response should be successful
    And the response should not be null

  Scenario: Verify product data is available
    Given the products API is available
    When I request the product catalog
    Then the product response status should be 200
