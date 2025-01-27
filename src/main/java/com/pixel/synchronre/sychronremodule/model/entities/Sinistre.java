package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity @Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
public class Sinistre
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SIN_ID_GEN")
    @SequenceGenerator(name = "SIN_ID_GEN", sequenceName = "SIN_ID_GEN")
    private Long sinId;
    private String sinCode;
    @Column(precision = 50, scale = 20)
    private BigDecimal sinMontant100;
    @Column(precision = 50, scale = 20)
    private BigDecimal sinMontantHonoraire;
    private LocalDate sinDateSurvenance;
    private LocalDate  sinDateDeclaration;
    @Column(precision = 50, scale = 20)
    private BigDecimal sinMontantTotPlacement;
    private String sinMontantTotPlacementLettre;
    private String sinCommentaire;

    @ManyToOne @JoinColumn(name = "aff_id")
    private Affaire affaire;
    @ManyToOne @JoinColumn(name = "sta_code") @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
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

    @Override
    public String toString()
    {
        return sinId + "_" + sinCode + "_" + sinMontant100 + "_" + affaire;
    }
}
