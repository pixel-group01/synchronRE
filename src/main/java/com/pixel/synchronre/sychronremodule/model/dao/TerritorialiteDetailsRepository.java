package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.pays.response.PaysListResp;
import com.pixel.synchronre.sychronremodule.model.entities.OrganisationPays;
import com.pixel.synchronre.sychronremodule.model.entities.Territorialite;
import com.pixel.synchronre.sychronremodule.model.entities.TerritorialiteDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TerritorialiteDetailsRepository extends JpaRepository<TerritorialiteDetails, Long> {

    @Query("""
    select p.paysCode from Pays p where p.paysCode not in ?2 and 
    exists(select td from TerritorialiteDetails td where td.territorialite.terrId = ?1 and td.pays.paysCode = p.paysCode)
    """)
    List<String> getPaysCodesToRemove(Long terrId, List<String> paysCodes);

    @Query("""
    select p.paysCode from Pays p where p.paysCode in ?2 and 
    not exists(select td from TerritorialiteDetails td where td.territorialite.terrId = ?1 and td.pays.paysCode = p.paysCode)
    """)
    List<String> getPaysCodesToAdd(Long terrId, List<String> paysCodes);



    @Query("delete from TerritorialiteDetails td where td.territorialite.terrId = ?1 and td.pays.paysCode = ?2")
    @Modifying
    void deleteByTerrIdAndPaysCode(Long terrId, String paysCode);

    @Query("""
select new com.pixel.synchronre.sychronremodule.model.dto.pays.response.PaysListResp(
    p.paysCode, p.paysIndicatif, p.paysNom, p.statut.staLibelle, p.devise.devCode, p.devise.devLibelle) 
    from OrganisationPays op join op.pays p where op.organisation.organisationCode in ?1
""")
    List<PaysListResp> getPaysByTerrId(Long terrId);

    @Query(" select (count(td.terrDetId)>0) from TerritorialiteDetails td where td.territorialite.terrId = ?1 and td.pays.paysCode = ?2")
    boolean terrHasPays(Long terrId, String paysCode);

    @Query("select td from TerritorialiteDetails td where td.territorialite.terrId = ?1 and td.pays.paysCode = ?2")
    TerritorialiteDetails findByTerrIdAndPaysCode(Long terrId, String paysCode);

    @Query(" select (count(td.terrDetId)>0) from TerritorialiteDetails td where td.territorialite.terrId = ?1 and td.organisation.organisationCode = ?2")
    boolean terrHasOrg(Long terrId, String orgCode);

    @Query("select td from TerritorialiteDetails td where td.territorialite.terrId = ?1 and td.organisation.organisationCode = ?2")
    TerritorialiteDetails findByTerrIdAndOrgCode(Long terrId, String orgCode);

    @Query("delete from TerritorialiteDetails td where td.territorialite.terrId = ?1 and td.organisation.organisationCode = ?2")
    @Modifying
    void deleteByTerrIdAndPOrgCode(Long terrId, String orgCode);

    @Query("""
    select o.organisationCode from Organisation o where o.organisationCode not in ?2 and 
    exists(select td from TerritorialiteDetails td where td.territorialite.terrId = ?1 and td.organisation.organisationCode = o.organisationCode)
    """)
    List<String> getOrgCodesToRemove(Long terrId, List<String> orgCodes);

    @Query("""
    select o.organisationCode from Organisation o where o.organisationCode in ?2 and 
    not exists(select td from TerritorialiteDetails td where td.territorialite.terrId = ?1 and td.organisation.organisationCode = o.organisationCode)
    """)
    List<String> getOrgCodesToAdd(Long terrId, List<String> orgCodes);
}