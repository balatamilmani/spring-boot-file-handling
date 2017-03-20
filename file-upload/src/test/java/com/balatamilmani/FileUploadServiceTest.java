package com.balatamilmani;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.balatamilmani.service.FileUploadService;


/**
 * @author Balamurugan Tamilmani
 *
 */
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class FileUploadServiceTest {

    @Autowired
    private MockMvc mvc;

	@Autowired
	FileUploadService fileUploadService;

    @Test
    public void shouldSaveUploadedFile() throws Exception {
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "FileServiceTestFile.txt", "text/plain", "Test Content".getBytes());
        MvcResult result = this.mvc.perform(fileUpload("/file").file(multipartFile))
               .andExpect(status().is(200)).andReturn();
        String fileId = result.getResponse().getContentAsString();
        assertNotNull(fileId);
    }

}
