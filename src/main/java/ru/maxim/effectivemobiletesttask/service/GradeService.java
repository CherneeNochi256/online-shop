package ru.maxim.effectivemobiletesttask.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.maxim.effectivemobiletesttask.dto.grade.GradeDtoRequest;
import ru.maxim.effectivemobiletesttask.dto.grade.GradeDtoResponse;
import ru.maxim.effectivemobiletesttask.entity.Grade;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.entity.PurchaseHistory;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.exception.CanNotPerformActionException;
import ru.maxim.effectivemobiletesttask.exception.ResourceNotFoundException;
import ru.maxim.effectivemobiletesttask.repository.GradeRepository;
import ru.maxim.effectivemobiletesttask.repository.ProductRepository;
import ru.maxim.effectivemobiletesttask.repository.PurchaseHistoryRepository;

import java.util.Set;

import static ru.maxim.effectivemobiletesttask.utils.AppConstants.ID;
import static ru.maxim.effectivemobiletesttask.utils.AppConstants.PRODUCT;

@Service
@RequiredArgsConstructor
public class GradeService {
    private static final String YOU_CAN_NOT_GRADE_THIS_PRODUCT = "You can't grade this product, because you didn't buy it";

    private final GradeRepository gradeRepository;
    private final ModelMapper mapper;
    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final ProductRepository productRepository;

    public ResponseEntity<GradeDtoResponse> estimateProduct(Long productId, User user, GradeDtoRequest gradeDto) {

        Set<PurchaseHistory> purchases = purchaseHistoryRepository.findByUser(user)
                .orElseThrow(() -> new CanNotPerformActionException(YOU_CAN_NOT_GRADE_THIS_PRODUCT));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT, ID, productId));

        for (PurchaseHistory purchase : purchases) {
            if (purchase.getProduct().equals(product)) {


                Grade grade = mapper.map(gradeDto, Grade.class);
                grade.setUser(user);
                grade.setProduct(product);

                Grade savedGrade = gradeRepository.save(grade);

                GradeDtoResponse response = mapper.map(savedGrade, GradeDtoResponse.class);

                return new ResponseEntity<>(response, HttpStatus.CREATED);
            }
        }
        throw new CanNotPerformActionException(YOU_CAN_NOT_GRADE_THIS_PRODUCT);
    }
}
