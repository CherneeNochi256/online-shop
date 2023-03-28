package ru.maxim.effectivemobiletesttask.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Set;


@Entity
@Table(name = "organization")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;


    @OneToMany(mappedBy = "organizations", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<Product> products;

}
