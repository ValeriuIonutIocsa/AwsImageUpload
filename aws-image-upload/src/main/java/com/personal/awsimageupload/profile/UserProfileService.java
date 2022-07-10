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
			throw new IllegalStateException(
					String.format("User profile %s not found", userProfileId));
		}

		// 4. Grab some metadata from file if any
		final Map<String, String> metadata = new HashMap<>();
		metadata.put("Content-Type", contentType);
		metadata.put("Content-Length", String.valueOf(file.getSize()));

		// 5. Store the image in s3 and update database (userProfileDataAccessService) with s3 image link
		final String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), userProfileId);
		final String fileName = String.format("%s-%s", file.getOriginalFilename(), userProfileId);
		try {
			fileStore.save(path, fileName, Optional.of(metadata), file.getInputStream());
            userProfile.setUserProfileImageLink(fileName);
		} catch (final IOException e) {
			throw new IllegalStateException(e);
		}
	}
}
