package upeu.edu.pe.ecommerce.infrastructure.controller;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import upeu.edu.pe.ecommerce.app.service.CartServices;
import upeu.edu.pe.ecommerce.app.service.ProductService;
import upeu.edu.pe.ecommerce.app.service.StockService;
import upeu.edu.pe.ecommerce.infrastructure.entity.ProductEntity;
import upeu.edu.pe.ecommerce.infrastructure.entity.StockEntity;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final ProductService productService;
    private final StockService stockService;
    private final CartServices cartServices;
    private final Logger log = LoggerFactory.getLogger(HomeController.class);

    public HomeController(ProductService productService, StockService stockService, CartServices cartServices) {
        this.productService = productService;
        this.stockService = stockService;
        this.cartServices = cartServices;
    }

    @GetMapping
    public String home(Model model, HttpSession httpSession) {
        Iterable<ProductEntity> products = productService.getProducts();
        model.addAttribute("products", products);
        setCommonAttributes(model, httpSession);
        return "home";
    }

    @GetMapping("/product-detail/{id}")
    public String productDetail(@PathVariable Integer id, Model model, HttpSession httpSession) {
        List<StockEntity> stocks = stockService.getStockByProductEntity(productService.getProductById(id));
        
        // CORRECCIÓN: Evitar IndexOutOfBoundsException
        int lastBalance = 0;
        if (stocks != null && !stocks.isEmpty()) {
            lastBalance = stocks.get(stocks.size() - 1).getBalance();
        }

        log.info("Id product: {}", id);
        log.info("stock size: {}", (stocks != null ? stocks.size() : 0));

        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("stock", lastBalance);
        setCommonAttributes(model, httpSession);
        return "user/productdetail";
    }

    @GetMapping("/nosotros")
    public String about(Model model, HttpSession httpSession) {
        setCommonAttributes(model, httpSession);
        return "user/nosotros";
    }

    @GetMapping("/contacto")
    public String contact(Model model, HttpSession httpSession) {
        setCommonAttributes(model, httpSession);
        return "user/contacto";
    }

    @GetMapping("/ubicacion")
    public String location(Model model, HttpSession httpSession) {
        setCommonAttributes(model, httpSession);
        return "user/ubicacion";
    }

    private void setCommonAttributes(Model model, HttpSession httpSession) {
        try {
            model.addAttribute("nombre", httpSession.getAttribute("name").toString());
            model.addAttribute("id", httpSession.getAttribute("iduser").toString());
        } catch (Exception e) {
        }
        model.addAttribute("cartCount", cartServices.getItemCarts() != null ? cartServices.getItemCarts().size() : 0);
    }
}