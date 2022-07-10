package com.personal.awsimageupload.datastore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.personal.awsimageupload.profile.UserProfile;

@Repository
public class FakeUserProfileDataStore {

	private static final List<UserProfile> USER_PROFILES = new ArrayList<>();

	static {
		USER_PROFILES.add(new UserProfile(
                UUID.fromString("8197f253-f969-499e-8a59-8ec3a7926cf1"), "jannetjones", null));
		USER_PROFILES.add(new UserProfile(
                UUID.fromString("be9407a6-7bf7-4da3-8b98-3968505a7d43"), "antoniojunior", null));
	}

	public static List<UserProfile> getUserProfiles() {
		return USER_PROFILES;
	}
}
