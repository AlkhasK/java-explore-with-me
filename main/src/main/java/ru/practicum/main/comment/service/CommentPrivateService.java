package ru.practicum.main.comment.service;

import ru.practicum.main.comment.model.dto.CommentDto;
import ru.practicum.main.comment.model.dto.NewCommentDto;

public interface CommentPrivateService {
    CommentDto userCreateComment(Long userId, Long eventId, NewCommentDto newCommentDto);

    CommentDto userUpdateComment(Long userId, Long commentId, NewCommentDto newCommentDto);

    void userDeleteComment(Long userId, Long commentId);
}
