package com.pixel.synchronre.typemodule.controller.web;

import com.pixel.synchronre.typemodule.controller.services.ITypeService;
import com.pixel.synchronre.typemodule.model.dtos.CreateTypeDTO;
import com.pixel.synchronre.typemodule.model.dtos.TypeParamDTO;
import com.pixel.synchronre.typemodule.model.dtos.TypeParamsDTO;
import com.pixel.synchronre.typemodule.model.dtos.UpdateTypeDTO;
import com.pixel.synchronre.typemodule.model.entities.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

//@RestController @RequestMapping(path = "/gest-ass/types")
@RequiredArgsConstructor
public class TypeResource
{
    private final ITypeService typeService;

    @PostMapping(path = "/create-type")
    public Type createType(CreateTypeDTO dto) throws UnknownHostException {
        return typeService.createType(dto);
    }

    @PostMapping(path = "/update-type")
    public Type updateType(UpdateTypeDTO dto) throws UnknownHostException {
        return typeService.updateType(dto);
    }

    @PostMapping(path = "/add-sub-type")
    public void addSubType(TypeParamDTO dto) throws UnknownHostException {
        typeService.addSousType(dto);
    }

    @PostMapping(path = "/rmv-sub-type")
    public void rmvSubType(TypeParamDTO dto) throws UnknownHostException {
        typeService.removeSousType(dto);
    }

    @PostMapping(path = "/set-sub-type")
    public void setSubType(TypeParamsDTO dto) throws UnknownHostException {
        typeService.setSousTypes(dto);
    }

    @GetMapping(path = "/set-sub-type")
    public Page<Type> searchTypes(@RequestParam(defaultValue = "") String key, String typeGroup, @RequestParam(defaultValue = "0") int num, @RequestParam(defaultValue = "2") int size) throws UnknownHostException {
        return typeService.searchPageOfTypes(key, typeGroup, num, size);
    }
}
