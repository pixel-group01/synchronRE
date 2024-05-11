package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.entities.CategorieCedante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategorieCedanteRepository extends JpaRepository<CategorieCedante, Long>
{
    @Query("""
    select c.cedId from Cedante c where c.cedId in ?2 and 
    not exists(select cc from CategorieCedante cc where cc.categorie.categorieId = ?1 and cc.cedante.cedId = c.cedId)
    """)
    List<Long> getCedIdsToAdd(Long categorieId, List<Long> cedIds);

    @Query("""
    select c.cedId from Cedante c where c.cedId in ?2 and 
    exists(select cc from CategorieCedante cc where cc.categorie.categorieId = ?1 and cc.cedante.cedId = c.cedId)
    """)
    List<Long> getCedIdsToRemove(Long categorieId, List<Long> cedIds);

    @Query("select (count(cc.catCedId)>0) from CategorieCedante cc where cc.cedante.cedId = ?1 and cc.categorie.categorieId = ?2")
    boolean exitsByCedIdAndCatId(Long cedId, Long categorieId);

    @Query("select cc from CategorieCedante cc where cc.cedante.cedId = ?1 and cc.categorie.categorieId = ?2")
    CategorieCedante findByCedIdAndCatId(Long cedId, Long categorieId);

    @Modifying
    @Query("delete from CategorieCedante cc  where cc.cedante.cedId = ?1 and cc.categorie.categorieId = ?2")
    void deleteByCedIdAndCatId(Long cedId, Long categorieId);

    @Query("""
        select new com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO(c.cedId, c.cedNomFiliale, c.cedSigleFiliale)
        from CategorieCedante cc left join cc.cedante c where cc.categorie.categorieId = ?1
""")
    List<ReadCedanteDTO> getShortCedantesByCatId(Long categorieId);
}