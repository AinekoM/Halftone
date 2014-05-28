package com.example.oldnewspaperfrontpage;

import android.graphics.Bitmap;

import com.example.oldnewspaperfrontpage.HalftoneFactory.Option;

public class ImageProcessingFactory 
{
	public enum Effect 
	{
		HALFTONE,
		NEGATIVE,
		GAUSSIAN
	};
	
	public static class Option implements FactoryOption
	{
		Effect effect;
		FactoryOption additionalOption;
		
		public Option()
		{
			effect = Effect.HALFTONE;
			additionalOption = null;
		}
		
		public Effect getEffect()
		{
			return effect;
		}
		
		public void setEffect(Effect fx)
		{
			effect = fx;
		}
		
		public FactoryOption getAdditionalOption()
		{
			return additionalOption;
		}
		
		public void setAdditionalOption(FactoryOption option)
		{
			additionalOption = option;
		}
	}
	
	public static ImageProcessing createImageProcessing(Bitmap tempImg, String imagePath, Option option)
	{
		ImageProcessing imgP = null;
		switch(option.getEffect())
		{
		case HALFTONE:
			HalftoneFactory.Option htOption = (HalftoneFactory.Option)option.getAdditionalOption();
			imgP = HalftoneFactory.createHalftone(tempImg, imagePath, htOption);
			break;
		case NEGATIVE:
			imgP = new Negative(tempImg, imagePath);
			break;
		case GAUSSIAN:
			imgP = new GaussianBlur(tempImg, imagePath);
			break;
		}
		return imgP;
	}

}
