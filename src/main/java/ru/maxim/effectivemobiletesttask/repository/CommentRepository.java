package ru.maxim.effectivemobiletesttask.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.maxim.effectivemobiletesttask.entity.Comment;

public interface CommentRepository  extends JpaRepository<Comment, Long> {
}
