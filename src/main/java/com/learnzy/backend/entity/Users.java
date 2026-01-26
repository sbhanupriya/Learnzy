package com.learnzy.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USER_ID")
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    @OneToMany(mappedBy="user")
    private List<Enrollment> enrollmentList;
}
