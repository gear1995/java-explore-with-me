package ru.practicum.main.category.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.model.Category;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category toCategory(CategoryDto categoryDto) {
        if (categoryDto == null) {
            return null;
        }

        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }

    public static List<CategoryDto> toCategoryDtoList(List<Category> list) {
        if (list == null) {
            return null;
        }

        List<CategoryDto> categoryDtoList = new ArrayList<>();
        for (Category category : list) {
            categoryDtoList.add(toCategoryDto(category));
        }

        return categoryDtoList;
    }
}
