/**
 * Copyright (C) 2011, FMSoft.GMUD.
 * 
 * Gmud: base server, initialize static data, start input thread, provide archive.
 * 
 * @author nxliao
 */
package cn.fmsoft.lnx.gmud.simple.core;

import android.app.Activity;
import android.content.Context;
import android.view.Display;

public class Gmud {

	final static boolean DEBUG = true;
	
	public final static String SAVE_PATH = "gmud_save";

	public final static int WQX_ORG_WIDTH = 160;
	public final static int WQX_ORG_HEIGHT = 80;
	static final int FRAME_TIME = 1000 / 60;

	static Map sMap;
	static Player sPlayer;

	public static boolean Running = false;
	
	static Context sContext;

	static Activity sActivity = null;
	
	static boolean sbConfig_MinScale = true;
	
	/**
	 * 清除数据，用于自杀
	 *  
	 * @return
	 */
	static boolean CleanSave() {
		return sPlayer.Clean(sContext);
	}

	/**
	 * 存档
	 * 
	 * @return
	 */
	static boolean WriteSave() {
		return sPlayer.Save(sContext);
	}

	/**
	 * 读档
	 * 
	 * @return
	 */
	static boolean LoadSave() {
		return sPlayer.load(sContext);
	}

	/**
	 * sleep 方式延时
	 * 
	 * @param millis
	 */
	static void GmudDelay(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 初始化一些静态数据
	static void prepare() {
		sMap = Map.getInstance();
		sPlayer = Player.getInstance();
	}

	public static void exit() {
		Input.Stop();

		Running = false;
		
		if (sActivity != null) {
			sActivity.finish();
		} else {
			System.exit(0);
		}
	}

	public static void setMinScale(boolean bMin) {
		sbConfig_MinScale = bMin;
	}

	// 计算初始缩放比例
	public static void checkScale() {
		try {
			final Display display = sActivity.getWindowManager()
					.getDefaultDisplay();
			
			// 按竖屏的模式计算宽高比
			int w = display.getWidth();
			int h = display.getHeight();
			if (sbConfig_MinScale && w > h) {
				int tmp = w;
				w = h;
				h = tmp;
			}

			final float scale;
			float scale_w, scale_h;
			scale_w = 1.0f * w / Gmud.WQX_ORG_WIDTH;
			scale_h = 1.0f * h / Gmud.WQX_ORG_HEIGHT;
			if (scale_w < scale_h) {
				scale = scale_w;
			} else {
				scale = scale_h;
			}
			int s = (int) scale;
			Video.resetScale(s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void bind(Activity activity) {
		sActivity = activity;
		
		checkScale();
	}

	public static void unbind(Activity activity) {
		sActivity = null;
	}

	public Gmud(Context context) {
		
		Running = true;
		
		sContext = context;

		Res.init(context);
		Video.VideoInit(1);

		new GmudMain(context).start();
	}
}
