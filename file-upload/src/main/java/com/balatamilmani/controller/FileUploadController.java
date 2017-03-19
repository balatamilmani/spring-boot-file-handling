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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.balatamilmani.dto.FileHolder;
import com.balatamilmani.exception.FileServiceFileNotFoundException;
import com.balatamilmani.model.FileEntity;
import com.balatamilmani.service.FileUploadService;

@Controller
public class FileUploadController {

	@Autowired
	FileUploadService fileUploadService;

    
    @RequestMapping(value="/greet", produces=MediaType.ALL_VALUE)
    @ResponseBody
    public String greet(){
    	System.out.println(fileUploadService);
    	return "Hello ...";
    }
    
	/**
	 * @param file The multipart file
	 * @return The unique file ID which can be used to retrieve file or its metadata
	 */
	@RequestMapping(value="/file", method = RequestMethod.POST)
	@ResponseBody
	public String handleFileUpload(@RequestParam("file") MultipartFile file) {

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		byte bytes[] = null;
		String fileUniqId = null;
		if (!file.isEmpty()) {
			try {
				IOUtils.copy(file.getInputStream(), buffer);
				bytes = buffer.toByteArray();
				fileUniqId = fileUploadService.saveFile(file.getOriginalFilename(), bytes);
			} catch (Exception e) {
				// need to be handled
			}
		} else {
			fileUniqId = "file upload failed";
		}

		return fileUniqId;
	}

	@RequestMapping("/file/{fileId}/metadata")
	@ResponseBody
	public FileEntity getFileMetaData(@PathVariable String fileId) throws Exception {
		FileEntity fe = fileUploadService.getMetaData(fileId);
		return fe;
	}

	/**
	 * @param fileId
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/file/{fileId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public void downloadFile(@PathVariable String fileId, HttpServletResponse response) throws Exception {

		FileHolder fh = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;

		try {
			fh = fileUploadService.downloadFile(fileId);
			inputStream = new ByteArrayInputStream(fh.getContent());
			outputStream = response.getOutputStream();
			response.reset();
			response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
			response.setContentType("application/txt");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + fh.getFileName() + "\"");
			IOUtils.copyLarge(inputStream, outputStream);
			outputStream.flush();
		} catch (IOException | RuntimeException e) {
			System.out.println(e.getMessage());
			throw new Exception();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}
	
    @ExceptionHandler(FileServiceFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(FileServiceFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
