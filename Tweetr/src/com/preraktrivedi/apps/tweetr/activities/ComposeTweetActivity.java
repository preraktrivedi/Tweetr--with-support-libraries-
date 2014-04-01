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
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.preraktrivedi.apps.tweetr.R;
import com.preraktrivedi.apps.tweetr.application.TweetrApp;
import com.preraktrivedi.apps.tweetr.datamodel.TweetrAppData;
import com.preraktrivedi.apps.tweetr.datamodel.User;
import com.preraktrivedi.apps.tweetr.restclient.TweetrJsonHttpResponseHandler;
import com.preraktrivedi.apps.tweetr.utils.LayoutUtils;

public class ComposeTweetActivity extends Activity {

	private static final String TAG = ComposeTweetActivity.class.getSimpleName();
	private EditText etTweetMessage;
	private TextView tvScreenName, tvUsername, tvTweetCount;
	private ImageView ivProfileImage;
	private Context mContext;
	private TweetrAppData mAppData;

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
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		styleActionBar();
		setContentView(R.layout.activity_compose_tweet);
		etTweetMessage = (EditText) findViewById(R.id.etTweetMessage);
		tvScreenName = (TextView) findViewById(R.id.tvScreenNameCompose);
		tvUsername = (TextView) findViewById(R.id.tvUserNameCompose);
		tvTweetCount = (TextView) findViewById(R.id.tvTweetCount);
		ivProfileImage = (ImageView) findViewById(R.id.ivProfileCompose);
		User user =  mAppData.getAuthenticatedTwitterUser();
		tvScreenName.setText("@" + user.getScreenName());
		tvUsername.setText(user.getName());
		etTweetMessage.requestFocus();
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
					LayoutUtils.validateTweetMsg(tweetMsg, tvTweetCount);
				}
			}
		});
	}

	public void onClickTweet(MenuItem mi) {
		String tweetMsg = etTweetMessage.getText().toString();
		if (TextUtils.isEmpty(tweetMsg)) {
			LayoutUtils.showToast(mContext, "Message cannot be empty");
			return;
		} 
		
		showLoader(true);
		TweetrApp.getRestClient().postTweet(tweetMsg, "", new TweetrJsonHttpResponseHandler(mContext, TAG) {
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

}