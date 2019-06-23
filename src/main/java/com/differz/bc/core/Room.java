package com.differz.bc.core;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ROOMS")
public class Room {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private UUID id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CREATOR_ID")
    private UUID creatorId;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "ROOM_USER",
            joinColumns = {@JoinColumn(name = "ROOM_ID")},
            inverseJoinColumns = {@JoinColumn(name = "USER_ID")}
    )
    private Set<User> users = new HashSet<>();
}
