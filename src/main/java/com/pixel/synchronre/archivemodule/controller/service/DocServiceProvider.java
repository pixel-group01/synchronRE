package com.pixel.synchronre.archivemodule.controller.service;

import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
import com.pixel.synchronre.typemodule.model.enums.TypeGroup;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class DocServiceProvider
{
    private final AffaireDocUploader affDocUploader;
    private final SinistreDocUploader sinDocUploader;
    private final ReglementDocUploader regDocUploader;
    private final TypeRepo typeRepo;

    public AbstractDocumentService getDocUploader(String typeDocUniqueCode)
    {
        if(typeDocUniqueCode == null) return null;
        Type typeDoc = typeRepo.findByUniqueCode(typeDocUniqueCode.toUpperCase());
        if(typeDoc == null || typeDoc.getTypeGroup() != TypeGroup.DOCUMENT) return null;

        return switch (typeDoc.getUniqueCode())
                {
                    case "DOC-AFF"->affDocUploader;
                    case "DOC_REG"->regDocUploader;
                    case "DOC_SIN"->sinDocUploader;
                    default -> null;
                };
    }
}