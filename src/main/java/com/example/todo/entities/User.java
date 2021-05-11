package com.example.todo.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User extends Auditable<String> implements Serializable{

    private static final long serialVersionUID = 4865903039190150223L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private long userId;

    @Column(nullable = false, unique = true)
    @Getter
    @Setter
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    private String firstName;
    private String lastName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean isEnabled;

    @Column(nullable = false)
    private boolean emailVerified;

    @OneToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "CT_ID")
    private ConfirmationToken confirmationToken;

    @OneToMany
    @JoinTable(name = "USER_TODOS",
            joinColumns = {@JoinColumn(name = "USER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "TODO_ID")}
    )
    private List<Todo> todos;
 
    @OneToMany
    @JoinTable(name = "USER_ROLES",
            joinColumns = {@JoinColumn(name = "USER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID")}
    )
    private Set<Role> roles;

}