package com.personal.awsimageupload.profile;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class UserProfile {

	private final UUID userProfileId;
	private final String username;
	private String userProfileImageLink; // S3 key

	public UserProfile(
			final UUID userProfileId,
			final String username,
			final String userProfileImageLink) {

		this.userProfileId = userProfileId;
		this.username = username;
		this.userProfileImageLink = userProfileImageLink;
	}

	@Override
	public boolean equals(
			final Object o) {

		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final UserProfile that = (UserProfile) o;
		return Objects.equals(userProfileId, that.userProfileId) &&
				Objects.equals(username, that.username) &&
				Objects.equals(userProfileImageLink, that.userProfileImageLink);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userProfileId, username, userProfileImageLink);
	}

	public UUID getUserProfileId() {
		return userProfileId;
	}

	public String getUsername() {
		return username;
	}

	public Optional<String> getUserProfileImageLink() {
		return Optional.ofNullable(userProfileImageLink);
	}

	public void setUserProfileImageLink(
			final String userProfileImageLink) {
		this.userProfileImageLink = userProfileImageLink;
	}
}
