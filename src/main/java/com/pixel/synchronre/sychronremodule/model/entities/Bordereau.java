package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder @Entity
public class Bordereau {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BORD_ID_GEN")
    @SequenceGenerator(name = "BORD_ID_GEN", sequenceName = "BORD_ID_GEN")
    private Long BordId;
    private String bordNum;
    @ManyToOne @JoinColumn(name = "repId")
    private Repartition repartition;
    @CreationTimestamp
    protected LocalDateTime createdAt;
    @UpdateTimestamp
    protected LocalDateTime updatedAt;
}
