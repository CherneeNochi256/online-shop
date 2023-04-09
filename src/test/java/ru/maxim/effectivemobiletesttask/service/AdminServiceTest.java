package ru.maxim.effectivemobiletesttask.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.maxim.effectivemobiletesttask.entity.Role;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.repository.UserRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;
    private AdminService underTest;

    @BeforeEach
    void setUp(){
        underTest = new AdminService(userRepository);
    }
    @Test
    void topUpUserBalance() {
        //given
        User user = new User();
        user.setUsername("randomUsername");
        user.setBalance(0.0);

        Double moneyAmount = 100.0;

        //when
        underTest.topUpUserBalance(user,moneyAmount);

        //then
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argumentCaptor.capture());

        User captorUser = argumentCaptor.getValue();

        assertEquals(moneyAmount,captorUser.getBalance());
    }

    @Test
    void deleteUser() {
        //given
        User user = new User();

        //when
        underTest.deleteUser(user);

        //then
        verify(userRepository).delete(user);

    }

    @Test
    void freezeUser() {
        //given

        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.USER);

        User user = new User();
        user.setUsername("randomUsername");
        user.setRoles(roles);



        //when
        underTest.freezeUser(user);

        //then
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argumentCaptor.capture());

        User captorUser = argumentCaptor.getValue();

        assertTrue(captorUser.getRoles().contains(Role.FROZEN));
        assertFalse(captorUser.getRoles().contains(Role.USER));
    }
}