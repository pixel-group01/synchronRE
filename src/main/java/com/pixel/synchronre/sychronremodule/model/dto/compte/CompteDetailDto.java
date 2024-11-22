package com.pixel.synchronre.sychronremodule.model.dto.compte;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CompteDetailDto
{
    private String designation;
    private BigDecimal debit;
    private BigDecimal credit;
    private Long typeId;
    public CompteDetailDto(Long typeId, String designation)
    {
        this.typeId = typeId;
        this.designation = designation;
        this.debit = BigDecimal.ZERO;
        this.credit = BigDecimal.ZERO;
    }
}
