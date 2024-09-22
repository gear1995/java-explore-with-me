package ru.practicum.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("admin/users")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsersByParam(@RequestParam List<Long> ids,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size) {
        log.info("Getting users by param");

        return userService.getUsersByParam(ids, from, size);
    }

    @PostMapping
    public UserDto createUser(@RequestBody @Valid UserDto userDto) {
        log.info("Creating user");

        return userService.createUser(userDto);
    }

    @DeleteMapping({"userId"})
    public void deleteUser(@RequestParam Long userId) {
        log.info("Deleting user");

        userService.deleteUSerById(userId);
    }
}
