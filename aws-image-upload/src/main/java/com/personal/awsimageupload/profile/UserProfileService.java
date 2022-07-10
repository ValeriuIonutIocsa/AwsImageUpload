package com.personal.awsimageupload.profile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.personal.awsimageupload.bucket.BucketName;
import com.personal.awsimageupload.filestore.FileStore;

@Service
public class UserProfileService {

	private final UserProfileDataAccessService userProfileDataAccessService;
	private final FileStore fileStore;

	@Autowired
	public UserProfileService(
			final UserProfileDataAccessService userProfileDataAccessService,
			final FileStore fileStore) {

		this.userProfileDataAccessService = userProfileDataAccessService;
		this.fileStore = fileStore;
	}

	List<UserProfile> getUserProfiles() {
		return userProfileDataAccessService.getUserProfiles();
	}

	void uploadUserProfileImage(
			final UUID userProfileId,
			final MultipartFile file) {

		// 1. Check if image is not empty
		if (file.isEmpty()) {
			throw new IllegalStateException("Cannot upload empty file [" + file.getName() + "]");
		}

		// 2. Check if file is an image
		final String contentType = file.getContentType();
		if (!Arrays.asList(
				ContentType.IMAGE_JPEG.getMimeType(),
				ContentType.IMAGE_PNG.getMimeType(),
				ContentType.IMAGE_GIF.getMimeType())
				.contains(contentType)) {
			throw new IllegalStateException("File must be an image [" + contentType + "]");
		}

		// 3. Check if the user exists is our database
		final UserProfile userProfile = tryComputeUserProfile(userProfileId);

		// 4. Grab some metadata from file if any
		final long contentLength = file.getSize();
		final Map<String, String> metadataMap = new HashMap<>();
		metadataMap.put("Content-Type", contentType);
		metadataMap.put("Content-Length", String.valueOf(contentLength));

		// 5. Store the image in s3 and update database (userProfileDataAccessService) with s3 image link
		final String path = BucketName.PROFILE_IMAGE.getBucketName() + "/" + userProfileId;
		final String fileName = "file-" + userProfileId;
		try {
			fileStore.save(path, fileName, contentLength, metadataMap, file.getInputStream());
			userProfile.setUserProfileImageLink(fileName);
		} catch (final IOException e) {
			throw new IllegalStateException(e);
		}
	}

	byte[] downloadUserProfileImage(
			final UUID userProfileId) {

		final UserProfile userProfile = tryComputeUserProfile(userProfileId);
		final String bucketName = BucketName.PROFILE_IMAGE.getBucketName();
		final String path = bucketName + "/" + userProfileId;

		final Optional<String> userProfileImageLink = userProfile.getUserProfileImageLink();
		return userProfileImageLink
				.map(key -> fileStore.download(path, key))
				.orElse(new byte[0]);
	}

	private UserProfile tryComputeUserProfile(
			final UUID userProfileId) {

		UserProfile userProfile = null;
		final List<UserProfile> userProfiles = userProfileDataAccessService.getUserProfiles();
		for (final UserProfile aUserProfile : userProfiles) {

			final UUID aUserProfileId = aUserProfile.getUserProfileId();
			if (aUserProfileId != null && aUserProfileId.equals(userProfileId)) {

				userProfile = aUserProfile;
				break;
			}
		}
		if (userProfile == null) {
			throw new IllegalStateException("User profile " + userProfileId + " not found");
		}
		return userProfile;
	}
}
