package com.ptit.hirex.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class FileUploadService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    @Value("${minio.url.public}")
    private String minioPublicUrl;

    public String uploadFile(MultipartFile file) throws Exception {
        try {
            String fileName = getSaveFileName(file);

            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());

            return minioPublicUrl + "/" + bucketName + "/" + fileName;

        } catch (MinioException | IOException e) {
            throw new Exception("Error uploading file: " + e.getMessage());
        }
    }

    private static String getSaveFileName(MultipartFile file) throws Exception {
        String fileType = file.getContentType();
        String folder;
        assert fileType != null;
        if (fileType.startsWith("image/")) {
            folder = "images";  // Save to images folder
        } else if (fileType.equals("application/pdf")) {
            folder = "pdfs";     // Save to pdfs folder
        } else {
            throw new Exception("Unsupported file type: " + fileType);
        }

        return folder + "/" + System.currentTimeMillis() + file.getOriginalFilename();
    }
}
