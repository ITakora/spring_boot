package com.ecommerce.project.controller;


import com.ecommerce.project.model.Category;
import com.ecommerce.project.sevice.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;



    @GetMapping("/public/categories")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<String> addCategory(@RequestBody Category category) {
        categoryService.createCategory(category);
        return new ResponseEntity<>("Category added successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<String> deleteCategory (@PathVariable Long id) {
        try{
            String status = categoryService.deleteCategory(id);
            return  ResponseEntity.ok(status);

        } catch(ResponseStatusException e){
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @PutMapping(" /public/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@RequestBody Category category, @PathVariable Long categoryId) {
        try {

            Category newCategory = categoryService.updateCategory(category, categoryId);
            return  new ResponseEntity<>("Category with category id : " + categoryId + " Successfully", HttpStatus.OK);

        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }


}
