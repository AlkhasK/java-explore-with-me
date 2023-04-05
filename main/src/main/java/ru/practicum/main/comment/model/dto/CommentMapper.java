package ru.practicum.main.comment.model.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.main.comment.model.Comment;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.model.dto.UserMapper;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final UserMapper userMapper;

    public CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setAuthor(userMapper.toUserShortDto(comment.getAuthor()));
        commentDto.setEventId(comment.getEvent().getId());
        commentDto.setText(comment.getText());
        commentDto.setCreatedOn(comment.getCreatedOn());
        commentDto.setStatus(comment.getStatus());
        commentDto.setModifiedOn(comment.getModifiedOn());
        return commentDto;
    }

    public Comment toComment(User author, Event event, NewCommentDto newCommentDto) {
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setEvent(event);
        comment.setText(newCommentDto.getText());
        return comment;
    }
}
