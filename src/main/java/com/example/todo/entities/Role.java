package com.example.todo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @Column(nullable = false)
    private String name;

    // @ManyToMany
    // @JoinTable(
    //     name = "roles_privileges", 
    //     joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), 
    //     inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
    // private Collection<Privilege> privileges;
}
