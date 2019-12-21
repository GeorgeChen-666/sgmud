/**
 * Copyright (C) 2011, FMSoft.GMUD.
 * 
 * Video, is used for clear, draw and fill (line, rectangle, arc, image, text).
 * 
 * @author nxliao
 */
package cn.fmsoft.lnx.gmud.simple.core;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import cn.fmsoft.lnx.gmud.simple.Show;
import cn.fmsoft.lnx.gmud.simple.R.id;

public class Video {
	
	static final int LARGE_FONT_SIZE = 16;
	static final int SMALL_FONT_SIZE = 12;
	
	static int largeFnt = LARGE_FONT_SIZE;
	static int smallFnt = SMALL_FONT_SIZE;
	static int largeOff = largeFnt;
	static int smallOff = smallFnt;
	

	static boolean VideoExited = true;

	static Bitmap lpmemimg;
	static Canvas lpmem;
	
	static Paint blackBrush, greenBrush;
	static Paint blackPen;
	
	static Bitmap pnum;

	static Paint sPaint;

	static Show sBinderShow;
	
	static int sWidth, sHeight;
	static Matrix sMatrix = new Matrix();
	
	public static int sScale = 1;
	
	static Rect sDirtyRect = new Rect();
	
	public static synchronized boolean HasBound() {
		return sBinderShow != null;
	}

	public static synchronized void Bind(Show show) {
		sWidth = show.getWidth();
		sHeight = show.getHeight();

		if (sBinderShow != show) {
			sBinderShow = show;
			GmudMain.Resume();
		}
	}

	public static synchronized void UnBind(Show show) {
		sBinderShow = null;
	}
	
	static boolean VideoInit(int scale) {
				
		sPaint = new Paint();
		sPaint.setAntiAlias(true);

		lpmem = new Canvas();
		
		resetScale(scale);
		
		// blackBrush = new SolidBrush(Color(255, 0, 0, 0));
		// greenBrush = new SolidBrush(Color(255, 143, 175, 80));
		// blackPen = new Pen(Color(255,0,0,0), 1.0f);
		pnum = Res.loadimage(248);

		blackBrush = new Paint();
		blackBrush.setAntiAlias(true);
		blackBrush.setARGB(255, 0, 0, 0);
		blackBrush.setStyle(Style.FILL);
		greenBrush = new Paint();
		greenBrush.setAntiAlias(true);
		greenBrush.setARGB(255, 143, 175, 80);
		greenBrush.setStyle(Style.FILL);
		blackPen = new Paint();
		blackPen.setAntiAlias(true);
		blackPen.setColor(0xff000000);
		blackPen.setStyle(Style.STROKE);
		blackPen.setStrokeWidth(1.0f);
		
//		// 得到系统默认字体属�?
//		Paint paint = blackBrush;
//		FontMetrics fm;
//		paint.setTextSize(largeFnt);
//		fm = paint.getFontMetrics();
//		largeOff = (int) (fm.descent - fm.ascent);
//		paint.setTextSize(smallFnt);
//		fm = paint.getFontMetrics();
//		smallOff = (int) (fm.descent - fm.ascent);
		
		VideoExited = false;
		VideoClear();
		VideoUpdate();

		return true;
	}

	static synchronized void resetScale(int scale) {
		if (!VideoExited && scale == sScale) {
			return; // no change
		}

		synchronized (lpmem) {
			sScale = scale;
			
			Bitmap backBitmap = lpmemimg;

//			if (lpmemimg != null) {
//				lpmemimg.recycle();
//			}

			lpmemimg = Bitmap.createBitmap(Gmud.WQX_ORG_WIDTH * sScale,
					Gmud.WQX_ORG_HEIGHT * sScale, Bitmap.Config.RGB_565);

			lpmem.setBitmap(lpmemimg);
			
			if (backBitmap != null) {
				Matrix matrix = new Matrix();
				matrix.setScale(
						1.0f * lpmemimg.getWidth() / backBitmap.getWidth(),
						1.0f * lpmemimg.getHeight() / backBitmap.getHeight());
				lpmem.drawBitmap(backBitmap, matrix, sPaint);
			}

			// calculate font size
			largeFnt = LARGE_FONT_SIZE * sScale;
			smallFnt = SMALL_FONT_SIZE * sScale;
			largeOff = largeFnt - sScale;
			smallOff = smallFnt - sScale;

			if (!VideoExited) {
			}
		}
	}

	static void VideoShutdown() {
		// DeleteObject(pnum);
		// DeleteObject(largeFnt);
		// DeleteObject(smallFnt);
		// DeleteObject(blackBrush);
		// DeleteObject(greenBrush);
		// DeleteObject(blackPen);
		//
		// DeleteObject(lpmemimg);
		// DeleteObject(lpmem);
		// DeleteObject(lpwnd);
		//
		// GdiplusShutdown(m_pGdiToken);
		// ReleaseDC(hw,m_hdc);
		// VideoExited = 1;

		VideoExited = true;
		lpmemimg.recycle();
		lpmemimg = null;
		lpmem = null;
	}

	static void exit(int code) {
		throw new RuntimeException("Video exit(" + code + ").");
	}

	static void VideoDrawLine(int x1, int y1, int x2, int y2) {
		if (VideoExited)
			exit(0);
		lpmem.drawLine(x1*sScale, y1*sScale, x2*sScale, y2*sScale, blackPen);
	}
	
	/**
	 * 绘制一个箭头
	 * @param x
	 * @param y
	 * @param w 宽度
	 * @param h 高度，如果小于0，表示向上
	 * @param type 类型，Bit0:左右方向 Bit1:实心 Bit2:用背景色(只用于实心)
	 */
	static void VideoDrawArrow(int x, int y, int w, int h, int type) {
		final Path path = new Path();
		x *= sScale;
		y *= sScale;
		w *= sScale;
		h *= sScale;
		path.moveTo(x, y);
		if ((type & 1) == 0) {
			path.lineTo(x + w, y);
			path.lineTo(x + w / 2, y + h);
		} else {
			path.lineTo(x, y + h);
			path.lineTo(x + w, y + h / 2);
		}
		path.close();

		if ((type & 2) == 0) {
			// clear
			lpmem.drawPath(path, greenBrush);
			lpmem.drawPath(path, blackPen);
		} else {
			if ((type & 4) == 0) {
				lpmem.drawPath(path, blackBrush);
			} else {
				lpmem.drawPath(path, greenBrush);
			}
		}
	}

	static void VideoClear() {
		if (VideoExited)
			exit(0);
		/*
		 * SolidBrush solidBrush(Color(255, 143, 175, 80));
		 * lpgraphics->FillRectangle(solidBrush.Clone(), Rect(0,0,480,240));
		 */
		lpmem.drawARGB(255, 143, 175, 80);
	}

	static void VideoClearRect(int x, int y, int width, int height) {
		if (VideoExited)
			exit(0);

		lpmem.drawRect(x*sScale, y*sScale, (x + width)*sScale, (y + height)*sScale, greenBrush);
	}

	static void VideoDrawRectangle(int x, int y, int width, int height) {
		if (VideoExited)
			exit(0);

		lpmem.drawRect(x*sScale, y*sScale, (x + width)*sScale, (y + height)*sScale, blackPen);
	}

	static void VideoFillRectangle(int x, int y, int width, int height) {
		VideoFillRectangle(x, y, width, height, 0);
	}

	static void VideoFillRectangle(int x, int y, int width, int height, int type) {
		if (VideoExited)
			exit(0);
		if (type != 0) {
			lpmem.drawRect(x*sScale, y*sScale, (x + width)*sScale, (y + height)*sScale, greenBrush);
		} else {
			lpmem.drawRect(x*sScale, y*sScale, (x + width)*sScale, (y + height)*sScale, blackBrush);
		}

	}

	static void VideoDrawArc(int x, int y, int r) {
		if (VideoExited)
			exit(0);

		// lpmem->DrawArc(blackPen, x - r, y - r, 2 * r, 2 * r, 0, 360);
		lpmem.drawCircle(x*sScale, y*sScale, r*sScale, blackPen);
	}

	static void VideoFillArc(int x, int y, int r) {
		if (VideoExited)
			exit(0);
		// lpmem->FillEllipse(blackBrush->Clone(), x - r, y - r, 2 * r, 2 * r);
		lpmem.drawCircle(x*sScale, y*sScale, r*sScale, blackBrush);
	}

	static void VideoDrawImage(/* Image* */Bitmap pI, int x, int y) {
		if (VideoExited)
			exit(0);

		// lpmem->DrawImage(pI, x, y);
//		lpmem.drawBitmap(pI, x*sScale, y*sScale, null);

		sMatrix.setScale(sScale, sScale);
		sMatrix.postTranslate(x*sScale, y*sScale);
		lpmem.drawBitmap(pI, sMatrix, sPaint);
	}

	private static void drawMultiText(String str, int x, int y, int restrictWidth, Paint paint) {

		FontMetrics fm = paint.getFontMetrics();// 得到系统默认字体属性
		final int fontHeight = (int) (Math.ceil(fm.descent - fm.ascent) - 2);// 获得字体高度

		// paint.getTextWidths(str, widths);

		int line = 0;
		int linestart = 0;
		int k = str.length();
		for (int i = 1; i < k; i++) {
			float w = blackBrush.measureText(str, linestart, i);
			if (w + x >= restrictWidth*sScale) {
				lpmem.drawText(str.substring(linestart, i-1), x*sScale, y*sScale + fontHeight * line, blackBrush);
				linestart = i-1;
				line++;
			}
		}
		if (linestart < k) {
			lpmem.drawText(str.substring(linestart, k), x*sScale, y*sScale + fontHeight * line, blackBrush);
			return;
		}
	}

	static void VideoDrawString(String str, int x, int y) {
		VideoDrawString(str, x, y, 0);
	}
	static void VideoDrawString(String str, int x, int y, int type) {
		if (VideoExited)
			exit(0);

		// 大字体y坐标:每行+16 //小字体y坐标:每行+13
		if (type != 0) {
			blackBrush.setTextSize(largeFnt);
			lpmem.drawText(str, x*sScale, y*sScale+largeOff, blackBrush);
		} else {
			blackBrush.setTextSize(smallFnt);
			drawMultiText(str, x, y+smallOff/sScale, Gmud.WQX_ORG_WIDTH, blackBrush);
		}
	}

	// static void VideoDrawString(const wchar_t*, int, int, int type = 0);
	static void VideoDrawStringSingleLine(final String str, int x, int y) {
		VideoDrawStringSingleLine(str, x, y, 0);
	}

	static void VideoDrawStringSingleLine(final String str, int x, int y, int type) {
		if (VideoExited)
			exit(0);

		// PointF origin(x, y); 
		// 大字体y坐标:每行+16 
		// 小字体y坐标:每行+13
		switch (type) {
		case 1:
			// lpmem->DrawString(str, -1, largeFnt, PointF(x, y), blackBrush);
			blackBrush.setTextSize(largeFnt);
			lpmem.drawText(str, x*sScale, y*sScale+largeOff, blackBrush);
			break;
		case 2:
			greenBrush.setTextSize(smallFnt);
			lpmem.drawText(str, x*sScale, y*sScale+smallOff, greenBrush);
			break;
		default:
			blackBrush.setTextSize(smallFnt);
			lpmem.drawText(str, x*sScale, y*sScale+smallOff, blackBrush);
		}
	}

	static void VideoDrawNumberData(String data, int x, int y) {
		if (VideoExited)
			exit(0);

		Rect rectSrc = new Rect(0, 0, 4, 5);
		Rect rectDst = new Rect(x*sScale, y*sScale, (x + 4)*sScale, (y + 5)*sScale);

		for (int i1 = 0; i1 < data.length(); i1++) {
			char c = data.charAt(i1);

			if ('0' <= c && c <= '9') {
				rectSrc.left = 4 * (c - '0');
				rectSrc.right = 4 * (c - '0') + 4;
			} else {
				rectSrc.left = 4 * 10;
				rectSrc.right = 4 * 10 + 4;
			}
			lpmem.drawBitmap(pnum, rectSrc, rectDst, sPaint);

			rectDst.left += 4*sScale;
			rectDst.right += 4*sScale;
		}
	}

	public static void VideoUpdate() {
		if (VideoExited)
			return;

		synchronized (lpmem) {
			
			if (sBinderShow == null) {
				return;
			}

			final int width = lpmemimg.getWidth();
			final int height = lpmemimg.getHeight();
			final int left = (sBinderShow.getWidth() - width) / 2;
			sDirtyRect.set(left, 0, left + width, height);

			// lpwnd->DrawImage(lpmemimg, 0, 0, 480, 240);

			// 锁定Canvas,进行相应的界面处理	
			Canvas c = sBinderShow.getHolder().lockCanvas(sDirtyRect);
			if (c != null) {

				c.drawBitmap(lpmemimg, left, 0, sPaint);

				// 画完后，unlock
				sBinderShow.getHolder().unlockCanvasAndPost(c);
			}
		}
	}

	static ArrayList<String> SplitString(String str, int width) {
		
		final Paint paint = blackBrush;
		paint.setTextSize(smallFnt);
		
		ArrayList<String> sv = new ArrayList<String>();
		
		int linestart = 0;
		// RectF r;
		int k = str.length();

		
		for (int k1 = 0; k1 < k; k1++) {
			if (str.charAt(k1) == '\n') {
				if (k1 != 0) {
					sv.add(str.substring(linestart, k1));
					linestart = k1 + 1;
				}
				continue;
			}
			
//			if (k1 == k - 1) {
//				sv.add(str.substring(linestart, k1 + 1));
//				break;
//			}
			
			float w = blackBrush.measureText(str, linestart, k1);
			if (w >= width*sScale) {
				sv.add(str.substring(linestart, k1-1));
				linestart = k1-1;
			}

			// lpmem->MeasureString(str->substr(linestart, length).c_str(),
			// length, smallFnt, origin, &r);
			// if (r.GetRight() > width)
//			{
//				length = k1 - linestart;
//				sv.push_back(str->substr(linestart, length));
//				linestart = k1;
//			}
		}
//		if (linestart < k) {
			sv.add(str.substring(linestart, k));
//		}
		return sv;
	}
}
