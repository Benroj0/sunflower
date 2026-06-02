package upeu.edu.pe.ecommerce.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import jakarta.servlet.http.HttpSession;
import upeu.edu.pe.ecommerce.app.repository.ProductRepository;
import upeu.edu.pe.ecommerce.app.service.ProductService;
import upeu.edu.pe.ecommerce.app.service.UploadFile;
import upeu.edu.pe.ecommerce.infrastructure.entity.ProductEntity;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock private ProductRepository productRepository;
    @Mock private UploadFile uploadFile;
    @Mock private HttpSession httpSession;

    @InjectMocks
    private ProductService productService;

    @Test
    void testSaveProductNewWithImage() throws IOException {
        ProductEntity product = new ProductEntity();
        // Agregamos bytes reales al archivo simulado
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "datos_de_imagen".getBytes());
        
        when(httpSession.getAttribute("iduser")).thenReturn("1");
        when(uploadFile.upload(file)).thenReturn("image.jpg");
        when(productRepository.saveProduct(any(ProductEntity.class))).thenAnswer(i -> i.getArgument(0));

        ProductEntity result = productService.saveProduct(product, file, httpSession);

        assertNotNull(result);
        assertEquals("image.jpg", result.getImage());
    }

    @Test
    void testSaveProductUpdateNoNewImage() throws IOException {
        ProductEntity product = new ProductEntity();
        product.setId(1);
        product.setImage("old.jpg");
        
        when(productRepository.getProductById(1)).thenReturn(product);
        when(productRepository.saveProduct(any(ProductEntity.class))).thenAnswer(i -> i.getArgument(0));

        ProductEntity result = productService.saveProduct(product, null, httpSession);

        assertEquals("old.jpg", result.getImage());
    }

    @Test
    void testSaveProductUpdateWithNewImage() throws IOException {
        // GIVEN
        ProductEntity inputProduct = new ProductEntity();
        inputProduct.setId(1);
        
        ProductEntity dbProduct = new ProductEntity();
        dbProduct.setId(1);
        dbProduct.setImage("old.jpg");
        
        // SOLUCIÓN: Agregamos bytes reales para que isEmpty() sea FALSE y entre al flujo correcto
        MockMultipartFile newFile = new MockMultipartFile("img", "new.jpg", "image/jpeg", "contenido_de_imagen_nueva".getBytes());
        
        when(productRepository.getProductById(1)).thenReturn(dbProduct);
        when(uploadFile.upload(newFile)).thenReturn("new.jpg");
        when(productRepository.saveProduct(any(ProductEntity.class))).thenAnswer(i -> i.getArgument(0));

        // WHEN
        ProductEntity result = productService.saveProduct(inputProduct, newFile, httpSession);

        // THEN
        assertEquals("new.jpg", result.getImage());
        verify(uploadFile).delete("old.jpg");
        verify(uploadFile).upload(newFile);
    }
}