package com.pixel.synchronre.sychronremodule.controller;



import com.pixel.synchronre.sychronremodule.model.dto.organisation.OrganisationDTO;
import com.pixel.synchronre.sychronremodule.model.dto.organisation.UpdatePaysOrgDTO;
import com.pixel.synchronre.sychronremodule.model.dto.pays.request.CreatePaysReq;
import com.pixel.synchronre.sychronremodule.model.dto.pays.request.UpdatePaysReq;
import com.pixel.synchronre.sychronremodule.model.dto.pays.response.PaysDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.pays.response.PaysListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Organisation;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceOrganisation;
import com.pixel.synchronre.sychronremodule.service.interfac.IservicePays;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;

@RestController @ResponseStatus(HttpStatus.OK)
@RequiredArgsConstructor
@RequestMapping("/organisations")
public class OrganisationController {

    private final IServiceOrganisation organisationService;

    @PostMapping(path = "/create")
    public OrganisationDTO createPays(@RequestBody @Valid OrganisationDTO dto)
    {
        return organisationService.create(dto);
    }

    @PutMapping(path = "/update")
    public OrganisationDTO updatePays(@RequestBody @Valid UpdatePaysOrgDTO dto)
    {
        return organisationService.update(dto);
    }

    @GetMapping(path = "/list")
    public List<OrganisationDTO> getListOrganisations()
    {
        return organisationService.getListOrganisations();
    }

    @GetMapping(path = "/search")
    public Page<OrganisationDTO> searchPays(@RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size)
    {
        return organisationService.search(key, PageRequest.of(page, size));
    }
}
