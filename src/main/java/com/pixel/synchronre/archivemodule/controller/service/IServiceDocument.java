package com.pixel.synchronre.archivemodule.controller.service;

import com.pixel.synchronre.archivemodule.model.dtos.request.UploadDocReq;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IServiceDocument
{
	void uploadFile(MultipartFile file, String destinationPath) throws RuntimeException;
	public byte[] downloadFile(String filePAth);
	void displayPdf(HttpServletResponse response, byte[] reportBytes, String displayName)  throws Exception;
	boolean deleteFile(String filePath);
	String generatePath(MultipartFile file, String objectFolder, String typeCode, String objectName);
	void renameFile(String oldPath, String newPath);
}
