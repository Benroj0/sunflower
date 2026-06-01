/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package upeu.edu.pe.ecommerce.infrastructure.adapter;

import org.springframework.stereotype.Repository;
import upeu.edu.pe.ecommerce.app.repository.CategoriesRepository;
import upeu.edu.pe.ecommerce.infrastructure.entity.CategoryEntity;


@Repository
public class CategoriesRepositoryImpl implements CategoriesRepository{
 private final CategoriesCrudRepository categoriesCrudRepository;

    public CategoriesRepositoryImpl(CategoriesCrudRepository categoriesCrudRepository) {
        this.categoriesCrudRepository = categoriesCrudRepository;
    }
 
 
    
    @Override
    public Iterable<CategoryEntity> getCategories() {
    return categoriesCrudRepository.findAll();
    }

    @Override
public CategoryEntity saveCategory(CategoryEntity categoryEntity) {
    return categoriesCrudRepository.save(categoryEntity);
}
    
}
