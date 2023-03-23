package ru.practicum.main.compilation.storage;

import ru.practicum.main.compilation.model.Compilation;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class Predicates {
    public static Predicate hasPinned(Root<Compilation> root, CriteriaBuilder cb, Boolean pinned) {
        return cb.equal(root.get("pinned"), pinned);
    }
}
