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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "password_reset_tokens")
@SequenceGenerator(name = "password_reset_token_seq", initialValue = 1)

public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "password_reset_token_seq")
    @Setter(AccessLevel.NONE)
    private Long id;

    private String token = UUID.randomUUID().toString();;

    @Column(nullable = false)
    private Date createdAt = new Date();

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "USER_ID")
    private User user;

}
