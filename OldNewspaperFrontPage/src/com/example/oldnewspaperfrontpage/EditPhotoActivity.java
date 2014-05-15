package com.example.oldnewspaperfrontpage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.os.Build;

public class EditPhotoActivity extends Activity {
	public final static String FLAG_EDITED_KEY = "com.example.oldnewspaperfrontpage.FLAG_EDITED_KEY";
	static final String TEMP_PHOTO_PATH_KEY = "com.example.oldnewspaperfrontpage.TEMP_PHOTO_PATH";
	static final String ORIGINAL_PHOTO_PATH_KEY = "com.example.oldnewspaperfrontpage.ORIGINAL_PHOTO_PATH";
	Bitmap tempImg;	
	Storage storage = null;
	Storage tempStorage = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_edit_photo);
		if (savedInstanceState == null)
		{
			storage = new Storage(getIntent().getStringExtra(StartActivity.SEND_TEMP_IMAGE_PATH_KEY));
			tempImg = storage.loadPhoto();
			try {
				createImageFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tempStorage.savePhoto(tempImg);
		}
		else
		{
			storage = new Storage(savedInstanceState.getString(ORIGINAL_PHOTO_PATH_KEY));
			tempStorage = new Storage(savedInstanceState.getString(TEMP_PHOTO_PATH_KEY));
			tempImg = tempStorage.loadPhoto();
		}
		tempImg = tempStorage.loadPhoto();
		displayImage();
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		tempStorage.savePhoto(tempImg);
		outState.putString(ORIGINAL_PHOTO_PATH_KEY, storage.getPath());
		outState.putString(TEMP_PHOTO_PATH_KEY, tempStorage.getPath());
	}
	
	protected void onResume() {
		super.onResume();
		tempImg = tempStorage.loadPhoto();
		displayImage();
	}
	
	private void displayImage() {
		ImageView viewImage = (ImageView) findViewById(R.id.imageView1);
		viewImage.setImageBitmap(tempImg);
		
	}

	private File createImageFile() throws IOException{
		//Give unique name by using time stamp
		String timeStamp = new SimpleDateFormat("HHmmss_ddMMyyyy").format(new Date());
		String imageFileName = "Cache_JPEG_" + timeStamp;
		//try to create a file in the private storage
		File image = File.createTempFile(imageFileName, ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
		//remember the path for later uses
		tempStorage = new Storage(image.getAbsolutePath());
		return image;
	}
	
	public void addCaption(View v){
		tempImg = storage.loadPhoto();
		tempStorage.savePhoto(tempImg);
		Intent intent = new Intent(this, AddCaptionActivity.class);
		intent.putExtra(FLAG_EDITED_KEY, tempStorage.getPath());
		startActivity(intent);
	}
	
	private void changePhoto() {
		Canvas temp = new Canvas(tempImg);
		Paint paint = new Paint();
		RectF rect = new RectF(0.0f, 0.0f, 150.0f, 150.0f);
		paint.setColor(Color.BLACK);
		temp.drawOval(rect, paint);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_photo, menu);
		return true;
	}
	public void confirm(View v){
		storage.savePhoto(tempImg);
		finish();
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
			View rootView = inflater.inflate(R.layout.fragment_edit_photo,
					container, false);
			return rootView;
		}
	}

}
