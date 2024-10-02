package ru.practicum.main.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.user.dto.UserDto;
import ru.practicum.main.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("admin/users")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsersByParam(@RequestParam(required = false) List<Long> ids,
                                         @RequestParam(defaultValue = "0", required = false) int from,
                                         @RequestParam(defaultValue = "10", required = false) int size) {
        log.info("Getting users by param");

        return userService.getUsersByParam(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Valid UserDto userDto) {
        log.info("Creating user");

        return userService.createUser(userDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Deleting user");

        userService.deleteUSerById(userId);
    }
}
