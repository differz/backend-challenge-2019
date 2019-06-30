package com.differz.bc.core;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "USERS")
public class User {

    @Id
    @Column(name = "ID")
    private UUID id = UUID.randomUUID();

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "CREDENTIALS")
    private String credentials;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = true;

    @Column(name = "DELETED", nullable = false)
    private Boolean deleted = false;

}
