package com.pixel.synchronre.init;

import com.pixel.synchronre.archivemodule.model.constants.DocumentsConstants;
import com.pixel.synchronre.sychronremodule.model.dao.ParamCessionLegaleRepository;
import com.pixel.synchronre.sychronremodule.model.entities.ParamCessionLegale;
import com.pixel.synchronre.sychronremodule.model.entities.Pays;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.enums.TypeGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service @RequiredArgsConstructor
public class FoldersIniter implements Loader
{
    private final TypeRepo typeRepo;
    /*@Bean
    @DependsOn("commandLineRunner")
    CommandLineRunner createSystemDirectories()
    {
        return(args)->
        {
            File agtUploadDir= new File(DocumentsConstants.UPLOADS_DIR);
            if(!agtUploadDir.exists()) agtUploadDir.mkdirs();
            typeRepo.findByTypeGroup(TypeGroup.DOCUMENT).forEach(type->
            {
                String objectFolder = type.getObjectFolder();
                typeRepo.findUniqueCodesByObjectFolder(objectFolder).forEach(uc->
                {
                    File typeDir = new File(DocumentsConstants.UPLOADS_DIR  + "\\" + objectFolder + "\\" + type.getUniqueCode()) ;
                    if(!typeDir.exists()) typeDir.mkdirs();
                });
            });
        };
    }*/

    @Override
    public void load() {
        File docUploadDir= new File(DocumentsConstants.UPLOADS_DIR);
        if(!docUploadDir.exists()) docUploadDir.mkdirs();
        typeRepo.findBaseTypes(TypeGroup.DOCUMENT).forEach(type->
        {
            String baseFolder = DocumentsConstants.UPLOADS_DIR  + File.separator + type.getObjectFolder();
            File baseDir = new File(baseFolder);
            if(!baseDir.exists()) baseDir.mkdirs();

            typeRepo.getChildren(type.getTypeId()).forEach(child->
            {
                File objectDir = new File(baseFolder + File.separator + child.getUniqueCode());
                if(!objectDir.exists()) objectDir.mkdirs();
            });
        });
    }
}
