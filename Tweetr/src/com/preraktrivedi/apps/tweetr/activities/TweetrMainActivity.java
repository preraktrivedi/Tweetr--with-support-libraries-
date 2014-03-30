package com.preraktrivedi.apps.tweetr.activities;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.widget.AbsListView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingRightInAnimationAdapter;
import com.preraktrivedi.apps.tweetr.R;
import com.preraktrivedi.apps.tweetr.adapters.TweetrAdapter;
import com.preraktrivedi.apps.tweetr.application.TweetrApp;
import com.preraktrivedi.apps.tweetr.datamodel.Tweet;
import com.preraktrivedi.apps.tweetr.datamodel.TweetrAppData;
import com.preraktrivedi.apps.tweetr.restclient.TweetrJsonHttpResponseHandler;
import com.preraktrivedi.apps.tweetr.utils.EndlessScrollListener;
import com.preraktrivedi.apps.tweetr.utils.LayoutUtils;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TweetrMainActivity extends Activity  implements OnDismissCallback {

	public static final String TAG = TweetrMainActivity.class.getSimpleName();
	private ArrayList<Tweet>  tweets;
	private TweetrAdapter tweetrAdapter;
	private PullToRefreshListView lvTweets;
	private Context mContext;
	private SwingRightInAnimationAdapter swingRightInAnimationAdapter;
	private TweetrAppData mAppData = TweetrAppData.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		intitUi();

		//		TweetrApp.getRestClient().getHomeTimeLineTweets(-1, -1, new JsonHttpResponseHandler() {
		//			@Override
		//			public void onSuccess(JSONArray jsonTweets) {
		//				Log.d(TAG, ">onSuccess TimelineTweets " + jsonTweets.toString());
		//				tweets = Tweet.fromJson(jsonTweets);
		//				tweetrAdapter = new TweetrAdapter(getBaseContext(), tweets);
		//				SwingRightInAnimationAdapter  swingRightInAnimationAdapter = new SwingRightInAnimationAdapter (new SwipeDismissAdapter(tweetrAdapter, (OnDismissCallback) mContext));
		//				swingRightInAnimationAdapter.setInitialDelayMillis(700);
		//				swingRightInAnimationAdapter.setAbsListView(lvTweets);
		//				swingRightInAnimationAdapter.setAnimationDurationMillis(400);
		//				lvTweets.setAdapter(swingRightInAnimationAdapter);
		//			}
		//			public void onFailure(Throwable e) {
		//				Log.d(TAG, ">onFailure TimelineTweets: " + e.toString());
		//			}
		//		});
	}

	private void intitUi() {
		LayoutUtils.showToast(mContext, "Signed in with user - " + mAppData.getAuthenticatedTwitterUser().getScreenName());
		setContentView(R.layout.activity_tweetr_main);
		styleActionBar();
		lvTweets = (PullToRefreshListView) findViewById(R.id.lvTweets);
		initAdapter();
		setupListeners();
	}

	private void initAdapter() {
		tweets = new ArrayList<Tweet>();
		tweetrAdapter = new TweetrAdapter(mContext, tweets);
		swingRightInAnimationAdapter = new SwingRightInAnimationAdapter(new SwipeDismissAdapter(tweetrAdapter, (OnDismissCallback) mContext));
		swingRightInAnimationAdapter.setInitialDelayMillis(700);
		swingRightInAnimationAdapter.setAbsListView(lvTweets);
		swingRightInAnimationAdapter.setAnimationDurationMillis(400);
		lvTweets.setAdapter(swingRightInAnimationAdapter);
		TweetrApp.getRestClient().getHomeTimeLineTweets(-1, -1,  new TweetrJsonHttpResponseHandler(mContext, TAG) {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				Log.d(TAG, ">onSuccess TimelineTweets " + jsonTweets.toString());
				tweets = Tweet.fromJson(jsonTweets);
				tweetrAdapter.addAll(tweets);
				tweetrAdapter.notifyDataSetChanged();
				swingRightInAnimationAdapter.notifyDataSetChanged();
			}
			public void onFailure(Throwable e) {
				Log.d(TAG, ">onFailure TimelineTweets: " + e.toString());
			}
		});
	}

	private void styleActionBar() {
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#297ABA")));
		getActionBar().setLogo(R.drawable.ic_logo_tweetr_white);
		getActionBar().setTitle(Html.fromHtml(LayoutUtils.getUserTitleText(mContext, mAppData.getAuthenticatedTwitterUser().getScreenName())));
	}

	private void setupListeners() {
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// Triggered only when new data needs to be appended to the list
				// Add whatever code is needed to append new items to your
				// AdapterView
				if (tweetrAdapter.getCount() > 0) {
					fetchTimelineAsync(tweetrAdapter.getItem(tweetrAdapter.getCount() - 1).getId() - 1, -1);
				}
			}
		});

		// Set a listener to be invoked when the list should be refreshed.
		lvTweets.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Your code to refresh the list contents
				// Make sure you call listView.onRefreshComplete()
				// once the loading is done. This can be done from here or any
				// place such as when the network request has completed successfully.
				fetchTimelineAsync(-1, tweetrAdapter.getItem(0).getId() + 1);
				lvTweets.onRefreshComplete();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tweetr_main, menu);
		return true;
	}

	@Override
	public void onDismiss(AbsListView arg0, int[] reverseSortedPositions) {
		for (int position : reverseSortedPositions) {
			Toast.makeText(mContext, "REMOVE TWEET", Toast.LENGTH_SHORT).show();
		}
	}

	public void fetchTimelineAsync(final long maxId, final long sinceId) {
		Log.d(TAG, ">fetchTimelineAsync");
		TweetrApp.getRestClient().getHomeTimeLineTweets(maxId, sinceId, new TweetrJsonHttpResponseHandler(mContext, TAG) {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				Log.d(TAG, ">onSuccess TimelineTweets " + jsonTweets.toString());
				if(jsonTweets.length() > 0) {
					tweets = Tweet.fromJson(jsonTweets);
					if (maxId > 0) {
						tweetrAdapter.addAll(tweets);
					}
					if (sinceId > 0) {
						for (int i = 0; i < tweets.size(); i++) {
							tweetrAdapter.insert(tweets.get(i), i);
						}
					}
					tweetrAdapter.notifyDataSetChanged();
					swingRightInAnimationAdapter.notifyDataSetChanged();
				} else {
					LayoutUtils.showToast(mContext, "No new tweets");
				}
			}
		});
	}
}
