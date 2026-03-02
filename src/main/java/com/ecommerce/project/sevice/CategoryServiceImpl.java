package com.ecommerce.project.sevice;

import com.ecommerce.project.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private List<Category> categories = new ArrayList<>();
    private Long nextId = 1L;


    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        category.setCategoryId(nextId++);
            categories.add(category);
    }

    @Override
    public String deleteCategory(Long id) {
        Category category = categories.stream().filter(c -> c.getCategoryId().equals(id)).findFirst().
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        categories.remove(category);
        return "category with category ID :"+ id +" has been deleted";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
      Optional<Category> optionalCategory = categories.stream().filter(c -> c.getCategoryId().equals(categoryId)).findFirst();

        if (optionalCategory.isPresent()){
            Category categoryToUpdate = optionalCategory.get();
            categoryToUpdate.setCategoryName(category.getCategoryName());
            return  categoryToUpdate;
        } else  {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }

    }
}
