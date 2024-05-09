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

import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RisqueCouvert
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RISQUE_ID_GEN")
    @SequenceGenerator(name = "RISQUE_ID_GEN", sequenceName = "RISQUE_ID_GEN")
    private Long risqueId;
    @ManyToOne
    @JoinColumn(name = "couverture_id")
    private Couverture couverture;
    private String description;
    @ManyToOne @JoinColumn(name = "traite_np_id")
    private TraiteNonProportionnel traiteNonProportionnel;
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

    public RisqueCouvert(Long risqueId) {
        this.risqueId = risqueId;
    }

    @Override
    public String toString() {
        return "RisqueCouvert{" +
                "risqueId=" + risqueId +
                ", description='" + description + '\'' +
                '}';
    }
}