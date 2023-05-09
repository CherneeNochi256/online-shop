package ru.maxim.effectivemobiletesttask.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@jakarta.persistence.Table(name = "tabl")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Builder
@AllArgsConstructor

public class Table {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String characteristic;
    @Column(name = "table_value")
    private String value;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;


}
