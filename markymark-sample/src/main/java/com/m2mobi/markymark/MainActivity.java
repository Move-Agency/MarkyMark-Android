/*
* Copyright (C) M2mobi BV - All Rights Reserved
*/

package com.m2mobi.markymark;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.m2mobi.markymarkandroid.MarkyMarkAndroid;
import com.m2mobi.markymarkcontentful.ContentfulFlavor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	private static final String LOG_TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		LinearLayout layout = findViewById(R.id.layout);

		String markDown = loadMarkDownFromAsset(this, "contentful.txt");

		final MarkyMark<View> mark = MarkyMarkAndroid.getMarkyMark(this, new ContentfulFlavor());
		List<View> views = mark.parseMarkDown(markDown);
		for (View view : views) {
			layout.addView(view);
		}
	}

	public String loadMarkDownFromAsset(final Context pContext, final String pFileName) {
		StringBuilder sb = new StringBuilder("");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(
					new InputStreamReader(pContext.getAssets().open(pFileName)));
			String mLine;
			while ((mLine = reader.readLine()) != null) {
				sb.append(mLine);
				sb.append("\n");
			}
		} catch (IOException e) {
			Log.e(LOG_TAG, "Unable to read asset file", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					Log.e(LOG_TAG, "Unable to close reader", e);
				}
			}
		}
		return sb.toString();
	}
}
