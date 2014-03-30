package com.preraktrivedi.apps.tweetr.datamodel;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tweet extends BaseModel {

	private User user;
	private String body;
	private String timestamp;

	public String getBody() {
		return this.body;
	}

	public User getUser() {
		return user;
	}

	public String getTimestamp() {
		return this.timestamp;
	}

	public long getId() {
		return getLong("id");
	}

	public boolean isFavorited() {
		return getBoolean("favorited");
	}

	public boolean isRetweeted() {
		return getBoolean("retweeted");
	}

	public static Tweet fromJson(JSONObject jo) {
		Tweet tweet = new Tweet();

		try {
			tweet.jsonObject = jo;
			tweet.user = User.fromJson(jo.getJSONObject("user"));
			tweet.timestamp = jo.getString("created_at");
			tweet.body = jo.getString("text");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return tweet;
	}

	public static ArrayList<Tweet> fromJson(JSONArray ja) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();	
		for (int i = 0; i < ja.length(); i++) {
			JSONObject jo = null;
			try {
				jo = ja.getJSONObject(i);
			} catch (JSONException e) {
				e.printStackTrace();
				continue;
			}
			Tweet tweet = Tweet.fromJson(jo);
			if (tweet != null) {
				tweets.add(tweet);
			}
		}
		return tweets;
	}
}