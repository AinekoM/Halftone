package com.example.oldnewspaperfrontpage;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/*******************************************************************************
 * Class inherited from Halftone that perform the halftoning on a provided bitmap 
 * image. Implement halftoning with rectangular shape.
 * 
 * @author Milena Mitic
 * @since		May 2014
 * @knownBugs	none
 *
 ******************************************************************************/
public class HalftoneRect extends Halftone{
	
	/***************************************************************************
	 * Constructor for the HalftoneRect class.
	 * 
	 * @param tempImg	The image to be halftoned
	 * @param imagePath	The path to create the output file
	 ***************************************************************************/
	public HalftoneRect(Bitmap tempImg, String imagePath) {
		super(tempImg, imagePath);
		// TODO Auto-generated constructor stub
	}
	
	/*********************************************************************
	 * Paint a grid cell of the image with the halftone version using
	 * rectangular shapes.
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
	 * @postcondition	The Graphic2D object of the resulting image will
	 * 						be drawn on accordingly.		
	 * 
	 * @return			N/A
	 * 
	 * @since			1.0
	 *********************************************************************/
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
			double percentage = 1 - (double)average / 255, rectArea, rectSide, rectSideLong;
			//coordinates of the top left and bottom right corners of the rectangle
			float x1, y1, x2, y2;
			rectArea = percentage * (grid * grid);
			//getting shorter side of a rectangle, using golden ration proportions between short and long sides
			rectSide = Math.sqrt(rectArea / 1.62);
			rectSideLong = rectSide*1.62;
			//Find corners of a rectangular
			x1 = (float) (startX + (grid - rectSideLong) / 2);
			y1 = (float) (startY + (grid - rectSide) / 2);
			x2 = (float) (startX + (grid - rectSideLong) / 2 + rectSideLong);
			y2 = (float) (startY + (grid - rectSide) / 2 + rectSide);
			//Paint the background of the grid cell.
			paint.setColor(Color.WHITE);
			RectF rect = new RectF(startX, startY, startX+grid, startY+grid);
			htgrp.drawRect(rect, paint);
			//paint the rectangle.
			rect = new RectF(x1, y1, x2, y2);
			paint.setColor(Color.BLACK);
			htgrp.drawRect(rect, paint);
		}
	}
}
