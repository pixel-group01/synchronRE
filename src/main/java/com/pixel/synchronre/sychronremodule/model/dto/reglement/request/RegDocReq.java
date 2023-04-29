package com.pixel.synchronre.sychronremodule.model.dto.reglement.request;

import com.pixel.synchronre.archivemodule.model.dtos.validator.ValidFileExtension;
import com.pixel.synchronre.archivemodule.model.dtos.validator.ValidFileSize;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.validator.ValidDocRegId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public class RegDocReq
    {
        @ValidDocRegId
        private Long docTypeId;
        private String description;
        @ValidFileExtension @ValidFileSize
        private MultipartFile regDoc;
    }
