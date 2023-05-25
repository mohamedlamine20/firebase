package com.example.firebase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import io.minio.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
public class FileService {
    @Value("${download.url}")

    String DOWNLOAD_URL ;
    @Value("${bucket.name}")
    String bucketName;

    @Value("${firebase.credentials.path}")
    String firebaseCredentialsPath;


    public Object upload(MultipartFile multipartFile) {

        try {
            String fileName = multipartFile.getOriginalFilename();
           // fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));

            File file = this.convertToFile(multipartFile, fileName);
            String TEMP_URL = this.uploadFile(file, fileName);
            file.delete();
            return new MessageResponse(HttpStatus.OK,"Successfully Uploaded !", TEMP_URL);
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.toString(), "Unsuccessfully Uploaded!");
        }

    }

    public Object download(String fileName) throws IOException {
        //String destFileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));
        String destFilePath = "/home/mohamed/Desktop/" + fileName;
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(firebaseCredentialsPath));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        Blob blob = storage.get(BlobId.of(bucketName, fileName));
        blob.downloadTo(Paths.get(destFilePath));
        return new MessageResponse(HttpStatus.OK, "Successfully Downloaded!",destFilePath);
    }

    private String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(firebaseCredentialsPath));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }

//    private String getExtension(String fileName) {
//        return fileName.substring(fileName.lastIndexOf("."));
//    }


    public  String uploadMini() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        MinioClient minioClient = MinioClient
                .builder()
                .endpoint("http://localhost:9000")
                .credentials("41ViwHVyGBKKFT4F","ERQESaZOuzIhONOHENoZoAAAKgpW5Wmv")
                .build();

        minioClient.bucketExists(BucketExistsArgs.builder().bucket("test").build());
        ObjectWriteResponse  response= minioClient.uploadObject(
                UploadObjectArgs.builder()
                        .bucket("test")
                        .object("image.png")
                        .filename("/home/mohamed/Desktop/firebase/image.png")
                        .contentType("image/png")
                .build()
        );
        return response.object();
    }
}

