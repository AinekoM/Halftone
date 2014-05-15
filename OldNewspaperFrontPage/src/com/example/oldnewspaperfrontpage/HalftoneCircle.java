package com.example.oldnewspaperfrontpage;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class HalftoneCircle extends Halftone{
	
	public HalftoneCircle(Bitmap tempImg, String imagePath) {
		super(tempImg, imagePath);
		// TODO Auto-generated constructor stub
	}

	protected void paintGrid(Canvas htgrp, int gridX, int gridY, int limitX, int limitY)
	{
		Paint paint =new Paint();
		float average = 0, count = 0, startX = gridX * grid, startY = gridY * grid;
		//Taking the average of the R, B and G channel of each pixel in the 
		//grid cell to get the grayscale value.
		for (int x = (int) startX; x < limitX; x++)
		{
			for (int y = (int) startY; y < limitY; y++)
			{
				Color currColor = new Color();
				int p = img.getPixel(x, y);
				double currAverage = ((double)Color.blue(p) + (double)Color.red(p) + (double)Color.green(p)) / 3;
				//Pre revision 1.1, the summing up is not necessary but the 
				//value will be assigned back to each channel of the pixel.
				average += currAverage; 
				count++;
			}
		}
		//Taking the average of grayscale values of all pixels in the cell. 
		average = average / count;
		//If the cell is to be absolute white.
		if (average == 0 || grid == 1 && average <= 127)
		{
			paint.setColor(Color.BLACK);
			htgrp.drawRect(startX, startY, startX+grid, startY+grid, paint);
		}
		//If the cell is to be absolute black.
		else if (average == 255 || grid == 1 && average > 127)
		{
			paint.setColor(Color.WHITE);
			htgrp.drawRect(startX, startY, startX+grid, startY+grid, paint);
		}
		else
		{
			//Take the percentage of the blackness of the cell and scale the 
			//area of the circle, thus calculating the width of the circle. 
			double percentage = 1 - (double)average / 255, circleArea, circleWidth;
			circleArea = percentage * (grid * grid);
			circleWidth = Math.sqrt(circleArea / (Math.PI)) * 2;
			//Find the top left coordinate to draw the circle.
			float anchorX = (float) (startX + (grid - circleWidth) / 2);
			float anchorY = (float) (startY + (grid - circleWidth) / 2);
			//Paint the background of the grid cell.
			paint.setColor(Color.WHITE);
			RectF rect = new RectF(startX, startY, startX+grid, startY+grid);
			htgrp.drawRect(rect, paint);
			//paint the circle.
			rect = new RectF(anchorX, anchorY, (float) (anchorX+circleWidth-1), (float) (anchorY+circleWidth-1));
			paint.setColor(Color.BLACK);
			htgrp.drawOval(rect, paint);
		}
	}
}