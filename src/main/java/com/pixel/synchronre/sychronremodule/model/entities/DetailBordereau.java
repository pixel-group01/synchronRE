package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.typemodule.model.entities.Type;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class DetailBordereau {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEB_ID_GEN")
    @SequenceGenerator(name = "DEB_ID_GEN", sequenceName = "DEB_ID_GEN")
    private Long debId;
    @ManyToOne @JoinColumn(name = "debBordId")
    private Bordereau bordereau;
    @ManyToOne @JoinColumn(name = "debRep")
    private Repartition repartition;
    private BigDecimal debPrime;
    @Column(precision = 50, scale = 20)
    private BigDecimal debTaux;
    @Column(precision = 50, scale = 20)
    private BigDecimal debCommission;
    @Column(precision = 50, scale = 20)
    private BigDecimal debPrimeAreverser;
    private boolean debStatut;
    @ManyToOne @JoinColumn(name = "type_code")
    private Type type;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
