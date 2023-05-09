package ru.maxim.effectivemobiletesttask.dto.purchaseHistory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.maxim.effectivemobiletesttask.dto.product.ProductDtoResponseIdAndTitle;
import ru.maxim.effectivemobiletesttask.dto.user.UserDtoResponseIdAndUsername;
import ru.maxim.effectivemobiletesttask.entity.PurchaseHistory;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link PurchaseHistory} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseHistoryDtoResponse implements Serializable {
    private Long id;
    private ProductDtoResponseIdAndTitle product;
    private UserDtoResponseIdAndUsername user;
    private Date date;
}