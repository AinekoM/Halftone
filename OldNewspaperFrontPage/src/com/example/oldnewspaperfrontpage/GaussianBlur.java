package com.example.oldnewspaperfrontpage;

import android.graphics.Bitmap;
import android.graphics.Color;

public class GaussianBlur extends ImageProcessing 
{
	static final int DEFAULT_RADIUS = 1;
	static final double DEFAULT_SIGMA = 10.0;
	int radius, size;
	double sigma;
	double[][] kernel;
	
	public GaussianBlur(Bitmap tempImg, String imagePath) 
	{
		super(tempImg, imagePath);
		radius = DEFAULT_RADIUS; 
		size = radius * 2 + 1;
		sigma = DEFAULT_SIGMA;
		kernelInit();
	}
	
	private void kernelInit()
	{
		double[] line = new double[size];
		kernel = new double[size][size];
		
		double invTwoStdevSqr = 1.0 / (2.0 * sigma * sigma);
		double invStdevSqrtTwoPi = 1.0 / (sigma * Math.sqrt(2.0 * Math.PI));
		
		int dev = -radius;
		double sum = 0.0;
		
		for (int i = 0; i < line.length; i++)
		{
			double var = dev * dev;
			line[i] = invStdevSqrtTwoPi * Math.exp(-var * invTwoStdevSqr);
			sum += line[i];
			dev++;
		}
		
		for (int i = 0; i < line.length; i++)
		{
			line[i] = line[i] / sum;
		}
		
		for (int j = 0; j < size; j++)
		{
			for(int i = 0; i < size; i++)
			{
				kernel[i][j] = line[j] * line[i];
			}
		}
	}
	
	private int wrapAroundX(int x)
	{
		if (x < 0)
		{
			return img.getWidth() + x;
		}
		if (x >= img.getWidth())
		{
			return x - img.getWidth();
		}
		return x;
	}
	
	private int wrapAroundY(int y)
	{
		if (y < 0)
		{
			return img.getHeight() + y;
		}
		if (y >= img.getHeight())
		{
			return y - img.getHeight();
		}
		return y;
	}
	
	private int blurAt(int x, int y)
	{
		int rgb;
		int i = -radius, j = -radius;
		double sumR = 0.0, sumG = 0.0, sumB = 0.0;
		while (i <= radius)
		{
			j = -radius;
			while (j <= radius)
			{
				int currX = x + i, currY = y + j;
				int mid = size / 2;
				currX = wrapAroundX(currX);
				currY = wrapAroundY(currY);
				int p = img.getPixel(currX, currY);
				sumR = sumR + Color.red(p) * kernel[i + mid][j + mid];
				sumG = sumG + Color.green(p) * kernel[i + mid][j + mid];
				sumB = sumB + Color.blue(p) * kernel[i + mid][j + mid];
				j++;
			}
			i++;
		}
		return Color.rgb((int)sumR, (int)sumG, (int)sumB);
	}

	@Override
	public Bitmap convert() 
	{
		for (int x = 0; x < img.getWidth(); x++)
		{
			for (int y = 0; y < img.getHeight(); y++)
			{
				img.setPixel(x, y, blurAt(x, y));
			}
		}
		return img;
	}

}
