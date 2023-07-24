package com.pixel.synchronre.archivemodule.model.dtos;

import com.pixel.synchronre.archivemodule.model.dtos.request.UploadDocReq;
import com.pixel.synchronre.archivemodule.model.entities.Document;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class DocMapper
{
    @Autowired protected TypeRepo typeRepo;

    @Mapping(target = "docDescription", expression = "java(\"Photo de profil\")")
    @Mapping(target = "docType", expression = "java(typeRepo.findByUniqueCode(dto.getDocUniqueCode()).orElseThrow(()->new com.pixel.synchronre.sharedmodule.exceptions.AppException(\"Type de document inconnu\")))")
    @Mapping(target = "user", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppUser(dto.getObjectId()))")
    public abstract Document mapToPhotoDoc(UploadDocReq dto);

    @Mapping(target = "docType", expression = "java(typeRepo.findByUniqueCode(dto.getDocUniqueCode()).orElseThrow(()->new com.pixel.synchronre.sharedmodule.exceptions.AppException(\"Type de document inconnu\")))")
    @Mapping(target = "reglement", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Reglement(dto.getObjectId()))")
    public abstract Document mapToReglementDoc(UploadDocReq dto);

    @Mapping(target = "docType", expression = "java(typeRepo.findByUniqueCode(dto.getDocUniqueCode()).orElseThrow(()->new com.pixel.synchronre.sharedmodule.exceptions.AppException(\"Type de document inconnu\")))")
    @Mapping(target = "placement", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Repartition(dto.getObjectId()))")
    public abstract Document mapToPlacementDoc(UploadDocReq dto);

    @Mapping(target = "docType", expression = "java(typeRepo.findByUniqueCode(dto.getDocUniqueCode()).orElseThrow(()->new com.pixel.synchronre.sharedmodule.exceptions.AppException(\"Type de document inconnu\")))")
    @Mapping(target = "affaire", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Affaire(dto.getObjectId()))")
    public abstract Document mapToAffaireDoc(UploadDocReq dto);

    @Mapping(target = "docType", expression = "java(typeRepo.findByUniqueCode(dto.getDocUniqueCode()).orElseThrow(()->new com.pixel.synchronre.sharedmodule.exceptions.AppException(\"Type de document inconnu\")))")
    @Mapping(target = "sinistre", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Sinistre(dto.getObjectId()))")
    @Mapping(target = "file", expression = "java(com.pixel.synchronre.sharedmodule.utilities.Base64ToFileConverter.convertToFile(dto.getBase64UrlFile(), dto.getExtension()))")
    public abstract Document mapToSinistreDoc(UploadDocReq dto);

}