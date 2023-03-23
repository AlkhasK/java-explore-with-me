package ru.practicum.main.compilation.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.main.compilation.model.Compilation;

import java.util.Optional;

public interface CompilationStorage extends JpaRepository<Compilation, Long>, JpaSpecificationExecutor<Compilation> {

    @Query("select c " +
            "from Compilation c " +
            "LEFT JOIN FETCH c.events " +
            "where c.id = :compId")
    Optional<Compilation> findByIdFetch(@Param("compId") Long compId);
}
