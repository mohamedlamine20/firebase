package com.example.firebase;
import java.io.IOException;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequiredArgsConstructor
public class FileUploadController {

    FileService fileService;


    public static final String ACCOUNT_SID = "AC9c6e48bf089162d3cb453ca1da7fb82b";
    public static final String AUTH_TOKEN ="58810a26baf6ff9e30df7356d141f8c3";

            Logger logger = LoggerFactory.getLogger(FileUploadController.class);


    @PostMapping("/file/upload")
    public Object upload(@RequestParam("file") MultipartFile multipartFile) {
        logger.info("upload | File Name : {}", multipartFile.getOriginalFilename());
        return fileService.upload(multipartFile);
    }

    @PostMapping("/file/download/{fileName}")
    public Object download(@PathVariable String fileName) throws IOException {
        logger.info("download | File Name : {}", fileName);
        return fileService.download(fileName);
    }
    @GetMapping(value = "/sendSMS")
    public ResponseEntity<String> sendSMS() {


        Twilio.init(ACCOUNT_SID,AUTH_TOKEN );
        Message.creator(new PhoneNumber("+213676547476"),
                new PhoneNumber("+13602276433"), "Hello from Twilio 📞").create();

        return new ResponseEntity<String>("Message sent successfully", HttpStatus.OK);
    }


}