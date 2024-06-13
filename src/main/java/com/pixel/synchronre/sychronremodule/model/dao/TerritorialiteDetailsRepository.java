package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.pays.response.PaysListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Association;
import com.pixel.synchronre.sychronremodule.model.entities.TerritorialiteDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TerritorialiteDetailsRepository extends JpaRepository<Association, Long> {

    @Query("""
    select p.paysCode from Pays p where p.paysCode not in ?2 and 
    exists(select td from Association td where td.territorialite.terrId = ?1 and td.pays.paysCode = p.paysCode and td.type.uniqueCode = 'TER-DET')
    """)
    List<String> getPaysCodesToRemove(Long terrId, List<String> paysCodes);

    @Query("""
    select p.paysCode from Pays p where p.paysCode in ?2 and 
    not exists(select td from Association td where td.territorialite.terrId = ?1 and td.pays.paysCode = p.paysCode and td.type.uniqueCode = 'TER-DET')
    """)
    List<String> getPaysCodesToAdd(Long terrId, List<String> paysCodes);



    @Query("delete from Association td where td.territorialite.terrId = ?1 and td.pays.paysCode = ?2 and td.type.uniqueCode = 'TER-DET'")
    @Modifying
    void deleteByTerrIdAndPaysCode(Long terrId, String paysCode);

    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.pays.response.PaysListResp(
    p.paysCode, p.paysIndicatif, p.paysNom, p.statut.staLibelle, p.devise.devCode, p.devise.devLibelle) 
    from Association td join td.pays p where td.territorialite.terrId = ?1 and td.type.uniqueCode = 'TER-DET'
""")
    List<PaysListResp> getPaysByTerrId(Long terrId);

    @Query(" select (count(td.assoId)>0) from Association td where td.territorialite.terrId = ?1 and td.pays.paysCode = ?2 and td.type.uniqueCode = 'TER-DET'")
    boolean terrHasPays(Long terrId, String paysCode);

    @Query("select td from Association td where td.territorialite.terrId = ?1 and td.pays.paysCode = ?2 and td.type.uniqueCode = 'TER-DET'")
    Association findByTerrIdAndPaysCode(Long terrId, String paysCode);

    @Query(" select (count(td.assoId)>0) from Association td where td.territorialite.terrId = ?1 and td.organisation.organisationCode = ?2 and td.type.uniqueCode = 'TER-DET'")
    boolean terrHasOrg(Long terrId, String orgCode);

    @Query("select td from Association td where td.territorialite.terrId = ?1 and td.organisation.organisationCode = ?2 and td.type.uniqueCode = 'TER-DET'")
    Association findByTerrIdAndOrgCode(Long terrId, String orgCode);

    @Query("delete from Association td where td.territorialite.terrId = ?1 and td.organisation.organisationCode = ?2")
    @Modifying
    void deleteByTerrIdAndPOrgCode(Long terrId, String orgCode);

    @Query("""
    select o.organisationCode from Organisation o where o.organisationCode not in ?2 and 
    exists(select td from Association td where td.territorialite.terrId = ?1 and td.organisation.organisationCode = o.organisationCode and td.type.uniqueCode = 'TER-DET')
    """)
    List<String> getOrgCodesToRemove(Long terrId, List<String> orgCodes);

    @Query("""
    select o.organisationCode from Organisation o where o.organisationCode in ?2 and 
    not exists(select td from Association td where td.territorialite.terrId = ?1 and td.organisation.organisationCode = o.organisationCode and td.type.uniqueCode = 'TER-DET')
    """)
    List<String> getOrgCodesToAdd(Long terrId, List<String> orgCodes);

    @Query("""
    select td.organisation.organisationCode from Association td join td.territorialite t where t.terrId = ?1 and td.type.uniqueCode = 'TER-DET'
    """)
    List<String> getOrgCodesByTerrId(Long terrId);


}