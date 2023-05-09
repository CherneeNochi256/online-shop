package ru.maxim.effectivemobiletesttask.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.maxim.effectivemobiletesttask.entity.PurchaseHistory;
import ru.maxim.effectivemobiletesttask.entity.User;

import java.util.Optional;
import java.util.Set;

public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory, Long> {

    Optional<Set<PurchaseHistory>> findByUser(User user);

    void removeById(Long id);
}
