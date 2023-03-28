package ru.maxim.effectivemobiletesttask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.maxim.effectivemobiletesttask.entity.User;




public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByPassword(String password);

}
