/**
 * 
 */
package com.nennig.vtglibrary.custobjs;

/**
 * @author Kevin Nennig (knennig213@gmail.com)
 * Tog In, Tog out, Split In, Split out, Tog Split, Split Tog
 */
public class Pin{
	private pinDirection direction;
	private pinColor color;
	
	public enum pinColor{
		PRIMARY, SECONDARY
	}
	
	public enum pinDirection{
		IN, OUT
	}
	
	public Pin(String dir, String col){
		if(dir.toLowerCase().equals("in"))
			setDirection(pinDirection.IN);
		else if(dir.toLowerCase().equals("out"))
			setDirection(pinDirection.OUT);
		
		if(col.equals("B"))
			setColor(pinColor.PRIMARY);
		else if(col.equals("W"))
			setColor(pinColor.SECONDARY);
	
	}

//	public Pin(){
//		direction = "";
//		color = "";
//	}
	
	/**
	 * @return the direction
	 */
	public pinDirection getDirection() {
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(pinDirection direction) {
		this.direction = direction;
	}

	/**
	 * @return the color
	 */
	public pinColor getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(pinColor color) {
		this.color = color;
	}
	 
	@Override
	public String toString(){
		return direction + "." + color;
	}
}
