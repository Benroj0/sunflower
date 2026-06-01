package upeu.edu.pe.ecommerce.infrastructure.controller;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.slf4j.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import upeu.edu.pe.ecommerce.app.repository.StockRepository;
import upeu.edu.pe.ecommerce.app.service.CategoriesServices;
import upeu.edu.pe.ecommerce.app.service.ProductService;
import upeu.edu.pe.ecommerce.infrastructure.entity.*;

@Controller
@RequestMapping("/admin/products")
public class ProductController {

    private final ProductService productService;
    private final CategoriesServices categoriesServices;
    private final StockRepository stockRepository;
    private final Logger log = LoggerFactory.getLogger(ProductController.class);

    public ProductController(ProductService productService, CategoriesServices categoriesServices, StockRepository stockRepository) {
        this.productService = productService;
        this.categoriesServices = categoriesServices;
        this.stockRepository = stockRepository;
    }

    // --- MÉTODOS DE ADMIN ---

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("listCategories", categoriesServices.getCategories());
        return "admin/products/create";
    }

    @PostMapping("/save-product")
    public String saveProduct(ProductEntity product, 
                              @RequestParam("category_id") Integer category_id, 
                              @RequestParam("stockInicial") Integer stockInicial,
                              @RequestParam("img") MultipartFile multipartFile,
                              HttpSession httpSession) throws IOException {
        
        CategoryEntity cat = new CategoryEntity();
        cat.setId(category_id);
        product.setCategoryEntity(cat);
        
        productService.saveProduct(product, multipartFile, httpSession);
        
        // Crear stock inicial solo al crear producto
        StockEntity stock = new StockEntity();
        stock.setDescripcion("Inventario Inicial");
        stock.setEntradas(stockInicial);
        stock.setSalidas(0);
        stock.setBalance(stockInicial);
        stock.setProductEntity(product);
        stockRepository.saveStock(stock);
        
        return "redirect:/admin/products/show";
    }

    @GetMapping("/show")
    public String showProduct(Model model, HttpSession httpSession) {
        Integer idUser = Integer.parseInt(httpSession.getAttribute("iduser").toString());
        UserEntity user = new UserEntity();
        user.setId(idUser);
        model.addAttribute("products", productService.getProductsByUser(user));
        return "admin/products/show";
    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable Integer id, Model model) {
        ProductEntity product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("listCategories", categoriesServices.getCategories());
        return "admin/products/edit";
    }

    // Método corregido: sin stockInicial para evitar Whitelabel en edición
    @PostMapping("/update-product")
    public String updateProduct(ProductEntity product, 
                                @RequestParam("category_id") Integer category_id, 
                                @RequestParam("img") org.springframework.web.multipart.MultipartFile multipartFile,
                                HttpSession httpSession) throws IOException {
        CategoryEntity cat = new CategoryEntity();
        cat.setId(category_id);
        product.setCategoryEntity(cat);
        productService.saveProduct(product, multipartFile, httpSession); 
        return "redirect:/admin/products/show";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Integer id) {
        productService.deleteProductById(id);
        return "redirect:/admin/products/show";
    }

    // --- MÉTODO DE USUARIO (DETALLE) ---

    @GetMapping("/home/product-detail/{id}")
    public String productDetail(@PathVariable Integer id, Model model) {
        ProductEntity product = productService.getProductById(id);
        
        // Buscamos el último stock del producto
        StockEntity ultimoStock = stockRepository.findTopByProductEntityOrderByIdDesc(product);
        int stockActual = (ultimoStock != null) ? ultimoStock.getBalance() : 0;
        
        model.addAttribute("product", product);
        model.addAttribute("stock", stockActual);
        return "user/productdetail";
    }
}