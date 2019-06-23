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
@Table(name = "MESSAGES")
public class Message {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private UUID id;

    @Column(name = "ROOM_ID")
    private UUID roomId;

    @Column(name = "USER_ID")
    private UUID userId;

    @Column(name = "MESSAGE")
    private String message;

}
