package ru.practicum.main.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("categories")
@RequiredArgsConstructor
@Slf4j
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getCategoriesByParam(@RequestParam(required = false, defaultValue = "0") Integer from,
                                                  @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("getCategoriesByParam");
        return categoryService.getCategoriesByParam(from, size);
    }

    @GetMapping("{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        log.info("getCategoryById");
        return categoryService.getCategoryById(catId);
    }
}
