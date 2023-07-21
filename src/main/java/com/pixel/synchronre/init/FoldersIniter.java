package com.pixel.synchronre.init;

import com.pixel.synchronre.archivemodule.model.constants.DocumentsConstants;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.enums.TypeGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;

@Service @RequiredArgsConstructor
public class FoldersIniter implements Loader
{
    private final TypeRepo typeRepo;

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
