package upeu.edu.pe.ecommerce.infrastructure.controller;

import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Map;
import org.slf4j.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import upeu.edu.pe.ecommerce.app.service.CartServices;

@Controller
@RequestMapping("user/cart")
public class CartController {
    private final CartServices cartServices;
    private final Logger log = LoggerFactory.getLogger(CartController.class);

    public CartController(CartServices cartServices) {
        this.cartServices = cartServices;
    }

    @PostMapping("/add-product")
    @ResponseBody
    public Map<String, Object> addProductCart(@RequestParam Integer idProduct,
                                 @RequestParam String nameProduct,
                                 @RequestParam Integer quantity,
                                 @RequestParam BigDecimal price){
        boolean isNew = cartServices.addItemCart(idProduct, nameProduct, quantity, price);
        int cartCount = cartServices.getItemCarts() != null ? cartServices.getItemCarts().size() : 0;
        return Map.of("cartCount", cartCount, "isNew", isNew);
    }

    @PostMapping("/update-quantity")
    public String updateQuantity(@RequestParam Integer idProduct, @RequestParam Integer quantity) {
        cartServices.updateQuantity(idProduct, quantity);
        return "redirect:/user/cart/get-cart";
    }

    @GetMapping("/get-cart")
    public String getCart(Model model, HttpSession httpSession){
        model.addAttribute("cart", cartServices.getItemCarts());
        model.addAttribute("total", cartServices.getTotalCart());
        model.addAttribute("cartCount", cartServices.getItemCarts() != null ? cartServices.getItemCarts().size() : 0);
        try {
            model.addAttribute("nombre", httpSession.getAttribute("name").toString());
            model.addAttribute("id", httpSession.getAttribute("iduser").toString());
        } catch (Exception e) {}
        return "user/cart/cart";
    }

    @GetMapping("/delete-item-cart/{id}")
    public String deleteItemCart(@PathVariable Integer id){
        cartServices.removeItemCart(id);
        return "redirect:/user/cart/get-cart";
    }
}