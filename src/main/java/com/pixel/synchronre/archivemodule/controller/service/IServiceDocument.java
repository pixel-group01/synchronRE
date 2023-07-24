package com.pixel.synchronre.archivemodule.controller.service;

import com.pixel.synchronre.archivemodule.model.dtos.request.UpdateDocReq;
import com.pixel.synchronre.archivemodule.model.dtos.request.UploadDocReq;
import com.pixel.synchronre.archivemodule.model.dtos.response.ReadDocDTO;
import com.pixel.synchronre.archivemodule.model.entities.Document;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

public interface IServiceDocument
{
	void uploadFile(MultipartFile file, String destinationPath) throws RuntimeException;
	byte[] downloadFile(String filePAth);

    @Transactional
	boolean uploadDocument(UploadDocReq dto) throws UnknownHostException;

	@Transactional
	boolean deleteDocument(Long docId) throws UnknownHostException;

	@Transactional
    boolean updateDocument(UpdateDocReq dto) throws IOException;

    void displayPdf(HttpServletResponse response, byte[] reportBytes, String displayName)  throws Exception;
	boolean deleteFile(String filePath);
	String generatePath(MultipartFile file, String objectFolder, String typeCode, String objectName);
	void renameFile(String oldPath, String newPath);

	Page<ReadDocDTO> getAllDocsForObject(Long affId, Long plaId, Long regId, Long sinId, Long userId, String key, Pageable pageable);
}
