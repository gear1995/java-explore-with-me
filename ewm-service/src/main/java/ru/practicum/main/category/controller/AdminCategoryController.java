package ru.practicum.main.category.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.service.CategoryService;

@RestController
@RequestMapping("admin/categories")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Create category");
        return categoryService.createCategory(categoryDto);
    }

    @DeleteMapping("{catId}")
    public void deleteCategory(@PathVariable("catId") long catId) {
        log.info("Delete category with id: {}", catId);
        categoryService.deleteCategoryById(catId);
    }

    @PatchMapping("{catId}")
    public CategoryDto updateCategory(@PathVariable long catId,
                                      @Valid @RequestBody CategoryDto categoryDto) {
        log.info("Update category with id: {}", catId);
        return categoryService.updateCategory(catId, categoryDto);
    }
}
