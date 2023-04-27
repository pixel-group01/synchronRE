package com.pixel.synchronre.archivemodule.controller.repositories;

import com.pixel.synchronre.archivemodule.model.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DocumentRepository extends JpaRepository<Document, Long>
{
    @Query("select d.docPath from Document d where d.docId = ?1")
    String getDocumentPath(Long docId);
}