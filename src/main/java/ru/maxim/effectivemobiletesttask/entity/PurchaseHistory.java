package ru.maxim.effectivemobiletesttask.entity;

import com.fasterxml.jackson.databind.DatabindException;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "purchase_history")
@Getter
@Builder
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class PurchaseHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @OneToOne
    @JoinColumn(name="user_id", nullable = false, updatable = false)
    private User user;


    @OneToOne
    @JoinColumn(name="product_id", nullable = false, updatable = false)
    private Product product;

    private Date date;
}
