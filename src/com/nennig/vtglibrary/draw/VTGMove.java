/**
 * 
 */
package com.nennig.vtglibrary.draw;

import com.nennig.vtglibrary.managers.SingletonMovePinMap.MovePins;
import com.nennig.vtglibrary.managers.SingletonMovePinMap.Pin;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;

/**
 * @author Kevin Nennig (knennig213@gmail.com)
 *
 */
public class VTGMove extends View {
	private MovePins curMove;
	
	/**
	 * @param context
	 */
	public VTGMove(Context context, MovePins pm) {
		super(context);
		init(pm);
	}
	
	public VTGMove(Context context, AttributeSet attrs) {
        super(context, attrs);
	}
	
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Try for a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();

        int w = Math.max(minw, MeasureSpec.getSize(widthMeasureSpec));

        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        int minh = (w - (int) mTextWidth) + getPaddingBottom() + getPaddingTop();
        int h = Math.min(MeasureSpec.getSize(heightMeasureSpec), minh);

        setMeasuredDimension(w, h);
    }

	/* (non-Javadoc)
	 * @see android.view.ViewGroup#onLayout(boolean, int, int, int, int)
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param pm
	 */
	private void init(MovePins pm) {
        // Force the background to software rendering because otherwise the Blur
        // filter won't work.
        setLayerToSW(this);
        
		curMove = pm;
	}
	
	private void setLayerToHW(View v) {
        if (!v.isInEditMode() && Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
    }
	
	@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        
        
        //This checks to see if one of two pins should be drawn
        if(curMove.pin0 == null)
        	drawOnePin(canvas, curMove.pin7);
        else 
        	drawTwoPins(canvas, curMove.pin7, curMove.pin7);
        
        if (curMove.pin2 == null)
        	drawOnePin(canvas, curMove.pin1);
        else 
        	drawTwoPins(canvas, curMove.pin1, curMove.pin2);
        
        if(curMove.pin4 == null)
        	drawOnePin(canvas, curMove.pin3);
        else 
        	drawTwoPins(canvas, curMove.pin3, curMove.pin4);
        
        if(curMove.pin6 == null)
        	drawOnePin(canvas, curMove.pin5);
        else
        	drawTwoPins(canvas, curMove.pin5, curMove.pin6);
        
	}

	/**
	 * @param fPIn
	 * @param pin7
	 */
	private void drawTwoPins(Canvas canvas, Pin fPin, Pin sPin) {
		canvas.drawCircle(cx, cy, radius, paint);
	}

	/**
	 * @param pin1
	 */
	private void drawOnePin(Canvas canvas, Pin pin) {
		// TODO Auto-generated method stub
		
	}

}
