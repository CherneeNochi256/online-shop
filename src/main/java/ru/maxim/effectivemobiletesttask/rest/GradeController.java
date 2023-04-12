package ru.maxim.effectivemobiletesttask.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.dto.GradeDto;
import ru.maxim.effectivemobiletesttask.entity.Grade;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.entity.PurchaseHistory;
import ru.maxim.effectivemobiletesttask.entity.User;
import ru.maxim.effectivemobiletesttask.service.GradeService;
import ru.maxim.effectivemobiletesttask.service.ProductService;
import ru.maxim.effectivemobiletesttask.service.PurchaseHistoryService;
import ru.maxim.effectivemobiletesttask.utils.EntityMapper;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

import java.util.Set;

@RestController
@RequestMapping("api/main/grade")
@RequiredArgsConstructor
public class GradeController {
    private final ProductService productService;
    private final PurchaseHistoryService purchaseHistoryService;
    private final GradeService gradeService;
    private final EntityMapper entityMapper;


    @PostMapping("{id}")
    public void estimate(@PathVariable("id") Long id,
                         @AuthenticationPrincipal User user,
                         @RequestBody GradeDto.Request gradeDto) {

        RestPreconditions.checkNotNull(gradeDto);

        Set<PurchaseHistory> purchases = RestPreconditions.checkPurchaseHistory(purchaseHistoryService.findByUser(user));
        Product product = RestPreconditions.checkProduct(productService.productById(id));
        Grade grade = entityMapper.gradeDtoToEntity(gradeDto);

        gradeService.estimateProduct(product, user, grade, purchases);
    }
}
