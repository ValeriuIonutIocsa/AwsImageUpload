package com.personal.awsimageupload.datastore;

import java.util.List;

import com.personal.awsimageupload.profile.UserProfile;

public interface UserProfileDataStore {

	List<UserProfile> getUserProfiles();
}
