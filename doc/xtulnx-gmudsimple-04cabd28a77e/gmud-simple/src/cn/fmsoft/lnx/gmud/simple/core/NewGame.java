package cn.fmsoft.lnx.gmud.simple.core;

import android.graphics.Bitmap;

public class NewGame {
	static final String Title = "　　 *英雄坛说*";
	static final String Begin_words[] = new String[] {
	 "　　在这件事没有发生",
	 "之前，我一直在平静地",
	 "生活着；如果这件事没",
	 "有发生，那么我依然会",
	 "这样平静甚至有点单调",
	 "地继续生活下去。可是",
	 "最终它还是发生了，就",
	 "是现在，在我按下按钮",
	 "的时候，我启动了远古",
	 "大陆英雄坛的时空转换",
	  "装置。当我从时空扭曲",
	  "的强大力场中恢复知觉",
	  "的时候，我已经来到了",
	  "这片传说中神秘的土地",
	  "。这里无法考证年代，",
	  "我所来到的地方好像是",
	  "中原偏西的位置，一个",
	  "不大不小的小镇，镇上",
	  "行人混杂，还算热闹。",
	  "从他们的交谈中我得知",
	  "这里叫“平安小镇”。",
	  "而当我注视自己的时候",
	  "才发现，我变成了一个",
	  "十四岁的少年！这里有",
	  "什么秘密？这是早就英",
	  "雄的熔炉或是邪恶的渊",
	  "源？我不知道，我只知",
	  "道，从今以后，我的生",
	  "活将完全改变……    ",
//	  "   By 绝爱仙剑ㄝ宝宝",
//	  "       QQ：156692474",
	  "",
	  "",
	  "",
	  "",
	  "",
	  ""
	};

	static final String Attrib_names[] = new String [] { 
	"膂力", "敏捷", "根骨", "悟性"};

	Bitmap charactor[] = new Bitmap[3];
	String selecttip;
	int points[] = new int[4];

	void ShowStory()
	{
		int i = 80;
		int j = 0;
		int a = 0;
		while(Input.Running && j < 31)
		{
			Input.ProcessMsg();
			if((Input.inputstatus & Input.kKeyEnt)!=0)
				break;
			if((Input.inputstatus & Input.kKeyExit)!=0)
				break;
			Input.ClearKeyStatus();
			j = a;
			i--;
			if(i < -(34 * 14))
				break;

			Gmud.GmudDelay(60);
			Video.VideoClearRect(0, 16, 160, 80);
			for(a = 0;a < 31;a++)
			{
				if(a*16+i > 1)
					break;
			}
			Video.VideoDrawStringSingleLine(Begin_words[a], 1, i + a * 16, 1);
			Video.VideoDrawStringSingleLine(Begin_words[a + 1], 1, i + (1+a) * 16, 1);
			Video.VideoDrawStringSingleLine(Begin_words[a + 2], 1, i + (2+a) * 16, 1);
			Video.VideoDrawStringSingleLine(Begin_words[a + 3], 1, i + (3+a) * 16, 1);
			Video.VideoDrawStringSingleLine(Begin_words[a + 4], 1, i + (4+a) * 16, 1);

			Video.VideoClearRect(0, 0, 160, 16);
			Video.VideoDrawStringSingleLine(Title,0,0,1);

			Video.VideoUpdate();
			Gmud.GmudDelay(60);
		}
		Video.VideoClear();
		Video.VideoUpdate();
	}

	int SelectChar()
	{
		charactor[0] = Res.loadimage(75);
		charactor[1] = Res.loadimage(81);
		charactor[2] = Res.loadimage(87);
		int id = 0;
		boolean flag = false;
		DrawChar(0);
		Video.VideoUpdate();
		Input.ClearKeyStatus();
		while(Input.Running)
		{
			Input.ProcessMsg();
			if((Input.inputstatus & Input.kKeyEnt)!=0)
			{
				Video.VideoClear();
//				DeleteObject(charactor[0]);
//				DeleteObject(charactor[1]);
//				DeleteObject(charactor[2]);
				Input.ProcessMsg();
				return id;
			}
			if((Input.inputstatus & Input.kKeyLeft)!=0)
			{
				id--;
				id = (id<0?2:id);
				DrawChar(id);
			}
			if((Input.inputstatus & Input.kKeyRight)!=0)
			{
				id++;
				id %= 3;
				DrawChar(id);
			}
			Input.ClearKeyStatus();
			Gmud.GmudDelay(Gmud.FRAME_TIME);
			Video.VideoUpdate();
		}
		return 0;
	}

	void DrawChar(int id)
	{
		Video.VideoClear();
		Video.VideoDrawRectangle(2, 2, 156, 76);
		Video.VideoDrawStringSingleLine("请选择您的人物:", 4, 4);
		Video.VideoDrawImage(charactor[0], 35, 20);
		Video.VideoDrawImage(charactor[1], 64, 20);
		Video.VideoDrawImage(charactor[2], 93, 20);
		Video.VideoDrawRectangle(id * 29 + 33, 18, 20, 20);
	}

	void DrawAlloc(int id)
	{
		Video.VideoClear();
		Video.VideoDrawRectangle(2, 2, 156, 76);
//		wchar_t num_str[4];
//		wchar_t linestr[24];
		for (int i2 = 0; i2 < 4; i2++)
		{
//			memset(linestr, 0, sizeof(wchar_t)*24);
//			_itow(points[i2], num_str, 10);
//			wcscpy(linestr, Attrib_names[i2]);
//			wcscat(linestr, ": < ");
//			wcscat(linestr, num_str);
//			wcscat(linestr, " >");
			String linestr = Attrib_names[i2] + ": < " + points[i2] + " >";
			Video.VideoDrawStringSingleLine(linestr, 24, 10 + i2 * 16);
			if (i2 == id)
				Video.VideoDrawStringSingleLine("→", 10, 10 + i2 * 16);
		}
	}

	void AllocPoint(Player pl)
	{
		points[0] = points[1] = points[2] = points[3] = 20;
		int Remaining = 0;
		int id = 0;
		DrawAlloc(0);
		Video.VideoUpdate();
		Input.ClearKeyStatus();
		while(Input.Running)
		{
			Input.ProcessMsg();
			if((Input.inputstatus & Input.kKeyEnt)!=0)
			{
				if(points[0] + points[1] + points[2] + points[3] == 80)
				{
					pl.pre_force = points[0];
					pl.pre_agility = points[1];
					pl.pre_aptitude = points[2];
					pl.pre_savvy = points[3];
					return;
				}
			} else 
			if((Input.inputstatus & Input.kKeyUp)!=0)
			{
				id--;
				id = (id<0 ? 3 : id);
				DrawAlloc(id);
				Video.VideoUpdate();
			} else 
			if((Input.inputstatus & Input.kKeyDown)!=0)
			{
				id++;
				id %= 4;
				DrawAlloc(id);
				Video.VideoUpdate();
			} else 
			if((Input.inputstatus & Input.kKeyLeft)!=0)
			{
				if (points[id] > 10)
				{
					points[id]--;
					Remaining++;
				}
				DrawAlloc(id);
				Video.VideoUpdate();
			} else 
			if((Input.inputstatus & Input.kKeyRight)!=0)
			{
				if (Remaining > 0 && points[id] < 30)
				{
					Remaining--;
					points[id]++;
				}
				DrawAlloc(id);
				Video.VideoUpdate();
			}
			Input.ClearKeyStatus();
			Gmud.GmudDelay(Gmud.FRAME_TIME);
		}
	}

//	int WINAPI NameDlgProc(HWND hwndDlg,UINT uMsg,WPARAM wParam,LPARAM lParam)
//	{	
//		switch(uMsg)
//		{
//		case WM_INITDIALOG:
//			return (INT_PTR)TRUE;
//		case WM_COMMAND:
//			if(1005 == LOWORD(wParam))
//			{
//				HWND hedit = GetDlgItem(hwndDlg, 1004);
//				GetWindowTextW(hedit, glPlayer->player_name, 16);	
//				if(glPlayer->player_name[0])
//				{
//					EndDialog(hwndDlg, 0);
//					return TRUE;
//				}
//			}
//		}
//		return FALSE;
//	}

//	void EnterName(HWND hwnd)
//	{
//		DialogBox(0, MAKEINTRESOURCE(1003), hwnd, NameDlgProc);
//	}
}
