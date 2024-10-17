package ru.practicum.main.comment.service;

import ru.practicum.main.comment.dto.CommentDto;

public interface CommentService {
    CommentDto createComment(Long userId, Long eventId, CommentDto commentDto);

    CommentDto getCommentById(Long commentId);

    CommentDto updateComment(Long userId, Long commentId, CommentDto commentDto);

    void deleteCommentById(Long userId, Long commentId);
}
