package ru.maxim.effectivemobiletesttask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.maxim.effectivemobiletesttask.entity.Organization;

public interface OrganizationsRepository extends JpaRepository<Organization, Long> {
}
