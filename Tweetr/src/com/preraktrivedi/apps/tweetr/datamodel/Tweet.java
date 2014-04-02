package com.preraktrivedi.apps.tweetr.datamodel;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tweet extends BaseModel {

	private User user;
	private String body;
	private String timestamp;
	private int retweetCount;
	private int favouritesCount;
	private String mediaUrl;

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

	public String getMediaUrl() {
		return mediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

	public boolean isFavorited() {
		return getBoolean("favorited");
	}

	public boolean isRetweeted() {
		return getBoolean("retweeted");
	}

	public int getRetweetCount() {
		return retweetCount;
	}
	public void setRetweetCount(int retweet_count) {
		this.retweetCount = retweet_count;
	}
	public int getFavouritesCount() {
		return favouritesCount;
	}
	public void setFavouritesCount(int favourites_count) {
		this.favouritesCount = favourites_count;
	}

	public static Tweet fromJson(JSONObject jo) {
		Tweet tweet = new Tweet();

		try {
			tweet.jsonObject = jo;
			tweet.user = User.fromJson(jo.getJSONObject("user"));
			tweet.timestamp = jo.getString("created_at");
			tweet.body = jo.getString("text");
			tweet.retweetCount = jo.getInt("retweet_count");
			tweet.favouritesCount = jo.getInt("favorite_count");
			try {
				tweet.mediaUrl = mediaUrlFromJsonEntity(jo.getJSONObject("entities"));
			} catch (JSONException e) {
				tweet.mediaUrl = "";
			}
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

	private static String mediaUrlFromJsonEntity(JSONObject entitiesObject) throws JSONException {
		String mediaUrl = "";
		if (entitiesObject.has("media") && !entitiesObject.isNull("media")) {
			JSONArray mediaArray = entitiesObject.getJSONArray("media");
			if (mediaArray.length() > 0) {
				JSONObject mediaJsonObj = mediaArray.getJSONObject(0);
				if (mediaJsonObj.has("media_url") && !mediaJsonObj.isNull("media_url")) {
					mediaUrl = mediaJsonObj.getString("media_url");
				}
			}
		}
		return mediaUrl;
	}
}