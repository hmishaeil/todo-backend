package com.example.todo.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
public class User extends Auditable<String> implements Serializable {

  private static final long serialVersionUID = 4865903039190150223L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, updatable = false)
  private long id;

  @Column(nullable = false, unique = true)
  private String username;

  private String firstName;
  private String lastName;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private boolean isEnabled;

  @Column(nullable = true)
  private Date verifiedAt;

  @Column(nullable = true)
  private String internalNote;

  @JsonManagedReference
  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<Todo> todos;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  private Collection<Role> roles;

}