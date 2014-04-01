package com.preraktrivedi.apps.tweetr.activities;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.preraktrivedi.apps.tweetr.R;
import com.preraktrivedi.apps.tweetr.application.TweetrApp;
import com.preraktrivedi.apps.tweetr.datamodel.Tweet;
import com.preraktrivedi.apps.tweetr.datamodel.TweetrAppData;
import com.preraktrivedi.apps.tweetr.datamodel.User;
import com.preraktrivedi.apps.tweetr.restclient.TweetrJsonHttpResponseHandler;
import com.preraktrivedi.apps.tweetr.utils.LayoutUtils;

public class DetailedTweetActivity extends Activity {

	private static final String TAG = DetailedTweetActivity.class.getSimpleName();
	private Context mContext;
	private TweetrAppData mAppData;
	private Tweet currentDetailedTweet;
	private EditText etTweetBox;
	private TextView tvScreenName, tvUsername, tvTweetBody, tvFavouritesCount, tvRetweetsCounts, tvTweetCharCount, tvSendTweet;
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
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		styleActionBar();
		setContentView(R.layout.activity_detailed_tweet);
		User user = currentDetailedTweet.getUser();
		ivProfileImage = (ImageView) findViewById(R.id.ivProfileCompose);
		ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivProfileImage);
		tvScreenName = (TextView) findViewById(R.id.tvScreenNameCompose);
		tvUsername = (TextView) findViewById(R.id.tvUserNameCompose);
		tvTweetBody = (TextView) findViewById(R.id.tvTweetBody);
		tvFavouritesCount = (TextView) findViewById(R.id.tvFavoritesCount); 
		tvRetweetsCounts  = (TextView) findViewById(R.id.tvRetweetsCount);
		tvTweetCharCount  = (TextView) findViewById(R.id.tv_replyTweetCount); 
		tvSendTweet = (TextView) findViewById(R.id.tv_sendTweet);
		etTweetBox = (EditText) findViewById(R.id.et_tweetbox);
		tvScreenName.setText("@" + user.getScreenName());
		tvUsername.setText(user.getName());
		tvTweetBody.setText(currentDetailedTweet.getBody());
		tvRetweetsCounts.setText(currentDetailedTweet.getRetweetCount() + " Retweets");
		tvFavouritesCount.setText(currentDetailedTweet.getFavouritesCount() + " Favourites");
		etTweetBox.setHint("Reply to " + user.getName());
		setupListeners();
	}

	private void setupListeners() {
		etTweetBox.addTextChangedListener(new TextWatcher() {

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
					LayoutUtils.validateTweetMsg(tweetMsg, tvTweetCharCount);
				}
			}
		});
		ivProfileImage.requestFocus();
		hideSoftKeyboard(etTweetBox);
		
		etTweetBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String screenName = "@" + currentDetailedTweet.getUser().getScreenName() + " ";
				etTweetBox.setText(screenName);
				etTweetBox.setSelection(screenName.length());
			}
		});
		
		tvSendTweet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				postTweet();
			}
		});
	}

	private void styleActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#297ABA")));
		getActionBar().setLogo(R.drawable.ic_logo_tweetr_white);
		getActionBar().setTitle(Html.fromHtml(LayoutUtils.getComposeTitle("Tweet")));
	}

	private void postTweet() {
		String tweetMsg = etTweetBox.getText().toString();
		if (TextUtils.isEmpty(tweetMsg)) {
			LayoutUtils.showToast(mContext, "Message cannot be empty");
			return;
		} 
		
		if (!tweetMsg.contains(currentDetailedTweet.getUser().getScreenName())) {
			LayoutUtils.showToast(mContext, "Replies to this tweet should mention the author!");
			return;
		}

		showLoader(true);
		TweetrApp.getRestClient().postTweet(tweetMsg, String.valueOf(currentDetailedTweet.getId()), new TweetrJsonHttpResponseHandler(mContext, TAG) {
			@Override
			public void onSuccess(JSONObject jsonTweet) {
				showLoader(false);
				finish();
			}
		});
	}

	private void showLoader(boolean show) {
		setProgressBarIndeterminateVisibility(show);
	}

	public void hideSoftKeyboard(View view) {
		InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

}
