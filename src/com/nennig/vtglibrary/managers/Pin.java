/**
 * 
 */
package com.nennig.vtglibrary.managers;

/**
 * @author Kevin Nennig (knennig213@gmail.com)
 *
 */
public class Pin{
	private pinDirection direction;
	private pinColor color;
	
	public enum pinColor{
		PRIMARY, SECONDARY
	}
	
	public enum pinDirection{
		INSIDE, OUTSIDE
	}
	
	public Pin(String dir, String col){
		if(dir.equals("I"))
			setDirection(pinDirection.INSIDE);
		else if(dir.equals("O"))
			setDirection(pinDirection.OUTSIDE);
		
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
