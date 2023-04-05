package ru.practicum.main.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.comment.model.dto.CommentDto;
import ru.practicum.main.comment.model.dto.NewCommentDto;
import ru.practicum.main.comment.service.CommentPrivateService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
public class CommentPrivateController {

    private final CommentPrivateService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(@PathVariable Long userId,
                             @RequestParam Long eventId,
                             @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("POST : user id : {} event id : {} create comment : {}", userId, eventId, newCommentDto);
        return commentService.userCreateComment(userId, eventId, newCommentDto);
    }

    @PatchMapping("/{commentId}")
    public CommentDto update(@PathVariable Long userId,
                             @PathVariable Long commentId,
                             @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("PATCH : user id : {} update comment  id : {} values : {}", userId, commentId, newCommentDto);
        return commentService.userUpdateComment(userId, commentId, newCommentDto);
    }


    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId, @PathVariable Long commentId) {
        log.info("DELETE : user id : {} delete comment  id : {}", userId, commentId);
        commentService.userDeleteComment(userId, commentId);
    }
}
