package com.personal.awsimageupload.profile;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserProfileService {

	private final UserProfileDataAccessService userProfileDataAccessService;

	@Autowired
	public UserProfileService(
			final UserProfileDataAccessService userProfileDataAccessService) {

		this.userProfileDataAccessService = userProfileDataAccessService;
	}

	List<UserProfile> getUserProfiles() {
		return userProfileDataAccessService.getUserProfiles();
	}

	void uploadUserProfileImage(
			final UUID userProfileId,
			final MultipartFile file) {

		// 1. Check if image is not empty
		// 2. Check if file is an image
		// 3. Check if the user exists is our database
		// 4. Grab some metadata from file if any
		// 5. Store the image in s3 and update database (userProfileDataAccessService) with s3 image link
	}
}
