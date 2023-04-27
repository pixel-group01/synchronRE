package com.pixel.synchronre.archivemodule.controller.web;

import com.pixel.synchronre.archivemodule.controller.repositories.DocumentRepository;
import com.pixel.synchronre.archivemodule.controller.service.IServiceDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller @RequiredArgsConstructor @RequestMapping(path = "/documents")
public class DocumentController
{
    private final IServiceDocument docService;
    private final DocumentRepository docRepo;
    @PostMapping(path = "/download")
    void downloadDocument(@PathVariable Long docId)
    {
        String docPath = docRepo.getDocumentPath(docId);
        docService.downloadFile(docPath);
    }
}
