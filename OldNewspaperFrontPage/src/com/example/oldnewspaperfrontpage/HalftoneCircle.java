package com.example.oldnewspaperfrontpage;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/*******************************************************************************
 * Class inherited halftone that perform halftoning on a given Bitmap image using
 * circular shapes.
 * 
 * @author Milena Mitic
 * @since		May 2014
 * @knownBugs	none
 *
 ******************************************************************************/
public class HalftoneCircle extends Halftone{
	
	/**************************************************************************
	 * Constructor of the HalftoneCircle object.
	 * 
	 * @param tempImg	The image to be halftoned
	 * @param imagePath	The path to output file
	 **************************************************************************/
	public HalftoneCircle(Bitmap tempImg, String imagePath) {
		super(tempImg, imagePath);
		// TODO Auto-generated constructor stub
	}

	/*********************************************************************
	 * Paint a grid cell of the image with the halftone version.
	 * Any inheritted class will implement this method with the appropriate
	 * shape used for halftoning.
	 * 
	 * @param htgrp		The Canvas object of the resulting image.
	 * @param gridX		The row of the current grid cell in the grid.
	 * @param gridY		The column of the current grid cell in the grid.
	 * @param gridSize	The size of the grid.
	 * @param limitX	The pixel x coordinate limit of the grid cell.
	 * @param limitY	The pixel y coordinate limit of the grid cell.
	 * 
	 * @precondition	The limitX and limitY is calculated by the next
	 * 						gridX and gridY if the grid cell is part of
	 * 						the grid or the image's width and/or height
	 * 						if the cell belong to the excess rim.
	 * 
	 * @postcondition	The Canvas object of the resulting image will
	 * 						be drawn on accordingly.		
	 * 
	 * @return			N/A
	 * 
	 * @since			1.0
	 *********************************************************************/
	protected void paintGrid(Canvas htgrp, float average, int startX, int startY)
	{
		Paint paint =new Paint();
		/*
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
		*/
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
