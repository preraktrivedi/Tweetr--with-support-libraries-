package com.preraktrivedi.apps.tweetr.activities;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.preraktrivedi.apps.tweetr.R;
import com.preraktrivedi.apps.tweetr.application.TweetrApp;
import com.preraktrivedi.apps.tweetr.datamodel.TweetrAppData;
import com.preraktrivedi.apps.tweetr.datamodel.User;
import com.preraktrivedi.apps.tweetr.utils.LayoutUtils;

public class ComposeTweetActivity extends Activity {

	private EditText etTweetMessage;
	private TextView tvScreenName, tvUsername, tvTweetCount;
	private ImageView ivProfileImage;
	private ProgressBar pbCompose;
	private Context mContext;
	private TweetrAppData mAppData;
	private static final int MAX_TWEET_CHAR_COUNT = 140;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		mAppData = TweetrAppData.getInstance();
		initializeView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose_tweet, menu);
		MenuItem item = menu.findItem(R.id.send_tweet);
		item.setTitle(Html.fromHtml(LayoutUtils.getComposeTitle("Tweet")));
		invalidateOptionsMenu();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initializeView() {
		styleActionBar();
		setContentView(R.layout.activity_compose_tweet);
		pbCompose = (ProgressBar) findViewById(R.id.pbCompose);
		etTweetMessage = (EditText) findViewById(R.id.etTweetMessage);
		tvScreenName = (TextView) findViewById(R.id.tvScreenNameCompose);
		tvUsername = (TextView) findViewById(R.id.tvUserNameCompose);
		tvTweetCount = (TextView) findViewById(R.id.tvTweetCount);
		ivProfileImage = (ImageView) findViewById(R.id.ivProfileCompose);
		User user =  mAppData.getAuthenticatedTwitterUser();
		tvScreenName.setText("@" + user.getScreenName());
		tvUsername.setText(user.getName());
		ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivProfileImage);
		setupListeners();
	}

	private void styleActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#297ABA")));
		getActionBar().setLogo(R.drawable.ic_logo_tweetr_white);
		getActionBar().setTitle(Html.fromHtml(LayoutUtils.getComposeTitle("Compose")));
	}

	private void setupListeners() {
		etTweetMessage.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s!=null) {
					String tweetMsg = s.toString();
					validateTweetMsg(tweetMsg);
				}
			}
		});
	}

	private void validateTweetMsg(String tweetMsg) {
		int tweetCountLeft = MAX_TWEET_CHAR_COUNT - tweetMsg.length();
		tvTweetCount.setText("" + tweetCountLeft);
		
		if(tweetCountLeft < 10) {
			tvTweetCount.setTextColor(Color.parseColor("#99E53D38"));
		} else if(tweetCountLeft < 70) {
			tvTweetCount.setTextColor(Color.parseColor("#99E88E23"));
		} else {
			tvTweetCount.setTextColor(Color.parseColor("#9900AB17"));
		}
	}

	public void onClickTweet(MenuItem mi) {
		String tweetMsg = etTweetMessage.getText().toString();
		if (TextUtils.isEmpty(tweetMsg)) {
			LayoutUtils.showToast(mContext, "Message cannot be empty");
			return;
		} 
		
		showProgressBar();
		TweetrApp.getRestClient().postTweet(tweetMsg, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject jsonTweet) {
				Intent i = new Intent();
				i.putExtra("jsonTweet", jsonTweet.toString());
				setResult(RESULT_OK, i);
				disableProgressBar();
				finish();

			}
		});
	}

	public void showProgressBar(){
		pbCompose.setVisibility(ProgressBar.VISIBLE);
	}

	public void disableProgressBar(){
		pbCompose.setVisibility(ProgressBar.INVISIBLE);
	}

}