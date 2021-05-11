package com.example.todo.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private Long id;

    private String token = UUID.randomUUID().toString();;

    @Column(nullable = false)
    private Date createdAt = new Date();

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

}
