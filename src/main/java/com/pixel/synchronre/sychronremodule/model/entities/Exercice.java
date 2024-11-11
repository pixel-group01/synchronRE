package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
@Entity @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Exercice {
    @Id
    private Long exeCode;
    private String exeLibelle;
    private boolean exeCourant;
    @ManyToOne @JoinColumn(name = "sta_code")
    private Statut statut;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Exercice(Long exeCode) {
        this.exeCode = exeCode;
    }
}
