package ru.maxim.effectivemobiletesttask.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.maxim.effectivemobiletesttask.AbstractTestcontainers;
import ru.maxim.effectivemobiletesttask.entity.Token;
import ru.maxim.effectivemobiletesttask.entity.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TokenRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    TokenRepository underTest;

    @Test
    void findAllValidTokenByUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("username");

        Token token = new Token();
        token.setExpired(false);
        token.setRevoked(false);
        token.setUser(user);

        Token token2 = new Token();
        token.setExpired(false);
        token.setRevoked(false);
        token2.setUser(user);

        userRepository.save(user);
        underTest.save(token);
        underTest.save(token2);


        List<Token> allValidTokenByUser = underTest.findAllValidTokenByUser(1L);

        assertEquals(2,allValidTokenByUser.size());
        assertTrue(allValidTokenByUser.containsAll(List.of(token,token2)));
    }
}