package ru.maxim.effectivemobiletesttask.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.dto.DiscountDto;
import ru.maxim.effectivemobiletesttask.entity.Discount;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.service.DiscountService;
import ru.maxim.effectivemobiletesttask.service.ProductService;
import ru.maxim.effectivemobiletesttask.utils.EntityMapper;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

@RestController
@RequestMapping("api/main/discount")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;
    private final ProductService productService;
    private final EntityMapper entityMapper;


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{id}")
    public DiscountDto.Response get(@PathVariable("id") Long id) {

        Discount discountById = discountService.getDiscountById(id);
        return entityMapper.entityToDiscountDto(RestPreconditions.checkDiscount(discountById));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("{productId}")
    public void createForOne(@RequestBody DiscountDto.Request discountDto,
                             @PathVariable Long productId) {
        RestPreconditions.checkNotNull(discountDto);

        Discount discount = entityMapper.discountDtoToEntity(discountDto);
        Product product = RestPreconditions.checkProduct(productService.productById(productId));

        discountService.createDiscountForProduct(product,discount);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("tags/{tag}")
    public void createForGroup(@RequestBody DiscountDto.Request discountDto,
                               @PathVariable String tag) {
        RestPreconditions.checkNotNull(discountDto);
        Discount discount = entityMapper.discountDtoToEntity(discountDto);

        discountService.createDiscountForGroup(discount,tag);

    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{id}")
    public void update(@RequestBody DiscountDto.Request discountDto,
                       @PathVariable("id") Long discountId) {
        RestPreconditions.checkNotNull(discountDto);
        Discount discount = entityMapper.discountDtoToEntity(discountDto);

        discountService.updateDiscount(discount,discountId);

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("tags/{tag}")
    public void updateForGroup(@RequestBody DiscountDto.Request discountDto,
                               @PathVariable String tag) {
       RestPreconditions.checkNotNull(discountDto);

        Discount discount = entityMapper.discountDtoToEntity(discountDto);

        discountService.updateDiscountForGroup(discount,tag);


    }

}

