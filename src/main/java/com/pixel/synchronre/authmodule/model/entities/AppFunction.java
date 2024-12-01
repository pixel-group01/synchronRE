package com.pixel.synchronre.authmodule.model.entities;

import com.pixel.synchronre.typemodule.model.entities.Type;
import lombok.*;

import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
public class AppFunction
{
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FNC_ID_GEN")
    @SequenceGenerator(name = "FNC_ID_GEN", sequenceName = "FNC_ID_GEN", allocationSize = 10)
    protected Long id;
    private Long visibilityId;// Id de la cédante. Dans un autre projet ça peut désigner l'ID d'une autre entité
    private Long cesId; // Id du cessionnaire //Seulement valable dans le cadre du projet SynchronRE
    private String name;
    @ManyToOne @JoinColumn(name = "USER_ID")
    private AppUser user;
    @ManyToOne @JoinColumn(name = "TYPE_ID")
    private Type typeFunction;
    protected int fncStatus;// 1 == actif, 2 == inactif, 3 == revoke
    protected LocalDate startsAt;
    protected LocalDate endsAt;

    @Override
    public String toString() {
        return id + "_" + name + "_"+ user + "_" + typeFunction;
    }

    public AppFunction(Long fncId) {
        this.id = fncId;
    }
}
