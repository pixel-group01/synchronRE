package com.pixel.synchronre.sychronremodule.model.dto.compte;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class CompteDetailsItems
{
    private Long compteDetailsId;
    private Long compteCedId;
    private Long trancheIdSelected;
    private Long periodeId;
    private Long cedIdSelected;
    private BigDecimal primeOrigine;
    private BigDecimal primeApresAjustement;
    private BigDecimal sinistrePaye;
    private BigDecimal depotSapConst;
    private BigDecimal depotSapLib;
    private BigDecimal interetDepotLib;
    private BigDecimal sousTotalDebit;
    private BigDecimal sousTotalCredit;
    private BigDecimal soldeCedante;
    private BigDecimal soldeRea;
    private BigDecimal totalMouvement;

    public CompteDetailsItems(BigDecimal primeOrigine, BigDecimal primeApresAjustement, BigDecimal sinistrePaye,
                              BigDecimal depotSapConst, BigDecimal depotSapLib, BigDecimal interetDepotLib)
    {
        this.primeOrigine = primeOrigine;
        this.primeApresAjustement = primeApresAjustement;
        this.sinistrePaye = sinistrePaye;
        this.depotSapConst = depotSapConst;
        this.depotSapLib = depotSapLib;
        this.interetDepotLib = interetDepotLib;
    }
    public CompteDetailsItems(Long compteCedId, BigDecimal primeOrigine, BigDecimal primeApresAjustement, BigDecimal sinistrePaye,
                              BigDecimal depotSapConst, BigDecimal depotSapLib, BigDecimal interetDepotLib)
    {
        this(primeOrigine, primeApresAjustement, sinistrePaye, depotSapConst, depotSapLib, interetDepotLib);
        this.compteCedId = compteCedId;
    }

    public CompteDetailsItems(Long trancheIdSelected, Long periodeId, Long cedIdSelected, BigDecimal primeOrigine,
                              BigDecimal primeApresAjustement, BigDecimal sinistrePaye,
                              BigDecimal depotSapConst, BigDecimal depotSapLib, BigDecimal interetDepotLib)
    {
        this(primeOrigine, primeApresAjustement, sinistrePaye, depotSapConst, depotSapLib, interetDepotLib);
        this.trancheIdSelected = trancheIdSelected;
        this.periodeId = periodeId;
        this.cedIdSelected = cedIdSelected;
    }

}
