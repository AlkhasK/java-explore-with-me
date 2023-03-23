package ru.practicum.main.user.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.user.model.User;

import java.util.List;

public interface UserStorage extends JpaRepository<User, Long> {

    Page<User> findByIdIn(List<Long> userIds, Pageable pageable);
}
