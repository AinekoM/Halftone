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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class HalftoneDialogActivity extends Activity implements OnSeekBarChangeListener{
	final String TEMP_PHOTO_PATH_KEY = "com.example.oldnewspaperfrontpage.TEMP_PHOTO_PATH";
	final String TEMP_STYLE_KEY = "com.example.oldnewspaperfrontpage.TEMP_STYLE";
	final String TEMP_ANGLE_KEY = "com.example.oldnewspaperfrontpage.TEMP_ANGLE";
	private SeekBar bar;
	private TextView currentValue;
	int angle = 0;
	int style = 0;
	static final int HALFTONE_STYLE_DIAMOND = 0;
	static final int HALFTONE_STYLE_RECTANGLE = 1;
	static final int HALFTONE_STYLE_CIRCLE = 2;
	Storage storage;
	Bitmap img;
	HalftoneFactory.Option option = new HalftoneFactory.Option();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_halftone_dialog);
		bar = (SeekBar)findViewById(R.id.seekBarAngle);
		bar.setOnSeekBarChangeListener(this);
		if (savedInstanceState == null) {
			storage = new Storage(getIntent().getStringExtra(StartActivity.SEND_TEMP_IMAGE_PATH_KEY));
		}else 		{
			storage = new Storage(savedInstanceState.getString(TEMP_PHOTO_PATH_KEY));
			angle = savedInstanceState.getInt(TEMP_ANGLE_KEY);
			style = savedInstanceState.getInt(TEMP_STYLE_KEY);
			switch (style)
			{
			case HALFTONE_STYLE_CIRCLE:
				option.setStyle(HalftoneStyle.CIRCLE);
				break;
			case HALFTONE_STYLE_DIAMOND:
				option.setStyle(HalftoneStyle.DIAMOND);
				break;
			case HALFTONE_STYLE_RECTANGLE:
				option.setStyle(HalftoneStyle.RECTANGLE);
			}
		}
		currentValue = (TextView)findViewById(R.id.angleValue);
		Button okButton = (Button)findViewById(R.id.dialogok);
		okButton.setOnClickListener(new OkButtonClickListener());
		img = storage.loadPhoto();
	}
	
	public void onSaveInstanceState(Bundle outState)
	{
		outState.putString(TEMP_PHOTO_PATH_KEY, storage.getPath());
		outState.putInt(TEMP_ANGLE_KEY, angle);
		outState.putInt(TEMP_STYLE_KEY, style);
	}
	
	public class OkButtonClickListener implements OnClickListener
	{
		public void onClick(View v) 
		{
			Button okButton = (Button)v;
			okButton.setClickable(false);
			applyHalftone(v);
			okButton.setClickable(true);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.halftone_dialog, menu);
		return true;
	}
	
	public void applyHalftone(View v){
		//TODO: call halftone factory with option and angle
		try {
			halftone(option, angle);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void setOptionDiamond(View v){
		option.setStyle(HalftoneStyle.DIAMOND);
		style = HALFTONE_STYLE_DIAMOND;
	}
	
	public void setOptionRectangle(View v){
		option.setStyle(HalftoneStyle.RECTANGLE);
		style = HALFTONE_STYLE_RECTANGLE;
	}
	
	public void setOptionCircle(View v){
		option.setStyle(HalftoneStyle.CIRCLE);
		style = HALFTONE_STYLE_CIRCLE;
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
			View rootView = inflater.inflate(R.layout.fragment_halftone_dialog,
					container, false);
			return rootView;
		}
	}

	@Override
	public void onProgressChanged(SeekBar bar, int value, boolean arg2) {
		currentValue.setText("Angle: "+value);
		angle = value;
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/**************************************************************************
	 * Perform halftoning on the current Bitmap with the given options.
	 * 
	 * @param option	The option specified by the user for this halftoning.
	 * @throws FileNotFoundException
	 */
	public void halftone(HalftoneFactory.Option option, int degree) throws FileNotFoundException{
		option.setAngle(degree);
		Halftone filter = HalftoneFactory.createHalftone(img, storage.getPath(), option);
		FileOutputStream out = new FileOutputStream(storage.getPath());
		img = filter.convert();
		img.compress(Bitmap.CompressFormat.JPEG, 100, out);
		storage.savePhoto(img);
		finish();
	}
}
