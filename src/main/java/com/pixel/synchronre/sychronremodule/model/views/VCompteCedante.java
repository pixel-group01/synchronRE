package com.pixel.synchronre.sychronremodule.model.views;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "v_compte_cedante")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class VCompteCedante
{
    @Id
    private Long rId;
    private Long compteCedId;
    private Long traiteNpId;
    private Long trancheId;
    private Long cedId;
    private Long compteId;
    private String traiReference;
    private String cedNomFiliale;
    private Double trancheTauxPrime;
    private String trancheLibelle;
    private Double primeApresAjustement;
}
