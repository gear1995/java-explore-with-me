package ru.practicum.main.category.service;

import ru.practicum.main.category.dto.CategoryDto;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    void deleteCategoryById(long catId);

    CategoryDto updateCategory(long catId, CategoryDto categoryDto);
}
