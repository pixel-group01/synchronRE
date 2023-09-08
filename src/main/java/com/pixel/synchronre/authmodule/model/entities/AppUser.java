package com.pixel.synchronre.authmodule.model.entities;

import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class AppUser
{
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_ID_GEN")
    @SequenceGenerator(name = "USER_ID_GEN", sequenceName = "USER_ID_GEN", allocationSize = 10)
    private Long userId;
    private String firstName;
    private String lastName;
    private Long visibilityId; // Id de la cédante. Dans un autre projet ça peut désigner l'ID d'une autre entité
    private Long cesId; // Id du cessionnaire //Seulement valable dans le cadre du projet SynchronRE
    private String password;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String tel;
    private boolean active;
    private boolean notBlocked;
    private Long currentFunctionId;
    private LocalDateTime changePasswordDate;
    @CreationTimestamp
    private LocalDateTime creationDate;
    @UpdateTimestamp
    private LocalDateTime lastModificationDate;
    @ManyToOne
    private Statut statut;

    public AppUser(Long userId, String firstName, String lastName, Long visibilityId, Long cesId, String password, String email, String tel, boolean active, boolean notBlocked, Long currentFunctionId, LocalDateTime changePasswordDate, LocalDateTime creationDate, LocalDateTime lastModificationDate) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.visibilityId = visibilityId;
        this.cesId = cesId;
        this.password = password;
        this.email = email;
        this.tel = tel;
        this.active = active;
        this.notBlocked = notBlocked;
        this.currentFunctionId = currentFunctionId;
        this.changePasswordDate = changePasswordDate;
        this.creationDate = creationDate;
        this.lastModificationDate = lastModificationDate;
    }

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
        return  "userId=" + userId +
                ", nom ='" + firstName + " " + lastName + '\'' +
                ", email='" + email + '\'' +
                ", tel='" + tel + '\'' +
                ", active=" + active +
                ", notBlocked=" + notBlocked +
                ", currentFunctionId=" + currentFunctionId ;
    }
}
