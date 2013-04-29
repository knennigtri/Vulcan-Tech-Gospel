/**
 * 
 */
package com.nennig.vtglibrary.custobjs;


/**
 * @author Kevin Nennig (knennig213@gmail.com)
 *
 */

public class MovePins{
	public String matrixID="";
	public Pin pin0;
	public Pin pin1;
	public Pin pin2;
	public Pin pin3;
	public Pin pin4;
	public Pin pin5;
	public Pin pin6;
	public Pin pin7;
	
	@Override
	public String toString(){
		String str = "<"+matrixID+">:";
		if(pin0 != null)
			str = str + " 0=" + pin0.toString();
		if(pin1 != null)
			str = str + " 1=" + pin1.toString();
		if(pin2 != null)
			str = str + " 2=" + pin2.toString();
		if(pin3 != null)
			str = str + " 3=" + pin3.toString();
		if(pin4 != null)
			str = str + " 4=" + pin4.toString();
		if(pin5 != null)
			str = str + " 5=" + pin5.toString();
		if(pin6 != null)
			str = str + " 6=" + pin6.toString();
		if(pin7 != null)
			str = str + " 7=" + pin7.toString();
		return str;
	}
}