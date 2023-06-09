package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.typemodule.model.entities.Type;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder @Entity
public class Bordereau {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BORD_ID_GEN")
    @SequenceGenerator(name = "BORD_ID_GEN", sequenceName = "BORD_ID_GEN")
    private Long BordId;
    @Column(unique = true)
    private String bordNum;
    private String bordStatut;
    private BigDecimal bordMtTotNoteDebit;
    @ManyToOne @JoinColumn(name = "repId")
    private Repartition repartition;
    @ManyToOne @JoinColumn(name = "affId")
    private Affaire affaire;
    @ManyToOne @JoinColumn(name = "type_code")
    private Type type;
    @CreationTimestamp
    protected LocalDateTime createdAt;
    @UpdateTimestamp
    protected LocalDateTime updatedAt;
}
