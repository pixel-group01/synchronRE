package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.views.VStatCompte;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface VStatCompteRepository extends JpaRepository<VStatCompte, Long>
{
    @Query("select vsc from VStatCompte  vsc where vsc.cedId = ?1 and vsc.trancheId = ?2 and vsc.periodeId = ?3")
    VStatCompte getStatsCompte(Long cedId, Long trancheId, Long periodeId);

    @Query("select vsc.primeOrigine from VStatCompte  vsc where vsc.cedId = ?1 and vsc.trancheId = ?2 and vsc.periodeId = ?3")
    BigDecimal getPrimeOrigine(Long cedId, Long trancheId, Long periodeId);

    @Query("select vsc.assiettePrimeExercice from VStatCompte  vsc where vsc.cedId = ?1 and vsc.trancheId = ?2 and vsc.periodeId = ?3")
    BigDecimal getAssiettePrimeExercice(Long cedId, Long trancheId, Long periodeId);

    @Query("select vsc from VStatCompte  vsc where vsc.trancheId = ?1 ")
    List<VStatCompte> getStatsCompte(Long trancheId, Pageable pageable);
}