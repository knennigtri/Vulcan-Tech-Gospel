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
	
	private moveID _firstID, _secondID;
	private axis _axis;
	
	enum axis{
		X,Y;
	}
	
	enum moveID{
		TOG, SPLIT, IN, OUT
	}
	
	public VTGMoveAxis(axis z, String axisID){
		boolean validString = true;
		moveID firstID = null, secondID = null;
		
		String[] split = axisID.split(" ");
		if(split.length == 2)
		{
			if(split[0].toLowerCase() == "tog")
				firstID = moveID.TOG;
			if(split[1].toLowerCase() == "tog")
				secondID = moveID.TOG;
			if(split[0].toLowerCase() == "split")
				firstID = moveID.SPLIT;
			if(split[1].toLowerCase() == "split")
				secondID = moveID.SPLIT;
			if(split[0].toLowerCase() == "in")
				firstID = moveID.IN;
			if(split[1].toLowerCase() == "in")
				secondID = moveID.IN;
			if(split[0].toLowerCase() == "out")
				firstID = moveID.OUT;
			if(split[1].toLowerCase() == "out")
				secondID = moveID.OUT;
		}
		else
			validString = false;
		
		if((firstID != null) && (secondID != null)){
			if(!(firstID.equals(moveID.IN)|| firstID.equals(moveID.OUT) || firstID.equals(secondID))){
				_firstID = firstID;
				_secondID = secondID;
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
					+ " String: " + axisID, ENABLE_DEBUG);
//			throw Exception;
		}
	}

	/**
	 * @return the _secondID
	 */
	public moveID get_secondID() {
		return _secondID;
	}

	/**
	 * @param _secondID the _secondID to set
	 */
	public void set_secondID(moveID _secondID) {
		this._secondID = _secondID;
	}

	/**
	 * @return the _firstID
	 */
	public moveID get_firstID() {
		return _firstID;
	}

	/**
	 * @param _firstID the _firstID to set
	 */
	public void set_firstID(moveID _firstID) {
		this._firstID = _firstID;
	}

	/**
	 * @return the _axis
	 */
	public axis get_axis() {
		return _axis;
	}

	/**
	 * @param _axis the _axis to set
	 */
	public void set_axis(axis _axis) {
		this._axis = _axis;
	}
	
	public String toString(){
		if(_firstID != null)
			return "axis: " + _axis.toString() + " " + _firstID.toString() + " " + _secondID;
		else
			return "Axis was not created correctly....";
	}
	
	
}
