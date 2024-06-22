package com.pixel.synchronre.sychronremodule.service.implementation;


import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sychronremodule.model.dao.OrganisationPaysRepository;
import com.pixel.synchronre.sychronremodule.model.dao.OrganisationRepository;
import com.pixel.synchronre.sychronremodule.model.dto.organisation.OrganisationDTO;
import com.pixel.synchronre.sychronremodule.model.dto.organisation.OrganisationMapper;
import com.pixel.synchronre.sychronremodule.model.dto.organisation.UpdatePaysOrgDTO;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceOrganisation;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class OrganisationService implements IServiceOrganisation
{
    private final OrganisationMapper orgMapper;
    private final OrganisationRepository orgRepo;
    private final OrganisationPaysRepository orgPaysRepo;
    private final ILogService logService;
    private final ObjectCopier<Organisation> orgCopier;
    private final TypeRepo typeRepo;


    @Override @Transactional
    public OrganisationDTO create(OrganisationDTO dto)
    {
        Organisation organisation = orgMapper.mapToOrgnaisation(dto);
        organisation = orgRepo.save(organisation);
        final Organisation organisationConstant = organisation;
        if(dto.getPaysCodes() == null || dto.getPaysCodes().isEmpty()) return orgMapper.mapToOrgnaisationDTO(organisation);
        dto.getPaysCodes().forEach(code->
        {
            Association orgPays = orgPaysRepo.save(new Association(new Pays(code), organisationConstant, new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
            logService.logg("Ajout d'un pays à une organisation", null, orgPays, "OrganisationPays");
        });
        logService.logg("Création d'une organisation", null, organisation, "Organisation");
        return orgMapper.mapToOrgnaisationDTO(organisation);
    }

    @Override
    public List<OrganisationDTO> getListOrganisations()
    {
        List<OrganisationDTO> organisations = orgRepo.getListOrganisations();
        organisations = organisations.stream().peek(o->o.setPaysList(orgPaysRepo.getPaysByOrgCodes(Collections.singletonList(o.getOrganisationCode())))).collect(Collectors.toList());
        return organisations;
    }

    @Override
    public Page<OrganisationDTO> search(String key, Pageable pageable)
    {
        Page<OrganisationDTO> organisationPage = orgRepo.search(key, pageable);
        List<OrganisationDTO> organisationList = organisationPage.stream().peek(o->o.setPaysList(orgPaysRepo.getPaysByOrgCodes(Collections.singletonList(o.getOrganisationCode())))).collect(Collectors.toList());
        return new PageImpl<>(organisationList, pageable, organisationPage.getTotalElements());
    }

    @Override @Transactional
    public OrganisationDTO update(UpdatePaysOrgDTO dto)
    {
        Organisation organisation = orgRepo.findById(dto.getOrgCode()).orElseThrow(()->new AppException("Organisation introuvable"));
        Organisation oldOrganisation = orgCopier.copy(organisation);
        organisation.setOrganisationLibelle(dto.getOrganisationLibelle());

        logService.logg("Modification d'une organisation", oldOrganisation, organisation, "Organisation");

        List<String> paysToRemove = orgPaysRepo.getPaysCodesToRemove(dto.getOrgCode(), dto.getPaysCodes());
        List<String> paysToAdd = orgPaysRepo.getPaysCodesToAdd(dto.getOrgCode(), dto.getPaysCodes());
        paysToRemove.forEach(paysCode-> {
                this.removePaysFromOrganisation(dto.getOrgCode(), paysCode);
        });
        paysToAdd.forEach(paysCode-> {
                this.addPaysToOrganisation(dto.getOrgCode(), paysCode);
        });

        return orgMapper.mapToOrgnaisationDTO(organisation);
    }

    private void addPaysToOrganisation(String orgCode, String paysCode){
        Organisation organisation = orgRepo.findById(orgCode).orElseThrow(()->new AppException("Organisation introuvable"));
        if(orgPaysRepo.orgHasPays(orgCode, paysCode)) return ;
        Association orgPays = orgPaysRepo.save(new Association(new Pays(paysCode), organisation, new Statut("ACT"),typeRepo.findByUniqueCode("ORG-PAYS").orElseThrow(()->new AppException("Type de document inconnu"))));
        logService.logg("Ajout d'un pays à une organisation", null, orgPays, "OrganisationPays");
    }

    private void removePaysFromOrganisation(String orgCode, String paysCode) {
        Organisation organisation = orgRepo.findById(orgCode).orElseThrow(()->new AppException("Organisation introuvable"));
        if(!orgPaysRepo.orgHasPays(orgCode, paysCode)) return ;
        Association orgPays = orgPaysRepo.findByOrgCodeAndPaysCode(orgCode, paysCode);
        orgPaysRepo.deleteByOrgCodeAndPaysCode(orgCode, paysCode);
        logService.logg("Suppression d'un pays à une organisation", orgPays, new Association(), "Association");
    }
}