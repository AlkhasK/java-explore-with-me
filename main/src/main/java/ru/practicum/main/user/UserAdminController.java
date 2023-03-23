package ru.practicum.main.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.user.model.dto.NewUserRequest;
import ru.practicum.main.user.model.dto.UserDto;
import ru.practicum.main.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserAdminController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> find(@RequestParam List<Long> ids,
                              @RequestParam(defaultValue = "0") Integer from,
                              @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET : find users ids : {} from : {} size : {}", ids, from, size);
        return userService.getUsers(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody NewUserRequest newUserRequest) {
        log.info("POST : create user : {}", newUserRequest);
        return userService.createUser(newUserRequest);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId) {
        log.info("DELETE : delete user id : {}", userId);
        userService.deleteUser(userId);
    }
}
