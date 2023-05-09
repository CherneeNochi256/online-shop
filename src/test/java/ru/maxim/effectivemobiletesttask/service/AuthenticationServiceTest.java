package ru.maxim.effectivemobiletesttask.service;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.maxim.effectivemobiletesttask.dto.auth.AuthenticationRequest;
import ru.maxim.effectivemobiletesttask.dto.auth.AuthenticationResponse;
import ru.maxim.effectivemobiletesttask.dto.auth.RegisterRequest;
import ru.maxim.effectivemobiletesttask.entity.Role;
import ru.maxim.effectivemobiletesttask.entity.Token;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.exception.ResourceNotFoundException;
import ru.maxim.effectivemobiletesttask.repository.TokenRepository;
import ru.maxim.effectivemobiletesttask.repository.UserRepository;
import ru.maxim.effectivemobiletesttask.security.JwtService;
import ru.maxim.effectivemobiletesttask.security.token.TokenType;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationService underTest;

    @Test
    public void testRegister() {
        //given
        RegisterRequest request = new RegisterRequest("username", "email@example.com", "mypassword");

        given(jwtService.generateToken(any(User.class)))
                .willReturn("");
        given(jwtService.generateRefreshToken(any(User.class)))
                .willReturn("");

        //when
        AuthenticationResponse response = underTest.register(request);
        //then
        assertEquals("", response.getAccessToken());
        assertEquals("", response.getRefreshToken());
    }


    @Test
    public void testRegister_CorrectUserIsSaved() {

        RegisterRequest request = new RegisterRequest("username", "email@example.com", "mypassword");
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        given(userRepository.save(captor.capture()))
                .willReturn(new User());
        given(passwordEncoder.encode(request.getPassword()))
                .willReturn("encodedPassword");

        underTest.register(request);

        assertEquals(captor.getValue().getUsername(), request.getUsername());
        assertEquals(captor.getValue().getEmail(), request.getEmail());
        assertNotEquals(captor.getValue().getPassword(), request.getPassword());
        assertNotNull(captor.getValue().getPassword());
        assertTrue(captor.getValue().getRoles().contains(Role.USER));
    }

    @Test
    public void testRegister_TestCorrectTokenIsSaved() {
        //given
        RegisterRequest request = new RegisterRequest("username", "email@example.com", "mypassword");

        ArgumentCaptor<Token> tokenCaptor = ArgumentCaptor.forClass(Token.class);

        given(jwtService.generateToken(any(User.class)))
                .willReturn("");
        given(jwtService.generateRefreshToken(any(User.class)))
                .willReturn("");
        given(userRepository.save(any(User.class)))
                .willReturn(new User());
        given(tokenRepository.save(tokenCaptor.capture()))
                .willReturn(new Token());

        //when
        underTest.register(request);
        //then
        Token token = tokenCaptor.getValue();

        assertEquals(token.getUser(), new User());
        assertEquals(token.getToken(), "");
        assertEquals(token.getTokenType(), TokenType.BEARER);
        assertFalse(token.isExpired());
        assertFalse(token.isRevoked());

    }


    @Test
    void authenticate_validRequest_returnAuthenticationResponse() {
        //given
        AuthenticationRequest request = new AuthenticationRequest("username", "password");

        User user = new User();
        user.setUsername("username");
        given(userRepository.findByUsername(request.getUsername()))
                .willReturn(Optional.of(user));

        String token = "token";
        given(jwtService.generateToken(user))
                .willReturn(token);
        given(jwtService.generateRefreshToken(user))
                .willReturn("refreshToken");

        AuthenticationResponse expectedResponse = AuthenticationResponse.builder()
                .accessToken(token)
                .refreshToken("refreshToken")
                .build();

        //when
        AuthenticationResponse response = underTest.authenticate(request);

        //then
        assertEquals(response, expectedResponse);
    }


    @Test
    void authenticate_TestThatTheCorrectTokenIsSaved() {
        //given
        AuthenticationRequest request = new AuthenticationRequest("username", "password");

        User user = new User();
        user.setUsername("username");
        given(userRepository.findByUsername(request.getUsername()))
                .willReturn(Optional.of(user));


        given(jwtService.generateToken(user))
                .willReturn("token");
        given(jwtService.generateRefreshToken(user))
                .willReturn("refreshToken");

        ArgumentCaptor<Token> tokenCaptor = ArgumentCaptor.forClass(Token.class);

        given(tokenRepository.save(tokenCaptor.capture()))
                .willReturn(new Token());

        //when
        underTest.authenticate(request);

        //then
        Token token = tokenCaptor.getValue();

        assertEquals(token.getUser(), user);
        assertEquals(token.getToken(), "token");
        assertEquals(token.getTokenType(), TokenType.BEARER);
        assertFalse(token.isExpired());
        assertFalse(token.isRevoked());
    }


    @Test
    void authenticate_TestThatAllPreviousUserTokensAreRevoked() {
        //given
        AuthenticationRequest request = new AuthenticationRequest("username", "password");

        User user = new User();
        user.setUsername("username");

        given(userRepository.findByUsername(request.getUsername()))
                .willReturn(Optional.of(user));
        given(jwtService.generateToken(user))
                .willReturn("token");
        given(jwtService.generateRefreshToken(user))
                .willReturn("refreshToken");
        given(tokenRepository.save(any(Token.class)))
                .willReturn(new Token());


        List<Token> tokens = List.of(new Token(), new Token());

        given(tokenRepository.findAllValidTokenByUser(user.getId()))
                .willReturn(tokens);

        //when
        underTest.authenticate(request);

        //then
        tokens.forEach(t -> {
            assertTrue(t.expired);
            assertTrue(t.revoked);
        });

        verify(tokenRepository).saveAll(tokens);
    }


    @Test
    public void testRefreshTokenWithNullAuthHeader() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        underTest.refreshToken(request, response);

        verify(jwtService, never()).extractUsername(any(String.class));
        verify(userRepository, never()).findByUsername(any(String.class));
        verify(response, never()).getOutputStream();
    }


    @Test
    public void testRefreshTokenWithInvalidAuthHeader() throws IOException {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        given(request.getHeader(HttpHeaders.AUTHORIZATION))
                .willReturn("Basic abc123");

        //when
        underTest.refreshToken(request, response);

        //then
        verify(jwtService, never()).extractUsername(any(String.class));
        verify(userRepository, never()).findByUsername(any(String.class));
        verify(response, never()).getOutputStream();
    }

    @Test
    public void testRefreshTokenWithUnknownUser() throws IOException {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        String refreshToken = "valid_refresh_token";
        String authHeader = "Bearer " + refreshToken;

        given(request.getHeader(HttpHeaders.AUTHORIZATION))
                .willReturn(authHeader);
        given(jwtService.extractUsername(refreshToken))
                .willReturn("unknown_user");
        given(userRepository.findByUsername("unknown_user"))
                .willReturn(Optional.empty());

        //when
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> underTest.refreshToken(request, response));

        //then
        verify(jwtService, times(1)).extractUsername(any(String.class));
        verify(response, never()).getOutputStream();
        assertEquals("User not found with name: 'unknown_user'", e.getApiResponse().getMessage());
    }


    @Test
    public void testRefreshTokenWithInvalidRefreshToken() throws IOException {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        String refreshToken = "invalid_refresh_token";
        String authHeader = "Bearer " + refreshToken;
        String userName = "test_user";

        User user = new User();
        user.setUsername(userName);

        given(request.getHeader(HttpHeaders.AUTHORIZATION))
                .willReturn(authHeader);
        given(jwtService.extractUsername(refreshToken))
                .willReturn(userName);
        given(userRepository.findByUsername(userName))
                .willReturn(Optional.of(user));
        given(jwtService.isTokenValid(refreshToken, user))
                .willReturn(false);

        //when
        underTest.refreshToken(request, response);

        //then
        verify(response, never()).getOutputStream();
    }

    @Test
    public void testRefreshTokenWithValidRefreshToken() throws IOException {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);


        String refreshToken = "valid_refresh_token";
        String authHeader = "Bearer " + refreshToken;
        String userName = "test_user";

        User user = new User();
        user.setUsername(userName);

        given(request.getHeader(HttpHeaders.AUTHORIZATION))
                .willReturn(authHeader);
        given(jwtService.extractUsername(refreshToken))
                .willReturn(userName);
        given(userRepository.findByUsername(userName))
                .willReturn(Optional.of(user));
        given(jwtService.isTokenValid(refreshToken, user))
                .willReturn(true);


        String accessToken = "new_access_token";
        given(jwtService.generateToken(user))
                .willReturn(accessToken);

        given(response.getOutputStream())
                .willReturn(new ServletOutputStream() {
                    @Override
                    public boolean isReady() {
                        return false;
                    }

                    @Override
                    public void setWriteListener(WriteListener listener) {

                    }

                    @Override
                    public void write(int b) throws IOException {

                    }
                });
        //when
        AuthenticationResponse authResponse = underTest.refreshToken(request, response);

        //then

        assertEquals(accessToken, authResponse.getAccessToken());
        assertEquals(refreshToken, authResponse.getRefreshToken());

    }


}