package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.exercice.response.ExerciceListResp;
import com.pixel.synchronre.sychronremodule.model.dto.statut.response.StatutListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Exercice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExerciceRepository extends JpaRepository<Exercice, Long> {
    @Query("select (count(e) > 0) from Exercice e where e.exeCode = ?1")
    boolean alreadyExistsByCode(Long exeCode);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.exercice.response.ExerciceListResp(e.exeCode,e.exeLibelle,e.exeCourant) 
        from Exercice  e where locate(upper(coalesce(?1, '') ), cast(e.exeCode as string)) >0 
                          or locate(upper(coalesce(?1, '') ), upper(cast(function('strip_accents',  coalesce(e.exeLibelle, '') ) as string)) ) >0 
                          order by e.exeCode desc
""")
    List<ExerciceListResp> searchExercice(String key);

    @Query("select e from Exercice e where e.exeCourant = true")
    List<Exercice> getExeCourant();

    @Query("select e from Exercice e where e.exeCode = (select max(e1.exeCode) from Exercice e1)")
    Exercice getLastExo();

    @Query("update Exercice e set e.exeCourant =false  where e.exeCourant = true")
    @Modifying
    void setExerciceAsNoneCourant();

    @Query("select (count(e)>0) from Exercice e where e.exeCode = ?1 and e.statut.staCode = 'ACT'")
    boolean exerciceIsActive(Long value);
}
