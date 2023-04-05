package com.pixel.synchronre.sychronRe.model.entities;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "affType")
public abstract class Affaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long affId;
    protected String affCode;
    protected String affAssure;
    protected String affActivite;
    protected LocalDate affDateEffet;
    protected LocalDate affDateEcheance;
    protected int affEtat;
    @ManyToOne @JoinColumn(name = "cedente_id")
    protected Cedente cedente;
    @ManyToOne @JoinColumn(name = "statut_code")
    protected Statut statut;
    @ManyToOne @JoinColumn(name = "couverture_id")
    protected Couverture couverture;
    @CreationTimestamp
    protected LocalDateTime createdAt;
    @UpdateTimestamp
    protected LocalDateTime updatedAt;
}
