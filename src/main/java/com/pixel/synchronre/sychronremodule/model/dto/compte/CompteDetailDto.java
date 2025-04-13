package com.pixel.synchronre.sychronremodule.model.dto.compte;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CompteDetailDto
{
    private String designation;
    private BigDecimal debit;
    private BigDecimal credit;
    private Long typeId;
    private String uniqueCode;
    public CompteDetailDto(Long typeId, String designation, String uniqueCode)
    {
        this.typeId = typeId;
        this.uniqueCode = uniqueCode;
        this.designation = designation;
        this.debit = BigDecimal.ZERO;
        this.credit = BigDecimal.ZERO;
    }

    public CompteDetailDto(Long typeId, BigDecimal debit, BigDecimal credit, String uniqueCode)
    {
        this.typeId = typeId;
        this.debit = debit;
        this.credit = credit;
        this.uniqueCode = uniqueCode;
    }
}
