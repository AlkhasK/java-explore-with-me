package ru.practicum.main.category.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.category.model.Category;

public interface CategoryStorage extends JpaRepository<Category, Long> {
}
