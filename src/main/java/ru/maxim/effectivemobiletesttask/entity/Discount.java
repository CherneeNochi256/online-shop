package ru.maxim.effectivemobiletesttask.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "discount")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Builder
@AllArgsConstructor

public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToMany(mappedBy = "discount",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Product> products;
    private Double discount;
    @Column(name = "discount_interval")
    private Long interval;


}
