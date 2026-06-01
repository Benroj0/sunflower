/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package upeu.edu.pe.ecommerce.infrastructure.adapter;

import org.springframework.stereotype.Repository;
import upeu.edu.pe.ecommerce.app.repository.OrderProductRepository;
import upeu.edu.pe.ecommerce.infrastructure.entity.OrderEntity;
import upeu.edu.pe.ecommerce.infrastructure.entity.OrderProductEntity;
import upeu.edu.pe.ecommerce.infrastructure.entity.ProductEntity;

@Repository
public class OrderProductRepositoryImpl implements OrderProductRepository{
    
    private final OrderProductCrudRepository orderProductCrudRepository;

    public OrderProductRepositoryImpl(OrderProductCrudRepository orderProductCrudRepository) {
        this.orderProductCrudRepository = orderProductCrudRepository;
    }

    @Override
    public OrderProductEntity createOrderProduct(OrderProductEntity orderProductEntity) {
      return orderProductCrudRepository.save(orderProductEntity);
      
    }

    @Override
    public Iterable<OrderProductEntity> getOrdersProducts() {
   return orderProductCrudRepository.findAll();
    
    }

    @Override
    public Iterable<OrderProductEntity> getOrdersProductByOrder(OrderEntity orderEntity) {
      return orderProductCrudRepository.findByOrderEntity(orderEntity);
    }

    @Override
    public Iterable<OrderProductEntity> getOrdersProductByProduct(ProductEntity productEntity) {
      return orderProductCrudRepository.findByProductEntity(productEntity);
    }

    @Override
    public Iterable<OrderProductEntity> getOrdersProductByProductId(Integer productId) {
      return orderProductCrudRepository.findByProductEntity_Id(productId);
    }

}
