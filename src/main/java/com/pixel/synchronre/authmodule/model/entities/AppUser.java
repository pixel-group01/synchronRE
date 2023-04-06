package com.pixel.synchronre.authmodule.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class AppUser
{
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_ID_GEN")
    @SequenceGenerator(name = "USER_ID_GEN", sequenceName = "USER_ID_GEN", allocationSize = 10)
    private Long userId;
    private String password;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String tel;
    private boolean active;
    private boolean notBlocked;
    private Long currentFunctionId;
    private LocalDateTime creationDate;
    private LocalDateTime lastModificationDate;

    public AppUser(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppUser)) return false;
        AppUser appUser = (AppUser) o;
        return Objects.equals(getUserId(), appUser.getUserId()) || Objects.equals(getEmail(), appUser.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, password, email, tel, active, notBlocked, currentFunctionId);
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "userId=" + userId +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", tel='" + tel + '\'' +
                ", active=" + active +
                ", notBlocked=" + notBlocked +
                ", currentFunctionId=" + currentFunctionId +
                '}';
    }
}
