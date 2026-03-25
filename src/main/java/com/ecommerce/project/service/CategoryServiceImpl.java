package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {



    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize) {

        List<Category> categories;

        if (pageNumber == null || pageNumber <= 0) {
             categories = categoryRepository.findAll();
        } else  {

            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Category> categoryPage = categoryRepository.findAll(pageable);

            categories = categoryPage.getContent();
        }


        if (categories.isEmpty()) {
            throw new APIException("No categories created");
        }

        List<CategoryDTO> categoryDTOS = categories.stream().map(category -> modelMapper.map(category, CategoryDTO.class)).toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);

        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category categoryFromDB = categoryRepository.findByCategoryName(category.getCategoryName().toLowerCase());

        if (categoryFromDB != null) {
            throw new APIException("Category already exists");
        }

        Category savedCategory = categoryRepository.save(category);

        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long id) {

        Category category = categoryRepository.findById(id).orElseThrow(() -> new  ResourceNotFoundException("Category", "id", id));

        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO category, Long categoryId) {



        Optional<Category> categories = categoryRepository.findById(categoryId);

        categories.orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        category.setCategoryId(categoryId);
        Category toCategory = modelMapper.map(category, Category.class);
         Category savedCategory = categoryRepository.save(toCategory);

         return modelMapper.map(savedCategory, CategoryDTO.class);

    }
}
