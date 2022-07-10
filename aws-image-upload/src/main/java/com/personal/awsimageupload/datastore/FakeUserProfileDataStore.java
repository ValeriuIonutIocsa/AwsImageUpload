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
		USER_PROFILES.add(new UserProfile(UUID.randomUUID(), "jannetjones", null));
		USER_PROFILES.add(new UserProfile(UUID.randomUUID(), "antoniojunior", null));
	}

	public static List<UserProfile> getUserProfiles() {
		return USER_PROFILES;
	}
}
