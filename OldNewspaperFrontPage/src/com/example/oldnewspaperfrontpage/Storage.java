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
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Storage 
{
	private String path;
	
	Storage()
	{
		path = "";
	}
	
	Storage(String paraPath)
	{
		path = paraPath;
	}
	
	public boolean setPath(String paraPath)
	{
		if (!hasPath())
		{
			path = paraPath;
			return true;
		}
		return false;
	}
	
	public boolean hasPath()
	{
		return (path.compareTo("") != 0);
	}
	
	public String getPath()
	{
		return path;
	}
	
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
					bmOptions.inPurgeable = true;
					bmOptions.inMutable = true;
					photo = BitmapFactory.decodeStream(inStream, null, bmOptions);
				}
				catch(FileNotFoundException ex)
				{

				}
			}
		}
		return photo;
	}

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
