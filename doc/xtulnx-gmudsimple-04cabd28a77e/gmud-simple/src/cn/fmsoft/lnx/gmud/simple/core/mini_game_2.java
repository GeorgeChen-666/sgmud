package cn.fmsoft.lnx.gmud.simple.core;

import android.graphics.Bitmap;

public class mini_game_2 {

	static int score = 0;
	static Bitmap up = null;  //up
	static Bitmap down = null;  //down 
	static Bitmap left = null;  //left
	static Bitmap right = null;  //right
	static Bitmap center = null;  //center
	static int dir = 0;
	static int char_dir = -1;

//	extern Image* gmud_loadimage(int);
//	extern void GmudDelay(unsigned int);
//	extern int RandomInt(int);

	static void draw()
	{
		int i = 160 - 4;
		int k = 80 - 4;
		int l = 24;
		int i1 = 78;
		Video.VideoClear();
//		wchar_t sc[10];
//		_itow(score, sc, 10);
//		wchar_t str[20];
//		wcscpy(str,L"score:");
		String str = String.format("score:%d", score);
		Video.VideoDrawRectangle(2, 2, 156, 76);
		Video.VideoDrawLine(2 + i1, l + 2, 157, l + 2);
		Video.VideoDrawRectangle(i1 + 2, 2, i1 + 2, 78);
		Video.VideoDrawString(str/*wcscat(str, sc)*/, 6, 6);
		if (dir >= 0)
		{
			int j1 = i1 + 4 + dir * (i1 / 5);
			switch (dir)
			{
			case 0: // '\0'
				Video.VideoDrawImage(left, j1, 5);
				break;

			case 1: // '\001'
				Video.VideoDrawImage(up, j1, 5);
				break;

			case 2: // '\002'
				Video.VideoDrawImage(down, j1, 5);
				break;

			case 3: // '\003'
				Video.VideoDrawImage(right, j1, 5);
				break;
			}
		}
		int k1 = 16;
		/////////
	Bitmap p = Res.loadimage(78);
		if (char_dir >= 0)
			switch (char_dir)
			{
			case 0: // '\0'
				Video.VideoDrawImage(p, i1 + 16, 60);
				break;

			case 1: // '\001'
				Video.VideoDrawImage(p, i1 + 16 + k1, 60 - k1 - 16);
				break;

			case 2: // '\002'
				Video.VideoDrawImage(p, i1 + 16 + k1, 60);
				break;

			case 3: // '\003'
				Video.VideoDrawImage(p, i1 + 16 + k1 + k1, 60);
				break;
			}
		else
			Video.VideoDrawImage(p, i1 + 16 + k1, 60);
		Video.VideoDrawImage(center, 16, 20);
		Video.VideoDrawLine(i1 + 16, 76, i1 + 16 + k1 + k1 + k1, 76);
//	DeleteObject(p);
		p.recycle();
		p = null;
	}

	static void GameMain()
	{
		score = 0;
		up = Res.loadimage(253);
		down = Res.loadimage(251);
		left = Res.loadimage(252);
		right = Res.loadimage(254);
		center = Res.loadimage(250);
		draw();
		Video.VideoUpdate();
		Input.ClearKeyStatus();
		int i = 0;
		int awarded = 0;
		while(Input.Running)
		{
			Input.ProcessMsg();
			if((Input.inputstatus & Input.kKeyExit)!=0)
				break;
			if ((Input.inputstatus & Input.kKeyLeft)!=0)
			{
				if (dir == 0 && awarded == 0)
				{
					awarded = 1;
					score += 3;
				}
				char_dir = 0;
			} else
			if ((Input.inputstatus & Input.kKeyUp)!=0)
			{
				if (dir == 1 && awarded == 0)
				{
					awarded = 1;
					score += 3;
				}
				char_dir = 1;
			} else
			if ((Input.inputstatus & Input.kKeyDown)!=0)
			{
				if (dir == 2 && awarded == 0)
				{
					awarded = 1;
					score += 3;
				}
				char_dir = 2;
			} else
			if ((Input.inputstatus & Input.kKeyRight)!=0)
			{
				if (dir == 3 && awarded == 0)
				{
					awarded = 1;
					score += 3;
				}
				char_dir = 3;
			}
			if (i > 600)
			{
				int k = util.RandomInt(4);
				if(k == dir)
					dir += util.RandomInt(7);
				else
					dir = k;
				dir %= 4;
				i = 0;
				awarded = 0;
			}
			draw();
			Video.VideoUpdate();
			Gmud.GmudDelay(20);
			Input.ClearKeyStatus();
			Gmud.GmudDelay(40);
			i += 60;
			char_dir = -1;
		}
//		DeleteObject(up);
//		DeleteObject(down);
//		DeleteObject(left);
//		DeleteObject(right);
//		DeleteObject(center);
		up.recycle();
		up=null;
		down.recycle();
		down=null;
		left.recycle();
		left=null;
		right.recycle();
		right=null;
		center.recycle();
		center=null;
	}
}
