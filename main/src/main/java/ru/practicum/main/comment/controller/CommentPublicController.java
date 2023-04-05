package ru.practicum.main.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.comment.model.dto.CommentDto;
import ru.practicum.main.comment.service.CommentPublicService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentPublicController {

    private final CommentPublicService commentService;

    @GetMapping("/{commentId}")
    public CommentDto find(@PathVariable Long commentId) {
        log.info("GET : find comment id : {}", commentId);
        return commentService.getComment(commentId);
    }

    @GetMapping
    public List<CommentDto> findAll(@RequestParam Long eventId,
                                    @RequestParam(defaultValue = "0") Integer from,
                                    @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET : find all comments for event id : {} from : {} size : {}", eventId, from, size);
        return commentService.getComments(eventId, from, size);
    }
}
