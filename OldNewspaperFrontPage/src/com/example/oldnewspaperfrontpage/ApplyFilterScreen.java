package com.example.oldnewspaperfrontpage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.oldnewspaperfrontpage.HalftoneFactory.Option.HalftoneStyle;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

/******************************************************************************
 * The Activity that handles the Convert Screen
 * 
 * @author 	Milena Mitic
 * @author	Nguyen Doan Bao An
 * @since	May 2014
 */
public class ApplyFilterScreen extends Activity {
	Bitmap tempImg;	
	final String TEMP_PHOTO_PATH_KEY = "com.example.oldnewspaperfrontpage.TEMP_PHOTO_PATH";
	final String FLAG_HALFTONED_KEY = "com.example.oldnewspaperfrontpage.HALFTONED_KEY";
	//String imagePath;
	Storage storage = null;
	Storage permStorage = null;
	boolean halftoned = false;
	
	/**************************************************************************
	 * The listener for the convert button.
	 * 
	 * @author 	Nguyen Doan Bao An
	 * @author	Milena Mitic
	 */
	public class ConvertButtonClickListener implements OnClickListener
	{
		public void onClick(View v) 
		{
			Button convertButton = (Button)v;
			convertButton.setClickable(false);
			
			openContextMenu(v);
				
			convertButton.setClickable(true);
		}

	}
	
	/**************************************************************************
	 * The listener for the share button.
	 * 
	 * @author 	Nguyen Doan Bao An
	 */
	public class ShareButtonClickListener implements OnClickListener
	{
		public void onClick(View v) 
		{
			Button shareButton = (Button)v;
			shareButton.setClickable(false);
			share(v);
			shareButton.setClickable(true);
		}

	}	
	
	/**************************************************************************
	 * The listener for the edit button.
	 * 
	 * @author 	Milena Mitic
	 */
	public class EditButtonClickListener implements OnClickListener
	{
		public void onClick(View v) 
		{
			Button editButton = (Button)v;
			editButton.setClickable(false);
			edit(v);
			editButton.setClickable(true);
		}
	}
	
	/**************************************************************************
	 * The listener for the save button.
	 * 
	 * @author	Milena Mitic
	 * @author 	Nguyen Doan Bao An
	 */
	public class SaveButtonClickListener implements OnClickListener
	{
		public void onClick(View v) 
		{
			Button saveButton = (Button)v;
			saveButton.setClickable(false);
			save(v);
			saveButton.setClickable(true);
		}
	}	
	
	/*************************************************************************
	 * Save the current bitmap to a file scannable by gallery and media scanner
	 * 
	 * @param v	The view that calls this method.
	 */
	private void save(View v) {
		permStorage = new Storage();
		try {
			permStorage.setPath(createScannableImageFile());
			permStorage.savePhoto(tempImg);
			galleryAddPic();
			//finish();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**********************************************************************
	 * Create a path to a photo scannable by the media scanner.  
	 * 
	 * @return				The path to an image scannable by the media 
	 * 						scanner
	 * @throws IOException	If file cannot be created.
	 ***********************************************************************/
	private String createScannableImageFile() throws IOException
	{
		//give unique name with time stamp
		String timeStamp = new SimpleDateFormat("HHmmss_ddMMyyyy").format(new Date());
		String imageFileName = "Dot_It_" + timeStamp + "";
		//look for a spot in the external storage that the media scanner can see 
		File storageDir = Environment.getExternalStorageDirectory();
		//create a file
		File image = File.createTempFile(imageFileName, ".jpg", storageDir);
		//remember the path for other uses
		return image.getAbsolutePath();
	}
	
	/***********************************************************************
	 * Send an intent to tell the media scanner to pick up the newly saved
	 * photo.
	 * 
	 * @param	None
	 **********************************************************************/
	private void galleryAddPic() 
	{
		//create the intent and tell it where the file is
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(permStorage.getPath());
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		//broadcast the intent to media scanner
		this.sendBroadcast(mediaScanIntent);
	}
	
	/************************************************************************
	 * Send an intent to call the edit image screen.
	 * 
	 * @param v	The view that calls this method
	 */
	private void edit(View v) {
		Intent intent = new Intent(this, EditPhotoActivity.class);
		intent.putExtra(StartActivity.SEND_TEMP_IMAGE_PATH_KEY, storage.getPath());
		startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_display_image_screen);
		registerForContextMenu(findViewById(R.id.convert_button));
		if (savedInstanceState == null)
		{
			//imagePath = getIntent().getStringExtra("tempImagePath");
			storage = new Storage(getIntent().getStringExtra(StartActivity.SEND_TEMP_IMAGE_PATH_KEY));
			//halftoned = getIntent().getBooleanExtra(EditPhotoActivity.FLAG_EDITED_KEY, false);
			//tempImg = BitmapFactory.decodeFile(imagePath);
		}
		else
		{
			storage = new Storage(savedInstanceState.getString(TEMP_PHOTO_PATH_KEY));
			
			halftoned = savedInstanceState.getBoolean(FLAG_HALFTONED_KEY);
		}
		tempImg = storage.loadPhoto();
		Button shareButton = (Button)findViewById(R.id.share_button);
		Button convertButton = (Button)findViewById(R.id.convert_button);
		Button editButton = (Button)findViewById(R.id.edit_button);
		Button saveButton = (Button)findViewById(R.id.save_button);
		shareButton.setOnClickListener(new ShareButtonClickListener());
		convertButton.setOnClickListener(new ConvertButtonClickListener());
		editButton.setOnClickListener(new EditButtonClickListener());
		saveButton.setOnClickListener(new SaveButtonClickListener());
		if (!halftoned)
		{
			shareButton.setClickable(false);
			editButton.setClickable(false);
			saveButton.setClickable(false);
		}
		displayImage();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		tempImg = storage.loadPhoto();
		displayImage();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		storage.savePhoto(tempImg);
		outState.putString(TEMP_PHOTO_PATH_KEY, storage.getPath());
		outState.putBoolean(FLAG_HALFTONED_KEY, halftoned);
	}

	/**************************************************************************
	 * Send an itent to call the applications on the device that can share
	 * an image and allow the user to pick one to share the current image
	 * in storage.
	 * 
	 * @param view	The view that calls this method
	 */
	public void share(View view)
	{
		Uri imageUri = Uri.fromFile(new File(storage.getPath()));
		//creates new intent with sharing action
		Intent intentShare = new Intent(Intent.ACTION_SEND);
		//provides data to be shared
		intentShare.putExtra(Intent.EXTRA_STREAM, imageUri);
		//sets type of the shared data
		intentShare.setType("image/jpeg");
		//Sets title of the window that displays sharing applications
    	String title = getResources().getString(R.string.share_with);
    	//creates chooser fort he sharing intent
    	Intent chooser = Intent.createChooser(intentShare, title);
    	//starts activity
    	startActivity(chooser);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.halftone_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		// Handle item selection
		HalftoneFactory.Option option = new HalftoneFactory.Option();
	    switch (item.getItemId()) {
	    	case R.id.action_settings:
	    		return true;
	    		
	        case R.id.diamond:
			try {
				option.setStyle(HalftoneStyle.DIAMOND);
				halftone(option);
				return true;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	            
	        case R.id.rectangle:
			try {
				option.setStyle(HalftoneStyle.RECTANGLE);
				halftone(option);
				return true;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        case R.id.circle:
			try {
				option.setStyle(HalftoneStyle.CIRCLE);
				halftone(option);
				return true;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	            
	    }
		return super.onOptionsItemSelected(item);
	}

	/**************************************************************************
	 * Display the current Bitmap.
	 * 
	 * @param	None
	 */
	public void displayImage(){
		ImageView viewImage = (ImageView) findViewById(R.id.display_image);
		viewImage.setImageBitmap(tempImg);
	}
	
	/**************************************************************************
	 * Perform halftoning on the current Bitmap with the given options.
	 * 
	 * @param option	The option specified by the user for this halftoning.
	 * @throws FileNotFoundException
	 */
	public void halftone(HalftoneFactory.Option option) throws FileNotFoundException{
		Halftone filter = HalftoneFactory.createHalftone(tempImg, storage.getPath(), option);
		FileOutputStream out = new FileOutputStream(storage.getPath());
		filter.convert().compress(Bitmap.CompressFormat.JPEG, 100, out);
		displayImage();
		halftoned = true;
		Button shareButton = (Button)findViewById(R.id.share_button);
		shareButton.setClickable(true);
		Button editButton = (Button)findViewById(R.id.edit_button);
		editButton.setClickable(true);
		Button saveButton = (Button)findViewById(R.id.save_button);
		saveButton.setClickable(true);
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
			View rootView = inflater.inflate(
					R.layout.fragment_display_image_screen, container, false);
			return rootView;
		}
	}
}
