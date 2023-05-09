package ru.maxim.effectivemobiletesttask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.maxim.effectivemobiletesttask.entity.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    @Query("from User u where u.id = :id")
    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

}
