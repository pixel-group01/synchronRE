package com.pixel.synchronre.archivemodule.controller.repositories;

import com.pixel.synchronre.archivemodule.model.dtos.response.ReadDocDTO;
import com.pixel.synchronre.archivemodule.model.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long>
{
    @Query("select d.docPath from Document d where d.docId = ?1")
    String getDocumentPath(Long docId);

    @Query("""
        select new com.pixel.synchronre.archivemodule.model.dtos.response.ReadDocDTO(
        d.docId, d.docNum, d.docDescription, d.docPath, d.docType.typeId, d.docType.uniqueCode, d.docType.name
        ) 
        from Document d left join d.affaire a left join d.placement p left join d.reglement r left join d.sinistre s left join d.user u
        where (a.affId is null or a.affId = :affId) 
        and (p.repId is null or p.repId = :plaId) 
        and (r.regId is null or r.regId = :regId) 
        and (s.sinId is null or s.sinId = :sinId) 
        and (u.userId is null or u.userId = :userId) 
    """)
    List<ReadDocDTO> getAllDocsForObject(@Param("affId") Long affId, @Param("plaId") Long plaId,
                                         @Param("regId") Long regId, @Param("sinId") Long sinId,
                                         @Param("userId") Long userId);
}