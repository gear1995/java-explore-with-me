package ru.practicum.main.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.category.repository.CategoryRepository;
import ru.practicum.main.event.repository.EventRepository;
import ru.practicum.main.exeption.NotFoundException;
import ru.practicum.main.exeption.ValidationException;

import java.util.List;

import static ru.practicum.main.category.mapper.CategoryMapper.*;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        try {
            return toCategoryDto(categoryRepository.save(toCategory(categoryDto)));
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    @Override
    public void deleteCategoryById(long catId) {
        categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id " + catId + " not found"));
        if (eventRepository.existsByCategoryId(catId)) {
            throw new ValidationException("Category already has events");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto updateCategory(long catId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id " + catId + " not found"));
        category.setName(categoryDto.getName());

        try {
            return toCategoryDto(categoryRepository.save(category));
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    @Override
    public List<CategoryDto> getCategoriesByParam(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return toCategoryDtoList(categoryRepository.findAll(pageable).toList());
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        return toCategoryDto(categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id " + catId + " not found")));
    }
}
