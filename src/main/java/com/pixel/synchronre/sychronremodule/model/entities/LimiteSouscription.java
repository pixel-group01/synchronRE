package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.beans.BeanInfo;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LimiteSouscription
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LIM_ID_GEN")
    @SequenceGenerator(name = "LIM_ID_GEN", sequenceName = "LIM_ID_GEN")
    private Long limiteSouscriptionId;
    private BigDecimal limSousMontant;
    @ManyToOne @JoinColumn(name = "ced_trai_id")
    private CedanteTraite cedanteTraite;
    @ManyToOne @JoinColumn(name = "risque_id")
    private RisqueCouvert risqueCouvert;
    @ManyToOne @JoinColumn(name = "traite_np_id")
    private TraiteNonProportionnel traiteNonProportionnel;
    @ManyToOne @JoinColumn(name = "tranche_ID")
    private Tranche tranche;
    @ManyToOne @JoinColumn(name = "STA_CODE")
    private Statut statut;
    @ManyToOne @JoinColumn(name = "user_creator")
    private AppUser userCreator;
    @ManyToOne @JoinColumn(name = "fon_creator")
    private AppFunction fonCreator;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
