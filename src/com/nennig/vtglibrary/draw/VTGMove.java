/**
 * 
 */
package com.nennig.vtglibrary.draw;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.nennig.constants.AppConfig;
import com.nennig.vtglibrary.R;
import com.nennig.vtglibrary.managers.MovePins;
import com.nennig.vtglibrary.managers.Pin;

/**
 * @author Kevin Nennig (knennig213@gmail.com)
 *
 */
public class VTGMove extends View {
	private static final String TAG = AppConfig.APP_PNAME + ".VTGMove";
	private MovePins curMove;
	
	private float mTextWidth = 0.0f;
    private float mTextHeight = 0.0f;
    
    private int primColor;
    private int secColor;
    private int pinEndShape;
    
    private Paint primPaint;
    private Paint secPaint;
	
	/**
	 * @param context
	 */
	public VTGMove(Context context) {
		super(context);
		init();
	}
	
	public VTGMove(Context context, AttributeSet attrs) {
        super(context, attrs);
        // This call uses R.styleable.VTGMove, which is an array of
        // the custom attributes that were declared in attrs.xml.
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.VTGMove,
                0, 0
        );
        
        try {
        	primColor = a.getColor(R.styleable.VTGMove_primaryColor, Color.BLACK);
        	secColor = a.getColor(R.styleable.VTGMove_secondaryColor, Color.DKGRAY);
        	pinEndShape = a.getInt(R.styleable.VTGMove_pinEndShape, 0);
        } finally {
        	a.recycle();
        }
        
        init();
	}
	
//    @Override
//    protected int getSuggestedMinimumWidth() {
//        return (int) mTextWidth * 2;
//    }
//
//    @Override
//    protected int getSuggestedMinimumHeight() {
//        return (int) mTextWidth;
//    }
	
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Try for a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();

        int w = Math.max(minw, MeasureSpec.getSize(widthMeasureSpec));
        
        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        int minh = w + getPaddingBottom() + getPaddingTop();
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
	
	public void addPins(MovePins pm){
		curMove = pm;
		invalidate();
	}
	

	/**
	 * @param pm
	 */
	private void init() {
        // Force the background to software rendering because otherwise the Blur
        // filter won't work.
//        setLayerToSW(this);
        
        primPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        primPaint.setStyle(Paint.Style.FILL);
        primPaint.setColor(primColor);
        
        secPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        secPaint.setStyle(Paint.Style.FILL);
        secPaint.setColor(secColor);
        
        
//		curMove = pm;
	}
	
//    private void setLayerToSW(View v) {
//        if (!v.isInEditMode() && Build.VERSION.SDK_INT >= 11) {
//            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }
//    }
//	
//	private void setLayerToHW(View v) {
//        if (!v.isInEditMode() && Build.VERSION.SDK_INT >= 11) {
//            setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        }
//    }
	
	private enum Pos{
		poxX, negX, posY, negY
	}
	
	private static MovePins testMove = new MovePins();
	static{
		testMove.matrixID = "0x0x0";
//		testMove.pin0 = new Pin("O", "B");
		testMove.pin1 = new Pin("O", "B");
//		testMove.pin2 = new Pin("I", "B");
		testMove.pin3 = new Pin("O", "B");
//		testMove.pin4 = new Pin("O", "B");
		testMove.pin5 = new Pin("I", "B");
//		testMove.pin6 = new Pin("I", "B");
		testMove.pin7 = new Pin("O", "B");
	}
	
	@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        curMove = testMove;
        
        RectF rec = new RectF(0,0,getMeasuredWidth(),getMeasuredHeight());
//        canvas.drawRect(rec, primPaint);
        
        if(curMove != null){
	        //This checks to see if one of two pins should be drawn
	        if(curMove.pin0 == null)
	        	drawOnePin(canvas, curMove.pin7, Pos.poxX);
	        else 
	        	drawTwoPins(canvas, curMove.pin7, curMove.pin0, Pos.poxX);
	        
	        if (curMove.pin2 == null)
	        	drawOnePin(canvas, curMove.pin1, Pos.posY);
	        else 
	        	drawTwoPins(canvas, curMove.pin1, curMove.pin2, Pos.posY);
	        
	        if(curMove.pin4 == null)
	        	drawOnePin(canvas, curMove.pin3, Pos.negX);
	        else 
	        	drawTwoPins(canvas, curMove.pin3, curMove.pin4, Pos.negX);
	        
	        if(curMove.pin6 == null)
	        	drawOnePin(canvas, curMove.pin5, Pos.negY);
	        else
	        	drawTwoPins(canvas, curMove.pin5, curMove.pin6, Pos.negY);
        }
	}
	
	

	/**
	 * @param fPIn
	 * @param pin7
	 */
	private void drawTwoPins(Canvas canvas, Pin fPin, Pin sPin, Pos pos) {
		//canvas.drawCircle(cx, cy, radius, paint);
	}

	/**
	 * @param pin1
	 */
	private void drawOnePin(Canvas canvas, Pin pin, Pos pos) {
		
		int w = getWidth();
		int h = getHeight();
		Log.d(TAG, "Drawing ONE Pin. w=" + w + " h=" + h);
		Point propBoxOrigin;
		float boxX=0, boxY=0;
		
		RectF box = null;
		
		int oLength; //holds the w/h of the outer side
		int iLength;	//hold the w/h of the in
		Paint paint = null;
		
		switch(pos){
			case poxX:
				boxX = (float) (w * (3.0 / 4.0));
				boxY = (float) (h * (3.0 / 8.0));
				Log.d(TAG, "Updating posX: " + boxX + " " + boxY);
				break;
			case posY:
				boxX = (float) (w * (3.0 / 8.0f));
				boxY = 0;
				break;
			case negX:
				boxX = 0;
				boxY = (float) (h * (3.0 / 8.0f));
				break;
			case negY:
				boxX = (float) (w * (3.0 / 8.0f));
				boxY = (float) (h * (3.0 / 4.0f));
				break;
			default:
				break;
		}
		
		Log.d(TAG, pos + " x=" + boxX + " y=" + boxY);
		
		box = new RectF(boxX, boxY, boxX + (w / 4), boxY - (h / 4));
//		propBoxOrigin = new Point(boxX, boxY);
		canvas.drawRect(box, primPaint);
		
	}
	
//	private class MoveView extends View {
//
//		/**
//		 * @param context
//		 */
//		public MoveView(Context context) {
//			super(context);
//		}
//		
//		@Override
//        protected void onDraw(Canvas canvas) {
//            super.onDraw(canvas);
//		
//		
//		}
//		
//		@Override
//        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//			
//		}
//	}
}
