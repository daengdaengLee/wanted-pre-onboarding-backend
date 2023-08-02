package io.github.daengdaenglee.wantedpreonboardingbackend.user.domain;

import jakarta.persistence.*;

@Entity
@Table(indexes = {@Index(columnList = "email")})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    protected User() {
    }

    public User(Email email, EncodedPassword password) {
        this.email = email.email();
        this.password = password.password();
    }

    public Long id() {
        return this.id;
    }

    public String email() {
        return this.email;
    }

    public String password() {
        return this.password;
    }

}
