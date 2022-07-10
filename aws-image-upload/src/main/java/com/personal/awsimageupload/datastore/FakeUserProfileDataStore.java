package com.personal.awsimageupload.datastore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.personal.awsimageupload.profile.UserProfile;

@Repository
public class FakeUserProfileDataStore implements UserProfileDataStore {

	private static final List<UserProfile> USER_PROFILES = new ArrayList<>();

	static {
		final UUID userProfileId = UUID.fromString("8197f253-f969-499e-8a59-8ec3a7926cf1");
		USER_PROFILES.add(new UserProfile(
				userProfileId, "jannetjones", "file-" + userProfileId));

		final UUID userProfileId2 = UUID.fromString("be9407a6-7bf7-4da3-8b98-3968505a7d43");
		USER_PROFILES.add(new UserProfile(
				userProfileId2, "antoniojunior", "file-" + userProfileId2));
	}

	@Override
	public List<UserProfile> getUserProfiles() {
		return USER_PROFILES;
	}
}
