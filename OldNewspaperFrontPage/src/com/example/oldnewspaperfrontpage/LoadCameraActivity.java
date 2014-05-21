package com.example.oldnewspaperfrontpage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

/*******************************************************************************
 * The Activity that handles the Load from Camera screen
 * 
 * @author 	Nguyen Doan Bao An
 * @author	Milena Mitic
 * @since	May 2014
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LoadCameraActivity extends Activity {

	//request code for the intent to call camera app
	public static final int TAKE_PHOTO_REQUEST = 1;
	//key for saved instance state bundle
	private static final String TEMP_PHOTO_PATH_KEY = "com.example.oldnewspaperfrontpage.TEMP_PHOTO_PATH";
	//path to the private file that serve as the temporary photo until the app is closed
	public Storage storage = null;
	
	public File photoInput = null;
	//the photo
	public Bitmap photo = null;

	/**********************************************************************
	 * The listener for the Take Photo Button.
	 * 
	 * @author Nguyen Doan Bao An
	 **********************************************************************/
	public class TakePhotoButtonClickListener implements OnClickListener
	{
		public void onClick(View v) 
		{
			//cause the button to be grayed out and tell the user they 
			//are being redirected to camera app
			Button sendButton = (Button)v;
			String newText = getResources().getString(R.string.taking_photo);
			sendButton.setText(newText);
			sendButton.setClickable(false);
			//call the camera app
			takePicture();
		}

	}
	
	/**********************************************************************
	 * The listener for the Use image for halftoning Button
	 * 
	 * @author Nguyen Doan Bao An
	 **********************************************************************/
	public class HalftoneButtonClickListener implements OnClickListener
	{
		public void onClick(View v) 
		{
			callApplyFilterScreen();
		}

	}
	
	/***********************************************************************
	 * Send an intent to call the Convert screen.
	 * 
	 * @param	None
	 */
	public void callApplyFilterScreen()
	{
		Intent intent = new Intent(this, ApplyFilterScreen.class);
		storage.savePhoto(photo);
		intent.putExtra(StartActivity.SEND_TEMP_IMAGE_PATH_KEY, storage.getPath());
		startActivity(intent);
	}

	/******************************************************************
	 * Call the camera app and tell it to capture a photo and save it 
	 * to the given path.
	 * 
	 * @param	None
	 *******************************************************************/
	private void takePicture()
	{
		//create the intent
		Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (photoInput != null)
		{
			//give the intent the path
			takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoInput));
			//send intent to camera app and wait for its result
			startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
		}
	}
	
	/************************************************************************
	 * Get the result from the camera app and set up the photo view.
	 * Override the original method.
	 **********************************************************************/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		//if the camera took your photo
		if (requestCode == TAKE_PHOTO_REQUEST && resultCode == RESULT_OK)
		{
			ImageView photoView = (ImageView)findViewById(R.id.cameraPhoto);
			//get the photo from temporary storage file
			photo = storage.loadPhoto();
			if (photo != null)
			{
				photoView.setImageBitmap(photo);
			}
			Button takePhotoButton = (Button)findViewById(R.id.takePhotoButton);
			takePhotoButton.setClickable(true);
			takePhotoButton.setText(R.string.take_photo);
			Button halftoneButton = (Button)findViewById(R.id.halftoneButton);
			halftoneButton.setClickable(true);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.load_camera_portrait);
		if (savedInstanceState != null)
		{
			storage = new Storage(savedInstanceState.getString(TEMP_PHOTO_PATH_KEY));
			photo = storage.loadPhoto();
		}
		else
		{
			storage = new Storage(getIntent().getStringExtra(StartActivity.SEND_TEMP_IMAGE_PATH_KEY));
		}
		photoInput = new File(storage.getPath());
		Button takePhotoButton = (Button)findViewById(R.id.takePhotoButton);
		Button halftoneButton = (Button)findViewById(R.id.halftoneButton);
		takePhotoButton.setOnClickListener(new TakePhotoButtonClickListener());
		halftoneButton.setOnClickListener(new HalftoneButtonClickListener());
		if (photo == null)
		{
			halftoneButton.setClickable(false);
		}
		else
		{
			ImageView photoView = (ImageView)findViewById(R.id.cameraPhoto);
			photoView.setImageBitmap(photo);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		storage.savePhoto(photo);
		outState.putString(TEMP_PHOTO_PATH_KEY, storage.getPath());
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
}
