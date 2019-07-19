package com.example.polls.model;

import com.example.polls.model.audit.DateAudit;
import com.example.polls.validation.UniqueEmail;
import com.example.polls.validation.UniqueUsername;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
@Getter
@Setter
@NoArgsConstructor
public class User extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @NotBlank
    @Size(max = 15)
    @UniqueUsername
    private String username;

    @NaturalId
    @NotBlank
    @Size(max = 40)
    @Email
    @UniqueEmail
    private String email;

    @NotBlank
    @Size(max = 100)
    private String password;

    private Date lastLoginDate;

    private Date lastFailedLoginDate;

    private Integer failedLoginAttempts;

    private Boolean locked;

    private Date lockedDate;

    private Boolean emailVerified;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public User(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
