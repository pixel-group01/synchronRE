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
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LimiteSouscription
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LIM_ID_GEN")
    @SequenceGenerator(name = "LIM_ID_GEN", sequenceName = "LIM_ID_GEN")
    private Long limiteSouscriptionId;
    private BigDecimal limSousMontant;
    @ManyToOne @JoinColumn(name = "risque_id")
    private RisqueCouvert risqueCouvert;
    @ManyToOne @JoinColumn(name = "categorie_id")
    private Categorie categorie;
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

    @Override
    public String toString() {
        return limiteSouscriptionId + "_" + risqueCouvert + "_" + categorie;
    }
}
