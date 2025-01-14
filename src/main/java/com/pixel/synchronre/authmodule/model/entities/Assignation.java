package com.pixel.synchronre.authmodule.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.time.LocalDate;

@Entity @DiscriminatorColumn(name = "ASS_TYPE") @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
public class Assignation
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ASS_ID_GEN")
    @SequenceGenerator(name = "ASS_ID_GEN", sequenceName = "ASS_ID_GEN", allocationSize = 10)
    protected Long assId;
    protected int assStatus;// 1 == actif, 2 == inactif, 3 == revoke
    protected LocalDate startsAt;
    protected LocalDate endsAt;
}
