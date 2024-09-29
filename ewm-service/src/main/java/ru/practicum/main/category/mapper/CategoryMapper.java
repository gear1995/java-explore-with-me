package ru.practicum.main.category.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.model.Category;

@UtilityClass
public class CategoryMapper {
    public CategoryDto toCategoryDto(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public Category toCategory(CategoryDto categoryDto) {
        if (categoryDto == null) {
            return null;
        }

        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }
}
