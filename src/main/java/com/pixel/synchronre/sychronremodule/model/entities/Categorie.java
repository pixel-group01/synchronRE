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
import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder @Entity
@Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAT_ID_GEN")
    @SequenceGenerator(name = "CAT_ID_GEN", sequenceName = "CAT_ID_GEN")
    private Long categorieId;
    private String categorieLibelle;
    private BigDecimal categorieCapacite;
    @ManyToOne @JoinColumn(name = "traite_np_id")
    private TraiteNonProportionnel traiteNonProportionnel;
    @ManyToOne @JoinColumn(name = "STA_CODE") @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Statut statut;
    @ManyToOne @JoinColumn(name = "user_creator")
    private AppUser userCreator;
    @ManyToOne @JoinColumn(name = "fon_creator")
    private AppFunction fonCreator;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    public Categorie(String categorieLibelle, BigDecimal categorieCapacite) {
        this.categorieLibelle = categorieLibelle;
        this.categorieCapacite = categorieCapacite;
    }

    public Categorie(Long categorieId) {
        this.categorieId = categorieId;
    }

    @Override
    public String toString() {
        return  categorieId +"_" + categorieLibelle;
    }
}
