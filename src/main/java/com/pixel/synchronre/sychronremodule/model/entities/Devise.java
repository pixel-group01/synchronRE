package com.pixel.synchronre.sychronremodule.model.entities;

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
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "devStatut")
    private Statut statut;
}
