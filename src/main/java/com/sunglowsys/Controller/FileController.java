package com.sunglowsys.Controller;

import com.sunglowsys.payload.FileResponse;
import com.sunglowsys.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;
    @Value("${project.image}")
    private  String path;

    private  String fileName;

    private List<String> extensionArray = Arrays.asList("png", "jpg","jpeg", "gif");

    @PostMapping("/upload")
    public ResponseEntity<FileResponse> fileUpload(@RequestParam(value = "image")MultipartFile image){
        try {
            if (!extensionArray.contains(checkExtension(image.getOriginalFilename()))) {

                return new ResponseEntity<>(new FileResponse(image.getOriginalFilename(), "Invalid file extension!"), HttpStatus.NOT_ACCEPTABLE);

            }
            fileName = fileService.uploadImage(path, image);
        }
        catch (IOException e){
            e.printStackTrace();
            return new ResponseEntity<>(new FileResponse( fileName, "Image is not uploaded due to error !!"), HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return  new ResponseEntity<>(new  FileResponse(fileName,"Image is successfully uploaded !!"),HttpStatus.OK);
    }
    private String checkExtension(String fileName){
        String[] splitArray = fileName.split("\\.");
        String extension = splitArray[splitArray.length-1];
        return extension;
    }
}
