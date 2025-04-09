package com.example.Employee_CRUD.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.Employee_CRUD.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.amazonaws.services.s3.model.GetObjectRequest;
import org.springframework.beans.factory.annotation.Value;

import java.net.URL;
import java.util.Base64;
import java.io.InputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Map;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.auth.AWSStaticCredentialsProvider;

@Component
public class UploadS3 {

    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;

    @Value("${aws.bucketName}")
    private String bucketName;

    public String getFileAsBase64(String fileName) throws IOException {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty.");
        }
        String imageUrl = "https://res.cloudinary.com/dtxxayb3j/image/upload/" + fileName;
        try (InputStream inputStream = new URL(imageUrl).openStream()) {
            byte[] imageBytes = inputStream.readAllBytes();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch image from Cloudinary", e);
        }
    }

    public boolean fileExists(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty.");
        }
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.AP_SOUTH_1)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
        return s3Client.doesObjectExist(bucketName, fileName);
    }

    public void deleteFromS3(String fileName) throws UserException {
        String[] parts = fileName.split("\\.");
        fileName = parts[0];
        try {
            if (fileName == null || fileName.isEmpty()) {
                throw new IllegalArgumentException("File name cannot be null or empty.");
            }
            Cloudinary cloudinary = new Cloudinary("cloudinary://895995814835695:qP17WFOUyypYFWgc50X8-rqXjPg@dtxxayb3j");
            Map result = cloudinary.uploader().destroy(fileName, ObjectUtils.emptyMap());
            if ("not found".equals(result.get("result"))) {
                throw new UserException("Profile picture not found!", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new UserException("Failed to delete file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void uploadToS3(byte[] file, String fileName) throws Exception {
        if (!isPhoto(file, fileName)) {
            throw new IllegalArgumentException("Invalid file format! Only real JPG or PNG images are allowed.");
        }
        try {
            String[] parts = fileName.split("\\.");
            fileName = parts[0];
            Cloudinary cloudinary = new Cloudinary("cloudinary://895995814835695:qP17WFOUyypYFWgc50X8-rqXjPg@dtxxayb3j");
            Map uploadParams = ObjectUtils.asMap(
                    "use_filename", true,
                    "unique_filename", false,
                    "overwrite", true,
                    "public_id", fileName
            );
            cloudinary.uploader().upload(file, uploadParams);
        } catch (Exception e) {
            throw new Exception("Failed to upload file: " + e.getMessage());
        }
    }

    private boolean isPhoto(byte[] fileBytes, String fileName) {
        if (!fileName.toLowerCase().endsWith(".jpg") && !fileName.toLowerCase().endsWith(".jpeg") && !fileName.toLowerCase().endsWith(".png")) {
            return false;
        }
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                return false;
            }
            return isJpegMagicNumber(fileBytes) || isPngMagicNumber(fileBytes);
        } catch (IOException e) {
            return false;
        }
    }

    private boolean isJpegMagicNumber(byte[] fileBytes) {
        if (fileBytes.length < 2) return false;
        return (fileBytes[0] & 0xFF) == 0xFF && (fileBytes[1] & 0xFF) == 0xD8;
    }

    private boolean isPngMagicNumber(byte[] fileBytes) {
        if (fileBytes.length < 8) return false;
        return (fileBytes[0] & 0xFF) == 0x89 &&
                (fileBytes[1] & 0xFF) == 0x50 &&
                (fileBytes[2] & 0xFF) == 0x4E &&
                (fileBytes[3] & 0xFF) == 0x47 &&
                (fileBytes[4] & 0xFF) == 0x0D &&
                (fileBytes[5] & 0xFF) == 0x0A &&
                (fileBytes[6] & 0xFF) == 0x1A &&
                (fileBytes[7] & 0xFF) == 0x0A;
    }
}
