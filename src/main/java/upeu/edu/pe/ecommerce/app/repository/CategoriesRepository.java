package upeu.edu.pe.ecommerce.app.repository;

import upeu.edu.pe.ecommerce.infrastructure.entity.CategoryEntity;

public interface CategoriesRepository {
    Iterable<CategoryEntity> getCategories();
    
    // Agrega esta línea para definir el método de guardado:
    CategoryEntity saveCategory(CategoryEntity categoryEntity);
}