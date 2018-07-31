package com.softserve.edu.cajillo.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
public class User extends DateAudit {

    @NotBlank
    @Size(min = 3, max = 20)
    @Column(name = "username", nullable = false, unique = true, length = 20)
    private String username;

    @NotBlank
    @Size(max = 40, message = "Email size < 40")
    @Email
    @Column(name = "email", unique = true, nullable = false, length = 40)
    private String email;

    @NotBlank
    @Size(min = 8, max = 32)
    @Column(name = "password", nullable = false, length = 32)
    private String password;

    @Column(name = "first_name", length = 40)
    private String firstName;

    @Column(name = "last_name", length = 40)
    private String lastName;

    @Column(name = "avatar", columnDefinition = "TEXT")
    private String avatar;
}