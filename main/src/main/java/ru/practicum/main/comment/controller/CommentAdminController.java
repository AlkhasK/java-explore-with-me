package ru.practicum.main.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.comment.model.dto.CommentDto;
import ru.practicum.main.comment.model.dto.NewCommentDto;
import ru.practicum.main.comment.service.CommentAdminService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
public class CommentAdminController {

    private final CommentAdminService commentService;

    @PatchMapping("/{commentId}")
    public CommentDto update(@PathVariable Long commentId,
                             @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("PATCH : admin update comment  id : {} values : {}", commentId, newCommentDto);
        return commentService.adminUpdateComment(commentId, newCommentDto);
    }


    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long commentId) {
        log.info("DELETE : admin delete comment  id : {}", commentId);
        commentService.adminDeleteComment(commentId);
    }
}
