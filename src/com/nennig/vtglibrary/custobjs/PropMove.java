/**
 * 
 */
package com.nennig.vtglibrary.custobjs;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.nennig.constants.AppConfig;
import com.nennig.constants.AppConstants.Set;

/**
 * @author Kevin Nennig (knennig213@gmail.com) This file is used to create the
 *         PropMove. This class is constructed and then built through the add
 *         method. The add method takes in the title of the field and the data
 *         for the field and then puts the information into this class if it is
 *         applicable.
 */
public class PropMove {
	private static final String TAG = AppConfig.APP_TITLE_SHORT + ".PropMove";

	// These Fields are directly related to the db file called
	// "detail_matrix_db.csv"
	private static final String field_matrixID = "matrixID";
	private static final String field_x_axis = "mX_axis";
	private static final String field_y_axis = "mY_axis";
	private static final String field_m13_name = "m13_name";
	private static final String field_m13_imageFileName = "m13_imageFileName";
	private static final String field_m11_name = "m11_name";
	private static final String field_m11_imageFileName = "m11_imageFileName";
	private static final String field_m15_name = "m15_name";
	private static final String field_m15_imageFileName = "m15_imageFileName";

	private MatrixID matrixID;

	private VTGMoveAxis mX_axis;
	private VTGMoveAxis mY_axis;

	private String m13_name;
	private String m13_imageFileName;

	private String m11_name;
	private String m11_imageFileName;

	private String m15_name;
	private String m15_imageFileName;

	private String m13_fileExt;
	private String m11_fileExt;
	private String m15_fileExt;
	private String defaultExt = "png";

	private ArrayList<String> fieldValues = new ArrayList<String>();

	public PropMove() {
		for (Field f : this.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			Object o;
			try {
				o = f.get(this);
			} catch (Exception e) {
				o = e;
			}
			if(f.getName().startsWith("m"))
				fieldValues.add(f.getName());
		}

		m13_name = "";
		m13_imageFileName = "";
		m11_name = "";
		m11_imageFileName = "";
		m15_name = "";
		m15_imageFileName = "";
		m13_fileExt = "";
		m11_fileExt = "";
		m15_fileExt = "";
		
	}

	public ArrayList<String> getDBFields() {
		return fieldValues;
	}

	public String getName(Set curSet) {
		if (curSet.equals(Set.ONEFIVE)) {
			return m15_name;
		} else if (curSet.equals(Set.ONEONE)) {
			return m11_name;
		}
		return m13_name;
	}

	public String getImageFileName(Set curSet) {
		if (curSet.equals(Set.ONEFIVE)) {
			return m15_imageFileName;
		} else if (curSet.equals(Set.ONEONE)) {
			return m11_imageFileName;
		}
		return m13_imageFileName;
	}
	
	public String get_fileExt(Set curSet) {
		if (curSet.equals(Set.ONEFIVE)) {
			return m15_fileExt;
		} else if (curSet.equals(Set.ONEONE)) {
			return m11_fileExt;
		}
		return m13_fileExt;
	}

	public MatrixID getMoveID() {
		return matrixID;
	}

	public VTGMoveAxis getXAxis() {
		return mX_axis;
	}

	public VTGMoveAxis getYAxis() {
		return mY_axis;
	}

	/**
	 * Huge adder that assigns the field value to the correct value.
	 * 
	 * @param name
	 * @param fieldValue
	 */
	public void add(String name, String fieldValue) {
		if (name.toLowerCase().equals(field_matrixID.toLowerCase()))
			matrixID = new MatrixID(fieldValue);
		else if (name.toLowerCase().equals(field_x_axis.toLowerCase())) {
			mX_axis = new VTGMoveAxis(VTGMoveAxis.axis.X, fieldValue);
		} else if (name.toLowerCase().equals(field_y_axis.toLowerCase())) {
			mY_axis = new VTGMoveAxis(VTGMoveAxis.axis.Y, fieldValue);
		}
		// 1:3 fields
		else if (name.toLowerCase().equals(field_m13_name.toLowerCase()))
			m13_name = fieldValue;
		else if (name.toLowerCase().equals(
				field_m13_imageFileName.toLowerCase())) {
			int i = fieldValue.lastIndexOf('.');
			if (i > 0) {
				m13_imageFileName = fieldValue.substring(0, i);
				m13_fileExt = fieldValue.substring(i + 1);
			} else {
				m13_imageFileName = fieldValue;
				m13_fileExt = defaultExt;
			}
		}
		// 1:1 fields
		else if (name.toLowerCase().equals(field_m11_name.toLowerCase()))
			m11_name = fieldValue;
		else if (name.toLowerCase().equals(
				field_m11_imageFileName.toLowerCase())) {
			int i = fieldValue.lastIndexOf('.');
			if (i > 0) {
				m11_imageFileName = fieldValue.substring(0, i);
				m11_fileExt = fieldValue.substring(i + 1);
			} else {
				m11_imageFileName = fieldValue;
				m11_fileExt = defaultExt;
			}
		}

		// 1:5 fields
		else if (name.toLowerCase().equals(field_m15_name.toLowerCase()))
			m15_name = fieldValue;
		else if (name.toLowerCase().equals(
				field_m15_imageFileName.toLowerCase())) {
			int i = fieldValue.lastIndexOf('.');
			if (i > 0) {
				m15_imageFileName = fieldValue.substring(0, i);
				m15_fileExt = fieldValue.substring(i + 1);
			} else {
				m15_imageFileName = fieldValue;
				m15_fileExt = defaultExt;
			}
		}
	}

	@Override
	public String toString() {
		String str = "";
		str = "{";
		str = str + matrixID + ": \n";
		str = str + mX_axis.toString() + "\n";
		str = str + mY_axis.toString() + "\n";
		str = str + field_m13_name + " = " + m13_name + "\n";
		str = str + field_m13_imageFileName + " = " + m13_imageFileName + "\n";
		str = str + "13FileExt = " + m13_fileExt + "\n";
		str = str + field_m11_name + " = " + m11_name + "\n";
		str = str + field_m11_imageFileName + " = " + m11_imageFileName + "\n";
		str = str + "11FileExt = " + m11_fileExt + "\n";
		str = str + field_m15_name + " = " + m15_name + "\n";
		str = str + field_m15_imageFileName + " = " + m15_imageFileName + "\n";
		str = str + "15FileExt = " + m15_fileExt + "\n";
		str = str + "}";
		return new String(str);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PropMove))
			return false;
		PropMove other = (PropMove) obj;
		if (other.matrixID.equals(this.matrixID))
			return true;
		return false;
	}
}