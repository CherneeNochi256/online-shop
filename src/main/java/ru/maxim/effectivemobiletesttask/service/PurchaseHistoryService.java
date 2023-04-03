package ru.maxim.effectivemobiletesttask.service;

import org.springframework.stereotype.Service;
import ru.maxim.effectivemobiletesttask.entity.PurchaseHistory;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.repository.PurchaseHistoryRepository;

import java.util.Set;

@Service
public class PurchaseHistoryService {
    private final PurchaseHistoryRepository purchaseHistoryRepository;

    public PurchaseHistoryService(PurchaseHistoryRepository purchaseHistoryRepository) {
        this.purchaseHistoryRepository = purchaseHistoryRepository;
    }

    public Set<PurchaseHistory> findByUser(User user){
        return purchaseHistoryRepository.findByUser(user).get();
    }
}
