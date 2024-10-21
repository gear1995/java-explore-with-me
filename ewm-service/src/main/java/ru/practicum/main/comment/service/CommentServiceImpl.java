package ru.practicum.main.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.main.comment.dto.CommentDto;
import ru.practicum.main.comment.model.Comment;
import ru.practicum.main.comment.repository.CommentRepository;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.repository.EventRepository;
import ru.practicum.main.exeption.NotFoundException;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.repository.UserRepository;

import static ru.practicum.main.comment.mapper.CommentMapper.toCommentDto;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;

    @Override
    public CommentDto createComment(Long userId, Long eventId, CommentDto commentDto) {
        User commentAuthor = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found", userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id %s not found", eventId)));

        Comment comment = Comment.builder()
                .author(commentAuthor)
                .created(commentDto.getCreated())
                .text(commentDto.getText())
                .event(event)
                .build();

        return toCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto getCommentById(Long commentId) {
        return toCommentDto(commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with id %s not found", commentId))));
    }

    @Override
    public CommentDto updateComment(Long userId, Long commentId, CommentDto commentDto) {
        User commentAuthor = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found", userId)));

        Comment savedComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with id %s not found", commentId)));

        savedComment.setAuthor(commentAuthor);

        if (commentDto.getEventId() != null) {
            Event event = eventRepository.findById(commentDto.getEventId())
                    .orElseThrow(() -> new NotFoundException(String.format("Event with id %s not found", commentDto.getEventId())));
            savedComment.setEvent(event);
        }

        if (commentDto.getText() != null) {
            savedComment.setText(commentDto.getText());
        }

        if (commentDto.getCreated() != null) {
            savedComment.setCreated(commentDto.getCreated());
        }

        return toCommentDto(commentRepository.save(savedComment));
    }

    @Override
    public void deleteCommentById(Long userId, Long commentId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found", userId)));

        commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with id %s not found", commentId)));

        commentRepository.deleteById(commentId);
    }
}
