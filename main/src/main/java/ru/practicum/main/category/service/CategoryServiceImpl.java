package ru.practicum.main.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.category.model.dto.CategoryDto;
import ru.practicum.main.category.model.dto.CategoryMapper;
import ru.practicum.main.category.model.dto.NewCategoryDto;
import ru.practicum.main.category.storage.CategoryStorage;
import ru.practicum.main.error.exception.CategoryConstraintViolationException;
import ru.practicum.main.error.exception.ObjectNotFoundException;
import ru.practicum.main.event.storage.EventStorage;
import ru.practicum.main.utils.JsonPatch;
import ru.practicum.main.utils.pagination.PageRequestWithOffset;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryStorage categoryStorage;

    private final CategoryMapper categoryMapper;

    private final JsonPatch jsonPatch;

    private final EventStorage eventStorage;

    @Transactional
    @Override
    public CategoryDto createCategory(NewCategoryDto newCatDto) {
        Category newCategory = categoryMapper.toCategory(newCatDto);
        Category createdCategory = categoryStorage.save(newCategory);
        return categoryMapper.toCategoryDto(createdCategory);
    }

    @Transactional
    @Override
    public void deleteCategory(Long catId) {
        categoryStorage.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        String.format("Category with id=%s was not found", catId)));
        if (eventStorage.countAllByCategory_Id(catId) != 0) {
            throw new CategoryConstraintViolationException("The category is not empty");
        }
        categoryStorage.deleteById(catId);
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(Long catId, NewCategoryDto updateCatDto) {
        Category category = categoryStorage.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        String.format("Category with id=%s was not found", catId)));
        Category patchCategory = categoryMapper.toCategory(updateCatDto);
        Category updatedCategory = jsonPatch.mergePatch(category, patchCategory, Category.class);
        updatedCategory = categoryStorage.save(updatedCategory);
        return categoryMapper.toCategoryDto(updatedCategory);
    }

    @Override
    public List<CategoryDto> getAllCategories(Integer from, Integer size) {
        Pageable pageable = PageRequestWithOffset.of(from, size);
        List<Category> categories = categoryStorage.findAll(pageable).getContent();
        return categories.stream()
                .map(categoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        Category category = categoryStorage.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        String.format("Category with id=%s was not found", catId)));
        return categoryMapper.toCategoryDto(category);
    }
}
