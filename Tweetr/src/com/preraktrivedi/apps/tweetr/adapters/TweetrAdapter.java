package com.preraktrivedi.apps.tweetr.adapters;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
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
		ImageView mediaImage;
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
			viewHolder.mediaImage = (ImageView) convertView.findViewById(R.id.ivMediaImageThumb);
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
		viewHolder.mediaImage.setVisibility(View.GONE);
		if (!TextUtils.isEmpty(tweet.getMediaUrl())) {
			String mediaUrl = tweet.getMediaUrl() + ":thumb";
			ImageLoader.getInstance().displayImage(mediaUrl, viewHolder.mediaImage);
			viewHolder.mediaImage.setVisibility(View.VISIBLE);
		}
	}
}