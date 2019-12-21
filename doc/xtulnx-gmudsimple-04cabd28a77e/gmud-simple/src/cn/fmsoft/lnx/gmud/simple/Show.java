/**
 * Copyright (C) 2011, FMSoft.GMUD.
 * 
 * @author nxliao
 */
package cn.fmsoft.lnx.gmud.simple;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import cn.fmsoft.lnx.gmud.simple.core.Gmud;
import cn.fmsoft.lnx.gmud.simple.core.Video;

public class Show extends SurfaceView implements SurfaceHolder.Callback {

	public Show(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
        SurfaceHolder holder = getHolder();  
        holder.addCallback(this);  
        setFocusable(false);
        setFocusableInTouchMode(false);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		Gmud.checkScale();
		
		int width;
		int height;
		if (widthMode == MeasureSpec.EXACTLY) {
			// Parent has told us how big to be. So be it.
			width = widthSize;
		} else {
			width = Gmud.WQX_ORG_WIDTH * Video.sScale;
		}
		
		if (heightMode == MeasureSpec.EXACTLY) {
			// Parent has told us how big to be. So be it.
			height = heightSize;
		} else {
			height = Gmud.WQX_ORG_HEIGHT * Video.sScale;
		}

		setMeasuredDimension(width, height);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Video.Bind(this);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Canvas c = getHolder().lockCanvas();
		if (c != null) {
			c.drawColor(Color.BLACK);
			getHolder().unlockCanvasAndPost(c);
		}
		Video.VideoUpdate();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Video.UnBind(this);
	}
}
