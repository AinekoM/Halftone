package com.example.oldnewspaperfrontpage;

import android.graphics.Bitmap;

public class HalftoneFactory {
	public static class Option
	{
		public enum HalftoneStyle
		{
			CIRCLE,
			DIAMOND,
			RECTANGLE
		}
		
		
		//private int angle;
		//private int gridSize;
		private HalftoneStyle style;
		
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
		
		public HalftoneStyle getStyle()
		{
			return style;
		}
		
		public void setStyle(HalftoneStyle paraStyle)
		{
			style = paraStyle;
		}
	}
	
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
