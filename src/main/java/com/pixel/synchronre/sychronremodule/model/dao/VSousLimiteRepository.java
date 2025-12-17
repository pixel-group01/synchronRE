package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.views.VSousLimite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VSousLimiteRepository extends JpaRepository<VSousLimite, Long> {
    @Query("""
    select v from VSousLimite v join TraiteNonProportionnel trnp on trnp.traiteNpId = v.traiteNpId
    where (
        locate(upper(coalesce(:key, '')), cast(v.sousLimMontant as string)) = 1
        or locate(upper(coalesce(:key, '')), upper(cast(function('strip_accents', coalesce(trnp.traiNumero, '')) as string))) > 0
        or locate(upper(coalesce(:key, '')), upper(cast(function('strip_accents', coalesce(v.couLibelles, '')) as string))) > 0
    )
      and v.traiteNpId = :traiteNpId
""")
    Page<VSousLimite> search(@Param("key") String key,
                                       @Param("traiteNpId") Long traiteNpId,
                                       Pageable pageable);

}
