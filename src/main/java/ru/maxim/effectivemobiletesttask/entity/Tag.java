package ru.maxim.effectivemobiletesttask.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;




@Entity
@Getter
@Table(name = "tag")
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Builder
@AllArgsConstructor

public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String tag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

}