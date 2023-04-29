package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.sharedmodule.enums.PersStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Devise {
    @Id
    private String devCode;
    private String devLibelle;
    private String devLibelleAbrege;
    private String devSymbole;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "devStatut")
    private Statut statut;



    public Devise(String devCode, String devLibelle, String devLibelleAbrege, String devSymbole, Statut statut) {
        this.devCode = devCode;
        this.devLibelle = devLibelle;
        this.devLibelleAbrege = devLibelleAbrege;
        this.devSymbole = devSymbole;
        this.statut = statut;
    }

}
