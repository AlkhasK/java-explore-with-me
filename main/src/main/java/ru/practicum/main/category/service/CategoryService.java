package ru.practicum.main.category.service;

import ru.practicum.main.category.model.dto.CategoryDto;
import ru.practicum.main.category.model.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(NewCategoryDto newCatDto);

    void deleteCategory(Long catId);

    CategoryDto updateCategory(Long catId, NewCategoryDto updateCatDto);

    List<CategoryDto> getAllCategories(Integer from, Integer size);

    CategoryDto getCategory(Long catId);
}
