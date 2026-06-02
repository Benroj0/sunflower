package upeu.edu.pe.ecommerce.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import upeu.edu.pe.ecommerce.app.repository.CategoriesRepository;
import upeu.edu.pe.ecommerce.app.service.CategoriesServices;
import upeu.edu.pe.ecommerce.infrastructure.entity.CategoryEntity;

@ExtendWith(MockitoExtension.class)
public class CategoriesServicesTest {

    @Mock private CategoriesRepository categoriesRepository;
    @InjectMocks private CategoriesServices categoriesServices;

    @Test
    void testGetCategories() {
        when(categoriesRepository.getCategories()).thenReturn(Collections.emptyList());
        Iterable<CategoryEntity> result = categoriesServices.getCategories();
        assertNotNull(result);
        verify(categoriesRepository).getCategories();
    }

    @Test
    void testSaveCategory() {
        CategoryEntity category = new CategoryEntity();
        when(categoriesRepository.saveCategory(any(CategoryEntity.class))).thenReturn(category);
        CategoryEntity result = categoriesServices.saveCategory(category);
        assertNotNull(result);
        verify(categoriesRepository).saveCategory(category);
    }
}