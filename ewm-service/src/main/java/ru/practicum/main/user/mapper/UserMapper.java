package ru.practicum.main.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.user.dto.UserDto;
import ru.practicum.main.user.model.User;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class UserMapper {
    public static UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toUser(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static List<UserDto> toUserDtoList(List<User> list) {
        if (list == null) {
            return null;
        }

        List<UserDto> dtoList = new ArrayList<>();
        for (User user : list) {
            dtoList.add(toUserDto(user));
        }

        return dtoList;
    }
}
