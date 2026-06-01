package upeu.edu.pe.ecommerce.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import upeu.edu.pe.ecommerce.app.service.CartServices;
import upeu.edu.pe.ecommerce.app.domain.ItemCart;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServicesTest {

    @InjectMocks
    private CartServices cartServices;

    @Test
    void testAddNewItemToCart() {
        // GIVEN: Un carrito vacío
        assertTrue(cartServices.getItemCarts() == null || cartServices.getItemCarts().isEmpty());

        // WHEN: Agregamos un producto nuevo
        boolean isNew = cartServices.addItemCart(1, "Producto Test", 2, BigDecimal.valueOf(100.0));

        // THEN: El producto debe ser nuevo (isNew = true)
        assertTrue(isNew);
        assertNotNull(cartServices.getItemCarts());
        assertEquals(1, cartServices.getItemCarts().size());
    }

    @Test
    void testAddExistingItemToCart() {
        // GIVEN: Agregamos un producto
        cartServices.addItemCart(1, "Producto Test", 2, BigDecimal.valueOf(100.0));

        // WHEN: Intentamos agregar el mismo producto
        boolean isNew = cartServices.addItemCart(1, "Producto Test", 3, BigDecimal.valueOf(100.0));

        // THEN: Debe indicar que no es nuevo (isNew = false)
        assertFalse(isNew);
        // Y debe haber solo 1 producto en el carrito
        assertEquals(1, cartServices.getItemCarts().size());
    }

    @Test
    void testGetTotalCart() {
        // GIVEN: Agregamos múltiples productos
        cartServices.addItemCart(1, "Producto 1", 2, BigDecimal.valueOf(100.0));
        cartServices.addItemCart(2, "Producto 2", 1, BigDecimal.valueOf(50.0));

        // WHEN: Calculamos el total
        BigDecimal total = cartServices.getTotalCart();

        // THEN: El total debe ser 250 (100*2 + 50*1)
        assertEquals(BigDecimal.valueOf(250.0), total);
    }

    @Test
    void testRemoveItemCart() {
        // GIVEN: Agregamos dos productos
        cartServices.addItemCart(1, "Producto 1", 2, BigDecimal.valueOf(100.0));
        cartServices.addItemCart(2, "Producto 2", 1, BigDecimal.valueOf(50.0));
        assertEquals(2, cartServices.getItemCarts().size());

        // WHEN: Removemos uno
        cartServices.removeItemCart(1);

        // THEN: Debe quedar solo 1 producto
        assertEquals(1, cartServices.getItemCarts().size());
    }

    @Test
    void testUpdateQuantity() {
        // GIVEN: Agregamos un producto
        cartServices.addItemCart(1, "Producto Test", 2, BigDecimal.valueOf(100.0));

        // WHEN: Actualizamos la cantidad a 5
        cartServices.updateQuantity(1, 5);

        // THEN: La cantidad debe haber cambiado
        ItemCart item = cartServices.getItemCarts().get(0);
        assertEquals(5, item.getQuantity());
    }

    @Test
    void testRemoveAllItems() {
        // GIVEN: Agregamos múltiples productos
        cartServices.addItemCart(1, "Producto 1", 2, BigDecimal.valueOf(100.0));
        cartServices.addItemCart(2, "Producto 2", 1, BigDecimal.valueOf(50.0));

        // WHEN: Limpiamos el carrito
        cartServices.removeAllItemsCart();

        // THEN: El carrito debe estar vacío
        assertTrue(cartServices.getItemCarts() == null || cartServices.getItemCarts().isEmpty());
    }
}
