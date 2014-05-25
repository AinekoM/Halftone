package com.example.oldnewspaperfrontpage;

import android.graphics.Bitmap;

public abstract class ImageProcessing 
{
	protected Bitmap img;
	
	public ImageProcessing(Bitmap tempImg, String imagePath)
	{
		img = tempImg;
	}
	
	abstract public Bitmap convert();

}
