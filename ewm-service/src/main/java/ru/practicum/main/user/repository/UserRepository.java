package ru.practicum.main.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main.user.dto.UserDto;
import ru.practicum.main.user.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<UserDto> getUsersByIdIn(List<Long> ids, Pageable pageable);
}
