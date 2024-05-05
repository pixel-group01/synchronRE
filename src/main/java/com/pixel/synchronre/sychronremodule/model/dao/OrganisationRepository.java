package com.pixel.synchronre.sychronremodule.model.dao;

import com.pixel.synchronre.sychronremodule.model.dto.organisation.OrganisationDTO;
import com.pixel.synchronre.sychronremodule.model.entities.Organisation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrganisationRepository extends JpaRepository<Organisation, String> {
    @Query("select new com.pixel.synchronre.sychronremodule.model.dto.organisation.OrganisationDTO(o.organisationCode, o.organisationLibelle, o.statut.staCode, o.statut.staLibelle) from Organisation o where o.statut = 'ACT'")
    List<OrganisationDTO> getListOrganisations();

    @Query("""
    select new com.pixel.synchronre.sychronremodule.model.dto.organisation.OrganisationDTO(o.organisationCode, o.organisationLibelle, o.statut.staCode, o.statut.staLibelle) 
    from Organisation o 
    where o.statut.staCode = 'ACT' and (
        locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(o.organisationCode, '') ) as string))) >0 or
        locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(o.organisationLibelle, '') ) as string))) >0 or
        locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(o.statut.staCode, '') ) as string))) >0 or
        locate(upper(coalesce(:key, '') ), upper(cast(function('strip_accents',  coalesce(o.statut.staLibelle, '') ) as string))) >0
    )
    """)
    Page<OrganisationDTO> search(String key, Pageable pageable);
}