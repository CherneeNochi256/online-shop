package ru.maxim.effectivemobiletesttask.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.maxim.effectivemobiletesttask.dto.ApiResponse;
import ru.maxim.effectivemobiletesttask.dto.user.UserDtoResponse;
import ru.maxim.effectivemobiletesttask.entity.Role;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.exception.ResourceNotFoundException;
import ru.maxim.effectivemobiletesttask.repository.UserRepository;

import static ru.maxim.effectivemobiletesttask.utils.AppConstants.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(USER, NAME, username));
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public ResponseEntity<UserDtoResponse> userById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER, ID, id));

        UserDtoResponse response = mapper.map(user, UserDtoResponse.class);

        return ResponseEntity.ok(response);
    }


    public ResponseEntity<UserDtoResponse> topUpUserBalance(Long userId, Double moneyAmount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER, ID, userId));
        user.setBalance(user.getBalance() + moneyAmount);
        User savedUser = userRepository.save(user);
        UserDtoResponse userDtoResponse = mapper.map(savedUser, UserDtoResponse.class);
        return ResponseEntity.ok(userDtoResponse);
    }


    public ResponseEntity<ApiResponse> deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER, ID, userId));
        userRepository.delete(user);
        return ResponseEntity.ok(new ApiResponse(Boolean.TRUE, "You have successfully deleted the user with id: " + userId));
    }

    public ResponseEntity<ApiResponse> freezeUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER, ID, userId));
        user.getRoles().remove(Role.USER);
        user.getRoles().add(Role.FROZEN);
        userRepository.save(user);
        return ResponseEntity.ok(new ApiResponse(Boolean.TRUE, "You have successfully frozen the user with id: " + userId));
    }

}
