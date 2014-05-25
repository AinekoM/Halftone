package com.example.oldnewspaperfrontpage;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

/*******************************************************************************
 * Class inherited halftone that perform halftoning on a Bitmap image with diamond
 * shapes
 * 
 * @author Milena Mitic
 * @since		May 2014
 * @knownBugs	none
 *
 ******************************************************************************/
public class HalftoneDiamond extends Halftone{
	
	/****************************************************************************
	 * Constructor for the HalftoneDiamond class. 
	 * 
	 * @param tempImg	The bitmap image to be halftoned
	 * @param imagePath	The path of the output image.
	 ***************************************************************************/
	public HalftoneDiamond(Bitmap tempImg, String imagePath) {
		super(tempImg, imagePath);
		// TODO Auto-generated constructor stub
	}
	
	/*********************************************************************
	 * Paint a grid cell of the image with the halftone version with diamond
	 * shape.
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
		//If the cell is to be absolute white.
		*/
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
			double percentage = 1 - (double)average / 255, diamondArea, p, q;
			float x1, x2, x3, x4, y1, y2, y3, y4;
			diamondArea = percentage * (grid * grid);
			//area of a rhomboid shape is A=p*q/2; q is longer diagonal; in this case p=0.6q, to get approximate diamond shape
			q = Math.sqrt(2*diamondArea/0.6);
			p = 0.6*q;
			//Find coordinates of four points of the diamond shape.
			x1 = startX + grid/2;
			y1 = (float) (startY + (grid-q)/2);
			x2 = (float) (startX + p + (grid-p)/2);
			y2 = startY + grid/2;
			x3 = startX + grid/2;
			y3 = (float) (startY + q + (grid-q)/2);
			x4 = (float) (startX + (grid-p)/2);
			y4 = startY + grid/2;
			//Paint the background of the grid cell.
			paint.setColor(Color.WHITE);
			RectF rect = new RectF(startX, startY, startX+grid, startY+grid);
			htgrp.drawRect(rect, paint);
			//paint the diamond.
			Path diamond = new Path();
			diamond.moveTo(x1, y1);
			diamond.lineTo(x2, y2);
			diamond.lineTo(x3, y3);
			diamond.lineTo(x4, y4);
			//rect = new RectF(anchorX, anchorY, (float) (anchorX+circleWidth-1), (float) (anchorY+circleWidth-1));
			paint.setColor(Color.BLACK);
			htgrp.drawPath(diamond, paint);
		}
	}
}
