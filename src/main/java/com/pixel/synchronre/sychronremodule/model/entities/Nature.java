package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Nature {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NAT_ID_GEN")
    @SequenceGenerator(name = "NAT_ID_GEN", sequenceName = "NAT_ID_GEN")
    private Long natId;
    private String natCode;
    private String natLibelle;
    @ManyToOne @JoinColumn(name = "nat_forme_code")
    private Forme forme;
    @ManyToOne @JoinColumn(name = "nat_sta_code")
    private Statut statut;
}
