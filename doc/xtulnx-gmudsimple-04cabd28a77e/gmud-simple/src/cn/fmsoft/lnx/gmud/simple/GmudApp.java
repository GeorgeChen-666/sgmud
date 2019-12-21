package cn.fmsoft.lnx.gmud.simple;

import android.app.Application;
import cn.fmsoft.lnx.gmud.simple.core.Gmud;

public class GmudApp extends Application {

	@Override
	public void onCreate() {
//		VMRuntime.getRuntime().setMinimumHeapSize(4 * 1024 * 1024);
		super.onCreate();
		
		new Gmud(getBaseContext());
	}

	/**
	 * There's no guarantee that this function is ever called.
	 */
	@Override
	public void onTerminate() {
		super.onTerminate();

	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
}
