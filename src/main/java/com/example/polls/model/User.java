package com.example.polls.model;

import com.example.polls.model.audit.DateAudit;
import com.example.polls.validation.UniqueEmail;
import com.example.polls.validation.UniqueUsername;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@UniqueUsername
@UniqueEmail
public class User extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @NotBlank
    @Size(max = 15)
    @Column(unique = true)
    private String username;

    @NaturalId
    @NotBlank
    @Size(max = 40)
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(max = 100)
    private String password;

    private Date lastLoginDate;

    private Date lastFailedLoginDate;

    private Integer failedLoginAttempts;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean locked = false;

    private Date lockedDate;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean emailVerified = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public User(String name, String username, String email, String password, Set<Role> roles) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public void newLogin() {
        lastLoginDate = new Date();
    }

    public void newFailedLogin() {
        if (failedLoginAttempts == null) {
            failedLoginAttempts = 0;
        }
        failedLoginAttempts++;
        lastFailedLoginDate = new Date();
    }

    public void lock() {
        locked = true;
        lockedDate = new Date();
    }

    public void unlock() {
        locked = false;
    }

    public void verifyEmail() {
        emailVerified = true;
    }
}
