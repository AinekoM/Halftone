package com.example.oldnewspaperfrontpage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;

/******************************************************************************
 * A class that handles the saving and loading of bitmap image.
 * 
 * @author	Nguyen Doan Bao An
 * @since	May 2014
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Storage 
{
	public static int STD_MAX_WIDTH = 1920;
	public static int STD_MAX_HEIGHT = 1200;
	private String path;
	private int maxPixCount;
	
	/**************************************************************************
	 * Parameterless constructor for storage, path is set to empty string.
	 * 
	 * @param	None
	 */
	Storage()
	{
		path = "";
		maxPixCount = STD_MAX_WIDTH * STD_MAX_HEIGHT;
	}
	
	/*************************************************************************
	 * Constructor for storage.
	 * 
	 * @param paraPath	The path for the imput/output Bitmap object.
	 */
	Storage(String paraPath)
	{
		path = paraPath;
		maxPixCount = STD_MAX_WIDTH * STD_MAX_HEIGHT;
	}
	
	/**************************************************************************
	 * Set the path for storage.
	 * 
	 * @param paraPath	The path for the imput/output Bitmap object.
	 * @return			true if there is no path for this storage object and thus 
	 * 					a path can be set and false otherwise
	 */
	public boolean setPath(String paraPath)
	{
		if (!hasPath())
		{
			path = paraPath;
			return true;
		}
		return false;
	}
	
	/**************************************************************************
	 * Check if the path for storage exist.
	 * 
	 * @param	None
	 * @return	true if there is no path for this storage object and thus 
	 * 			a path can be set and false otherwise
	 */
	public boolean hasPath()
	{
		return (path.compareTo("") != 0);
	}
	
	/*************************************************************************
	 * Retrieve the path for this storage object.
	 * 
	 * @param	None
	 * @return	A String Object representing the path.
	 */
	public String getPath()
	{
		return path;
	}
	
	/**************************************************************************
	 * Create a mutable Bitmap object with config RGB_565 and no alpha channel
	 * from the image at path.
	 * 
	 * @param	None
	 * @return	The Bitmap object if the path is valid and null otherwise
	 */
	public Bitmap loadPhoto()
	{
		Bitmap photo = null;
		if (hasPath())
		{
			File tempImage = new File(path);
			FileInputStream inStream = null;
			if (tempImage.isFile())
			{
				try
				{
					//try to read the photo into the bitmap object
					inStream = new FileInputStream(tempImage);
					BitmapFactory.Options bmOptions = new BitmapFactory.Options();
					bmOptions.inJustDecodeBounds = true;
					bmOptions.inPurgeable = true;
					photo = BitmapFactory.decodeStream(inStream, null, bmOptions);
					bmOptions.inJustDecodeBounds = false;
					int pixCount = bmOptions.outHeight * bmOptions.outWidth;
					if (pixCount > maxPixCount)
					{
						int scale = 1;
						while (pixCount / (scale * scale) > maxPixCount)
						{
							scale++;
						}
						bmOptions.inSampleSize = scale;
					}
					inStream = new FileInputStream(tempImage);
					bmOptions.inPurgeable = true;
					bmOptions.inMutable = true;
					bmOptions.inDither = true;
					bmOptions.inPreferredConfig = Config.RGB_565;
					photo = BitmapFactory.decodeStream(inStream, null, bmOptions);
				}
				catch(FileNotFoundException ex)
				{

				}
			}
		}
		return photo;
	}

	/*************************************************************************
	 * Save a bitmap object to a jpeg image at the given path.
	 * 
	 * @param photo	The Bitmap to be saved
	 * @return		true if the path is valid and thus save succeed and false
	 * 				otherwise
	 */
	public boolean savePhoto(Bitmap photo)
	{
		OutputStream outStream = null;
		if (photo != null && hasPath())
		{
			try
			{
				outStream = new BufferedOutputStream(new FileOutputStream(path));
				photo.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
				return true;
			}
			catch (IOException e)
			{

			}
		}
		return false;
	}
}
