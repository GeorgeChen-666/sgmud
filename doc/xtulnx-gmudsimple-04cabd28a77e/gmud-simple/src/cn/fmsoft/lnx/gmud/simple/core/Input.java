/**
 * Copyright (C) 2011, FMSoft.GMUD.
 * 
 * Input: collect all input(key) messages. And, it is the main thread at background.
 * 
 * @author nxliao
 */

package cn.fmsoft.lnx.gmud.simple.core;

import android.view.KeyEvent;

public class Input {

	static final int kKeyExit = 1;
	static final int kKeyEnt = 2;
	static final int kKeyDown = 4;
	static final int kKeyLeft = 8;
	static final int kKeyUp = 16;
	static final int kKeyRight = 32;
	static final int kKeyPgUp = 64;
	static final int kKeyPgDn = 128;
	static final int kKeyFly = 256;

	public static boolean Running = false;
	
	static int inputstatus = 0;
	static int lastkey = 0;
	static int SetKeyFlag = -1;
	static byte kbkeys[] = new byte[9];
	static byte setKeytp[] = new byte[9];

	/**
	 * 初始化输入
	 */
	static void InitInput() {
		Running = true;
		GmudDefaultKey();
		ClearKeyStatus();
	}
	
	static void ProcessMsg() {
		// MSG msg;
		// Running = GetMessage(&msg, NULL, 0, 0);
		// TranslateMessage(&msg);
		// DispatchMessage(&msg);
		// if(Running <= 0)
		// {
		// Running = 0;
		// exit(0);
		// }
		return;
	}
	
	static synchronized void Stop() {
		Running = false;
	}

	static synchronized void ClearKeyStatus() {
		inputstatus = 0;
	}

	static void GmudDefaultKey() {
		kbkeys[0] = KeyEvent.KEYCODE_W;
		kbkeys[1] = KeyEvent.KEYCODE_S;
		kbkeys[2] = KeyEvent.KEYCODE_DPAD_LEFT;
		kbkeys[3] = KeyEvent.KEYCODE_DPAD_RIGHT;
		kbkeys[4] = KeyEvent.KEYCODE_A; // Delete 左连
		kbkeys[5] = KeyEvent.KEYCODE_D; // PGDN 右连
		kbkeys[6] = KeyEvent.KEYCODE_E; // enter 输入
		kbkeys[7] = KeyEvent.KEYCODE_Q; // alt跳出
		kbkeys[8] = KeyEvent.KEYCODE_F; // End 轻功
	}
	
	static public synchronized void GmudSetKey(int mask) {
		inputstatus = mask;
	}

	static synchronized void GmudProcessKey(int key) {
		int id = 0;
		while (id < 9) {
			if (key == kbkeys[id])
				break;
			id++;
		}
		boolean changed = (key != lastkey);
		lastkey = key;
		if (changed) 
		{
			switch (id) {
			case 0:
				inputstatus = kKeyUp;
				return;
			case 1:
				inputstatus = kKeyDown;
				return;
			case 2:
				inputstatus = kKeyLeft;
				return;
			case 3:
				inputstatus = kKeyRight;
				return;
			case 4:
				inputstatus = kKeyPgUp|kKeyLeft;
				return;
			case 5:
				inputstatus = kKeyPgDn|kKeyRight;
				return;
			case 6:
				inputstatus = kKeyEnt;
				return;
			case 7:
				inputstatus = kKeyExit;
				return;
			case 8:
				inputstatus = kKeyFly;
				return;
			}
		}
	}

	public static boolean onKey(int keyCode, KeyEvent event) {
		
		if (!Running) {
			return false;
		}
		
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
//			if (keyCode==KeyEvent.KEYCODE_Q) {
//				Gmud.sPlayer.potential += 1000;
//			} else if (keyCode==KeyEvent.KEYCODE_W) {
//				Gmud.sPlayer.exp+=2000;
//			}
			
			GmudProcessKey(keyCode);
			if ((inputstatus & (kKeyDown|kKeyUp|kKeyLeft|kKeyRight))!=0) {
//				lastkey = 0;
//				inputstatus = 0;
			}
		} else if (event.getAction() == KeyEvent.ACTION_UP) {
			lastkey = 0;
		}
		return false;
	}
	

}
