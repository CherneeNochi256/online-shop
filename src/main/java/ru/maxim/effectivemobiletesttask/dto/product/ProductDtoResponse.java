package ru.maxim.effectivemobiletesttask.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.maxim.effectivemobiletesttask.dto.comment.CommentDtoResponseIdMessageUser;
import ru.maxim.effectivemobiletesttask.dto.discount.DiscountDtoResponse;
import ru.maxim.effectivemobiletesttask.dto.organization.OrganizationDtoRequest;
import ru.maxim.effectivemobiletesttask.entity.Product;

import java.io.Serializable;
import java.util.Set;

/**
 * A DTO for the {@link Product} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDtoResponse implements Serializable {
    private Long id;
    private String title;
    private OrganizationDtoRequest organization;
    private Double price;
    private Long quantity;
    private DiscountDtoResponse discount;
    private Set<CommentDtoResponseIdMessageUser> comments;
}