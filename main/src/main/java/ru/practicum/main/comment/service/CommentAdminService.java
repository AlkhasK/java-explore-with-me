package ru.practicum.main.comment.service;

import ru.practicum.main.comment.model.dto.CommentDto;
import ru.practicum.main.comment.model.dto.NewCommentDto;

public interface CommentAdminService {
    CommentDto adminUpdateComment(Long commentId, NewCommentDto newCommentDto);

    void adminDeleteComment(Long commentId);
}
