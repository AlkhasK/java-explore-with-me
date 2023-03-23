package ru.practicum.main.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.error.exception.ObjectNotFoundException;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.model.dto.NewUserRequest;
import ru.practicum.main.user.model.dto.UserDto;
import ru.practicum.main.user.model.dto.UserMapper;
import ru.practicum.main.user.storage.UserStorage;
import ru.practicum.main.utils.pagination.PageRequestWithOffset;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    private final UserMapper userMapper;

    @Override
    public List<UserDto> getUsers(List<Long> userIds, Integer from, Integer size) {
        Pageable pageable = PageRequestWithOffset.of(from, size);
        List<User> users = userStorage.findByIdIn(userIds, pageable).getContent();
        return users.stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserDto createUser(NewUserRequest newUserRequest) {
        User newUser = userMapper.toUser(newUserRequest);
        User createdUser = userStorage.save(newUser);
        return userMapper.toUserDto(createdUser);
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        userStorage.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User with id=%s was not found", userId)));
        userStorage.deleteById(userId);
    }
}
