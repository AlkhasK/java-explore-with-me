package ru.practicum.main.comment.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.main.comment.model.Comment;

import java.util.Optional;

public interface CommentStorage extends JpaRepository<Comment, Long> {

    @Query("select c " +
            "from Comment c " +
            "join fetch c.event " +
            "join fetch c.author " +
            "where c.id = :commentId " +
            "and c.author.id = :authorId")
    Optional<Comment> findByIdUserIdFetch(@Param("commentId") Long commentId, @Param("authorId") Long authorId);

    @Query("select c " +
            "from Comment c " +
            "join fetch c.event " +
            "join fetch c.author " +
            "where c.id = :commentId")
    Optional<Comment> findByIdFetch(@Param("commentId") Long commentId);

    @Query(value = "select c " +
            "from Comment c " +
            "join fetch c.event " +
            "join fetch c.author " +
            "where c.event.id = :eventId",
            countQuery = "select count(c.id) " +
                    "from Comment c " +
                    "where c.event.id = :eventId")
    Page<Comment> findAllByEventIdFetch(@Param("eventId") Long eventId, Pageable pageable);

    @Query("select c " +
            "from Comment c " +
            "join fetch c.event " +
            "join fetch c.author " +
            "where c.id = :commentId " +
            "and c.event.initiator.id = :authorId " +
            "and c.event.id = :eventId ")
    Optional<Comment> findByIdAuthorIdEventIdFetch(@Param("commentId") Long commentId,
                                                   @Param("authorId") Long authorId,
                                                   @Param("eventId") Long eventId);

    Optional<Comment> findByIdAndEvent_IdAndEvent_Initiator_Id(Long commentId, Long eventId, Long userId);

    void deleteByIdAndEvent_Id(Long commentId, Long eventId);
}
