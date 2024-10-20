package ru.practicum.main.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main.comment.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
