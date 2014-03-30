package com.preraktrivedi.apps.tweetr.datamodel;

import org.json.JSONObject;

public class User {

	private String name;
	private long id;
	private String screenName;
	private String profileImageUrl;
	private String profileBackGroundImageUrl;
	private int statusesCount;
	private int followersCount;
	private int friendsCount;
	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public String getProfileBackGroundImageUrl() {
		return profileBackGroundImageUrl;
	}

	public void setProfileBackGroundImageUrl(String profileBackGroundImageUrl) {
		this.profileBackGroundImageUrl = profileBackGroundImageUrl;
	}

	public int getStatusesCount() {
		return statusesCount;
	}

	public void setStatusesCount(int statusesCount) {
		this.statusesCount = statusesCount;
	}

	public int getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}

	public int getFriendsCount() {
		return friendsCount;
	}

	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static User fromJson(JSONObject jo) {
		User user = new User();
		try {
			if (!jo.isNull("name")) {
				user.setName(jo.getString("name"));
			}
			if (!jo.isNull("profile_image_url")) {
				user.setProfileImageUrl(jo.getString("profile_image_url"));
			}
			if (!jo.isNull("profile_background_image_url")) {
				user.setProfileBackGroundImageUrl(jo
						.getString("profile_background_image_url"));
			}
			if (!jo.isNull("description")) {
				user.setDescription(jo.getString("description"));
			}
			try {
				if (jo.getLong("id") > 0) {
					user.setId(Long.valueOf(jo.getLong("id")));
				}
			} catch (Exception e) {
			}

			if (!jo.isNull("screen_name")) {
				user.setScreenName(jo.getString("screen_name"));
			}
			try {
				if (jo.getLong("friends_count") > 0) {
					user.setFriendsCount(jo.getInt("friends_count"));
				}
			} catch (Exception e) {
			}
			try {
				if (jo.getInt("followers_count") > 0) {
					user.setFollowersCount(jo.getInt("followers_count"));
				}
			} catch (Exception e) {

			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return user;

	}
}