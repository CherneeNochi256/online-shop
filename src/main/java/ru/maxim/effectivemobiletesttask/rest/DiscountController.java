package ru.maxim.effectivemobiletesttask.rest;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.entity.Discount;
import ru.maxim.effectivemobiletesttask.entity.Product;
import ru.maxim.effectivemobiletesttask.service.DiscountService;
import ru.maxim.effectivemobiletesttask.utils.RestPreconditions;

@RestController
@RequestMapping("api/main/discount")
public class DiscountController {

    private final DiscountService discountService;


    public DiscountController(DiscountService discountService) {

        this.discountService = discountService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{id}")
    public Discount get(@PathVariable("id") Discount discount) {
        return RestPreconditions.checkDiscount(discount);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("{product}")
    public void createForOne(@RequestBody Discount discount,
                             @PathVariable Product product) {
        RestPreconditions.checkNotNull(discount);
        RestPreconditions.checkProduct(product);

        discountService.createDiscountForProduct(product,discount);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("tags/{tag}")
    public void createForGroup(@RequestBody Discount discount,
                               @PathVariable String tag) {
        RestPreconditions.checkNotNull(discount);

        discountService.createDiscountForGroup(discount,tag);

    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{id}")
    public void update(@RequestBody Discount discount,
                       @PathVariable("id") Long discountId) {
        RestPreconditions.checkNotNull(discount);

        discountService.updateDiscount(discount,discountId);

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("tags/{tag}")
    public void updateForGroup(@RequestBody Discount discount,
                               @PathVariable String tag) {
       RestPreconditions.checkNotNull(discount);

       discountService.updateDiscountForGroup(discount,tag);


    }

}

