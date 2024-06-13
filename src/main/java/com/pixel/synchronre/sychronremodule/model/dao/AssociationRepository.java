package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.pays.response.PaysListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Association;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AssociationRepository extends JpaRepository<Association, Long> {
    @Query("""
    select p.paysCode from Pays p where p.paysCode not in ?2 and 
    exists(select op from Association op where op.organisation.organisationCode = ?1 and op.pays.paysCode = p.paysCode and op.type.uniqueCode = 'ORG-PAYS')
    """)
    List<String> getPaysCodesToRemove(String orgCode, List<String> paysCodes);

    @Query("""
    select p.paysCode from Pays p where p.paysCode in ?2 and 
    not exists(select op from Association op where op.organisation.organisationCode = ?1 and op.pays.paysCode = p.paysCode and op.type.uniqueCode = 'ORG-PAYS')
    """)
    List<String> getPaysCodesToAdd(String orgCode, List<String> paysCodes);

    @Query(" select (count(op.assoId)>0) from Association op where op.organisation.organisationCode = ?1 and op.pays.paysCode = ?2 and op.type.uniqueCode = 'ORG-PAYS'")
    boolean orgHasPays(String orgCode, String paysCode);

    @Query("select op from Association op where op.organisation.organisationCode = ?1 and op.pays.paysCode = ?2 and op.type.uniqueCode = 'ORG-PAYS'")
    Association findByOrgCodeAndPaysCode(String orgCode, String paysCode);

    @Query("delete from Association op where op.organisation.organisationCode = ?1 and op.pays.paysCode = ?2 and op.type.uniqueCode = 'ORG-PAYS'")
    @Modifying
    void deleteByOrgCodeAndPaysCode(String orgCode, String paysCode);

    @Query("""
select new com.pixel.synchronre.sychronremodule.model.dto.pays.response.PaysListResp(
    p.paysCode, p.paysIndicatif, p.paysNom, p.statut.staLibelle, p.devise.devCode, p.devise.devLibelle) 
    from Pays p where p.paysCode in 
    (select p1.paysCode from Association op join op.pays p1 where op.organisation.organisationCode in ?1 and op.type.uniqueCode = 'ORG-PAYS')
""")
    List<PaysListResp> getPaysByOrgCodes(List<String> orgCode);
}