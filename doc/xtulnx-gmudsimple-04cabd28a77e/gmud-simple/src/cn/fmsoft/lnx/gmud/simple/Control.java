/**
 * Copyright (C) 2011, FMSoft.GMUD.
 * 
 * @author nxliao
 */
package cn.fmsoft.lnx.gmud.simple;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import cn.fmsoft.lnx.gmud.simple.core.Input;

public class Control extends View {
	
	static boolean sbConfig_Hide = false;
	
	// soft key
	static final Rect sSoftKey[] = new Rect[9];
	
//	static final String sSoftKeyTitle[] = new String[] {
//		"Quit", "Enter", 
//		"Down", "Left", "Up", "Right", 
//		"LL", "RR", "Fly" };
	final String sSoftKeyTitle[];
	
	static final int[][] sSoftKeyPort = new int[][] {
		{ 0, 0 }, { 2, 0 },
		{ 1, 3 }, { 0, 3 }, { 1, 2 }, { 2, 3 }, 
		{ 0, 1 }, { 2, 1 }, { 1, 1 } };
//	static final int[][] sSoftKeyLand = new int[][] {
//		{ 3, 0 }, { 5, 0 },
//		{ 1, 1 }, { 0, 1 }, { 1, 0 }, { 2, 1 }, 
//		{ 3, 1 }, { 5, 1 }, { 4, 1 } };
	static final int[][] sSoftKeyLand = new int[][] {
		{ 0, 1 }, { 0, 0 },
		{ 4, 1 }, { 3, 1 }, { 4, 0 }, { 5, 1 }, 
		{ 3, 0 }, { 5, 0 }, { 1, 0 } };

	int sSoftKeySpaceV, sSoftKeySpaceH, sSoftKeyLeft, sSoftKeyTop, sSoftKeyW,
			sSoftKeyH;
	
	boolean bPort;
	
	public Control(Context context, AttributeSet attrs) {		
		super(context, attrs);
		sSoftKeyTitle = getResources().getStringArray(R.array.soft_key);
//		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		setFocusable(true);
		setFocusableInTouchMode(true);
		requestFocus();
	}
	
	public void hide(boolean bhide) {
		sbConfig_Hide = bhide;
		requestLayout();
	}

	// Ovrride this method, to hide Chinese input-method.
	@Override
	public boolean dispatchKeyEventPreIme(KeyEvent event) {
//        Activity activity = getActivity();
//        if (activity != null && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//            KeyEvent.DispatcherState state = getKeyDispatcherState();
//            if (state != null) {
//                if (event.getAction() == KeyEvent.ACTION_DOWN
//                        && event.getRepeatCount() == 0) {
//                    state.startTracking(event, this);
//                    return true;
//                } else if (event.getAction() == KeyEvent.ACTION_UP
//                        && !event.isCanceled() && state.isTracking(event)) {
////                    hideInputMethod();
//                    activity.onBackPressed();
//                    return true;
//                }
//            }
//        }
//        return super.dispatchKeyEventPreIme(event);
//        return onKeyPreIme(event.getKeyCode(), event);
//		super.dispatchKeyEvent(event);
//		return true;
		if (Input.Running) {
			int code = event.getKeyCode();
			if (KeyEvent.KEYCODE_ENTER==code || (KeyEvent.KEYCODE_0 <= code && code <= KeyEvent.KEYCODE_Z)) {
				Input.onKey(code, event);
				return true;
			}
		}
		return super.dispatchKeyEventPreIme(event);
	}

	void drawSoftKey(Canvas canvas) {
		
		final int bgColor = bPort?0xff333333:0x60222222;
		
		Paint paint = new Paint();
		paint.setAlpha(40);
		paint.setTextSize(sSoftKeyH/2);
		paint.setTextAlign(Align.CENTER);
		for (int i = 0; i < 9; i++) {
//			if ((Input.inputstatus & (1 << i)) != 0) {
//				paint.setColor(0x60c00000);
//			} else 
			{
				paint.setColor(bgColor);
			}
			paint.setStyle(Style.FILL);
			canvas.drawRect(sSoftKey[i], paint);
			paint.setStyle(Style.STROKE);
			paint.setColor(0x8000c000);
			canvas.drawRect(sSoftKey[i], paint);
			canvas.drawText(sSoftKeyTitle[i],sSoftKey[i].centerX(), sSoftKey[i].centerY(), paint);
		}
	}
	
	private void setRectPosition(int rectId, int x, int y) {
//		x = (sWidth - 6*50)/2 + x * 50;
//		y = (sHeight - 2 * 40) / 1 + y * 40;
		if (sSoftKey[rectId]==null) {
			sSoftKey[rectId] = new Rect();
		}
//		sSoftKey[rectId].set(x, y, x+45, y+36);

		x = (sSoftKeyLeft+(sSoftKeyW+sSoftKeySpaceH)*x);
		y = (sSoftKeyTop+(sSoftKeyH+sSoftKeySpaceV)*y);
		sSoftKey[rectId].set(x, y, x+sSoftKeyW, y+sSoftKeyH);
	}
	
	int resetSoftKeyPosition(int width, int height) { 
		int ret;
		int [][]pos;
		if (width < height) {
			int w = width / 3;
			sSoftKeyW = w * 3/4;
			sSoftKeySpaceH = w * 1/4;
			sSoftKeyLeft = 0;
			
			sSoftKeyH = height / 8;
			sSoftKeySpaceV = sSoftKeyH / 8;
			sSoftKeyTop = height - (sSoftKeyH+sSoftKeySpaceV)*4;
			
			ret = (sSoftKeyH+sSoftKeySpaceV)*4;
			pos = sSoftKeyPort;
		} else {
			int w = width / 6;
			sSoftKeyW = w * 3/4;
			sSoftKeySpaceH = w * 1/4;
			sSoftKeyLeft = 0;
			
			sSoftKeyH = height / 5;
			sSoftKeySpaceV = sSoftKeyH / 8;
			sSoftKeyTop = height - (sSoftKeyH+sSoftKeySpaceV)*2;
			
			ret = (sSoftKeyH+sSoftKeySpaceV)*2;
			pos = sSoftKeyLand;
		}
		
		sSoftKeyTop = 0;
		
		for (int i=0; i<9; i++) {
			setRectPosition(i, pos[i][0], pos[i][1]);
		}
		return ret;
	}
	
	int checkSoftKey(int x, int y) {
		int mask = 0;
		for (int j = 0; j < 9; j++) {
			if (sSoftKey[j].contains(x, y)) {
				mask |= 1<<j;
			}
		}
		return mask;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (sbConfig_Hide) {
			return false;
		}
		
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		if (action == MotionEvent.ACTION_DOWN
				|| action == MotionEvent.ACTION_CANCEL
				|| action == MotionEvent.ACTION_UP
				|| action == MotionEvent.ACTION_POINTER_DOWN
				|| action == MotionEvent.ACTION_POINTER_UP) {

//			boolean press = action == MotionEvent.ACTION_DOWN
//					|| action == MotionEvent.ACTION_POINTER_DOWN;

			int mask = 0;
			
			int count = event.getPointerCount();
			for (int i = 0; i < count; i++) {
				int x = (int) event.getX(i);
				int y = (int) event.getY(i);
				mask |= checkSoftKey(x, y);
			}
			Input.GmudSetKey(mask);
		}
		return super.onTouchEvent(event);
	}
	
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

//        int width;
        int height;
        
        bPort =  (widthSize < heightSize);
        
        height = resetSoftKeyPosition(widthSize, heightSize);
        
        if (sbConfig_Hide) {
        	widthSize = 1;
        	height = 1;
        }

        setMeasuredDimension(widthSize, height);
        
        requestFocus();
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
    	if (sbConfig_Hide) {
    		canvas.drawARGB(40, 90, 0, 0);
    		return ;
    	}
    	
    	drawSoftKey(canvas);
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return Input.onKey(keyCode, event);
	}
}
