package ru.maxim.effectivemobiletesttask.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.maxim.effectivemobiletesttask.entity.Grade;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.entity.PurchaseHistory;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.repository.GradeRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;

    public void estimateProduct(Product product, User user, Grade grade, Set<PurchaseHistory> purchases) {
        for (PurchaseHistory purchase : purchases) {
            if (purchase.getProduct().equals(product)) {

                Grade resultGrade = new Grade();

                resultGrade.setUser(user);
                resultGrade.setValue(grade.getValue());
                resultGrade.setProduct(product);

                gradeRepository.save(resultGrade);
            }
        }
    }
}
