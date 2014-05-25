package com.example.oldnewspaperfrontpage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.example.oldnewspaperfrontpage.HalftoneFactory.Option.HalftoneStyle;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class ChooseFilterActivity extends Activity {
	Bitmap tempImg;	
	Storage storage = null;
	HalftoneFactory.Option option = new HalftoneFactory.Option();
	final String TEMP_PHOTO_PATH_KEY = "com.example.oldnewspaperfrontpage.TEMP_PHOTO_PATH";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		option.setStyle(HalftoneStyle.DIAMOND);
		setContentView(R.layout.fragment_choose_filter);

		if (savedInstanceState == null) {
			storage = new Storage(getIntent().getStringExtra(StartActivity.SEND_TEMP_IMAGE_PATH_KEY));
		} else {
			storage = new Storage(savedInstanceState.getString(TEMP_PHOTO_PATH_KEY));
		}
		tempImg = storage.loadPhoto();
	}
	
	public void negative(View v) throws FileNotFoundException{
		Halftone filter = HalftoneFactory.createHalftone(tempImg, storage.getPath(), option);
		FileOutputStream out;
		out = new FileOutputStream(storage.getPath());
		filter.convert().compress(Bitmap.CompressFormat.JPEG, 100, out);
		storage.savePhoto(tempImg);
		finish();
	}
	
	public void gaussian() throws FileNotFoundException{
		Halftone filter = HalftoneFactory.createHalftone(tempImg, storage.getPath(), option);
		FileOutputStream out;
		out = new FileOutputStream(storage.getPath());
		filter.convert().compress(Bitmap.CompressFormat.JPEG, 100, out);
		storage.savePhoto(tempImg);
		finish();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choose_filter, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_choose_filter,
					container, false);
			return rootView;
		}
	}

}
