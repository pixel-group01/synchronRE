package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity
public class Organisation {
    @Id
    private String organisationCode;
    private String organisationLibelle;
    @ManyToOne
    @JoinColumn(name = "STA_CODE")
    protected Statut statut;
    @ManyToOne @JoinColumn(name = "user_creator")
    protected AppUser userCreator;
    @ManyToOne @JoinColumn(name = "fon_creator")
    protected AppFunction fonCreator;
    @CreationTimestamp
    protected LocalDateTime createdAt;
    @UpdateTimestamp
    protected LocalDateTime updatedAt;

    public Organisation(String organisationCode) {
        this.organisationCode = organisationCode;
    }
}
