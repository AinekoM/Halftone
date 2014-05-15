package com.example.oldnewspaperfrontpage;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public abstract class Halftone {
	private final static int RGB_VALUE = 255;
	protected  Bitmap img = null;
	protected	 int grid = 0;
	//private  File outputfile = null;
	
	/****************************************************************************************
     * Constructor of the Halftone class. 
	 * @param imagePath 
     * 
     * @param String[] args Three parameters provided by user. First is the image to be converted,
     * 						second is the output file path and third is the size of the grid.
     * @pre none
     * @post none
     * @return new Halftone object
	 *****************************************************************************************/
	public Halftone(Bitmap tempImg, String imagePath){
		img = tempImg;
		grid = 10;
	}
	
	
	/***************************************************************************************
     * Converts image into halftone version.
     * 
     * @param none
     * @pre none
     * @post none
     * @return none
	 ***************************************************************************************/
	public Bitmap convert() 
	{ 
		Canvas halftone = new Canvas(img);
		boolean widthExceed = false, heightExceed = false;
		int imgWidth = img.getWidth(), imgHeight = img.getHeight(), gridWidth = imgWidth / grid, gridHeight = imgHeight / grid;
		//Check for the grid size.
		if (grid > imgWidth || grid > imgHeight)
		{
			System.out.println("Grid too large...");
		}
		else
		{
			//Check for the excess rim.
			if (imgWidth % grid > 0)
			{
				widthExceed = true;
			}
			if (imgHeight % grid > 0)
			{
				heightExceed = true;
			}
			//Paint the grid.
			for (int gridX = 0; gridX < gridWidth; gridX++)
			{
				for (int gridY = 0; gridY < gridHeight; gridY++)
				{
					int limitX = (gridX + 1) * grid, limitY = (gridY + 1) * grid;
					paintGrid(halftone, gridX, gridY, limitX, limitY);
				}
			}
			//Paint the excess rims if applicable
			if (widthExceed)
			{
				int gridX = gridWidth;
				for (int gridY = 0; gridY < gridHeight; gridY++)
				{
					int limitX = imgWidth, limitY = (gridY + 1) * grid;
					paintGrid(halftone, gridX, gridY, limitX, limitY);
				}
			}
			if (heightExceed)
			{
				int gridY = gridHeight;
				for (int gridX = 0; gridX < gridWidth; gridX++)
				{
					int limitX = (gridX + 1) * grid, limitY = imgHeight;
					paintGrid(halftone, gridX, gridY, limitX, limitY);
				}
			}
			if (widthExceed && heightExceed)
			{
				int gridX = gridWidth, gridY = gridHeight;
				paintGrid(halftone, gridX, gridY, imgWidth, imgHeight);
			}
		}
		return img;
	}
	
	/*********************************************************************
	 * Paint a grid cell of the image with the halftone version.
	 * 
	 * @param img		The original image.
	 * @param htgrp		The Graphic2D object of the resulting image.
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
	abstract protected void paintGrid(Canvas htgrp, int gridX, int gridY, int limitX, int limitY);
}