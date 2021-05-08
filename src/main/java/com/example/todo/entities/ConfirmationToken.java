package com.example.todo.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "confirmation_tokens")
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long ctId;

    @Column(unique=true)
    private String confirmationToken = UUID.randomUUID().toString();

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate = new Date();

}
