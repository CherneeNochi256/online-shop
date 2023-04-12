package ru.maxim.effectivemobiletesttask.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.maxim.effectivemobiletesttask.entity.PurchaseHistory;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.repository.PurchaseHistoryRepository;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PurchaseHistoryService {
    private final PurchaseHistoryRepository purchaseHistoryRepository;


    public Set<PurchaseHistory> findByUser(User user){
        return purchaseHistoryRepository.findByUser(user).orElse(null);
    }

}
