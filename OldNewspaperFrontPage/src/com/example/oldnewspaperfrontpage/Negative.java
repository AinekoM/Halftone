package com.example.oldnewspaperfrontpage;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Negative extends ImageProcessing 
{
	public static int RGB_VALUE = 255;
	
	public Negative(Bitmap tempImg, String imagePath) 
	{
		super(tempImg, imagePath);
	}

	@Override
	public Bitmap convert() 
	{
		for (int x = 0; x < img.getWidth(); x++)
		{
			for (int y = 0; y < img.getHeight(); y++)
			{
				int p = img.getPixel(x, y);
				double currAverage = (0.114 * (double)Color.blue(p) + 0.299 * (double)Color.red(p) + 0.587 * (double)Color.green(p));
				int inverse = RGB_VALUE - (int)currAverage;
				img.setPixel(x, y, Color.rgb(inverse, inverse, inverse));
			}
		}
		return img;
	}

}
