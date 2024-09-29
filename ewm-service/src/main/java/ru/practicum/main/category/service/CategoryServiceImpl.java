package ru.practicum.main.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.mapper.CategoryMapper;
import ru.practicum.main.category.repository.CategoryRepository;
import ru.practicum.main.exeption.NotFoundException;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategory(categoryDto)));
    }

    @Override
    public void deleteCategoryById(long catId) {
        categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id " + catId + " not found"));
    }

    @Override
    public CategoryDto updateCategory(long catId, CategoryDto categoryDto) {
        categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id " + catId + " not found"));

        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategory(categoryDto)));
    }
}
