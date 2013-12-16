/**
 * 
 */
package com.nennig.vtglibrary.custobjs;

import com.nennig.constants.Dlog;

/**
 * @author Kevin Nennig (knennig213@gmail.com)
 *
 *Tog In, Tog out, Split In, Split out, Tog Split, Split Tog
 */
public class VTGMoveAxis {
	private static final String TAG = "VTGMoveAxis";
	private static final boolean ENABLE_DEBUG = true;
	
	private Orientation _handOrientation, _propOrientation;
	private axis _axis;
	
	public static enum axis{
		X,Y;
	}
	
	public static enum Orientation{
		TOG, SPLIT, IN, OUT
	}
	
	public VTGMoveAxis(axis z, String axisID){
		boolean validString = true;
		Orientation firstID = null, secondID = null;
		
		String[] split = axisID.split(" ");
		if(split.length == 2)
		{
			if(split[0].toLowerCase().contains("tog"))
				firstID = Orientation.TOG;
			if(split[1].toLowerCase().contains("tog"))
				secondID = Orientation.TOG;
			if(split[0].toLowerCase().contains("split"))
				firstID = Orientation.SPLIT;
			if(split[1].toLowerCase().contains("split"))
				secondID = Orientation.SPLIT;
			if(split[0].toLowerCase().contains("in"))
				firstID = Orientation.IN;
			if(split[1].toLowerCase().contains("in"))
				secondID = Orientation.IN;
			if(split[0].toLowerCase().contains("out"))
				firstID = Orientation.OUT;
			if(split[1].toLowerCase().contains("out"))
				secondID = Orientation.OUT;
		}
		else
			validString = false;
		
		if((firstID != null) && (secondID != null)){
			if(!(firstID.equals(Orientation.IN)) && !(firstID.equals(Orientation.OUT)) && !(firstID.equals(secondID))){
				_handOrientation = firstID;
				_propOrientation = secondID;
			}
			else
				validString = false;
		}
		else 
			validString = false;
		
		if(z != null)
			set_axis(z);
		
		if(!validString)
		{
			Dlog.d(TAG, "Cannot create VTGMoveAxis, incorrect parameters: " + firstID + " " + secondID
					+ " String: '" + axisID +"'", ENABLE_DEBUG);
//			throw Exception;
		}
	}

	/**
	 * @return the _propOrientation
	 */
	public Orientation getPropOrientation() {
		return _propOrientation;
	}

	/**
	 * @param _propOrientation the _propOrientation to set
	 */
	public void setPropOrientation(Orientation _secondID) {
		this._propOrientation = _secondID;
	}

	/**
	 * @return the _handOrientation
	 */
	public Orientation getHandOrientation() {
		return _handOrientation;
	}

	/**
	 * @param _handOrientation the _handOrientation to set
	 */
	public void setHandOrientation(Orientation _firstID) {
		this._handOrientation = _firstID;
	}

	/**
	 * @return the _axis
	 */
	public axis getAxis() {
		return _axis;
	}

	/**
	 * @param _axis the _axis to set
	 */
	public void set_axis(axis _axis) {
		this._axis = _axis;
	}
	
	public String toString(){
		if(_handOrientation != null)
			return "axis: " + _axis.toString() + " " + _handOrientation.toString() + " " + _propOrientation;
		else
			return "Axis was not created correctly....";
	}
	
	
}
