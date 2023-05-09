package ru.maxim.effectivemobiletesttask.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.maxim.effectivemobiletesttask.entity.Organization;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.entity.ValidationForm;

import java.util.Optional;
import java.util.Set;

public interface ValidationFormRepository extends JpaRepository<ValidationForm, Long> {
    Optional<ValidationForm> findByOrganization(Organization organization);


    @Query("from ValidationForm")
    Optional<Set<ValidationForm>> findAllCustom(PageRequest pageRequest);

}