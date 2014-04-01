package com.preraktrivedi.apps.tweetr.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.preraktrivedi.apps.tweetr.R;
import com.preraktrivedi.apps.tweetr.datamodel.Tweet;
import com.preraktrivedi.apps.tweetr.datamodel.TweetrAppData;
import com.preraktrivedi.apps.tweetr.datamodel.User;
import com.preraktrivedi.apps.tweetr.utils.LayoutUtils;

public class DetailedTweetActivity extends Activity {

	private Context mContext;
	private TweetrAppData mAppData;
	private Tweet currentDetailedTweet;
	private TextView tvScreenName, tvUsername, tvTweetBody;
	private ImageView ivProfileImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		mAppData = TweetrAppData.getInstance();
		if (mAppData.getCurrentDetailedTweet() != null) {
			currentDetailedTweet = mAppData.getCurrentDetailedTweet();
			initializeView();
		} else {
			LayoutUtils.showToast(mContext, getString(R.string.error_something_wrong));
			this.finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail_tweet, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			mAppData.setCurrentDetailedTweet(null);
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initializeView() {
		styleActionBar();
		setContentView(R.layout.activity_detailed_tweet);
		tvScreenName = (TextView) findViewById(R.id.tvScreenNameCompose);
		tvUsername = (TextView) findViewById(R.id.tvUserNameCompose);
		tvTweetBody = (TextView) findViewById(R.id.tvTweetBody);
		ivProfileImage = (ImageView) findViewById(R.id.ivProfileCompose);
		User user = currentDetailedTweet.getUser();
		tvScreenName.setText("@" + user.getScreenName());
		tvUsername.setText(user.getName());
		tvTweetBody.setText(currentDetailedTweet.getBody());
		ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivProfileImage);
		//setupListeners();
	}

	private void styleActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#297ABA")));
		getActionBar().setLogo(R.drawable.ic_logo_tweetr_white);
		getActionBar().setTitle(Html.fromHtml(LayoutUtils.getComposeTitle("Tweet")));
	}

}
