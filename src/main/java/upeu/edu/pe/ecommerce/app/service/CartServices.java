package upeu.edu.pe.ecommerce.app.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import upeu.edu.pe.ecommerce.app.domain.ItemCart;

public class CartServices {

    private List<ItemCart> itemCarts;
    private HashMap<Integer, ItemCart> itemHashMap;

    public CartServices() {
        this.itemHashMap = new HashMap<>();
        this.itemCarts = new ArrayList<>();
    }

    public boolean addItemCart(Integer idProducto, String nameProduct, Integer quantity, BigDecimal price) {
        boolean isNew = !itemHashMap.containsKey(idProducto);
        ItemCart itemCart = new ItemCart(idProducto, nameProduct, quantity, price);
        itemHashMap.put(itemCart.getIdProduct(), itemCart);
        fillList();
        return isNew;
    }

    // --- NUEVO MÉTODO PARA ACTUALIZAR ---
    public void updateQuantity(Integer idProduct, Integer quantity) {
        if (itemHashMap.containsKey(idProduct)) {
            ItemCart item = itemHashMap.get(idProduct);
            item.setQuantity(quantity); // Asegúrate que tu clase ItemCart tenga este setter
            fillList();
        }
    }

    public BigDecimal getTotalCart() {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemCart itemCart : itemCarts) {
            total = total.add(itemCart.getTotalPriceItem());
        }
        return total;
    }

    public void removeItemCart(Integer idProduct) {
        itemHashMap.remove(idProduct);
        fillList();
    }

    public void removeAllItemsCart() {
        itemHashMap.clear();
        itemCarts.clear();
    }

    private void fillList() {
        itemCarts.clear();
        itemHashMap.forEach((k, v) -> itemCarts.add(v));
    }

    public List<ItemCart> getItemCarts(){
        return itemCarts;
    }
}