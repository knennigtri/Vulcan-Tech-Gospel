/**
 * 
 */
package com.nennig.vtglibrary.draw;

import java.io.InputStream;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.nennig.constants.AppConfig;
import com.nennig.constants.Dlog;
import com.nennig.vtglibrary.R;
import com.nennig.vtglibrary.custobjs.PropMove;
import com.nennig.vtglibrary.custobjs.VTGMoveAxis;
import com.nennig.vtglibrary.custobjs.VTGMoveAxis.Orientation;
//import com.nennig.vtglibrary.custobjs.Pin;

/**
 * @author Kevin Nennig (knennig213@gmail.com)
 * This class is a view class that takes in a MovePins object and then draws the move's pins within the view.
 * The class follows a simple convention of code reuse to create each of the 4 regions of drawable space for the
 * pins and then draws the pins in that area according to color, direction, and the view's size.
 */
public class VTGMove extends View {
	private static final String TAG = AppConfig.APP_TITLE_SHORT + ".VTGMove";
	private static final boolean ENABLEDEBUG = true;
    
    private int primColor;
    
    private Paint primCirclePaint;
    private Paint primLinePaint;
    private Paint proPaint;
    
    RectF drawAreaBounds = new RectF();
    
	int drawAreaPadding = 0;
	int pinBoxPadding = 20; //simple padding for each pin box
	
	/*
	 * These are all precalculated values that are used in this app to assure that fractions are not mixed up
	 */
	float oneHalf = 1.0f/2.0f;
	
	float oneFourth = 1.0f/4.0f;
	float threeFourths = 3.0f/4.0f;
	
	float oneEigth = 1.0f/8.0f;
	float twoEigths = 2.0f/8.0f;
	float threeEigths = 3.0f/8.0f;
	float sevenEigths = 7.0f/8.0f;
	
	float oneSixteenth = 1.0f/16.0f;
	
	/*
	 * Enum to determine which box we are calculating
	 */
	private enum PinBoxPos{
		RIGHT, LEFT, TOP, BOTTOM
	}	
	
	private InputStream moveIcon;
	private InputStream defaultIcon;
    float iconL=0;
    float iconT=0;
    float iconB=0;
    float iconR=0;
	
	/*
	 * This is Test Data
	 */
//	private static MovePins testMove = new MovePins();
//	static{
//		testMove.matrixID = "0x0x0";
//		testMove.pin0 = new Pin("O", "B");
//		testMove.pin1 = new Pin("I", "B");
//		testMove.pin2 = new Pin("I", "B");
//		testMove.pin3 = new Pin("I", "W");
//		testMove.pin4 = new Pin("I", "W");
//		testMove.pin5 = new Pin("I", "B");
//		testMove.pin6 = new Pin("I", "B");
//		testMove.pin7 = new Pin("O", "B");
//	}
//	static MovePins curMove = testMove;
//	
	private PropMove _pMove; //holds current move object
	
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
        } finally {
        	a.recycle();
        }
        
        init();
	}
	
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
	
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		drawAreaBounds = new RectF(0+drawAreaPadding, 0+drawAreaPadding, w-drawAreaPadding, h-drawAreaPadding);
	}
    
	/**
	 * THis initiates the class. It sets up the primary and secondary paints and gets the initial
	 * drawing area bounds.
	 */
	private void init() {
        // Force the background to software rendering because otherwise the Blur
        // filter won't work.
        int lineWidth = 5;
		
        primCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        primCirclePaint.setStyle(Paint.Style.FILL);
        primCirclePaint.setColor(primColor);
    
        primLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        primLinePaint.setStyle(Paint.Style.STROKE);
        primLinePaint.setColor(primColor);
        primLinePaint.setStrokeWidth(lineWidth);

        proPaint = new Paint();
		proPaint.setColor(Color.BLACK);
		int line = ((getHeight() / 2)-15) / 3;
		proPaint.setTextSize(40);
		proPaint.setTextScaleX(1.2f);
        
        drawAreaBounds = new RectF(0.0f +drawAreaPadding,0.0f +drawAreaPadding,
        		getMeasuredWidth()-drawAreaPadding,getMeasuredHeight()-drawAreaPadding);
        drawAreaBounds.offsetTo(getPaddingLeft(), getPaddingTop());
	}
	
	/**
	 * This adds the move pins to the view so that they can be drawn
	 * @param mp
	 */
	public void addPins(PropMove mp){
		_pMove = mp;
		moveIcon = null;
		invalidate();
	}
	/**
	 * This adds the move pins to the view so that they can be drawn
	 * @param mp
	 */
	public void addPinsAndIcon(PropMove mp, InputStream isIcon){
		_pMove = mp;
		moveIcon = isIcon;
		invalidate();
	}
	
	/**
	 * 
	 */
	public void removePinsAndIcon() {
		_pMove = null;
		moveIcon = null;
		invalidate();
	}
	
	/**
	 * 
	 */
	public void addDefaultIcon(InputStream dIcon) {
		defaultIcon = dIcon;
		invalidate();
	}

	/**
	 * 
	 */
	public void removeDefaultIcon() {
		defaultIcon = null;
		invalidate();
	}
	
	/**
	 * The overridden onDraw method takes the MovePins object and draws each of the pins according to the object.
	 * depending on which pins are valid, the view will be draw accordingly.
	 */
	@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);  
        
        if(_pMove != null){
	        //This checks to see if one of two pins should be drawn
	        if(_pMove.getXAxis().getHandOrientation().equals(VTGMoveAxis.Orientation.SPLIT)){
	        	RectF rightRec = makePinBox(PinBoxPos.RIGHT);
	        	RectF leftRec = makePinBox(PinBoxPos.LEFT);
	        	
	        	switch(_pMove.getXAxis().getPropOrientation()){
	        	case IN: //Shows Double Pin!
		        	drawOnePin(canvas, PinBoxPos.RIGHT, Orientation.IN, rightRec);
		        	drawOnePin(canvas, PinBoxPos.LEFT, Orientation.IN, leftRec);
		        	break;
	        	case OUT: // Only one side works
		        	drawOnePin(canvas, PinBoxPos.RIGHT, Orientation.OUT, rightRec);
		        	drawOnePin(canvas, PinBoxPos.LEFT, Orientation.OUT, leftRec);
		        	break;
	        	case TOG: // Case Works!
		        	drawOnePin(canvas, PinBoxPos.RIGHT, Orientation.IN, rightRec);
		        	drawOnePin(canvas, PinBoxPos.LEFT, Orientation.OUT, leftRec);
		        	break;
	        	default:
					Dlog.d(TAG, "prop orientation wrong: " + _pMove.getXAxis().getPropOrientation(), ENABLEDEBUG);
	        	}
	        	iconR = rightRec.left; //determines the right of the icon based on the left of the right pin box
	        	iconL = leftRec.right; //determines the left of the icon based on the right of the left pin box
	        }
	        if(_pMove.getYAxis().getHandOrientation().equals(VTGMoveAxis.Orientation.SPLIT)){
	        	RectF topRec = makePinBox(PinBoxPos.TOP);
	        	RectF bottomRec = makePinBox(PinBoxPos.BOTTOM);
	        	
	        	switch(_pMove.getYAxis().getPropOrientation()){
	        	case IN:
		        	drawOnePin(canvas, PinBoxPos.TOP, Orientation.IN, topRec);
		        	drawOnePin(canvas, PinBoxPos.BOTTOM, Orientation.IN, bottomRec);
		        	break;
	        	case OUT: // Only one side works
		        	drawOnePin(canvas, PinBoxPos.TOP, Orientation.OUT, topRec);
		        	drawOnePin(canvas, PinBoxPos.BOTTOM, Orientation.OUT, bottomRec);
		        	break;
	        	case TOG: // Case Works!
		        	drawOnePin(canvas, PinBoxPos.TOP, Orientation.OUT, topRec);
		        	drawOnePin(canvas, PinBoxPos.BOTTOM, Orientation.IN, bottomRec);
		        	break;
	        	default:
					Dlog.d(TAG, "prop orientation wrong: " + _pMove.getYAxis().getPropOrientation(), ENABLEDEBUG);
					break;
	        	}
	        	iconT = topRec.bottom; //determines the top of the icon based on the bottom of the top pin box
	        	iconB = bottomRec.top; //determines the bottom of the icon based on the top of the bottom pin box
	        }
	        if(_pMove.getXAxis().getHandOrientation().equals(VTGMoveAxis.Orientation.TOG)){
	        	RectF rightRec = makePinBox(PinBoxPos.RIGHT);
	        	RectF leftRec = makePinBox(PinBoxPos.LEFT);
	        	
	        	switch (_pMove.getXAxis().getPropOrientation()) {
				case IN:
					drawTwoPins(canvas, PinBoxPos.RIGHT, Orientation.IN, rightRec);
					drawTwoPins(canvas, PinBoxPos.LEFT, Orientation.IN, leftRec);
					break;
				case OUT:
					drawTwoPins(canvas, PinBoxPos.RIGHT, Orientation.OUT, rightRec);
					drawTwoPins(canvas, PinBoxPos.LEFT, Orientation.OUT, leftRec);
					break;
				case SPLIT:
					drawSplitPins(canvas, _pMove.getXAxis().getAxis(), rightRec);
					drawSplitPins(canvas, _pMove.getXAxis().getAxis(), leftRec);
					break;
				default:
					Dlog.d(TAG, "prop orientation wrong: " + _pMove.getXAxis().getPropOrientation(), ENABLEDEBUG);
					break;
				}
	        	iconR = rightRec.left; //determines the right of the icon based on the left of the right pin box
	        	iconL = leftRec.right; //determines the left of the icon based on the right of the left pin box
	        }
	        if(_pMove.getYAxis().getHandOrientation().equals(VTGMoveAxis.Orientation.TOG)){
	        	RectF topRec = makePinBox(PinBoxPos.TOP);
	        	RectF bottomRec = makePinBox(PinBoxPos.BOTTOM);
	        	
	        	switch (_pMove.getYAxis().getPropOrientation()) {
				case IN:
					drawTwoPins(canvas, PinBoxPos.TOP, Orientation.IN, topRec);
					drawTwoPins(canvas, PinBoxPos.BOTTOM, Orientation.IN, bottomRec);
					break;
				case OUT:
					drawTwoPins(canvas, PinBoxPos.TOP, Orientation.OUT, topRec);
					drawTwoPins(canvas, PinBoxPos.BOTTOM, Orientation.OUT, bottomRec);
					break;
				case SPLIT:
					drawSplitPins(canvas, _pMove.getYAxis().getAxis(), topRec);
					drawSplitPins(canvas, _pMove.getYAxis().getAxis(), bottomRec);
					break;
				default:
					Dlog.d(TAG, "prop orientation wrong: " + _pMove.getYAxis().getPropOrientation(), ENABLEDEBUG);
					break;
				}
	        	iconT = topRec.bottom; //determines the top of the icon based on the bottom of the top pin box
	        	iconB = bottomRec.top; //determines the bottom of the icon based on the top of the bottom pin box
	        }
	        
	        if(moveIcon != null){
	        	float size = Math.min(Math.abs(iconL-iconR), Math.abs(iconT-iconB)) * (9.0f/10.0f);
	        	Bitmap bm = getBitmapImage(moveIcon, size);
	        	float left = (getWidth() / 2.0f) - (size / 2.0f);
	        	float top =  (getHeight() / 2.0f) - (size / 2.0f);
	        	canvas.drawBitmap(bm, left, top, null);
	        }
        }
//        if(!proMessage.equals("")){
//        	int x = Math.round(getWidth() * (1.0f/4.0f));
//        	int y = Math.round(getHeight() * (1.0f/4.0f));
////        	for(String line: proMessage.split("\n")){
////        		canvas.drawText(line,x,y, proPaint);
////        	      y+=proPaint.ascent()+proPaint.descent() + 5;
////        	}
//        	drawMultilineText(proMessage,x,y, proPaint,canvas,2,new Rect(x,y,x+(2*x), y+(2*y)));
//        	Log.d(TAG, "Text displayed");
//        }
        if(defaultIcon != null){
        	float size = Math.min(drawAreaBounds.width() * (3.0f/4.0f),drawAreaBounds.height() * (3.0f/4.0f));
        	Bitmap bm = getBitmapImage(defaultIcon, size);
        	float left = (getWidth() / 2.0f) - (size / 2.0f);
        	float top =  (getHeight() / 2.0f) - (size / 2.0f);
        	canvas.drawBitmap(bm, left, top, null);
        }
	}
	
	/**
	 * This method takes in the parameters for two pins and then draws each pin according the the parameters
	 * @param canvas - the drawable area
	 * @param boxPos - The area which the pin box is located
	 * @param primPin - the primary pin that will be drawn
	 * @param primPinBox - the primary box where the primary pin will be drawn
	 * @param secPin - the secondary pin that will be drawn
	 * @param secPinBox - the secondary box where the secondary pin will be drawn
	 */
	private void drawTwoPins(Canvas canvas, PinBoxPos boxPos, Orientation dir, RectF pinBox ) {
		RectF[] halves = makeHalfPinBoxes(boxPos, pinBox);
		if(halves.length == 2){
			drawOnePin(canvas, boxPos, dir, halves[0]); //primary
			drawOnePin(canvas, boxPos, dir, halves[1]); //secondary
		}
		else
			Dlog.d(TAG, "half rectangles not calculated correctly", ENABLEDEBUG);
	}

	/**
	 * This is the main method for drawing the pins. This takes the position pf the pin box, the box size, and the 
	 * details of the pin and draws it onto the view.
	 * @param canvas - the drawable area
	 * @param boxPos - The area which the pin box is located
	 * @param pin - the pin that will be drawn
	 * @param pinBox - the box where the pin will be drawn
	 */
	private void drawOnePin(Canvas canvas, PinBoxPos boxPos, Orientation direction, RectF pinBox) {
		//Setup the Paint for the current pin
		Paint curCirclePaint = null;
		Paint curLinePaint = null;
		curCirclePaint = primCirclePaint;
		curLinePaint = primLinePaint;
	
		//calculates the points for the line of the pin
		float lStartX=0,lStartY=0,lStopX=0,lStopY=0;
		switch(boxPos){
		case RIGHT:
			lStartY = pinBox.top + (pinBox.height() * oneHalf);
			lStopY =  lStartY;
			lStartX = pinBox.left;
			lStopX = pinBox.right;
			break;
		case TOP:
			lStartX = pinBox.left + (pinBox.width() * oneHalf);
			lStopX = lStartX;
			lStartY = pinBox.top;
			lStopY = pinBox.bottom;
			break;
		case LEFT:
			lStartY = pinBox.top + (pinBox.height() * oneHalf);
			lStopY =  lStartY;
			lStartX = pinBox.left;
			lStopX = pinBox.right;
			break;
		case BOTTOM:
			lStartX = pinBox.left + (pinBox.width() * oneHalf);
			lStopX = lStartX;
			lStartY = pinBox.top;
			lStopY = pinBox.bottom;
			break;
		default:
			break;
		}
		canvas.drawLine(lStartX,lStartY,lStopX,lStopY, curLinePaint);
			
		//calculates the center and radius for the pin head
		float cx=0, cy=0, cRad=0, cxHand=0, cyHand=0;
		switch(boxPos){
		case RIGHT:
			cx = boxRightCX(direction, pinBox);
			cy = pinBox.top + pinBox.height() * oneHalf;
			cRad = pinBox.width() * oneEigth;
			break;
		case TOP:
			cx = pinBox.left + pinBox.width() * oneHalf;
			cy = boxTopCY(direction, pinBox);
			cRad = pinBox.height() * oneEigth;
			break;
		case LEFT:
			cx = boxLeftCX(direction, pinBox);
			cy = pinBox.top + pinBox.height() * oneHalf;
			cRad = pinBox.width() * oneEigth;
			break;
		case BOTTOM:
			cx = pinBox.left + pinBox.width() * oneHalf;
			cy = boxBottomCY(direction, pinBox);
			cRad = pinBox.height() * oneEigth;
			break;
		default:
			break;
		}
		canvas.drawCircle(cx, cy, cRad, curCirclePaint);	
	}

	/**
	 * This creates the Tog Split pin. This is a pin with a head on both sides of the line 
	 * and then a center point in the line to distinguish where the hands are.
	 * @param canvas - The drawable area
	 * @param pos - the identifier where the pin box is located
	 * @param boxRec - the box where the pin will be drawn
	 */
	private void drawSplitPins(Canvas canvas, VTGMoveAxis.axis axis, RectF pinBox) {
		//Setup the Paint for the current pin
		Paint curCirclePaint = null;
		Paint curLinePaint = null;
		curCirclePaint = primCirclePaint;
		curLinePaint = primLinePaint;
	
		//calculates the points for the line of the pin
		float lStartX=0,lStartY=0,lStopX=0,lStopY=0;
		switch(axis){
		case X:
			lStartY = pinBox.top + (pinBox.height() * oneHalf);
			lStopY =  lStartY;
			lStartX = pinBox.left;
			lStopX = pinBox.right;
			break;
		case Y:
			lStartX = pinBox.left + (pinBox.width() * oneHalf);
			lStopX = lStartX;
			lStartY = pinBox.top;
			lStopY = pinBox.bottom;
			break;
		default:
			break;
		}
		canvas.drawLine(lStartX,lStartY,lStopX,lStopY, curLinePaint);
		
		float cx,cy,cRad;
		//Calculates and draws the right/top circle
		cx=0; cy=0; cRad=0;
		switch(axis){
		case X:
			cx = boxRightCX(Orientation.OUT, pinBox);
			cy = pinBox.top + pinBox.height() * oneHalf;
			cRad = pinBox.width() * oneEigth;
			break;
		case Y:
			cx = pinBox.left + pinBox.width() * oneHalf;
			cy = boxTopCY(Orientation.OUT, pinBox);
			cRad = pinBox.height() * oneEigth;
			break;
		default:
			break;
		}
		canvas.drawCircle(cx, cy, cRad, curCirclePaint);
		
		//Calculates and draws the left/bottom circle
		cx=0; cy=0; cRad=0;
		switch(axis){
		case X:
			cx = boxRightCX(Orientation.IN, pinBox);
			cy = pinBox.top + pinBox.height() * oneHalf;
			cRad = pinBox.width() * oneEigth;
			break;
		case Y:
			cx = pinBox.left + pinBox.width() * oneHalf;
			cy = boxTopCY(Orientation.IN, pinBox);
			cRad = pinBox.height() * oneEigth;
			break;
		default:
			break;
		}
		canvas.drawCircle(cx, cy, cRad, curCirclePaint);
		
		//calculates and draws the center point of the line
		cx=0; cy=0; cRad=0;
		cx = pinBox.centerX();
		cy = pinBox.centerY();
		cRad = pinBox.width() * oneSixteenth;
		canvas.drawCircle(cx, cy, cRad, curCirclePaint);
		
	}
	
	/**
	 * This method takes in the PinBoxPos enum and then calculates the box in that position. The box returned will
	 * be the drawable area for the pins
	 * @param boxPos - enum to determine what area box is being created
	 * @return - box that will contain the pin(s) in that area
	 */
	private RectF makePinBox(PinBoxPos boxPos){
		Dlog.d(TAG, "Drawing single Pin. w=" + drawAreaBounds.width() + " h=" + drawAreaBounds.height(),ENABLEDEBUG);
		
		//Get the origin for each box
		float boxX=0, boxY=0;		
		switch(boxPos){
			case RIGHT:
				boxX = (float) (drawAreaBounds.width() * threeFourths);
				boxY = (float) (drawAreaBounds.height() * threeEigths);
				break;
			case TOP:
				boxX = (float) (drawAreaBounds.width() * threeEigths);
				boxY = 0;
				break;
			case LEFT:
				boxX = 0;
				boxY = (float) (drawAreaBounds.height() * threeEigths);
				break;
			case BOTTOM:
				boxX = (float) (drawAreaBounds.width() * threeEigths);
				boxY = (float) (drawAreaBounds.height() * threeFourths);
				break;
			default:
				break;
		}
		
		Dlog.d(TAG, boxPos + " boxX=" + boxX + " boxY=" + boxY, ENABLEDEBUG);
		
		RectF pinBox = new RectF(boxX + pinBoxPadding, 
				boxY + pinBoxPadding,
				boxX + (drawAreaBounds.width() / 4) - pinBoxPadding,
				boxY + (drawAreaBounds.height() / 4) - pinBoxPadding
				);
		Dlog.d(TAG, "Box is: " + pinBox,ENABLEDEBUG);

		return pinBox;
	}
	
	/**
	 * This method takes the pin box parameter and splits it into two primary and secondary boxes
	 * @param boxPos - The area which the pin box is located
	 * @param pinBox - the box that will be split into two smaller boxes
	 * @return - 2 item array with the primary box in 0 and the secondary box in 1
	 */
	private RectF[] makeHalfPinBoxes(PinBoxPos boxPos, RectF pinBox){
		RectF primBox = null;
		RectF secBox = null;
		
		float pLeft=0,pTop=0,pRight=0,pBottom=0;
		float sLeft=0,sTop=0,sRight=0,sBottom=0;
		switch(boxPos){
			case RIGHT:
				pLeft = pinBox.left;
				pTop = pinBox.top + pinBox.height() * oneHalf;
				pRight = pinBox.right;
				pBottom = pinBox.bottom;
				sLeft = pinBox.left;
				sTop = pinBox.top;
				sRight = pinBox.right;
				sBottom = pinBox.top + pinBox.height() * oneHalf;
				break;
			case TOP:
				pLeft = pinBox.left + pinBox.width() * oneHalf;
				pTop = pinBox.top;
				pRight = pinBox.right;
				pBottom = pinBox.bottom;
				sLeft = pinBox.left;
				sTop = pinBox.top;
				sRight = pinBox.left + pinBox.width() * oneHalf;
				sBottom = pinBox.bottom;
				break;
			case LEFT:
				pLeft = pinBox.left;
				pTop = pinBox.top;
				pRight = pinBox.right;
				pBottom = pinBox.top + pinBox.height() * oneHalf;
				sLeft = pinBox.left;
				sTop = pinBox.top + pinBox.height() * oneHalf;
				sRight = pinBox.right;
				sBottom = pinBox.bottom;
				break;
			case BOTTOM:
				pLeft = pinBox.left;
				pTop = pinBox.top;
				pRight = pinBox.left + pinBox.width() * oneHalf;
				pBottom = pinBox.bottom;
				sLeft = pinBox.left + pinBox.width() * oneHalf;
				sTop = pinBox.top;
				sRight = pinBox.right;
				sBottom = pinBox.bottom;
				break;
			default:
				break;
		}
		primBox = new RectF(pLeft, pTop, pRight, pBottom);
		secBox = new RectF(sLeft, sTop, sRight, sBottom);
		return new RectF[] {primBox,secBox};
	}
	
		
	/**
	 * Calculates the y coordinate for the bottom pin box according to the pin direction
	 * @param direction - direction of the pin
	 * @param pinBox - the box where the pin is being drawn
	 * @return - the y coordinate for the bottom pin box
	 */
	private float boxBottomCY(Orientation direction, RectF pinBox) {
		if(direction.equals(Orientation.IN))
			return pinBox.top + pinBox.height() * oneEigth;
		else
			return pinBox.top + pinBox.height() * sevenEigths;
	}

	/**
	 * Calculates the y coordinate for the top pin box according to the pin direction
	 * @param direction - direction of the pin
	 * @param pinBox - the box where the pin is being drawn
	 * @return - the y coordinate for the top pin box
	 */
	private float boxTopCY(Orientation direction, RectF pinBox) {
		if(direction.equals(Orientation.IN))
			return pinBox.top + pinBox.height() * sevenEigths;
		else
			return pinBox.top + pinBox.height() * oneEigth;
	}

	/**
	 * Calculates the x coordinate for the right pin box according to the pin direction
	 * @param direction - direction of the pin
	 * @param pinBox - the box where the pin is being drawn
	 * @return - the x coordinate for the right pin box
	 */
	private float boxRightCX(Orientation direction, RectF pinBox) {
		if(direction.equals(Orientation.IN))
			return pinBox.left + pinBox.width() * oneEigth;
		else
			return pinBox.left + pinBox.width() * sevenEigths;
	}
	
	/**
	 * Calculates the x coordinate for the left pin box according to the pin direction
	 * @param direction - direction of the pin
	 * @param pinBox - the box where the pin is being drawn
	 * @return - the x coordinate for the left pin box
	 */
	private float boxLeftCX(Orientation direction, RectF pinBox) {
		if(direction.equals(Orientation.IN))
			return pinBox.left + pinBox.width() * sevenEigths;
		else
			return pinBox.left + pinBox.width() * oneEigth;
	}
	
	private static Bitmap getBitmapImage(InputStream iStream, float reqWidth) {
		Dlog.d(TAG, "ReqSize: " + reqWidth, ENABLEDEBUG);
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(iStream, null, options);
        
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap newBitmap = BitmapFactory.decodeStream(iStream, null, options);
        
        final int height = options.outHeight;
        final int width = options.outWidth;
        Dlog.d(TAG, "H: " + height + " W: " + width,ENABLEDEBUG);
       
        int newHeight = (int) Math.round((reqWidth / (float) width)*height);
        int newWidth= Math.round(reqWidth);
       
        Dlog.d(TAG, "nH: " + newHeight + " nW: " + newWidth,ENABLEDEBUG);
    	newBitmap = Bitmap.createScaledBitmap(newBitmap, newWidth, newHeight, true);
        
        return newBitmap;
    }
	
	   private void drawMultilineText(String str, int x, int y, Paint paint, Canvas canvas, int fontSize, Rect drawSpace) {
		    int      lineHeight = 0;
		    int      yoffset    = 0;
		    String[] lines      = str.split(" ");

		    // set height of each line (height of text + 20%)
		    lineHeight = (int) (calculateHeightFromFontSize(str, fontSize) * 1.2);
		    // draw each line
		    String line = "";
		    for (int i = 0; i < lines.length; ++i) {

		        if(calculateWidthFromFontSize(line + " " + lines[i], fontSize) <= drawSpace.width()){
		            line = line + " " + lines[i];

		        }else{
		            canvas.drawText(line, x, y + yoffset, paint);
		            yoffset = yoffset + lineHeight;
		            line = lines[i];
		        }
		    }
		    canvas.drawText(line, x, y + yoffset, paint);

		}

		private int calculateWidthFromFontSize(String testString, int currentSize)
		{
		    Rect bounds = new Rect();
		    Paint paint = new Paint();
		    paint.setTextSize(currentSize);
		    paint.getTextBounds(testString, 0, testString.length(), bounds);

		    return (int) Math.ceil( bounds.width());
		}

		private int calculateHeightFromFontSize(String testString, int currentSize)
		{
		    Rect bounds = new Rect();
		    Paint paint = new Paint();
		    paint.setTextSize(currentSize);
		    paint.getTextBounds(testString, 0, testString.length(), bounds);

		    return (int) Math.ceil( bounds.height());
		}

	/**
	 * @param x
	 * @param y
	 */
	public boolean iconIsTouched(float x, float y) {
		if((x > iconL) && (x < iconR) && (y > iconT) && (y < iconB))
			return true;
		return false;
		
	}
}
