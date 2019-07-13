package com.differz.bc.core;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ORDERS")
public class Order {
    @Id
    @Column(name = "ID")
    private UUID id = UUID.randomUUID();

    @Column(name = "USER_ID")
    private UUID userId;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "ORDER_ITEMS",
            joinColumns = {@JoinColumn(name = "ORDER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "ITEM_ID")}
    )
    private Set<Item> items = new HashSet<>();
}
