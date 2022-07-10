package com.personal.awsimageupload.filestore;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

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
			final long contentLength,
			final Map<String, String> metadataMap,
			final InputStream inputStream) {

		try {

			final ObjectMetadata objectMetadata = new ObjectMetadata();

			objectMetadata.setContentLength(contentLength);

			if (metadataMap != null) {

				for (final Map.Entry<String, String> mapEntry : metadataMap.entrySet()) {

					final String key = mapEntry.getKey();
					final String value = mapEntry.getValue();
					objectMetadata.addUserMetadata(key, value);
				}
			}

			s3.putObject(path, fileName, inputStream, objectMetadata);

		} catch (final AmazonServiceException e) {
			throw new IllegalStateException("Failed to store file to s3", e);
		}
	}

	public byte[] download(
			final String path,
			final String key) {

		try {
			final S3Object s3Object = s3.getObject(path, key);
			final S3ObjectInputStream objectContent = s3Object.getObjectContent();
			return IOUtils.toByteArray(objectContent);

		} catch (final AmazonServiceException | IOException e) {
			throw new IllegalStateException("Failed to download file from s3", e);
		}
	}
}
