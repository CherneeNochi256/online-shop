package ru.maxim.effectivemobiletesttask.entity;

import jakarta.persistence.*;
import lombok.*;


import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@jakarta.persistence.Table(name = "product")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String title;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;
    private Double price;
    private Long quantity;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "discount_id")
    private Discount discount;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Comment> comments;
    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Tag> tags;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product")
    private Set<Table> tables;
    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Grade> grades;


    @OneToOne(optional = false, mappedBy="product")
    private PurchaseHistory purchaseHistory;

}
