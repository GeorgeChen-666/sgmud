package cn.fmsoft.lnx.gmud.simple.core;

import android.graphics.Bitmap;

public class mini_game_1 {
	static int b = 0;
	static int x = 0;
	static int y = 0;
	static int score = 0;
	static Bitmap bd = null;

	// extern Image* gmud_loadimage(int);
	// extern void GmudDelay(unsigned int);
	// extern int RandomInt(int);

	static void draw_process() {
//		int i = 13;
//		
//		Video.VideoDrawRectangle(2, 2, 156, 76);
//		Video.VideoDrawRectangle(8, 8 + i, 40, 8);
//		Video.VideoDrawArc(28 + b, 12 + i, 4);
//		Video.VideoFillArc(28 + b, 12 + i, 2);
//		Video.VideoDrawLine(5, 75, 155, 75);
	}
	
	static void draw() {
		int i = 13;
		Video.VideoClear();
		// wchar_t sc[10];
		// _itow(score, sc, 10);
		// wchar_t str[20];
		// wcscpy(str,L"score:");
		String str = String.format("score:%d", score);
		Video.VideoDrawRectangle(2, 2, 156, 76);
		Video.VideoDrawString(str/* wcscat(str, sc) */, 8, 4);
		Video.VideoDrawRectangle(8, 8 + i, 40, 8);
		Video.VideoDrawArc(28 + b, 12 + i, 4);
		Video.VideoFillArc(28 + b, 12 + i, 2);
		Video.VideoDrawLine(5, 75, 155, 75);

		Bitmap p = Res.loadimage(78);
		Video.VideoDrawImage(p, 48, 80 - 5 - 16); // draw player

		Video.VideoDrawImage(bd, 120, 80 - 60 - 5);
		Video.VideoDrawArc(48 + 16 + x, 80 - 9 - y, 4);
		Video.VideoFillArc(48 + 16 + x, 80 - 9 - y, 2);

		p.recycle();
		// DeleteObject(p);
	}

	static void GameMain() {
		boolean flag = true;
		int dir = -1;
		bd = Res.loadimage(249);
		score = 0;
		draw();
		Video.VideoUpdate();
		Input.ClearKeyStatus();
		while (Input.Running) {
			Input.ProcessMsg();
			if ((Input.inputstatus & Input.kKeyExit) != 0)
				break;
			if ((Input.inputstatus & Input.kKeyEnt) != 0) {
				if (flag) {
					if (b != 0) {
						b = 0;
						dir = -1;
						draw();
						Video.VideoUpdate();
					} else {
						flag = false;
					}
				} else if (!flag) {
					flag = true;
					dir = -1;
					if (b == 0 || b == 1) {
						score += 15; // hit!s
						while (y < 30) {
							y += util.RandomInt(2);
							x++;
							draw();
							Video.VideoUpdate();
							Gmud.GmudDelay(15);
						}
						while (y < 50) {
							y++;
							x += util.RandomInt(2);
							draw();
							Video.VideoUpdate();
							Gmud.GmudDelay(15);
						}
						while (x < 59) {
							y--;
							x++;
							draw();
							Video.VideoUpdate();
							Gmud.GmudDelay(15);
						}
						while (y > 5) {
							y--;
							draw();
							Video.VideoUpdate();
							Gmud.GmudDelay(15);
						}
						y = x = 0;
					}
				}
			}
			if (!flag) {
				if (b > -16 && b < 16)
					if (dir > 0)
						b += util.RandomInt(3);
					else
						b -= util.RandomInt(3);
				else {
					if (b <= -16)
						b += util.RandomInt(3);

					if (b >= 16)
						b -= util.RandomInt(3);
					dir = -dir;
				}
				draw();
				Video.VideoUpdate();
			}
			Input.ClearKeyStatus();
			Gmud.GmudDelay(20);
		}
		// DeleteObject(bd);
		bd.recycle();
		bd = null;
	}
}
