package ru.practicum.server.hit.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.hit.model.Hit;

public interface HitRepository extends JpaRepository<Hit, Long> {
}
