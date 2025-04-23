package com.pixel.synchronre.sychronremodule.model.dto.compte;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CompteDetailDto
{
    private Long compteDetId;
    private String designation;
    private BigDecimal debit;
    private BigDecimal credit;
    private Long typeId;
    private String uniqueCode;
    private int order;
    private boolean debitDisabled;
    private boolean creditDisabled;
    public CompteDetailDto(Long typeId, String designation, String uniqueCode, int order, boolean debitDisabled, boolean creditDisabled)
    {
        this.typeId = typeId;
        this.uniqueCode = uniqueCode;
        this.designation = designation;
        this.debit = BigDecimal.ZERO;
        this.credit = BigDecimal.ZERO;
        this.order = order;
        this.debitDisabled = debitDisabled;
        this.creditDisabled = creditDisabled;
    }

    public CompteDetailDto(Long typeId, String designation, BigDecimal debit, BigDecimal credit, String uniqueCode, int order, boolean debitDisabled, boolean creditDisabled)
    {
        this.typeId = typeId;
        this.designation = designation;
        this.debit = debit;
        this.credit = credit;
        this.uniqueCode = uniqueCode;
        this.order = order;
        this.debitDisabled = debitDisabled;
        this.creditDisabled = creditDisabled;
    }
}
