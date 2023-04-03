package ru.maxim.effectivemobiletesttask.entity;

import com.fasterxml.jackson.databind.DatabindException;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@jakarta.persistence.Table(name = "purchase_history")
@Getter
@Setter
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
