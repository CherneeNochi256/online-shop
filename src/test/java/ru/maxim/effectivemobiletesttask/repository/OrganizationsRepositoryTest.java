package ru.maxim.effectivemobiletesttask.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.maxim.effectivemobiletesttask.entity.Organization;
import ru.maxim.effectivemobiletesttask.entity.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@DataJpaTest
class OrganizationsRepositoryTest {
    @Autowired
    OrganizationsRepository underTest;

    @Test
    void findByName() {
        //given

        Organization organization = new Organization();
        organization.setName("organization1");

        underTest.save(organization);
        //when

        Organization organizationFromDb = underTest.findByName(organization.getName()).get();

        //then
        assertEquals(organization,organizationFromDb);
    }
}