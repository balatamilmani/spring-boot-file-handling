/*
 * Copyright (c) 2017. Balamurugan Tamilmani (balamurugan.leo@gmail.com). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are not permitted.
 */
package com.balatamilmani.service;

import com.balatamilmani.dto.FileHolder;
import com.balatamilmani.model.FileEntity;

/**
 * @author Balamurugan Tamilmani
 *
 */

public interface FileUploadService {
	public String saveFile(String fileName, byte bytes[]) throws Exception;
	
	public FileHolder downloadFile(String fileId) throws Exception;
	
	public FileEntity getMetaData(String fileId) throws Exception;
}
