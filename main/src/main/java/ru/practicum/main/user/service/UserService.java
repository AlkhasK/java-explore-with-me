package ru.practicum.main.user.service;

import ru.practicum.main.user.model.dto.NewUserRequest;
import ru.practicum.main.user.model.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(List<Long> userIds, Integer from, Integer size);

    UserDto createUser(NewUserRequest newUserRequest);

    void deleteUser(Long userId);
}
