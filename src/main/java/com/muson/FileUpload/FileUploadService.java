package com.muson.FileUpload;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileUploadService {
    public void upload(MultipartFile file) throws IOException, IllegalStateException {
        file.transferTo(new File("D:\\MusonFiles\\" + file.getOriginalFilename())); //create this folder in your D:\\ volume
    }
}
