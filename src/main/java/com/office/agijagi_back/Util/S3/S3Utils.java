package com.office.agijagi_back.Util.S3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class S3Utils {
    @Value("${cloud.aws.region.static}")
    private String region;

    // AWS S3 자격 증명 지정
    @Bean
    public AmazonS3Client amazonS3Client(){
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region).enablePathStyleAccess()
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .build();
    }
}
