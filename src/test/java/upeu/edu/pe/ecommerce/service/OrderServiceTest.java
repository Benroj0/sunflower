package upeu.edu.pe.ecommerce.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// Cambia estas rutas por los imports reales de tus clases
import upeu.edu.pe.ecommerce.app.repository.OrderRepository;
import upeu.edu.pe.ecommerce.app.service.OrderService;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository; // Simula el repositorio en memoria

    @InjectMocks
    private OrderService orderService; // Inyecta el simulador en tu servicio

    @Test
    public void cuandoSeCalculaTotal_debeSerCorrecto() {
        // Tu primera prueba unitaria aquí (No tocará MySQL)
        assertTrue(true); 
    }
}