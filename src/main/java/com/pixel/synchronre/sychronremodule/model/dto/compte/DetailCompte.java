package com.pixel.synchronre.sychronremodule.model.dto.compte;

import com.pixel.synchronre.typemodule.model.entities.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DetailCompte
{
    private String designation;
    private BigDecimal debit;
    private BigDecimal credit;
    public DetailCompte(String designation)
    {
        this.designation = designation;
        this.debit = BigDecimal.ZERO;
        this.credit = BigDecimal.ZERO;
    }
}
