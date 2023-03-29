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
@ToString
@EqualsAndHashCode
public class PurchaseHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @OneToOne(optional = false)
    @JoinColumn(name="user_id", unique = true, nullable = false, updatable = false)
    private User user;


    @OneToOne(optional = false)
    @JoinColumn(name="product_id", unique = true, nullable = false, updatable = false)
    private Product product;

    private Date date;
}
