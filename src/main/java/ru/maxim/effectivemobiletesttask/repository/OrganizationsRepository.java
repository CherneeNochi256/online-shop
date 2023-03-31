package ru.maxim.effectivemobiletesttask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.maxim.effectivemobiletesttask.entity.Organization;

import java.util.Optional;

public interface OrganizationsRepository extends JpaRepository<Organization, Long> {
    Optional<Organization> findByName(String name);
}
