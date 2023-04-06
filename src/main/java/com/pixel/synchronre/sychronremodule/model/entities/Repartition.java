package com.pixel.synchronre.sychronremodule.model.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "typRep")
public abstract class Repartition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long repId;
    protected String repInterlocuteur;
    protected boolean repStatut;
    @ManyToOne @JoinColumn(name = "cessionnaire_id")
    protected Cessionnaire cessionnaire;
    @ManyToOne @JoinColumn(name = "libelle_id")
    protected Libelle libelle;
    @CreationTimestamp
    protected LocalDateTime createdAt;
    @UpdateTimestamp
    protected LocalDateTime updatedAt;
}
