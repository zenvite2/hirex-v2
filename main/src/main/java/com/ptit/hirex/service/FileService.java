package com.ptit.hirex.service;

import com.ptit.hirex.constants.FileConstant;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    private String getFilePathKey(String originalFilename, String companyId, String typeUpload) {
        // Generate new filename
        Date current = new Date();

        String newFileName = String.valueOf(current.getTime());
        String modifiedFilename = modifyFilename(originalFilename, newFileName);

        return Util.generateFileDirectory(typeUpload, companyId, modifiedFilename);
    }

    private String modifyFilename(String originalFilename, String newFilename) {
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

        return newFilename + fileExtension;
    }

    public String uploadImageFile(MultipartFile file, String oldFileName, String companyId, String typeUpload) {
        try {
            String filePathKey = getFilePathKey(file.getOriginalFilename(), companyId, typeUpload);
            byte[] fileBytes = resizeImage(file.getBytes(), typeUpload);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileBytes);

            if (oldFileName != null && !oldFileName.isEmpty()) {
                minioClient.removeObject(
                        RemoveObjectArgs.builder().bucket(bucketName).object(oldFileName).build()
                );
            }

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filePathKey) // Set directory structure
                            .stream(byteArrayInputStream, fileBytes.length, -1)
                            .build());

            return Util.generateFileDirectory(bucketName, filePathKey);
        } catch (Exception e) {
            log.error("Upload image file to minio failed: " + e.getMessage(), e);

            return null;
        }
    }

    public String uploadAchievementsAttached(MultipartFile file, String companyId, String typeUpload) {
        try {
            String filePathKey = getFilePathKey(file.getOriginalFilename(), companyId, typeUpload);
            byte[] fileBytes = resizeImage(file.getBytes(), typeUpload);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileBytes);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filePathKey)
                            .stream(byteArrayInputStream, fileBytes.length, -1)
                            .build());

            return Util.generateFileDirectory(bucketName, filePathKey);
        } catch (Exception e) {
            log.error("Upload image file to minio failed: " + e.getMessage(), e);

            return null;
        }
    }

    @SneakyThrows
    private byte[] resizeImage(byte[] imageData, String typeUpload) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);

            BufferedImage image = ImageIO.read(inputStream);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            int originalWidth = image.getWidth();
            int originalHeight = image.getHeight();
            Integer resizeDimension = getResizeDimensionByTypeUpload(typeUpload);

            if (resizeDimension != null && Math.max(originalWidth, originalHeight) > resizeDimension) {
                String formatName = getImageFormat(imageData);

                if (!Util.isNullOrEmpty(formatName)) {
                    // Resize the image
                    int newWidth;
                    int newHeight;

                    if (originalWidth > originalHeight) {
                        newWidth = resizeDimension;
                        newHeight = (int) (((double) originalHeight / originalWidth) * newWidth);
                    } else {
                        newHeight = resizeDimension;
                        newWidth = (int) (((double) originalWidth / originalHeight) * newHeight);
                    }

                    image = getBufferedImageResize(image, newWidth, newHeight);

                    ImageIO.write(image, formatName, outputStream);

                    imageData = outputStream.toByteArray();

                    outputStream.reset();
                }
            }

            return imageData;
        } catch (IOException e) {
            log.error("Compress image failed " + e.getMessage(), e);

            throw e;
        }
    }

    private static BufferedImage getBufferedImageResize(BufferedImage originalImage, int width, int height) {
        int imageType = originalImage.getType();
        if (imageType == BufferedImage.TYPE_CUSTOM) {
            imageType = BufferedImage.TYPE_INT_ARGB; // Avoid error case that BufferedImage does not accept TYPE_CUSTOM
        }

        BufferedImage resizedImage = new BufferedImage(width, height, imageType);
        Graphics2D graphics = resizedImage.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR); // for better image quality
        graphics.drawImage(originalImage, 0, 0, width, height, null);
        graphics.dispose();

        return resizedImage;
    }

    @SneakyThrows
    private static String getImageFormat(byte[] imageData) {
        try {
            ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(imageData));

            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);

            if (imageReaders.hasNext()) {
                ImageReader reader = imageReaders.next();
                return reader.getFormatName();
            }
        } catch (IOException e) {
            log.error("Get image format failed " + e.getMessage(), e);

            return null;
        }

        return null;
    }

    private Integer getResizeDimensionByTypeUpload(String typeUpload) {
        switch (typeUpload) {
            case "avatar":
                return FileConstant.AVATAR_RESIZE_DIMENSION_MAX;
            case "coverImage":
                return FileConstant.COVER_IMAGE_RESIZE_DIMENSION_MAX;
            default:
                return null;
        }
    }
}
