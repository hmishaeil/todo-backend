package com.example.todo.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "email_verifications")
@NamedNativeQuery(name = "EmailVerification.getUser", 
                  query = "select * from users u inner join email_verifications c on u.id = c.user_id where c.confirmation_token = ?1", 
                  resultClass = User.class)

public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    private String confirmationToken = UUID.randomUUID().toString();

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date confirmedAt;

    @OneToOne(targetEntity = User.class, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;
}
