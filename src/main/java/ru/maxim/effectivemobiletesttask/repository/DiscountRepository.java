package ru.maxim.effectivemobiletesttask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.maxim.effectivemobiletesttask.entity.Discount;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

}
