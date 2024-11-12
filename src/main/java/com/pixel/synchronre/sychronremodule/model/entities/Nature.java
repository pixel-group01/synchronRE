package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.sychronremodule.model.enums.FORME;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Nature {
    @Id
    private String natCode;
    private String natLibelle;
    @Enumerated(EnumType.STRING)
    private FORME forme;
    @ManyToOne @JoinColumn(name = "bran_id")
    private Branche branche;
    @ManyToOne @JoinColumn(name = "nat_sta_code")
    private Statut statut;

    public Nature(String natCode) {
        this.natCode = natCode;
    }
}
