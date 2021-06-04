package com.example.todo.entities;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import java.util.Date;
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable<U> {
    @CreatedBy
    private U createdBy;

    @CreatedDate
    private Date createdAt;

    @LastModifiedBy
    private U updatedBy;

    @LastModifiedDate
    private Date updatedAt;
}
