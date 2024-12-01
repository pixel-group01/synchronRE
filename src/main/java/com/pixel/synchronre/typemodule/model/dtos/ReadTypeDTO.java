package com.pixel.synchronre.typemodule.model.dtos;

import com.pixel.synchronre.sharedmodule.enums.PersStatus;
import com.pixel.synchronre.typemodule.model.entities.Type;
import com.pixel.synchronre.typemodule.model.enums.TypeGroup;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.util.List;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class ReadTypeDTO
{
    private Long typeId;
    private String typeGroup;
    private String uniqueCode;
    private String name;
    private String status;
    private String objectFolder;
    private int typeOrdre;
    private List<ReadTypeDTO> children;

    public ReadTypeDTO(Long typeId, TypeGroup typeGroup, String uniqueCode, String name, PersStatus status) {
        this.typeId = typeId;
        this.typeGroup = typeGroup.name();
        this.uniqueCode = uniqueCode;
        this.name = name;
        this.status = status.name();
    }

    public ReadTypeDTO(Long typeId, TypeGroup typeGroup, String uniqueCode, String name, PersStatus status, String objectFolder, int typeOrdre) {
        this.typeId = typeId;
        this.typeGroup = typeGroup.name();
        this.uniqueCode = uniqueCode;
        this.name = name;
        this.status = status.name();
        this.objectFolder = objectFolder;
        this.typeOrdre = typeOrdre;
    }

    public ReadTypeDTO(Type type) {
        BeanUtils.copyProperties(type, this);
    }
}