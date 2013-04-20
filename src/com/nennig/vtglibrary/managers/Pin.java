/**
 * 
 */
package com.nennig.vtglibrary.managers;

/**
 * @author Kevin Nennig (knennig213@gmail.com)
 *
 */
public class Pin{
	private String direction;
	private String color;
	
	public Pin(String dir, String col){
		setDirection(dir);
		setColor(col);
	}

	public Pin(){
		direction = "";
		color = "";
	}
	
	/**
	 * @return the direction
	 */
	public String getDirection() {
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(String direction) {
		this.direction = direction;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}
	 
	@Override
	public String toString(){
		return direction + "." + color;
	}
}
