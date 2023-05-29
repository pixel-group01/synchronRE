package com.pixel.synchronre.archivemodule.controller.web;

import com.pixel.synchronre.archivemodule.controller.repositories.DocumentRepository;
import com.pixel.synchronre.archivemodule.controller.service.AbstractDocumentService;
import com.pixel.synchronre.archivemodule.model.entities.Document;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller @RequiredArgsConstructor @RequestMapping(path = "/documents")
public class DocumentController
{
    private final AbstractDocumentService docService;
    private final DocumentRepository docRepo;

    @PostMapping(path = "/download")
    public byte[] downloadDocument(@PathVariable Long docId) throws Exception {
        Document doc = docRepo.findById(docId).orElse(null);
        if(doc == null) return null;
        String docPath = doc.getDocPath();
        return docService.downloadFile(docPath);
    }

    @PostMapping(path = "/display")
    void displayDocument(HttpServletResponse response, @PathVariable Long docId) throws Exception {
        Document doc = docRepo.findById(docId).orElse(null);
        if(doc == null) return;
        String docPath = doc.getDocPath();
        String name = FilenameUtils.getName(doc.getDocPath());
        byte[] docBytes = docService.downloadFile(docPath);

        docService.displayPdf(response, docBytes, name);
    }
}
