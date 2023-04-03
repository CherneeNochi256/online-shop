package ru.maxim.effectivemobiletesttask.service;

import org.springframework.stereotype.Service;
import ru.maxim.effectivemobiletesttask.entity.Organization;
import ru.maxim.effectivemobiletesttask.repository.OrganizationsRepository;

@Service
public class OrganizationService {
    private final OrganizationsRepository organizationsRepository;

    public OrganizationService(OrganizationsRepository organizationsRepository) {
        this.organizationsRepository = organizationsRepository;
    }

    public Organization organizationById(Long id){
        return organizationsRepository.findById(id).get();
    }
}
