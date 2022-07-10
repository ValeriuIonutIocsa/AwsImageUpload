package com.personal.awsimageupload.filestore;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

@Service
public class FileStore {

	private final AmazonS3 s3;

	@Autowired
	public FileStore(
			final AmazonS3 s3) {
		this.s3 = s3;
	}

	public void save(
            final String path,
            final String fileName,
            final Optional<Map<String, String>> optionalMetadata,
            final InputStream inputStream) {

		final ObjectMetadata objectMetadata = new ObjectMetadata();
		optionalMetadata.ifPresent(map -> map.forEach(objectMetadata::addUserMetadata));
		try {
			s3.putObject(path, fileName, inputStream, objectMetadata);
		} catch (final AmazonServiceException e) {
			throw new IllegalStateException("Failed to store file to s3", e);
		}
	}
}
