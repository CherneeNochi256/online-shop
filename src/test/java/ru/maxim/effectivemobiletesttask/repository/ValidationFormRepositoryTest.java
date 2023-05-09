package ru.maxim.effectivemobiletesttask.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.maxim.effectivemobiletesttask.AbstractTestcontainers;
import ru.maxim.effectivemobiletesttask.entity.Organization;
import ru.maxim.effectivemobiletesttask.entity.ValidationForm;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ValidationFormRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private OrganizationsRepository organizationsRepository;
    @Autowired
    private ValidationFormRepository underTest;

    @Test
    void findAllCustom() {
        Organization organization = new Organization();
        organization.setStatus("ACTIVE");
        organization.setName("Name");

        ValidationForm form = new ValidationForm();
        form.setOrganization(organization);

        Organization organization2 = new Organization();
        organization.setStatus("ACTIVE");
        organization.setName("Name");

        ValidationForm form2 = new ValidationForm();
        form.setOrganization(organization2);

        organizationsRepository.save(organization);
        organizationsRepository.save(organization2);

        underTest.save(form);
        underTest.save(form2);


        PageRequest pageRequest = PageRequest.of(0, 4);

        Set<ValidationForm> allCustom = underTest.findAllCustom(pageRequest).orElse(null);

        assertEquals(2,allCustom.size());
        assertTrue(allCustom.containsAll(List.of(form,form2)));
    }
}