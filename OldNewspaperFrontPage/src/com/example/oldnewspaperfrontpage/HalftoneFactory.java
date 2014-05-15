package com.example.oldnewspaperfrontpage;

import android.graphics.Bitmap;

/*******************************************************************************
 * A class that allow the creation of the appropriate halftone object according
 * to the option specified by the user.
 * 
 * @author 	Nguyen Doan Bao An
 * @since	May 2014
 */
public class HalftoneFactory {
	
	/***************************************************************************
	 * A class that allow the user to specify the options for their halftone object
	 * creation. Option include style used.
	 * 
	 * @author 	Nguyen Doan Bao An
	 * @since	May 2014
	 */
	public static class Option
	{
		//The styles currently implemented
		public enum HalftoneStyle
		{
			CIRCLE,
			DIAMOND,
			RECTANGLE
		}
		
		
		//private int angle;
		//private int gridSize;
		private HalftoneStyle style;
		
		/***********************************************************************
		 * Constructor for the Option object. Style is default to circle.
		 * 
		 * @param:	None
		 */
		public Option()
		{
			//angle = 0;
			//gridSize = 10;
			style = HalftoneStyle.CIRCLE;
		}
		
		/*
		public int getAngle()
		{
			return angle;
		}
		
		public void setAngel(int paraAngle)
		{
			angle = paraAngle;
		}
		
		public int getGridSize()
		{
			return gridSize;
		}
		
		public void setGridSize(int paraGridSize)
		{
			gridSize = paraGridSize;
		}
		*/
		
		/**********************************************************************
		 * Retrieve the style currently used.
		 * 
		 * @param	None
		 * @return	A HalftoneStyle value as specified by the HalftoneStyle enum
		 **********************************************************************/
		public HalftoneStyle getStyle()
		{
			return style;
		}
		
		/***********************************************************************
		 * Set the style to be used.
		 * 
		 * @param paraStyle	The style to be used for the halftone object as specified
		 * 					by the HalftoneStyle enum.
		 */
		public void setStyle(HalftoneStyle paraStyle)
		{
			style = paraStyle;
		}
	}
	
	/**************************************************************************
	 * Produce the appropriate halftone object with the options given.
	 * 
	 * @param tempImg	The Bitmap image to be halftoned
	 * @param imagePath	The path of the output image.
	 * @param option	The Option object carrying the options specified for this
	 * 					creation.
	 * @return			The Halftone object appropriate for the options specified. 
	 */
	public static Halftone createHalftone(Bitmap tempImg, String imagePath, Option option)
	{
		Halftone halftone = null;
		
		switch (option.getStyle())
		{
		case CIRCLE: 
			halftone = new HalftoneCircle(tempImg, imagePath);
			break;
		case RECTANGLE:
			halftone = new HalftoneRect(tempImg, imagePath);
			break;
		case DIAMOND:
			halftone = new HalftoneDiamond(tempImg, imagePath);
			break;
		}
		
		return halftone;
	}
}
