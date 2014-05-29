package com.example.oldnewspaperfrontpage.test.apptesting;
import android.graphics.Bitmap;

import com.example.oldnewspaperfrontpage.Halftone;
import com.example.oldnewspaperfrontpage.HalftoneCircle;
import com.example.oldnewspaperfrontpage.HalftoneDiamond;
import com.example.oldnewspaperfrontpage.HalftoneFactory;
import com.example.oldnewspaperfrontpage.HalftoneFactory.Option.HalftoneStyle;
import com.example.oldnewspaperfrontpage.HalftoneRect;

import junit.framework.TestCase;

public class HaltoneFactoryTest extends TestCase {
	Halftone temp;
	Bitmap tempImg = null;
	String tempPath = "";
	HalftoneFactory.Option option;
	
	HalftoneDiamond tempD = new HalftoneDiamond(tempImg, tempPath);
	HalftoneCircle tempC = new HalftoneCircle(tempImg, tempPath);
	HalftoneRect tempR = new HalftoneRect(tempImg, tempPath);
	
	public void testCreateHalftone() {
		option = new HalftoneFactory.Option();
		option.setStyle(HalftoneStyle.DIAMOND);
		temp = HalftoneFactory.createHalftone(tempImg, tempPath, option);
		assertEquals("Diamond option should return HalftoneDiamond object", tempD.getClass().equals(temp.getClass()), true);
		
		option.setStyle(HalftoneStyle.CIRCLE);
		temp = HalftoneFactory.createHalftone(tempImg, tempPath, option);
		assertEquals("Circle option should return HalftoneCircle object", tempC.getClass().equals(temp.getClass()), true);
		
		option.setStyle(HalftoneStyle.RECTANGLE);
		temp = HalftoneFactory.createHalftone(tempImg, tempPath, option);
		assertEquals("Rect option should return HalftoneRect object", tempR.getClass().equals(temp.getClass()), true);
	}

}
