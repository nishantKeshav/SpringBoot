package com.example.Employee_CRUD.utils;

import org.springframework.stereotype.Component;
import com.amazonaws.services.s3.model.GetObjectRequest;
import org.springframework.beans.factory.annotation.Value;

import java.util.Base64;
import java.io.InputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

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
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty.");
        }
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.AP_SOUTH_1)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
        InputStream inputStream = s3Client.getObject(new GetObjectRequest(bucketName, fileName)).getObjectContent();
        byte[] fileBytes = inputStream.readAllBytes();
        return Base64.getEncoder().encodeToString(fileBytes);
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

    public void deleteFromS3(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty.");
        }
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.AP_SOUTH_1)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
        s3Client.deleteObject(bucketName, fileName);
    }

    public void uploadToS3(byte[] file, String fileName) throws Exception {
        if (!isPhoto(file, fileName)) {
            throw new IllegalArgumentException("Invalid file format! Only real JPG or PNG images are allowed.");
        }

        try {
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(Regions.AP_SOUTH_1)
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .build();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(file);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.length);
            metadata.setContentType(isJpegMagicNumber(file) ? "image/jpeg" : "image/png");
            s3Client.putObject(bucketName, fileName, inputStream, metadata);
            inputStream.close();
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
