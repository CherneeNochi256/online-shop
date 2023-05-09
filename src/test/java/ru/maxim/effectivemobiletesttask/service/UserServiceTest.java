package ru.maxim.effectivemobiletesttask.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.maxim.effectivemobiletesttask.dto.ApiResponse;
import ru.maxim.effectivemobiletesttask.dto.user.UserDtoResponse;
import ru.maxim.effectivemobiletesttask.entity.Role;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.exception.ResourceNotFoundException;
import ru.maxim.effectivemobiletesttask.repository.UserRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private ModelMapper mapper;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService underTest;


    @Test
    void createUser() {
        //given
        User user = new User();
        user.setUsername("randomUsername");

        //when
        underTest.createUser(user);

        //then
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argumentCaptor.capture());

        User captorUser = argumentCaptor.getValue();

        assertEquals(user, captorUser);
    }

    @Test
    public void testUserByIdWithExistingId() {
        //given
        Long existingId = 1L;
        User existingUser = new User();
        existingUser.setId(existingId);
        existingUser.setUsername("John Doe");
        given(userRepository.findById(existingId))
                .willReturn(Optional.of(existingUser));

        UserDtoResponse userDtoResponse = new UserDtoResponse();
        userDtoResponse.setId(existingUser.getId());
        userDtoResponse.setUsername(existingUser.getUsername());

        given(mapper.map(existingUser, UserDtoResponse.class))
                .willReturn(userDtoResponse);
        //when
        ResponseEntity<UserDtoResponse> response = underTest.userById(existingId);
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(existingId, response.getBody().getId());
        assertEquals("John Doe", response.getBody().getUsername());
    }

    @Test
    public void testUserByIdWithNonExistingId() {
        //given
        Long nonExistingId = 2L;
        given(userRepository.findById(nonExistingId))
                .willReturn(Optional.empty());
        //when
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> underTest.userById(nonExistingId));

        //then
        assertEquals("User not found with id: '2'", e.getApiResponse().getMessage());
    }

    @Test
    public void givenUserIdAndMoneyAmount_whenTopUpUserBalance_thenUserBalanceIsUpdated() {
        // given
        Long userId = 123L;
        Double moneyAmount = 50.0;
        User user = new User();
        user.setId(userId);
        user.setBalance(100.0);

        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));
        given(userRepository.save(user))
                .willReturn(user);

        UserDtoResponse userDtoResponse = new UserDtoResponse();
        userDtoResponse.setId(userId);
        userDtoResponse.setBalance(moneyAmount + user.getBalance());

        given(mapper.map(user, UserDtoResponse.class))
                .willReturn(userDtoResponse);

        // when
        ResponseEntity<UserDtoResponse> response = underTest.topUpUserBalance(userId, moneyAmount);

        // then
        verify(userRepository, times(1)).save(user);
        assertEquals(150.0, user.getBalance());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user.getId(), response.getBody().getId());
        assertEquals(user.getBalance(), response.getBody().getBalance());
    }

    @Test
    public void testDeleteUser() {
        // given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));
        ApiResponse expectedResponse = new ApiResponse(Boolean.TRUE, "You have successfully deleted the user with id: " + userId);

        // when
        ResponseEntity<ApiResponse> actualResponse = underTest.deleteUser(userId);

        // then
        verify(userRepository).delete(user);
        assertEquals(expectedResponse, actualResponse.getBody());
    }

    @Test
    public void testDeleteUser_UserNotFound_ThrowsException() {
        // given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        given(userRepository.findById(userId))
                .willReturn(Optional.empty());
        // when
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> underTest.deleteUser(userId));

        // then
        verify(userRepository, never()).delete(user);
        assertEquals("User not found with id: '1'",e.getApiResponse().getMessage());
    }


    @Test
    public void freezeUserSuccess() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        user.setRoles(roles);
        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));

        ResponseEntity<ApiResponse> response = underTest.freezeUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getSuccess());
        assertEquals("You have successfully frozen the user with id: " + userId, response.getBody().getMessage());
        assertFalse(user.getRoles().contains(Role.USER));
        assertTrue(user.getRoles().contains(Role.FROZEN));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void freezeUserNotFound() {
        Long userId = 2L;
        given(userRepository.findById(userId))
                .willReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> underTest.freezeUser(userId));

        //then
        verify(userRepository,never()).save(any(User.class));
        assertEquals("User not found with id: '2'",e.getApiResponse().getMessage());
    }

}
