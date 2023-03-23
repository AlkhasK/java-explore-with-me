package ru.practicum.main.category.model.dto;

import org.springframework.stereotype.Component;
import ru.practicum.main.category.model.Category;

@Component
public class CategoryMapper {

    public Category toCategory(NewCategoryDto newCatDto) {
        Category category = new Category();
        category.setName(newCatDto.getName());
        return category;
    }

    public CategoryDto toCategoryDto(Category cat) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(cat.getId());
        categoryDto.setName(cat.getName());
        return categoryDto;
    }
}
