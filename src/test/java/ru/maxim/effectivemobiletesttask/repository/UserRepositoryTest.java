package ru.maxim.effectivemobiletesttask.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.maxim.effectivemobiletesttask.entity.User;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository underTest;

    @Test
    void findById() {
        //given
        User user = new User();
        user.setId(1L);
        user.setUsername("username");

        underTest.save(user);
        //when

        User userFromDb = underTest.findById(1L).get();

        //then

        assertEquals(user,userFromDb);
    }

    @Test
    void findByUsername() {
    }
}