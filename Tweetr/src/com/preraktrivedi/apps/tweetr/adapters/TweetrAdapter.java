package com.preraktrivedi.apps.tweetr.adapters;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.preraktrivedi.apps.tweetr.R;
import com.preraktrivedi.apps.tweetr.datamodel.Tweet;
import com.preraktrivedi.apps.tweetr.utils.LayoutUtils;

public class TweetrAdapter extends ArrayAdapter<Tweet> {

	private Context mContext;
	
	private static class ViewHolder {
		ImageView profile;
		TextView name;
		TextView body;
		TextView tvTimeStamp;
	}

	public TweetrAdapter(Context context, List<Tweet> tweets) {
		super(context, R.layout.tweet_item, tweets);
		this.mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final Tweet tweet = getItem(position);
		ViewHolder viewHolder;

		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.tweet_item, null, false);
			viewHolder.body = (TextView) convertView.findViewById(R.id.tvBody);
			viewHolder.name = (TextView) convertView.findViewById(R.id.tvName);
			viewHolder.profile = (ImageView) convertView.findViewById(R.id.ivProfile);
			viewHolder.tvTimeStamp = (TextView) convertView.findViewById(R.id.tvTimeStamp);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		configureView(tweet, viewHolder);

		return convertView;
	}

	private void configureView(Tweet tweet, ViewHolder viewHolder) {
		ImageLoader.getInstance().displayImage(tweet.getUser().getProfileImageUrl(), viewHolder.profile);
		viewHolder.name.setText(Html.fromHtml(LayoutUtils.getFormattedUsername(tweet.getUser())));
		viewHolder.body.setText(Html.fromHtml(tweet.getBody()));
		viewHolder.tvTimeStamp.setText(LayoutUtils.getFormattedTimestamp(mContext, tweet.getTimestamp()));
		
		

		//		profile.setOnClickListener(new OnClickListener() {
		//			@Override
		//			public void onClick(View v) {
		//				SimpleTwitterApp.getRestClient().getSpecifiedUserProfile(tweet.getUser().getScreenName(),
		//						new JsonHttpResponseHandler() {
		//					@Override
		//					public void onSuccess(JSONObject jo) {
		//						User authUser = User.fromJson(jo);
		//						Intent i = new Intent(getContext(), ProfileActivity.class);
		//						Bundle bundle = new Bundle();
		//						//Can send serializable object
		//						bundle.putString("screenName", authUser.getScreenName());
		//						bundle.putString("userProfileImageUrl", authUser.getProfileImageUrl());
		//						bundle.putString("name", authUser.getName());
		//						bundle.putString("tag", authUser.getDescription());
		//						bundle.putInt("followers", authUser.getFollowersCount());
		//						bundle.putInt("following", authUser.getFriendsCount());
		//						i.putExtras(bundle);
		//						getContext().startActivity(i);
		//					}
		//				});
		//			}
		//		});
	}
}