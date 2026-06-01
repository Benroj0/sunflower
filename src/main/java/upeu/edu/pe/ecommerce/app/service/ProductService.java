/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package upeu.edu.pe.ecommerce.app.service;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.web.multipart.MultipartFile;
import upeu.edu.pe.ecommerce.app.repository.ProductRepository;
import upeu.edu.pe.ecommerce.infrastructure.entity.ProductEntity;
import upeu.edu.pe.ecommerce.infrastructure.entity.UserEntity;
import org.slf4j.*;

public class ProductService {

    private final ProductRepository productRepository;
    private final UploadFile uploadFile;
    private final Logger log = LoggerFactory.getLogger(ProductService.class);

    public ProductService(ProductRepository productRepository, UploadFile uploadFile) {
        this.productRepository = productRepository;
        this.uploadFile = uploadFile;
    }

    public Iterable<ProductEntity> getProducts() {
        return productRepository.getProducts();
    }

    public Iterable<ProductEntity> getProductsByUser(UserEntity userEntity) {
        return productRepository.getProductsByUser(userEntity);
    }

    public ProductEntity getProductById(Integer id) {
        return productRepository.getProductById(id);
    }

    public ProductEntity saveProduct(ProductEntity productEntity, MultipartFile multipartFile, HttpSession httpSession) throws IOException {
        
        // 1. Lógica para crear un nuevo producto
        if (productEntity.getId() == null) {
            UserEntity user = new UserEntity();
            
            // Validación de seguridad para evitar el NullPointerException en la sesión
            Object idUserAttribute = httpSession.getAttribute("iduser");
            if (idUserAttribute != null) {
                user.setId(Integer.parseInt(idUserAttribute.toString()));
            } else {
                log.warn("⚠️ ¡Sesión vacía! Asignando ID de usuario '1' por defecto para pruebas.");
                user.setId(1); // Modifica este ID si tu usuario administrador en la BD tiene otro número
            }
            
            productEntity.setDateCreated(LocalDateTime.now());
            productEntity.setDateUpdated(LocalDateTime.now());
            productEntity.setUserEntity(user);
            
            // Sube y guarda el nombre de la imagen procesada
            productEntity.setImage(uploadFile.upload(multipartFile));
            return productRepository.saveProduct(productEntity);
            
        } else {
            // 2. Lógica para actualizar un producto existente
            ProductEntity productDB = productRepository.getProductById(productEntity.getId());
            log.info("product {}", productDB);

            // Actualizar la imagen del producto convenientemente
            if (multipartFile == null || multipartFile.isEmpty()) {
                productEntity.setImage(productDB.getImage());
            } else {
                if (!productDB.getImage().equals("default.jpg") && productDB.getImage() != null) {
                    uploadFile.delete(productDB.getImage());
                }
                productEntity.setImage(uploadFile.upload(multipartFile));
            }

            productEntity.setCode(productDB.getCode());
            productEntity.setUserEntity(productDB.getUserEntity());
            productEntity.setDateCreated(productDB.getDateCreated());
            productEntity.setDateUpdated(LocalDateTime.now());
            return productRepository.saveProduct(productEntity);
        }
    }

    public boolean deleteProductById(Integer id) {
        return productRepository.deleteProductById(id);
    }
}