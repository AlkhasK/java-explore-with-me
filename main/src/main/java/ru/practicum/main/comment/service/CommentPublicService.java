package ru.practicum.main.comment.service;

import ru.practicum.main.comment.model.dto.CommentDto;

import java.util.List;

public interface CommentPublicService {
    CommentDto getComment(Long commentId);

    List<CommentDto> getComments(Long eventId, Integer from, Integer size);
}
