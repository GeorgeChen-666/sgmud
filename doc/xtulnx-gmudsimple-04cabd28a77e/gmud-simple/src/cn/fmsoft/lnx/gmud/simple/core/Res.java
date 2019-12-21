/**
 * Copyright (C) 2011, FMSoft.LNX.Gmud
 */

package cn.fmsoft.lnx.gmud.simple.core;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Res {
	static final String STR_YOU_STRING = "你";
	static final String STR_NO_INNER_KONGFU_STRING = "你必须选择你要用的内功心法.";
	static final String STR_FP_PLUS_LIMIT_STRING = "你目前的加力上限为%d";
	
	static final String sText[] = new String[] {
		"0.txt",
		"1.txt",
		"2.txt",
		"3.txt",
		"4.txt",
		"5.txt",
	};
	
	/* image id: 244~254 */
	static final String sImage[] = new String[] {
		"PNG/hp.png",
		"PNG/mp.png",
		"PNG/fp.png",
		"PNG/hit.png",
		"PNG/num.png",
		"PNG/bd.png",
		"PNG/ctr.png",
		"PNG/d.png",
		"PNG/l.png",
		"PNG/u.png",
		"PNG/r.png",
	};
	
	static final String sData[] = new String [] {
		"MapElem.dat",
		"MapEvent.dat",
		"NPCSkill.dat",
	};
	
//	private static Context mContext;
	private static AssetManager mAssetManager;
	
	
	static public void init(Context context) {
//		mContext = context;
		mAssetManager = context.getAssets();
	}
	
	//id:0-物品描述 1-人物描述 2-招式描述 3-绝招描述 4-伤害描述 5-对话
	static String readtext(int id, int startpoint, int endpoint) {
		
		if (id < 0 || id > 5) {
			return "";
		}

		final int length = endpoint - startpoint;
		
		final String fileName = sText[id];
		
		try {
			InputStream is = mAssetManager.open(fileName, AssetManager.ACCESS_RANDOM);
			is.skip(startpoint);
			byte[] data = new byte[length + 0];
			is.read(data, 0, length);
			is.close();

			final String text = new String(data, "utf8");

			return text;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}

	static Bitmap loadimage(int id) {
		if (id < 0 || id > 255) {
			return null;
		}

		Bitmap image;
		
		String fileName;
		if (id >= 244) {
			fileName = sImage[id - 244];
		} else {
			fileName = String.format("PNG/%d.png", id);
		}
        
		try {
			InputStream is = mAssetManager.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			image = null;
		}
		
		return image;
	}

	// id:0-map元素 1-map事件 2-招式
	static byte[] getarraydata(int id, int startpoint, int endpoint) {
		if (id < 0 || id > 3) {
			return null;
		}

		final int length = endpoint - startpoint;
		final String fileName = sData[id];
		try {
			InputStream is = mAssetManager.open(fileName, AssetManager.ACCESS_RANDOM);
			is.skip(startpoint);
			byte[] b = new byte[length];
			is.read(b, 0, length);
			is.close();

			// System.arraycopy(value, offset, buffer, 0, count);
			
			return b;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	static int[] convert(byte[] buff, int startpoint, int endpoint) {		
		final int size = (endpoint-startpoint+3)>>2;
		final int[] out = new int [size];
	
		int i, j;
		for (i = startpoint, j = 0; i < endpoint; i += 4, j++) {
//			out[j] = ((buff[i] & 0xff) << 24) | ((buff[i + 1] & 0xff) << 16)
//					| ((buff[i + 2] & 0xff) << 8) | (buff[i + 3] & 0xff);
			out[j] = ((buff[i+3] & 0xff) << 24) | ((buff[i + 2] & 0xff) << 16)
			| ((buff[i + 1] & 0xff) << 8) | (buff[i + 0] & 0xff);
		}
		
		return out;
	}
}
