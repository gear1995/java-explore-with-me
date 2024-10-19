package ru.practicum.main.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.comment.dto.CommentDto;
import ru.practicum.main.comment.service.CommentService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("users/{userId}/comments")
@RequiredArgsConstructor
@Slf4j
public class PrivateCommentController {
    private final CommentService commentService;

    @PostMapping("{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @RequestBody @Valid CommentDto commentDto) {
        log.info("Creating comment for user {} with comment: {}", userId, commentDto);
        if (commentDto.getCreated() == null) {
            commentDto.setCreated(LocalDateTime.now());
        }
        return commentService.createComment(userId, eventId, commentDto);
    }

    @PatchMapping("{commentId}")
    public CommentDto updateComment(@PathVariable Long commentId,
                                    @RequestBody CommentDto commentDto,
                                    @PathVariable Long userId) {
        log.info("Updating comment for user {} with comment: {}", commentId, commentDto);
        return commentService.updateComment(userId, commentId, commentDto);
    }

    @DeleteMapping("{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable Long userId,
                                  @PathVariable Long commentId) {
        log.info("Deleting comment for user {} with comment: {}", userId, commentId);
        commentService.deleteCommentById(userId, commentId);
    }
}
