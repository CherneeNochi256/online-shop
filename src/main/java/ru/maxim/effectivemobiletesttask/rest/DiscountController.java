package ru.maxim.effectivemobiletesttask.rest;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.dto.discount.DiscountDtoRequest;
import ru.maxim.effectivemobiletesttask.dto.discount.DiscountDtoResponse;
import ru.maxim.effectivemobiletesttask.entity.Discount;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.service.DiscountService;
import ru.maxim.effectivemobiletesttask.service.ProductService;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

@RestController
@RequestMapping("api/main/discount")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;
    private final ProductService productService;
    private final ModelMapper mapper;


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{id}")
    public DiscountDtoResponse get(@PathVariable("id") Long id) {

        Discount discountById = discountService.getDiscountById(id);
        Discount discount = RestPreconditions.checkDiscount(discountById);
        return mapper.map(discount, DiscountDtoResponse.class);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("{productId}")
    public void createForOne(@RequestBody DiscountDtoRequest discountDto,
                             @PathVariable Long productId) {
        RestPreconditions.checkNotNull(discountDto);

        Discount discount = mapper.map(discountDto, Discount.class);
        Product product = RestPreconditions.checkProduct(productService.productById(productId));

        discountService.createDiscountForProduct(product, discount);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("tags/{tag}")
    public void createForGroup(@RequestBody DiscountDtoRequest discountDto,
                               @PathVariable String tag) {
        RestPreconditions.checkNotNull(discountDto);
        Discount discount = mapper.map(discountDto, Discount.class);

        discountService.createDiscountForGroup(discount, tag);

    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{id}")
    public void update(@RequestBody DiscountDtoRequest discountDto,
                       @PathVariable("id") Long discountId) {
        RestPreconditions.checkNotNull(discountDto);
        Discount discount = mapper.map(discountDto, Discount.class);

        discountService.updateDiscount(discount, discountId);

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("tags/{tag}")
    public void updateForGroup(@RequestBody DiscountDtoRequest discountDto,
                               @PathVariable String tag) {
        RestPreconditions.checkNotNull(discountDto);

        Discount discount = mapper.map(discountDto, Discount.class);

        discountService.updateDiscountForGroup(discount, tag);


    }

}

