package ru.maxim.effectivemobiletesttask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.maxim.effectivemobiletesttask.entity.Grade;

public interface GradeRepository extends JpaRepository<Grade, Long> {
}