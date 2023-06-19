package com.example.khub.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Service
public class ImageStore {

    @Value("${amazon.aws.s3.bucket-name}")
    private String bucketName;

    @Autowired
    private AmazonS3 amazonS3;

    public void upload(String fileName, Map<String, String> optionalMetadata, InputStream inputStream) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        if (!optionalMetadata.isEmpty()) {
            optionalMetadata.forEach(objectMetadata::addUserMetadata);
        }
        try {
            amazonS3.putObject(bucketName, fileName, inputStream, objectMetadata);
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to upload to S3", e);
        }
    }

    public String generatePresignedUrl(String fileName) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 10);
        return amazonS3.generatePresignedUrl(bucketName, fileName, calendar.getTime(), HttpMethod.GET).toString();
    }
}
