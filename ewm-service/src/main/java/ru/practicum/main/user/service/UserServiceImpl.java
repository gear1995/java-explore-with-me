package ru.practicum.main.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.main.exeption.NotFoundException;
import ru.practicum.main.user.dto.UserDto;
import ru.practicum.main.user.mapper.UserMapper;
import ru.practicum.main.user.repository.UserRepository;

import java.util.List;

import static ru.practicum.main.user.mapper.UserMapper.toUserDto;
import static ru.practicum.main.user.mapper.UserMapper.toUserDtoList;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsersByParam(List<Long> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (ids == null || ids.isEmpty()) {
            return toUserDtoList(userRepository.findAll(pageable).toList());
        } else {
            return toUserDtoList(userRepository.findAllById(ids));
        }
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        return toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    @Override
    public void deleteUSerById(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        userRepository.deleteById(userId);
    }
}
