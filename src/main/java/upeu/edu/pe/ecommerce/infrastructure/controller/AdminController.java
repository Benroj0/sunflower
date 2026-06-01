package upeu.edu.pe.ecommerce.infrastructure.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import upeu.edu.pe.ecommerce.app.service.CategoriesServices;
import upeu.edu.pe.ecommerce.app.service.OrderProductService;
import upeu.edu.pe.ecommerce.app.service.StockService;
import upeu.edu.pe.ecommerce.app.service.ProductService;
import upeu.edu.pe.ecommerce.infrastructure.entity.StockEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import upeu.edu.pe.ecommerce.infrastructure.entity.CategoryEntity;
import upeu.edu.pe.ecommerce.infrastructure.entity.ProductEntity;


@Controller
@RequestMapping("/admin")
public class AdminController {
    
    private final ProductService productService;
    private final CategoriesServices categoriesServices;
    private final OrderProductService orderProductService;
    private final StockService stockService;

    // Inyectamos los servicios en el constructor
    public AdminController(ProductService productService, CategoriesServices categoriesServices, OrderProductService orderProductService, StockService stockService) {
        this.productService = productService;
        this.categoriesServices = categoriesServices;
        this.orderProductService = orderProductService;
        this.stockService = stockService;
    }
    
    @GetMapping
    public String home(Model model, HttpSession httpSession){
        Iterable<ProductEntity> products = productService.getProducts();
        model.addAttribute("products",products);
        addAdminSession(model, httpSession);
        return "admin/home";
    }

    // Muestra la lista de categorías y el formulario de creación en la misma pantalla
    @GetMapping("/categories")
    public String categoriesHome(Model model, HttpSession httpSession) {
        Iterable<CategoryEntity> categories = categoriesServices.getCategories();
        model.addAttribute("categories", categories);
        addAdminSession(model, httpSession);
        return "admin/categories/show";
    }

    // Procesa el guardado de la nueva categoría
    @PostMapping("/categories/save")
    public String saveCategory(CategoryEntity category) {
        category.setStatus("1"); // Seteamos por defecto activo
        categoriesServices.saveCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/product-orders/{id}")
    public String viewProductOrders(@PathVariable Integer id, Model model, HttpSession httpSession) {
        ProductEntity product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/admin";
        }
        model.addAttribute("product", product);
        model.addAttribute("orders", orderProductService.getOrdersProductByProductId(id));
        addAdminSession(model, httpSession);
        return "admin/products/product_orders";
    }

    @GetMapping("/stock")
    public String stockOverview(Model model, HttpSession httpSession) {
        Iterable<ProductEntity> products = productService.getProducts();
        Map<Integer, List<StockEntity>> stockMap = new HashMap<>();
        for (ProductEntity p : products) {
            List<StockEntity> stocks = stockService.getStockByProductEntity(p);
            stockMap.put(p.getId(), stocks != null ? stocks : List.of());
        }
        model.addAttribute("products", products);
        model.addAttribute("stockMap", stockMap);
        addAdminSession(model, httpSession);
        return "admin/stock/show";
    }

    private void addAdminSession(Model model, HttpSession httpSession) {
        try {
            model.addAttribute("nombre", httpSession.getAttribute("name").toString());
        } catch (Exception e) {
        }
    }
}