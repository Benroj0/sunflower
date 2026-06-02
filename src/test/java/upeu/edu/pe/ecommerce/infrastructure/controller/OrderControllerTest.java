package upeu.edu.pe.ecommerce.infrastructure.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import upeu.edu.pe.ecommerce.app.service.*;
import upeu.edu.pe.ecommerce.infrastructure.entity.UserEntity;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock private CartServices cartServices;
    @Mock private UserService userService;
    @Mock private Model model;
    @Mock private HttpSession httpSession;

    @Test
    void testShowSumaryOrder() {
        // GIVEN
        when(httpSession.getAttribute("iduser")).thenReturn("1");
        when(userService.findById(1)).thenReturn(new UserEntity());
        when(cartServices.getItemCarts()).thenReturn(new ArrayList<>());
        when(cartServices.getTotalCart()).thenReturn(BigDecimal.ZERO);

        // WHEN
        String view = orderController.showSumaryOrder(model, httpSession);

        // THEN
        assertEquals("user/summaryorder", view);
        verify(model, atLeastOnce()).addAttribute(eq("total"), any());
    }
}