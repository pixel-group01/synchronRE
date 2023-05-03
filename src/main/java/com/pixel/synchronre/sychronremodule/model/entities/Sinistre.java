package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Data @Builder @Entity
public class Sinistre
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SIN_ID_GEN")
    @SequenceGenerator(name = "SIN_ID_GEN", sequenceName = "SIN_ID_GEN")
    private Long sinId;
    private String sinCode;
    private BigDecimal sinMontant100;
    private BigDecimal sinMontantHonoraire;
    private LocalDate sinDateSurvenance;
    private LocalDate  sinDateDeclaration;
    private String sinCommentaire;

    @ManyToOne @JoinColumn(name = "aff_id")
    private Affaire affaire;
    @ManyToOne @JoinColumn(name = "sta_code")
    private Statut statut;
    @ManyToOne @JoinColumn(name = "user_id")
    private AppUser userCreator;
    @ManyToOne @JoinColumn(name = "function_id")
    private AppFunction functionCreator;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Sinistre(Long sinId) {
        this.sinId = sinId;
    }
}
