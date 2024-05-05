package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.pays.response.PaysListResp;
import com.pixel.synchronre.sychronremodule.model.entities.OrganisationPays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrganisationPaysRepository extends JpaRepository<OrganisationPays, Long>
{
    @Query("""
    select p.paysCode from Pays p where p.paysCode not in ?2 and 
    exists(select op from OrganisationPays op where op.organisation.organisationCode = ?1 and op.pays.paysCode = p.paysCode)
    """)
    List<String> getPaysCodesToRemove(String orgCode, List<String> paysCodes);

    @Query("""
    select p.paysCode from Pays p where p.paysCode in ?2 and 
    not exists(select op from OrganisationPays op where op.organisation.organisationCode = ?1 and op.pays.paysCode = p.paysCode)
    """)
    List<String> getPaysCodesToAdd(String orgCode, List<String> paysCodes);

    @Query(" select (count(op.orgPayId)>0) from OrganisationPays op where op.organisation.organisationCode = ?1 and op.pays.paysCode = ?2")
    boolean orgHasPays(String orgCode, String paysCode);

    @Query("select op from OrganisationPays op where op.organisation.organisationCode = ?1 and op.pays.paysCode = ?2")
    OrganisationPays findByOrgCodeAndPaysCode(String orgCode, String paysCode);

    @Query("delete from OrganisationPays op where op.organisation.organisationCode = ?1 and op.pays.paysCode = ?2")
    @Modifying
    void deleteByOrgCodeAndPaysCode(String orgCode, String paysCode);

    @Query("""
select new com.pixel.synchronre.sychronremodule.model.dto.pays.response.PaysListResp(
    p.paysCode, p.paysIndicatif, p.paysNom, p.statut.staLibelle, p.devise.devCode, p.devise.devLibelle) 
    from OrganisationPays op join op.pays p where op.organisation.organisationCode = ?1
""")
    List<PaysListResp> getPaysByOrgCode(String orgCode);
}