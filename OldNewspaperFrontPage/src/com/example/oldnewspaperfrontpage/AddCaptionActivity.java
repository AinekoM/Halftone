package com.example.oldnewspaperfrontpage;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddCaptionActivity extends Activity {
	Bitmap tempImg;	
	Storage tempStorage = null;
	static final String TEMP_PHOTO_PATH_KEY = "com.example.oldnewspaperfrontpage.TEMP_PHOTO_PATH";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_add_caption);
		if (savedInstanceState == null)
		{
			tempStorage = new Storage(getIntent().getStringExtra(EditPhotoActivity.FLAG_EDITED_KEY));
			tempImg = tempStorage.loadPhoto();
		}
		else
		{
			tempStorage = new Storage(savedInstanceState.getString(TEMP_PHOTO_PATH_KEY));
			tempImg = tempStorage.loadPhoto();
		}
	}
	
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		tempStorage.savePhoto(tempImg);
		outState.putString(TEMP_PHOTO_PATH_KEY, tempStorage.getPath());
	}
	
	public void getText(View v){
		EditText text = (EditText) findViewById(R.id.editText);
		String caption = text.getText().toString();
		addCaption(caption);
	}

	private void addCaption(String caption) {
		Canvas temp = new Canvas(tempImg);
		Paint p = new Paint();
		float imgHeight = tempImg.getHeight();
		float textHeight = imgHeight * 0.09f;
		float imgWidth = tempImg.getWidth();
		p.setTextSize(textHeight);
		float width = p.measureText(caption);
		Path path = new Path();
		RectF rect = new RectF(imgWidth/2-(width*caption.length())/2, tempImg.getHeight()-textHeight/4-textHeight, imgWidth/2+(width*caption.length())/2, tempImg.getHeight());
		temp.drawRect(rect, p);
		p.setColor(Color.WHITE);
		p.setTextAlign(Paint.Align.CENTER);
		p.setTypeface(Typeface.SANS_SERIF);
		p.setStyle(Paint.Style.FILL);
		//temp.drawTextOnPath(caption, path, 1, 1, p);
		temp.drawText(caption, 0, caption.length(), imgWidth/2, tempImg.getHeight()-textHeight/4, p);
		tempStorage.savePhoto(tempImg);
		finish();
	}
}
