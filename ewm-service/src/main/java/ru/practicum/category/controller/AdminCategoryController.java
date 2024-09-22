package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

@RestController
@RequestMapping(path = "admin/categories")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public CategoryDto createCategory(@RequestBody CategoryDto categoryDto) {
        log.info("Create category");
        return categoryService.createCategory(categoryDto);
    }

    @DeleteMapping({"catId"})
    public void deleteCategory(@RequestParam("catId") long catId) {
        log.info("Delete category with id: {}", catId);
        categoryService.deleteCategoryById(catId);
    }

    @PatchMapping({"catId"})
    public CategoryDto updateCategory(@RequestParam long catId,
                                      @RequestBody CategoryDto categoryDto) {
        log.info("Update category with id: {}", catId);
        return categoryService.updateCategory(catId, categoryDto);
    }
}
