package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity
public class CategorieCedante
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAT_CED_ID_GEN")
    @SequenceGenerator(name = "CAT_CED_ID_GEN", sequenceName = "CAT_CED_ID_GEN")
    private Long catCedId;
    @ManyToOne @JoinColumn(name = "categorie_id")
    private Categorie categorie;
    @ManyToOne @JoinColumn(name = "ced_id")
    private Cedante cedante;

    public CategorieCedante(Long catCedId) {
        this.catCedId = catCedId;
    }

    public CategorieCedante(Categorie categorie, Cedante cedante) {
        this.categorie = categorie;
        this.cedante = cedante;
    }

    @Override
    public String toString() {
        return catCedId + "/"+ categorie +"/"+ cedante;
    }
}
