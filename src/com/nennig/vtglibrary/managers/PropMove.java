/**
 * 
 */
package com.nennig.vtglibrary.managers;

import com.nennig.constants.AppConstants;

/**
 * @author Kevin Nennig (knennig213@gmail.com)
 *
 */
public class PropMove{
	public String moveID;
	public String m13_name;
	public String m13_imageFileName;
	public String m13_type;
	public String m13_drawX;
	public String m13_drawY;
	public String m13_pdf;

	
	public String m11_name;
	public String m11_imageFileName;
	public String m11_type;
	public String m11_drawX;
	public String m11_drawY;
	
	public String getName(String curSet){
		if(curSet.equals(AppConstants.SET_1313)){
			return m13_name;
		}
		else if(curSet.equals(AppConstants.SET_1111))
		{
			return m11_name;
		}
		return m13_name;
	}
	public String getImageFileName(String curSet){
		if(curSet.equals(AppConstants.SET_1313)){
			return m13_imageFileName;
		}
		else if(curSet.equals(AppConstants.SET_1111))
		{
			return m11_imageFileName;
		}
		return m13_imageFileName;
	}
	public String getType(String curSet){
		if(curSet.equals(AppConstants.SET_1313)){
			return m13_type;
		}
		else if(curSet.equals(AppConstants.SET_1111))
		{
			return m11_type;
		}
		return m13_type;
	}
	public String getDrawX(String curSet){
		if(curSet.equals(AppConstants.SET_1313)){
			return m13_drawX;
		}
		else if(curSet.equals(AppConstants.SET_1111))
		{
			return m11_drawX;
		}
		return m13_drawX;
	}
	public String getDrawY(String curSet){
		if(curSet.equals(AppConstants.SET_1313)){
			return m13_drawY;
		}
		else if(curSet.equals(AppConstants.SET_1111))
		{
			return m11_drawY;
		}
		return m13_drawY;
	}
	
	/**
	 * Huge adder that assigns the field value to the correct value.
	 * @param name
	 * @param fieldValue
	 */
	public void add(String name, String fieldValue) {
		if(name.toLowerCase().equals("moveid".toLowerCase()))
			moveID = fieldValue;
		else if(name.toLowerCase().equals("m13_name".toLowerCase()))
			m13_name = fieldValue;
		else if(name.toLowerCase().equals("m13_imageFileName".toLowerCase()))
			m13_imageFileName = fieldValue;
		else if(name.toLowerCase().equals("m13_type".toLowerCase()))
			m13_type = fieldValue;
		else if(name.toLowerCase().equals("m13_drawX".toLowerCase()))
			m13_drawX = fieldValue;
		else if(name.toLowerCase().equals("m13_drawY".toLowerCase()))
			m13_drawY = fieldValue;
		else if(name.toLowerCase().equals("m13_pdf".toLowerCase()))
			m13_pdf = fieldValue;
		
		
		else if(name.toLowerCase().equals("m11_name".toLowerCase()))
			m11_name = fieldValue;
		else if(name.toLowerCase().equals("m11_imageFileName".toLowerCase()))
			m11_imageFileName = fieldValue;
		else if(name.toLowerCase().equals("m11_type".toLowerCase()))
			m11_type = fieldValue;
		else if(name.toLowerCase().equals("m11_drawX".toLowerCase()))
			m11_drawX = fieldValue;
		else if(name.toLowerCase().equals("m11_drawY".toLowerCase()))
			m11_drawY = fieldValue;	
	}
	
	public void setName(String str){
		m13_name = str;
	}
}