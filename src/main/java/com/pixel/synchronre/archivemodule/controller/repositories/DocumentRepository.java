package com.pixel.synchronre.archivemodule.controller.repositories;

import com.pixel.synchronre.archivemodule.model.dtos.response.ReadDocDTO;
import com.pixel.synchronre.archivemodule.model.entities.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        d.docId, d.docNum, d.docName, d.docDescription, d.docPath, d.docType.typeId, d.docType.uniqueCode, d.docType.name
        ) 
        from Document d left join d.affaire a left join d.placement p left join d.reglement r left join d.sinistre s left join d.user u
        where (
        locate(upper(coalesce(:key, '')), upper(cast(function('strip_accents',  coalesce(d.docDescription, '') ) as string))) >0 
        or locate(upper(coalesce(:key, '')), upper(cast(function('strip_accents',  coalesce(d.docType.name, '') ) as string))) >0 
        or locate(upper(coalesce(:key, '')), upper(cast(function('strip_accents',  coalesce(d.docNum, '') ) as string))) >0 
        or locate(upper(coalesce(:key, '')), upper(cast(function('strip_accents',  coalesce(d.docName, '') ) as string))) >0
        )
        and
        (a.affId is null or a.affId = :affId) 
        and (p.repId is null or p.repId = :plaId) 
        and (r.regId is null or r.regId = :regId) 
        and (s.sinId is null or s.sinId = :sinId) 
        and (u.userId is null or u.userId = :userId) 
    """)
    Page<ReadDocDTO> getAllDocsForObject(@Param("affId") Long affId, @Param("plaId") Long plaId,
                                         @Param("regId") Long regId, @Param("sinId") Long sinId,
                                         @Param("userId") Long userId, @Param("key") String key, Pageable pageable);
}