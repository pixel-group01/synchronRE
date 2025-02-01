package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.typemodule.model.entities.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.time.LocalDate;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity
public class Periode
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "PERIODE_ID_GEN", sequenceName = "PERIODE_ID_GEN")
    private Long periodeId;
    private LocalDate periode;
    @ManyToOne @JoinColumn(name = "type_id")
    private Type type;
    private String periodeName;

    public Periode(Long periodeId) {
        this.periodeId = periodeId;
    }

    @Override
    public String toString() {
        return periodeId + "_" + periode + "_" + type;
    }
}
