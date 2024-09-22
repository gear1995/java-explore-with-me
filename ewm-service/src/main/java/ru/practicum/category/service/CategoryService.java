package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    void deleteCategoryById(long catId);

    CategoryDto updateCategory(long catId, CategoryDto categoryDto);
}
