package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.sychronremodule.model.enums.FORME;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity
public class Nature {
    @Id
    private String natCode;
    private String natLibelle;
    @Enumerated(EnumType.STRING)
    private FORME forme;
    @ManyToOne @JoinColumn(name = "nat_sta_code")
    private Statut statut;

    public Nature(String natCode) {
        this.natCode = natCode;
    }
}
