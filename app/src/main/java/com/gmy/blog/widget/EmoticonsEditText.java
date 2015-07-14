package com.gmy.blog.widget;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import u.aly.s;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

public class EmoticonsEditText extends EditText {

	public EmoticonsEditText(Context context) {
		super(context);
	}

	public EmoticonsEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public EmoticonsEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		if (!TextUtils.isEmpty(text)) {
			super.setText(replace(text.toString()), type);
		} else {
			super.setText(text, type);
		}
	}

	private Pattern buildPattern() {
		return Pattern.compile("\\\\ue[a-z0-9]{3}", Pattern.CASE_INSENSITIVE);
	}

	private Pattern buildPattern2() {
		return Pattern.compile("@+[^&#@]+#", Pattern.CASE_INSENSITIVE);
	}

	private Pattern buildPattern3() {
		return Pattern.compile("#+[^&#@]+#", Pattern.CASE_INSENSITIVE);
	}

	private CharSequence replace(String text) {
		try {
			// SpannableString spannableString = new SpannableString(text);
			SpannableString spannableString = new SpannableString(text);
			int start = 0;
			Pattern pattern = buildPattern();
			Matcher matcher = pattern.matcher(text);
			while (matcher.find()) {
				String faceText = matcher.group();
				String key = faceText.substring(1);
				BitmapFactory.Options options = new BitmapFactory.Options();
				Bitmap bitmap = BitmapFactory.decodeResource(
						getContext().getResources(),
						getContext().getResources().getIdentifier(key,
								"drawable", getContext().getPackageName()),
						options);
				ImageSpan imageSpan = new ImageSpan(getContext(), bitmap);
				int startIndex = text.indexOf(faceText, start);
				int endIndex = startIndex + faceText.length();
				if (startIndex >= 0)
					spannableString.setSpan(imageSpan, startIndex, endIndex,
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				start = (endIndex - 1);
			}

			Pattern pattern2 = buildPattern2();// @gmy
			Matcher matcher2 = pattern2.matcher(text);
			while (matcher2.find()) {
				String faceText = matcher2.group();

				faceText = faceText.substring(0, faceText.length() - 1);
				Log.i("gmyboy", "--------fore@---faceText------" + faceText);
				int startIndex = text.indexOf(faceText, start);// 0
				int endIndex = startIndex + faceText.length();// 4
				Log.i("gmyboy", "--------fore@---start------" + startIndex+"\n"+"--------fore@---end------" + endIndex);
				spannableString.removeSpan(spannableString.getSpanEnd(endIndex));
				spannableString.setSpan(
						new ForegroundColorSpan(Color.parseColor("#4877AB")),
						startIndex, endIndex,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

				start = (endIndex);
			}
			Pattern pattern3 = buildPattern3();
			Matcher matcher3 = pattern3.matcher(text);
			while (matcher3.find()) {
				String faceText = matcher3.group();
				Log.i("gmyboy", "--------fore#---------" + faceText);
				int startIndex = text.indexOf(faceText, start);
				int endIndex = startIndex + faceText.length();// 6
				spannableString.setSpan(
						new ForegroundColorSpan(Color.parseColor("#4877AB")),
						startIndex, endIndex,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				start = (endIndex);
			}

			return spannableString;
		} catch (Exception e) {
			return text;
		}
	}
}
