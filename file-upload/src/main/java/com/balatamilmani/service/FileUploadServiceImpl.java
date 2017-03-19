/*
 * Copyright (c) 2017. Balamurugan Tamilmani (balamurugan.leo@gmail.com). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are not permitted.
 */
package com.balatamilmani.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.balatamilmani.dao.FileDaoImpl;
import com.balatamilmani.dto.FileHolder;
import com.balatamilmani.exception.FileServiceException;
import com.balatamilmani.model.FileEntity;

/**
 * @author Balamurugan Tamilmani
 *
 */

@Service
@Transactional
public class FileUploadServiceImpl implements FileUploadService{

	@Autowired
	private FileDaoImpl fileDao;
	
	@Override
	public String saveFile(String fileName, byte[] bytes) throws FileServiceException{
		FileEntity fe = null;
		long fileSize = bytes.length;
		String dirPath = null;
		String absoluteFilePath = null;
		
		//Create a Unique ID for File
		String fileId = UUID.randomUUID().toString();
		
		dirPath = System.getProperty("java.io.tmpdir");
		absoluteFilePath = dirPath+FileSystems.getDefault().getSeparator()+fileId+"_"+fileName;
		
		//save the file meta data
		fe = new FileEntity();
		fe.setFileId(fileId);
		fe.setFileName(fileName);
		fe.setFileSize(fileSize);
		fileDao.persist(fe);
		
		//Write the file
		try {
			Files.write(Paths.get(absoluteFilePath), bytes);
		} catch (IOException e) {
			throw new FileServiceException("Couldn't save the File", e);
		}		
		return fileId;
	}

	@Override
	public FileHolder downloadFile(String fileId) throws FileServiceException{
		FileEntity fe = null;
		FileHolder fh = null;
		InputStream inputStream = null;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		byte bytes[] = null;
		
		fh = new FileHolder();
		fe = getFileEntity(fileId);

		Path path = Paths.get(System.getProperty("java.io.tmpdir")+FileSystems.getDefault().getSeparator()+fileId+"_"+fe.getFileName());
		fh.setFileName(fe.getFileName());
			try {
				inputStream = Files.newInputStream(path);
				IOUtils.copy(inputStream, buffer);
				bytes = buffer.toByteArray();
				fh.setContent(bytes);
				
			} catch (IOException|RuntimeException e) {
				throw new FileServiceException("Couldn't retrieve the File", e);
			} finally {
				if(inputStream != null){
					try {
						inputStream.close();
					} catch (IOException e) {
						//Ignore
					}
				}
				if(buffer != null){
					try {
						buffer.close();
					} catch (IOException e) {
						//Ignore
					}
				}
			}
			return fh;
	}

	@Override
	public FileEntity getFileEntity(String fileId) throws FileServiceException {
		FileEntity fe = fileDao.find(fileId);
		if(fe == null){
			throw new FileServiceException("Invalid FileID, no File found with the given ID");
		}
		return fe;
	}
}
