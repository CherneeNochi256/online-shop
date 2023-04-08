package ru.maxim.effectivemobiletesttask.rest;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

class AdminControllerTest {

    @Mock
    UserService userService;

    @InjectMocks
    AdminController adminController;

    @Test
    void topUp_VerifyThatUserBalanceWasTopUp() throws Exception {

        //given
        Long userId = 2L;
        Double moneyAmount = 85.0;
        User user = new User();
        user.setBalance(0.0);
        Double balance = user.getBalance();

        doReturn(user).when(this.userService).userById(userId);
        //when

        adminController.topUp(userId,moneyAmount);

        //then

        assertEquals(balance + moneyAmount, user.getBalance());
    }

    @Test
    void deleteUser() {
    }

    @Test
    void freezeUser() {
    }
}