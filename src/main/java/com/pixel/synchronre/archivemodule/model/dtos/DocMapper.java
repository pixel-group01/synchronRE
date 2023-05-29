package com.pixel.synchronre.archivemodule.model.dtos;

import com.pixel.synchronre.archivemodule.model.dtos.request.UploadDocReq;
import com.pixel.synchronre.archivemodule.model.entities.Document;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class DocMapper
{
    @Autowired protected TypeRepo typeRepo;

    @Mapping(target = "docDescription", expression = "java(\"Photo de profil\")")
    @Mapping(target = "docType", expression = "java(typeRepo.findByUniqueCode(dto.getDocUniqueCode()))")
    @Mapping(target = "user", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppUser(dto.getObjecId()))")
    public abstract Document mapToPhotoDoc(UploadDocReq dto);

    @Mapping(target = "docType", expression = "java(typeRepo.findByUniqueCode(dto.getDocUniqueCode()))")
    @Mapping(target = "reglement", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Reglement(dto.getObjecId()))")
    public abstract Document mapToReglementDoc(UploadDocReq dto);

    @Mapping(target = "docType", expression = "java(typeRepo.findByUniqueCode(dto.getDocUniqueCode()))")
    @Mapping(target = "placement", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Repartition(dto.getObjecId()))")
    public abstract Document mapToPlacementDoc(UploadDocReq dto);

    @Mapping(target = "docType", expression = "java(typeRepo.findByUniqueCode(dto.getDocUniqueCode()))")
    @Mapping(target = "affaire", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Affaire(dto.getObjecId()))")
    public abstract Document mapToAffaireDoc(UploadDocReq dto);
}