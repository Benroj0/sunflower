/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package upeu.edu.pe.ecommerce.app.repository;

import upeu.edu.pe.ecommerce.infrastructure.entity.OrderEntity;
import upeu.edu.pe.ecommerce.infrastructure.entity.OrderProductEntity;
import upeu.edu.pe.ecommerce.infrastructure.entity.ProductEntity;


public interface OrderProductRepository {

    public OrderProductEntity createOrderProduct(OrderProductEntity orderProductEntity);
    public Iterable<OrderProductEntity> getOrdersProducts();
    public Iterable<OrderProductEntity> getOrdersProductByOrder(OrderEntity orderEntity);
    public Iterable<OrderProductEntity> getOrdersProductByProduct(ProductEntity productEntity);
    public Iterable<OrderProductEntity> getOrdersProductByProductId(Integer productId);

}
