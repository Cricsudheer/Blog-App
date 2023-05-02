package com.springboot.blog.controller;

import com.springboot.blog.entity.Category;
import com.springboot.blog.payload.CategoryDto;
import com.springboot.blog.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/categories")
public class CategoryController {
    private CategoryService categoryService  ;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    // add category rest api
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> addCategory(@RequestBody CategoryDto categoryDto){
        CategoryDto categoryDto1 = categoryService.addCategory(categoryDto);
        return new ResponseEntity<>(categoryDto1 , HttpStatus.CREATED);
    }

    @GetMapping("{id}")

    public ResponseEntity<CategoryDto> getCategory(@PathVariable("id") Long id){
        CategoryDto categoryDto = categoryService.getCategory(id);
        return ResponseEntity.ok(categoryDto);
    }
    @GetMapping
    public  ResponseEntity<List<CategoryDto>> getCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }


    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public  ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto , @PathVariable("id") Long id){
        return ResponseEntity.ok(categoryService.updateCategory(categoryDto,id));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public  ResponseEntity<String> deleteCategory( @PathVariable("id") Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Deleted");
    }
}
