package com.personal.awsimageupload.config;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AmazonConfig {

	@Bean
	public AmazonS3 s3() {

		final Properties properties = new Properties();
		final Path credentialsFilePath = Paths.get("C:\\IVI\\Apps\\AWS\\rootkey.properties");
		try (final InputStream inputStream = new BufferedInputStream(
				Files.newInputStream(credentialsFilePath))) {
			properties.load(inputStream);
		} catch (final Exception e) {
			throw new IllegalStateException("failed to load AWS root key properties", e);
		}
		final String accessKey = properties.get("AWSAccessKeyId").toString();
		final String secretKey = properties.get("AWSSecretKey").toString();

		final AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
		return AmazonS3ClientBuilder
				.standard()
				.withRegion("eu-central-1")
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
				.build();
	}
}
