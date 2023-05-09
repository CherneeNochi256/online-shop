package ru.maxim.effectivemobiletesttask.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.maxim.effectivemobiletesttask.dto.discount.DiscountDtoRequest;
import ru.maxim.effectivemobiletesttask.dto.discount.DiscountDtoResponse;
import ru.maxim.effectivemobiletesttask.service.DiscountService;

@RestController
@RequestMapping("api/v1/discounts")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{id}")
    public ResponseEntity<DiscountDtoResponse> get(@PathVariable("id") Long id) {
        return discountService.getDiscountById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<DiscountDtoResponse> createForOne(@RequestBody @Valid DiscountDtoRequest discountDto,
                                                            @RequestParam Long productId) {
        return discountService.createDiscountForProduct(discountDto, productId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("products")
    public ResponseEntity<DiscountDtoResponse> createForGroup(@RequestBody @Valid DiscountDtoRequest discountDto,
                                                              @RequestParam String tag) {
        return discountService.createDiscountForGroup(discountDto, tag);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<DiscountDtoResponse> update(@RequestBody @Valid DiscountDtoRequest discountDto,
                                                      @PathVariable("id") Long discountId) {
        return discountService.updateDiscount(discountDto, discountId);

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping
    public ResponseEntity<DiscountDtoResponse> updateForGroup(@RequestBody @Valid DiscountDtoRequest discountDto,
                                                              @RequestParam String tag) {
        return discountService.updateDiscountForGroup(discountDto, tag);
    }

}

