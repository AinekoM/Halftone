package com.example.oldnewspaperfrontpage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.os.Build;

/******************************************************************************
 * Activity that handles the start screen
 * 
 * @author 	Milena Mitic
 * @author	Nguyen Doan Bao An
 * @since 	May 2014
 *
 */
public class StartActivity extends Activity {
	public static final int LOAD_IMAGE_REQUEST = 1;
	public static final String SEND_TEMP_IMAGE_PATH_KEY = "com.example.oldnewspaperfrontpage.SEND_TEMP_IMAGE_PATH_KEY";
	File temp = null;
	Bitmap tempImg = null;
	public static String absolutePathTempPhoto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_start);
		try {
			temp = createImageFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		File temp = new File(absolutePathTempPhoto);
		temp.delete();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
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
	 * Sends an intent to the gallery to display images for picking. It relays on
	 * onActivityResult to display the selected image.
	 * 
	 * @param view View used by this intent
	 */
	public void loadImage(View v) {
		//intent that 'requests' the pick action from image gallery (images stored on SD card - external storage)
    	Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //starts new activity that will display selected image
    	startActivityForResult(intent, LOAD_IMAGE_REQUEST);
    }
	
	/**
	 * Handles the result of the loadImage intent. It decodes the stream returned 
	 * by the intent and displays the image.
	 * 
	 * @param requestCode code that identifies the intent that sent the request
	 * @param resultCode  code that informs about successful run of the intent
	 * @param data Intent, data returned by the requesting intent
	 * @throws FileNotFoundException
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		Uri imageUri;				
		//If the pick operation was successful and the request was sent by loadImage, display the image
		if (resultCode == RESULT_OK && requestCode == LOAD_IMAGE_REQUEST) {
			//get image uri
            imageUri = data.getData();
            try {
            	//gets an input stream from uri
            	InputStream stream = getContentResolver().openInputStream(imageUri);
            	//decodes the stream (extracts image)
            	tempImg = BitmapFactory.decodeStream(stream);
            	FileOutputStream out = new FileOutputStream(absolutePathTempPhoto);
            	tempImg.compress(Bitmap.CompressFormat.JPEG, 100, out);
            	Intent display = new Intent(this, ApplyFilterScreen.class);
            	display.putExtra(SEND_TEMP_IMAGE_PATH_KEY, absolutePathTempPhoto);
            	startActivity(display);
            } catch (FileNotFoundException e) {
            	  e.printStackTrace();
            }
        }
	}
	
	/**************************************************************************
	 * Create a uniquely named storage file not scnnable by media scanner and
	 * remember the path.
	 * 
	 * @return	The file created.
	 * @throws 	IOException
	 */
	private File createImageFile() throws IOException{
		//Give unique name by using time stamp
		String timeStamp = new SimpleDateFormat("HHmmss_ddMMyyyy").format(new Date());
		String imageFileName = "Cache_JPEG_" + timeStamp;
		//try to create a file in the private storage
		File image = File.createTempFile(imageFileName, ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
		//remember the path for later uses
		absolutePathTempPhoto = image.getAbsolutePath();
		return image;
	}
	
	/**************************************************************************
	 * Send an intent to start the Load from Camera screen
	 * 
	 * @param view	The button that calls this function.
	 */
	public void startCamera(View view)
	{
		Intent intent = new Intent(this, LoadCameraActivity.class);
		intent.putExtra(SEND_TEMP_IMAGE_PATH_KEY, absolutePathTempPhoto);
		startActivity(intent);
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
			View rootView = inflater.inflate(R.layout.fragment_start,
					container, false);
			return rootView;
		}
	}

}