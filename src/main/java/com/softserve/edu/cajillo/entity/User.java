package com.softserve.edu.cajillo.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import com.softserve.edu.cajillo.entity.enums.UserAccountStatus;
import lombok.*;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Size(min = 3, max = 20)
    @Column(name = "username", nullable = false, unique = true, length = 20)
    private String username;

    @Size(max = 40, message = "Email size < 40")
    @Email
    @Column(name = "email", unique = true, length = 40)
    private String email;

    @ToString.Exclude
    @Column(name = "password", length = 75)
    private String password;

    @Column(name = "first_name", length = 40)
    private String firstName;

    @Column(name = "last_name", length = 40)
    private String lastName;

    @Column(name = "avatar", columnDefinition = "TEXT")
    private String avatar;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status")
    private UserAccountStatus accountStatus = UserAccountStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "signup_type")
    private SignupType signupType;

    public enum SignupType{
        GENERAL,
        GOOGLE,
        GITHUB
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "chosen_language")
    private ChosenLanguage chosenLanguage;

    public enum ChosenLanguage {
        en,
        uk,
        ge,
        fr
    }

    public User(String username, String email, String password, SignupType signupType, ChosenLanguage chosenLanguage) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.signupType = signupType;
        this.chosenLanguage = chosenLanguage;
    }
}
