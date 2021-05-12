package com.example.todo.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User extends Auditable<String> implements Serializable{

    private static final long serialVersionUID = 4865903039190150223L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private long id;

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

    @Column(nullable = true)
    private Date verified_at;

    @OneToMany
    @JoinTable(name = "USERS_TODOS",
            joinColumns = {@JoinColumn(name = "USER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "TODO_ID")}
    )
    private List<Todo> todos;
 
    @ManyToMany 
    @JoinTable( 
        name = "users_roles", 
        joinColumns = @JoinColumn(
          name = "user_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id")) 
    private Collection<Role> roles;

//     @OneToMany
//     @JoinTable(name = "USERS_ROLES",
//             joinColumns = {@JoinColumn(name = "USER_ID")},
//             inverseJoinColumns = {@JoinColumn(name = "ROLE_ID")}
//     )
//     private Set<Role> roles;

}