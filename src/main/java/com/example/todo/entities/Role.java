package com.example.todo.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String name;
}
