package ru.practicum.main.user.service;

import ru.practicum.main.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsersByParam(List<Long> ids, int from, int size);

    UserDto createUser(UserDto userDto);

    void deleteUSerById(Long userId);
}
