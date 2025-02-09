package com.pixel.synchronre.statsmodule.model.repositories;

import com.pixel.synchronre.statsmodule.model.dtos.StatChiffreAffaireParPeriodeDTO;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ChiffreAffaireRepo extends JpaRepository<Affaire, Long>
{
    @Query(nativeQuery = true,
           value = """
                WITH encaissement AS (
                    SELECT
                        p.aff_id,
                        SUM(p.montant_encaisse) AS montant_encaisse
                    FROM V_paiement p
                    WHERE p.date_reglement BETWEEN :debut AND :fin
                    GROUP BY p.aff_id
                    ORDER BY p.aff_id
                ),
                     reversement AS (
                         SELECT
                             rev.aff_id,
                             rev.ces_id,
                             MAX(rev.date_reglement) AS date_reversement,
                             SUM(rev.montant_reverse) AS montant_reverse
                         FROM V_reversement rev
                         WHERE rev.date_reglement BETWEEN :debut AND :fin
                         GROUP BY rev.aff_id, rev.ces_id
                         ORDER BY rev.aff_id, rev.ces_id
                     )
                    select
                                ROW_NUMBER() OVER (ORDER BY aff.aff_id) AS r_id
                                        ,db.deb_id
                                        ,aff.aff_id
                                        ,notDeb.bord_num
                                        ,aff.aff_code
                                        ,ced.ced_id
                                        ,ced.ced_nom_filiale
                                        ,ces.ces_id
                                        ,ces.ces_nom
                                        ,aff.aff_assure
                                        ,cou.cou_libelle
                                        ,aff.aff_date_effet
                                        ,aff.aff_date_echeance
                                        ,aff.exe_code
                                        ,pla.rep_id
                                        ,pla.rep_prime                           AS montant_cede
                                        ,pla.commission_court                    AS commission_nelsonre
                                        ,pla.prime_nette                         AS montant_a_reverser
                                        ,rev.montant_reverse montant_reverse
                                        ,rev.date_reversement
                                        ,enc.montant_encaisse as montant_encaisse
                
                from affaire aff
                         left join encaissement enc on enc.aff_id = aff.aff_id
                         JOIN cedante ced on aff.cedente_id = ced.ced_id
                         JOIN couverture cou on aff.couverture_id = cou.cou_id
                         left join (select * from bordereau b join type t on b.type_code = t.type_id and t.unique_code = 'NOT_DEB_FAC') notDeb on aff.aff_id = notDeb.aff_id
                         left JOIN detail_bordereau db on notDeb.bord_id = db.deb_bord_id
                
                         LEFT JOIN V_placement pla ON db.deb_rep = pla.rep_id
                         left JOIN cessionnaire ces ON pla.cessionnaire_id = ces.ces_id
                         left join reversement rev on aff.aff_id = rev.aff_id and ces.ces_id = rev.ces_id
                where aff.exe_code = coalesce(:exeCode, aff.exe_code) and ced.ced_id = coalesce(:cedId, ced.ced_id) and ces.ces_id = coalesce(:cesId, ces.ces_id)
            """)
    Page<Object[]> getStatsChiffreAffaire(@Param(value = "exeCode") Long exeCode,
                                          @Param(value = "cedId")Long cedId,
                                          @Param(value = "cesId")Long cesId,
                                          @Param(value = "debut")LocalDate debut,
                                          @Param(value = "fin")LocalDate fin, Pageable pageable);
}
