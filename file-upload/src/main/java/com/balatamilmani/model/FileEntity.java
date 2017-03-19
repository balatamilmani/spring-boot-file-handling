/*
 * Copyright (c) 2017. Balamurugan Tamilmani (balamurugan.leo@gmail.com). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are not permitted.
 */

package com.balatamilmani.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
/**
 * @author Balamurugan Tamilmani
 *
 */
@Entity(name="btam_file")
public class FileEntity {

	@Id
	@Column(name="file_id")
	String fileId;
	@Column(name="file_name")
	String fileName;
	@Column(name="file_size")
	Long fileSize;
	
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
}
