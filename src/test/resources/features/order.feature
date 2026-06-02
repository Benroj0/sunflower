Feature: Flujo Completo de Pedidos en la Tienda Sunflower

  Scenario: Un cliente entra a la tienda, agrega un producto al carrito y realiza el pedido con éxito
    Given the products API is available
    And the ecommerce API is up
    When I request the product catalog
    And I send a POST request to add a product with id 1, name "Planta Sunflower Premium", quantity 2, and price 45.0
    And I submit the checkout request to finalize the order
    Then the response status should be 200
    And the order should be successfully recorded in the system