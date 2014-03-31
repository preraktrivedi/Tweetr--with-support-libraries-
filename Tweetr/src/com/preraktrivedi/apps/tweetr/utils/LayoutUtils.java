package com.preraktrivedi.apps.tweetr.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.preraktrivedi.apps.tweetr.R;
import com.preraktrivedi.apps.tweetr.datamodel.User;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class LayoutUtils {

	private static final long WEEK_IN_MILLIS = 604800000;
	private static final long SECOND_IN_MILLIS = 1000;

	public static void showToast(Context context, String message) {
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 300);
		toast.show();
	}

	public static String getUserTitleText(Context context, String username) {
		StringBuilder str = new StringBuilder("<font color=\"#E0EAEF\">");
		str.append(context.getResources().getString(R.string.app_name) + " - @");
		str.append(username);
		str.append("</font>");

		return str.toString();
	}

	public static String getComposeTitle(String msg) {
		StringBuilder str = new StringBuilder("<font color=\"#E0EAEF\">");
		str.append(msg);
		str.append("</font>");

		return str.toString();
	}

	public static String getFormattedTimestamp(Context context, String timestamp) {
		String dateString = timestamp;
		Date date = new Date();
		try {
			date = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH).parse(dateString);
		} catch (ParseException e) {
			Log.e("getFormattedTimestamp", e.toString());
		}

		CharSequence charSeq = DateUtils.getRelativeDateTimeString(context, date.getTime(), SECOND_IN_MILLIS,  WEEK_IN_MILLIS,  0);
		String formattedTime = charSeq.toString();
		formattedTime = formattedTime.substring(0, formattedTime.indexOf(","));
		Log.d("getFormattedTimestamp", " formatted time - " + formattedTime);
		return getShortFormatTime(formattedTime);
	}

	private static String getShortFormatTime(String formattedTime) {
		String input = formattedTime.replace(" ", "");
		StringBuilder shortFormat = new StringBuilder();
		
		for (int i = 0; i < input.length(); i++) {
			if(Character.isLetter(input.charAt(i))) {
				shortFormat.append(input.charAt(i));
				break;
			} else {
				shortFormat.append(input.charAt(i));
			}
		}
		Log.d("getFormattedTimestamp", " short formatted time - " + shortFormat.toString());
		return shortFormat.toString();
	}

	public static String getFormattedUsername(User user) {
		StringBuilder formattedName = new StringBuilder("<b>");
		formattedName.append(user.getName());
		formattedName.append("</b><small> <font color = '#777777'>@");
		formattedName.append(user.getScreenName());
		formattedName.append("</font></small>");
		return formattedName.toString();
	}
}
