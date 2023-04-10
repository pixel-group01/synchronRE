package com.pixel.synchronre.typemodule.controller.resources;

import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.dtos.CreateTypeDTO;
import com.pixel.synchronre.typemodule.model.dtos.ReadTypeDTO;
import com.pixel.synchronre.typemodule.model.dtos.TypeParamDTO;
import com.pixel.synchronre.typemodule.model.dtos.UpdateTypeDTO;
import com.pixel.synchronre.typemodule.model.entities.Type;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import com.pixel.synchronre.typemodule.controller.services.ITypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;

@Profile({"dev", "prod"})
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/types") @Validated
public class TypeResource
{
    private final TypeRepo typeRepo;
    private final ITypeService typeService;

    @GetMapping(path = "") //@PreAuthorize("hasAuthority('DEV')")
    public List<ReadTypeDTO> getAllTypes()
    {
        return typeRepo.findAllTypes();
    }

    /*@GetMapping(path = "/find-by-unique-code/{uniqueCode}") //@PreAuthorize("hasAuthority('DEV')")
    public ReadTypeDTO getTypeByUniqueCode(@PathVariable String uniqueCode)
    {
        return typeRepo.findByUniqueCode(uniqueCode);
    }*/

    //@PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/{group-code}")
    public List<ReadTypeDTO> getByGroupCode(@PathVariable String groupCode)
    {
        return typeRepo.findByTypeGroup(groupCode); //typeRepo.findByGroupCode(groupCode);
    }

    //@PreAuthorize("isAnonymous()")
    @GetMapping(path = "/sous-type-of/{parentId}")
    public List<ReadTypeDTO> getSousTypeOf(@PathVariable @Positive Long parentId)
    {
        return typeRepo.findSousTypeOf(parentId);
    }

    //@PreAuthorize("permitAll()")
    @GetMapping(path = "/is-sous-type-of/{parentId}/{childId}")
    public boolean isSousTypeOf(@PathVariable @Positive Long parentId, @PathVariable @Positive Long childId)
    {
        return typeRepo.isSousTypeOf(parentId, childId);
    }

    //@PreAuthorize("permitAll()")
    @GetMapping(path = "/is-deletable/{typeId}")
    public boolean isDeletable(@PathVariable @Positive Long typeId)
    {
        return typeRepo.isDeletable(typeId);
    }

    //@PreAuthorize("permitAll()")
    @GetMapping(path = "/exists-by-uniqueCode")
    public boolean existsByUniqueCode(@RequestParam(defaultValue = "0") Long typeId, @RequestParam String uniqueCode)
    {
        return typeId.equals(0) ? typeRepo.existsByUniqueCode(uniqueCode) : typeRepo.existsByUniqueCode(typeId, uniqueCode);
    }

   // @PreAuthorize("isAnonymous()")
    @GetMapping(path = "/exists-by-id")
    public boolean existsById(@RequestParam Long typeId)
    {
        return typeRepo.existsById(typeId);
    }

    //@PreAuthorize("hasAuthority('DEV')")
    @PostMapping(path = "/create") @ResponseStatus(HttpStatus.CREATED)
    public Type createType(@RequestBody @Valid CreateTypeDTO dto) throws UnknownHostException {

        return typeService.createType(dto);
    }

    //@PreAuthorize("hasAuthority('DEV')")
    @PutMapping(path = "/update") @ResponseStatus(HttpStatus.OK)
    public Type updateType(@RequestBody @Valid UpdateTypeDTO dto) throws UnknownHostException {
        return typeService.updateType(dto);
    }

    @PostMapping(path = "/add-sub-type")
    public void addSubType(TypeParamDTO dto) throws UnknownHostException {
        typeService.addSousType(dto);
    }

    //@PreAuthorize("hasAuthority('DEV')")
    @PutMapping(path = "/set-sous-type") @ResponseStatus(HttpStatus.OK)
    public void setSousType(@RequestBody @Valid TypeParamDTO dto) throws UnknownHostException {
        typeService.addSousType(dto);
    }

    //@PreAuthorize("hasAuthority('DEV')")
    @PutMapping(path = "/remove-sous-type") @ResponseStatus(HttpStatus.OK)
    public void removeSousType(@RequestBody @Valid TypeParamDTO dto) throws UnknownHostException {
        typeService.removeSousType(dto);
    }

    @GetMapping(path = "/list")
    public Page<Type> searchTypes(@RequestParam(defaultValue = "") String key, String typeGroup, @RequestParam(defaultValue = "0") int num, @RequestParam(defaultValue = "2") int size) throws UnknownHostException {
        return typeService.searchPageOfTypes(key, typeGroup, num, size);
    }
}
