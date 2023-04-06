package ru.maxim.effectivemobiletesttask.service;

import org.springframework.stereotype.Service;
import ru.maxim.effectivemobiletesttask.entity.Role;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.repository.UserRepository;

@Service
public class AdminService {

    private final UserRepository userRepository;

    public AdminService( UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void topUpUserBalance(User user, Double moneyAmount) {
        user.setBalance(user.getBalance() + moneyAmount);
    }


    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public void freezeUser(User user) {
        user.getRoles().remove(Role.USER);
        user.getRoles().add(Role.FROZEN);
        userRepository.save(user);
    }
}
