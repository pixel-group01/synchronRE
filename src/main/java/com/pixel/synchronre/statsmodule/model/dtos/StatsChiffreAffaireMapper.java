package com.pixel.synchronre.statsmodule.model.dtos;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StatsChiffreAffaireMapper
{
    public StatChiffreAffaireParPeriodeDTO mapToStatsChiffreAffaireDTO(Object[] objs)
    {
        StatChiffreAffaireParPeriodeDTO dto = new StatChiffreAffaireParPeriodeDTO(
                ((Long) objs[0]), // r_id
                ((Long) objs[1]), // deb_id
                ((Long) objs[2]), // aff_id
                (String) objs[3],                   // bord_num
                (String) objs[4],                   // aff_code
                ((Long) objs[5]), // ced_id
                (String) objs[6],                   // ced_nom_filiale
                ((Long) objs[7]), // ces_id
                (String) objs[8],                   // ces_nom
                (String) objs[9],                   // aff_assure
                (String) objs[10],                  // cou_libelle
                ((Date) objs[11]) == null ? null : ((Date) objs[11]).toLocalDate(), // aff_date_effet
                ((Date) objs[12]) == null ? null : ((Date) objs[12]).toLocalDate(), // aff_date_echeance
                (Long) objs[13],                  // exe_code
                ((Long) objs[14]), // rep_id
                (BigDecimal) objs[15],              // montant_cede
                (BigDecimal) objs[16],              // commission_nelsonre
                (BigDecimal) objs[17],              // montant_a_reverser
                (BigDecimal) objs[18],              // montant_reverse
                ((Date) objs[19]) == null ? null : ((Date) objs[19]).toLocalDate(), // date_reversement
                (BigDecimal) objs[20]               // montant_encaisse
        );
        return dto;
    }
    public List<StatChiffreAffaireParPeriodeDTO> mapToStatsChiffreAffaireDTOList(List<Object[]> results)
    {
        return results.stream().map(row -> mapToStatsChiffreAffaireDTO(row)).collect(Collectors.toList());
    }

}
