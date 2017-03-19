/*
 * Copyright (c) 2017. Balamurugan Tamilmani (balamurugan.leo@gmail.com). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are not permitted.
 */
package com.balatamilmani.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.balatamilmani.dto.FileHolder;
import com.balatamilmani.exception.FileServiceException;
import com.balatamilmani.model.FileEntity;
import com.balatamilmani.service.FileUploadService;

@RestController
@RequestMapping("/file")
public class FileUploadController {

	@Autowired
	FileUploadService fileUploadService;
    
	/**
	 * @param file The multipart file
	 * @return The unique file ID which can be used to retrieve file or its metadata
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String handleFileUpload(@RequestParam("file") MultipartFile file) throws FileServiceException{

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		byte bytes[] = null;
		String fileUniqId = null;
		if (!file.isEmpty()) {
			try {
				IOUtils.copy(file.getInputStream(), buffer);
				bytes = buffer.toByteArray();
				fileUniqId = fileUploadService.saveFile(file.getOriginalFilename(), bytes);
			} catch (IOException e) {
				throw new FileServiceException("Couldn't save the File", e);
			}
		} else {
			throw new FileServiceException("File is empty or no File chosen");
		}
		return fileUniqId;
	}

	@RequestMapping("/{fileId}/metadata")
	public FileEntity getFileMetaData(@PathVariable String fileId) throws FileServiceException {
		FileEntity fe = fileUploadService.getFileEntity(fileId);
		return fe;
	}

	/**
	 * @param fileId
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{fileId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public void downloadFile(@PathVariable String fileId, HttpServletResponse response) throws FileServiceException {

		FileHolder fh = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;

		try {
			fh = fileUploadService.downloadFile(fileId);
			inputStream = new ByteArrayInputStream(fh.getContent());
			outputStream = response.getOutputStream();
			response.reset();
			response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
			response.setHeader("Content-Disposition", "attachment;filename=\"" + fh.getFileName() + "\"");
			IOUtils.copyLarge(inputStream, outputStream);
			outputStream.flush();
		} catch (IOException e) {
			throw new FileServiceException("Couldn't retrieve the File", e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					//Ignore
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					//Ignore
				}
			}
		}
	}
	
    @ExceptionHandler(FileServiceException.class)
    public String handleStorageFileNotFound(FileServiceException exc) {
    	return exc.getMessage();
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleStorageFileNotFound(Exception exc) {
        return ResponseEntity.notFound().build();
    }
}
