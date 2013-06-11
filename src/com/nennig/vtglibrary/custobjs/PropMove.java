/**
 * 
 */
package com.nennig.vtglibrary.custobjs;

import android.util.Log;

import com.nennig.constants.AppConfig;
import com.nennig.constants.AppConstants;
import com.nennig.constants.AppConstants.*;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @author Kevin Nennig (knennig213@gmail.com)
 *
 */
public class PropMove{
    private static final String TAG = AppConfig.APP_TITLE_SHORT + ".PropMove";

    private MatrixID moveID;
	
	private String m13_name;
    private String m13_imageFileName;
    private String m13_pdf;

    private String m11_name;
    private String m11_imageFileName;

    private String m15_name;
    private String m15_imageFileName;

    private String m13_fileExt;
    private String m11_fileExt;
    private String m15_fileExt;
    private String defaultExt = "png";

    private ArrayList<String> fieldValues = new ArrayList<String>();

    public PropMove(){
        for (Field f : this.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            Object o;
            try {
                o = f.get(this);
            } catch (Exception e) {
                o = e;
            }
            fieldValues.add(f.getName());
        }

        m13_name = "";
        m13_imageFileName = "";
        m13_pdf = "";
        m13_fileExt = "";
        m11_name = "";
        m11_imageFileName = "";
        m11_fileExt = "";
        m15_name = "";
        m15_imageFileName = "";
        m15_fileExt = "";
    }

    public ArrayList<String> getDBFields(){
        return fieldValues;
    }

	public String getName(Set curSet){
		if(curSet.equals(Set.ONEFIVE)){
			return m15_name;
		}
		else if(curSet.equals(Set.ONEONE))
		{
			return m11_name;
		}
		return m13_name;
	}
	public String getImageFileName(Set curSet){
		if(curSet.equals(Set.ONEFIVE)){
			return m15_imageFileName;
		}
		else if(curSet.equals(Set.ONEONE))
		{
			return m11_imageFileName;
		}
		return m13_imageFileName;
	}
    public String get_fileExt(Set curSet){
        if(curSet.equals(Set.ONEFIVE)){
            return m15_fileExt;
        }
        else if(curSet.equals(Set.ONEONE))
        {
            return m11_fileExt;
        }
        return m13_fileExt;
    }

    public MatrixID getMoveID(){
        return moveID;
    }
	
	/**
	 * Huge adder that assigns the field value to the correct value.
	 * @param name
	 * @param fieldValue
	 */
	public void add(String name, String fieldValue) {
		if(name.toLowerCase().equals("moveid".toLowerCase()))
			moveID = new MatrixID(fieldValue);

        //1:3 fields
        else if(name.toLowerCase().equals("m13_name".toLowerCase()))
			m13_name = fieldValue;
		else if(name.toLowerCase().equals("m13_imageFileName".toLowerCase()))
        {
            int i = fieldValue.lastIndexOf('.');
            if (i > 0) {
                m13_imageFileName = fieldValue.substring(0,i);
                m13_fileExt = fieldValue.substring(i+1);
            }
            else
            {
                m13_imageFileName = fieldValue;
                m13_fileExt = defaultExt;
            }
        }
		else if(name.toLowerCase().equals("m13_pdf".toLowerCase()))
			m13_pdf = fieldValue;
		
		//1:1 fields
		else if(name.toLowerCase().equals("m11_name".toLowerCase()))
			m11_name = fieldValue;
		else if(name.toLowerCase().equals("m11_imageFileName".toLowerCase()))
        {
            int i = fieldValue.lastIndexOf('.');
            if (i > 0) {
                m11_imageFileName = fieldValue.substring(0,i);
                m11_fileExt = fieldValue.substring(i+1);
            }
            else
            {
                m11_imageFileName = fieldValue;
                m11_fileExt = defaultExt;
            }
        }

        //1:5 fields
        else if(name.toLowerCase().equals("m15_name".toLowerCase()))
            m15_name = fieldValue;
        else if(name.toLowerCase().equals("m15_imageFileName".toLowerCase()))
        {
            int i = fieldValue.lastIndexOf('.');
            if (i > 0) {
                m15_imageFileName = fieldValue.substring(0,i);
                m15_fileExt = fieldValue.substring(i+1);
            }
            else
            {
                m15_imageFileName = fieldValue;
                m15_fileExt = defaultExt;
            }
        }
	}

	@Override
	public String toString(){
		String str = "";
		str = "{";
		str = str + moveID + ": \n";
		str = str + "13Name = " + m13_name + "\n";
		str = str + "13Image = " + m13_imageFileName + "\n";
        str = str + "13FileExt = " + m13_fileExt + "\n";
		str = str + "11Name = " + m11_name + "\n";		
		str = str + "11Image = " + m11_imageFileName + "\n";
        str = str + "15FileExt = " + m11_fileExt + "\n";
        str = str + "15Name = " + m15_name + "\n";
        str = str + "15Image = " + m15_imageFileName + "\n";
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
        if(other.moveID.equals(this.moveID))
            return true;
        return false;
    }
}