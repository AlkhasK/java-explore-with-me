package ru.practicum.main.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.comment.model.Comment;
import ru.practicum.main.comment.model.Status;
import ru.practicum.main.comment.model.dto.CommentDto;
import ru.practicum.main.comment.model.dto.CommentMapper;
import ru.practicum.main.comment.model.dto.NewCommentDto;
import ru.practicum.main.comment.storage.CommentStorage;
import ru.practicum.main.error.exception.ConditionNotMetException;
import ru.practicum.main.error.exception.ObjectNotFoundException;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.State;
import ru.practicum.main.event.storage.EventStorage;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.storage.UserStorage;
import ru.practicum.main.utils.pagination.PageRequestWithOffset;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentAdminService, CommentPublicService, CommentPrivateService {

    private final CommentStorage commentStorage;
    private final EventStorage eventStorage;
    private final UserStorage userStorage;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentDto userCreateComment(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User author = userStorage.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User with id=%s was not found", userId)));
        Event event = eventStorage.findWithCategoryAndInitiator(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Event with id=%s was not found", eventId)));
        if (event.getState() != State.PUBLISHED) {
            throw new ConditionNotMetException("Event must have status published");
        }
        Comment comment = commentMapper.toComment(author, event, newCommentDto);
        comment.setStatus(Status.UNMODIFIED);
        comment.setCreatedOn(LocalDateTime.now());
        Comment createdComment = commentStorage.save(comment);
        return commentMapper.toCommentDto(createdComment);
    }

    @Override
    @Transactional
    public CommentDto userUpdateComment(Long userId, Long commentId, NewCommentDto newCommentDto) {
        Comment comment = commentStorage.findByIdUserIdFetch(commentId, userId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        String.format("Comment with id=%s user id=%s was not found", commentId, userId)));
        comment.setText(newCommentDto.getText());
        comment.setStatus(Status.MODIFIED);
        Comment updatedComment = commentStorage.save(comment);
        return commentMapper.toCommentDto(updatedComment);
    }

    @Override
    @Transactional
    public void userDeleteComment(Long userId, Long commentId) {
        commentStorage.findByIdUserIdFetch(commentId, userId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        String.format("Comment with id=%s user id=%s was not found", commentId, userId)));
        commentStorage.deleteById(commentId);
    }

    @Override
    @Transactional
    public CommentDto adminUpdateComment(Long commentId, NewCommentDto newCommentDto) {
        Comment comment = commentStorage.findByIdFetch(commentId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        String.format("Comment with id=%s was not found", commentId)));
        comment.setStatus(Status.MODERATED);
        comment.setText(newCommentDto.getText());
        Comment updatedComment = commentStorage.save(comment);
        return commentMapper.toCommentDto(updatedComment);
    }

    @Override
    @Transactional
    public void adminDeleteComment(Long commentId) {
        commentStorage.findByIdFetch(commentId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        String.format("Comment with id=%s was not found", commentId)));
        commentStorage.deleteById(commentId);
    }

    @Override
    public CommentDto getComment(Long commentId) {
        Comment comment = commentStorage.findByIdFetch(commentId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        String.format("Comment with id=%s was not found", commentId)));
        return commentMapper.toCommentDto(comment);
    }

    @Override
    public List<CommentDto> getComments(Long eventId, Integer from, Integer size) {
        Pageable pageable = PageRequestWithOffset.of(from, size);
        List<Comment> comments = commentStorage.findAllByEventIdFetch(eventId, pageable).getContent();
        return comments.stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
