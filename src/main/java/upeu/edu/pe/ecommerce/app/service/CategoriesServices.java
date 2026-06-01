package upeu.edu.pe.ecommerce.app.service;

import upeu.edu.pe.ecommerce.app.repository.CategoriesRepository;
import upeu.edu.pe.ecommerce.infrastructure.entity.CategoryEntity;

public class CategoriesServices {

    private final CategoriesRepository categoriesRepository;

    public CategoriesServices(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public Iterable<CategoryEntity> getCategories() {
        return categoriesRepository.getCategories();
    }

    // Nuevo método para guardar categorías en la Base de Datos
    public CategoryEntity saveCategory(CategoryEntity categoryEntity) {
        return categoriesRepository.saveCategory(categoryEntity);
    }
}