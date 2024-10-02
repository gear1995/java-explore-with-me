package ru.practicum.main.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.repository.CategoryRepository;
import ru.practicum.main.exeption.NotFoundException;

import java.util.List;

import static ru.practicum.main.category.mapper.CategoryMapper.toCategory;
import static ru.practicum.main.category.mapper.CategoryMapper.toCategoryDto;
import static ru.practicum.main.category.mapper.CategoryMapper.toCategoryDtoList;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        return toCategoryDto(categoryRepository.save(toCategory(categoryDto)));
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

        return toCategoryDto(categoryRepository.save(toCategory(categoryDto)));
    }

    @Override
    public List<CategoryDto> getCategoriesByParam(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return toCategoryDtoList(categoryRepository.findAll(pageable).toList());
    }
}
