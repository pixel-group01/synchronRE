package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Territorialite
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TERR_ID_GEN")
    @SequenceGenerator(name = "TERR_ID_GEN", sequenceName = "TERR_ID_GEN")
    private Long terrId;
    private String terrLibelle;
    private BigDecimal terrTaux;
    @Column(length=4000)
    private String terrDescription;
    @ManyToOne @JoinColumn(name = "traite_np_id")
    private TraiteNonProportionnel traiteNonProportionnel;
    @ManyToOne
    @JoinColumn(name = "STA_CODE")
    protected Statut statut;
    @ManyToOne @JoinColumn(name = "user_creator")
    protected AppUser userCreator;
    @ManyToOne @JoinColumn(name = "fon_creator")
    protected AppFunction fonCreator;
    @CreationTimestamp
    protected LocalDateTime createdAt;
    @UpdateTimestamp
    protected LocalDateTime updatedAt;

    public Territorialite(String terrLibelle, BigDecimal terrTaux,
                          String terrDescription,
                          TraiteNonProportionnel traiteNonProportionnel,
                           Statut statut,
                          AppUser userCreator, AppFunction fonCreator)
    {
        this.terrLibelle = terrLibelle;
        this.terrTaux = terrTaux;
        this.terrDescription = terrDescription;
        this.traiteNonProportionnel = traiteNonProportionnel;
        this.statut = statut;
        this.userCreator = userCreator;
        this.fonCreator = fonCreator;
    }

    public Territorialite(Long terrId) {
        this.terrId = terrId;
    }
}