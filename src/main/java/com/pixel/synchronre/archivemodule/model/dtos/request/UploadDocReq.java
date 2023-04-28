package com.pixel.synchronre.archivemodule.model.dtos.request;

import com.pixel.synchronre.archivemodule.model.dtos.validator.ValidArchiveType;
import com.pixel.synchronre.archivemodule.model.dtos.validator.*;
import com.pixel.synchronre.authmodule.model.dtos.appuser.ExistingUserId;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.ExistingAffId;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.validator.ExistingPlaId;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.validator.ExistingRepId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ValidFileExtension
public class UploadDocReq
{
    @ExistingUserId(groups = {OnUSerUpload.class})
    @ExistingRepId(groups = {OnRegUpload.class})
    @ExistingPlaId(groups = {OnPlaUpload.class})
    @ExistingAffId(groups = {OnAffUpload.class})
    private Long objecId;
    @ValidArchiveType
    private String docUniqueCode;
    private String docNum;
    private String docDescription;
    @ValidFileSize
    private MultipartFile file;
}
