/*
 * Copyright (c) 2017. Balamurugan Tamilmani (balamurugan.leo@gmail.com). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are not permitted.
 */
SET database sql syntax ORA TRUE;

DROP SCHEMA PUBLIC CASCADE


CREATE TABLE btam_file (file_id VARCHAR2(50), file_name VARCHAR2(100), file_size NUMBER);