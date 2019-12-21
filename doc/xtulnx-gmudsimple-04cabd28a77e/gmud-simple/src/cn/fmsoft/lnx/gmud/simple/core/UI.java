package cn.fmsoft.lnx.gmud.simple.core;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UI {
	
	/**
	 * 是否自动[确认]，用于学习技能  */
	static private int auto_confirm;
	
	
	// [8]
	static final String boss_map_name[] = new String[] {"青龙坛", "地罡坛", "朱雀坛", "山岚坛", "玄武坛", "紫煞坛", "天微坛", "白虎坛"};

	static final int boss_map_id[] = new int[] {23, 73, 59, 79, 31, 54, 64, 44};

	static final String old_award_words = "你被奖励了：20点实战经验 10点潜能 50金钱";

	// [199]
	static final int dialog_point[] = new int []{0, 59, 112, 165, 220, 292, 322, 355, 394, 421, 
		475, 551, 580, 631, 688, 742, 799, 829, 884, 926, 
		977, 1028, 1139, 1202, 1253, 1292, 1337, 1382, 1463, 1505, 
		1538, 1595, 1658, 1712, 1781, 1829, 1907, 1952, 2030, 2093, 
		2129, 2201, 2256, 2331, 2379, 2442, 2520, 2595, 2676, 2754, 
		2847, 2901, 2949, 3003, 3063, 3135, 3180, 3267, 3336, 3429, 
		3471, 3536, 3605, 3698, 3731, 3815, 3896, 3941, 4004, 4022, 
		4067, 4094, 4148, 4178, 4232, 4265, 4322, 4370, 4412, 4515, 
		4639, 4767, 4833, 4923, 5113, 5227, 5272, 5356, 5455, 5488, 
		5542, 5636, 5714, 5801, 5892, 6003, 6093, 6129, 6171, 6237, 
		6294, 6396, 6456, 6515, 6557, 6671, 6773, 6827, 6854, 6950, 
		6974, 7061, 7148, 7199, 7325, 7388, 7502, 7559, 7655, 7730, 
		7793, 7883, 7952, 8012, 8033, 8054, 8075, 8096, 8117, 8138, 
		8159, 8180, 8201, 8222, 8243, 8264, 8300, 8354, 8459, 8534, 
		8657, 8687, 8756, 8819, 8951, 9029, 9107, 9167, 9245, 9323, 
		9383, 9479, 9545, 9605, 9668, 9716, 9779, 9845, 9908, 9980, 
		10034, 10103, 10142, 10217, 10331, 10454, 10511, 10574, 10679, 10778, 
		10838, 10931, 10976, 11042, 11117, 11171, 11240, 11297, 11369, 11438, 
		11504, 11603, 11651, 11771, 11822, 11885, 11948, 12011, 12074, 12149, 
		12263, 12377, 12476, 12524, 12620, 12701, 12748, 12832, 12898
	};

	static void DrawDead()
	{
		for (int i1 = 112; i1 < 123; i1++)
		{
			UI.ShowDialog2(i1);
			Video.VideoUpdate();
			Gmud.GmudDelay(1500);
		}
	}

	static void BattleWin(int i1)
	{
		String str = "战斗胜利，大获全胜!\n战斗获得\n金钱：";
//		wchar_t num[8];
//		str += _itow(i1, num, 10);
		str += i1;
		str += "\n物品：";
		int l1;
		for (int j1 = 0; j1 < 5 && (l1 = Battle.sBattle.NPC_item[j1]) != 0; j1++)
		{
			str += Items.item_names[l1];
			str += " ";
		}
		DrawDialog(str);
		Video.VideoUpdate();
		Input.ClearKeyStatus();
		Gmud.GmudDelay(200);
		while(Input.inputstatus != Input.kKeyExit)
			Gmud.GmudDelay(100);
	}

	static void DrawMapTip(String s1)
	{
		int i1 = 12;
		ArrayList<String> as = Video.SplitString(s1, Gmud.WQX_ORG_WIDTH - 3);
		for (int j1 = 0; j1 < as.size();)
		{
			Video.VideoClearRect(0, Gmud.WQX_ORG_WIDTH / 2 - i1 - 4, Gmud.WQX_ORG_WIDTH, i1 + 3);
			Video.VideoDrawRectangle(0, Gmud.WQX_ORG_WIDTH / 2 - i1 - 4, Gmud.WQX_ORG_WIDTH - 1, i1 + 3);
			Video.VideoDrawStringSingleLine(as.get(j1)/*[j1].c_str()*/, 2, Gmud.WQX_ORG_WIDTH / 2 - i1 - 2);
			Video.VideoUpdate();
			j1++;
			Input.ClearKeyStatus();
			while(Input.inputstatus == 0)
				Gmud.GmudDelay(100);
		}
	}

	static void ShowDialog2(int id)
	{
		String s1 = Res.readtext(5, dialog_point[id], dialog_point[1 + id]);
		int j1 = 13;
		Video.VideoFillRectangle(0, 0, 160, 80, 0);
		ArrayList<String> as = Video.SplitString(s1, Gmud.WQX_ORG_WIDTH - 16);
		for (int i2 = 0; i2 < as.size() && i2 < 5; i2++)
			Video.VideoDrawStringSingleLine(as.get(0+i2)/*[0 + i2].c_str()*/, 12, 10 + i2 * j1, 2);

		Video.VideoUpdate();
		Input.ClearKeyStatus();
	}

	static void ShowDialog(int id)
	{
//		wstring s1(gmud_readtext(5, dialog_point[id], dialog_point[1+id]));
		String s1 = Res.readtext(5, dialog_point[id], dialog_point[1+id]);
		DrawDialog(s1);
	}

	static void DrawDialog(String s1)
	{
		int i1 = 13 * 2;
		ArrayList<String> as = Video.SplitString(s1, Gmud.WQX_ORG_WIDTH);
		for (int j1 = 0; j1 < as.size();)
		{
			Video.VideoClearRect(1, 1, Gmud.WQX_ORG_WIDTH - 2, i1);
			Video.VideoDrawRectangle(1, 1, Gmud.WQX_ORG_WIDTH - 2, i1);
			Video.VideoDrawStringSingleLine(as.get(j1)/*[j1].c_str()*/, 1, 2);
			if (++j1 < as.size())
			{
				Video.VideoDrawStringSingleLine(as.get(j1)/*[j1].c_str()*/, 1, 14);
				j1++;
			}
			Video.VideoUpdate();
			Input.ClearKeyStatus();
			Gmud.GmudDelay(300);
			if (j1 < as.size())
				DrawFlashCursor(Gmud.WQX_ORG_WIDTH - 14, i1 - 10, 8);
			else
			{
				while(Input.inputstatus == 0)
					Gmud.GmudDelay(100);
			}
		}
	}

	/**
	 * 绘制一个向下闪烁的光标
	 * @param x
	 * @param y
	 * @param w
	 * @return
	 */
	static int DrawFlashCursor(int x, int y, int w) {
		final int h = w / 2 + 1;
		boolean blink = false;
		int count = 0;
		Input.ClearKeyStatus();
		Input.ProcessMsg();
		while (Input.inputstatus == 0) {
			if (count == 0) {
				if (!blink) {
					Video.VideoDrawArrow(x, y, w, h, 0 + 2 + 0);
	
					// Video.VideoClearRect(x, y, w + 1, w);
					// for (int j2 = 0; j2 < w / 2 + 1; j2++)
					// 	Video.VideoDrawLine(x + j2, y + j2, (x + w) - j2, y + j2);
				} else {
					Video.VideoDrawArrow(x, y, w, h, 0 + 2 + 4);
					// Video.VideoClearRect(x, y, w + 1, w);
				}
				Video.VideoUpdate();
				
				count = 6;
				blink = !blink;
			} else {
				count--;
			}
			Input.ClearKeyStatus();
			Gmud.GmudDelay(50);
		}
		return Input.inputstatus;
	}

	/**
	 * 绘制一个实心向右的箭头
	 * @param x
	 * @param y
	 */
	static void DrawCursor(int x, int y) {
		Video.VideoDrawArrow(x, y, 7, 11, 1+2);
		
//		Video.VideoDrawLine(x, y, x, y + 11);
//		for (int k1 = 0; k1 < 6; k1++)
//			Video.VideoDrawLine(x + k1 + 1, y + k1 + 1, x + k1 + 1, (y + 10) - k1);
	}

	static void DrawMainMenu(int id)
	{
		int j1 = 13 * 2;
		int k1 = 160 - 16;
		byte byte0 = 8;
		if (k1 < 64 + j1 * 4)
		{
			k1 = 160 - 2;
			byte0 = 1;
		}
		int l1 = 13 + j1;
		int i2 = 15;
		Video.VideoClearRect(byte0, 0, k1, i2);
		Video.VideoDrawRectangle(byte0, 0, k1, i2);
		Video.VideoDrawStringSingleLine("查看", byte0 + 11, 2);
		Video.VideoDrawStringSingleLine("物品", byte0 + l1 + 11, 2);
		Video.VideoDrawStringSingleLine("技能", byte0 + 2 * l1 + 11, 2);
		Video.VideoDrawStringSingleLine("功能", byte0 + 3 * l1 + 11, 2);
		int j2 = byte0 + 2 + id * l1;
		int k2;
		if ((k2 = (i2 - 11) / 2) == 0)
			k2 = 2;
		DrawCursor(j2, k2);
	}

	static void MainMenu()
	{
		int j1 = 0;
		DrawMainMenu(0);
		Video.VideoUpdate();
		Input.ClearKeyStatus();
		while(Input.Running)
		{
			Input.ProcessMsg();
			if ((Input.inputstatus & Input.kKeyLeft)!=0)
			{
				if (j1 > 0)
					j1--;
				else
					j1 = 3;
				DrawMainMenu(j1);
				Video.VideoUpdate();
			}
			else
			if ((Input.inputstatus & Input.kKeyRight)!=0)
			{
				if (j1 < 3)
					j1++;
				else
					j1 = 0;
				DrawMainMenu(j1);
				Video.VideoUpdate();
			}
			else
			if((Input.inputstatus & Input.kKeyEnt)!=0)
			{
				if (j1 == 0)
				{
					ViewPlayer();
					Gmud.sMap.DrawMap(-1);
					DrawMainMenu(j1);
					Video.VideoUpdate();
				}
				else
				if (j1 == 1)
				{
					PlayerItem();
					Gmud.sMap.DrawMap(-1);
					DrawMainMenu(j1);
					Video.VideoUpdate();
				}
				else
				if (j1 == 2)
				{
					PlayerSkill();
					Gmud.sMap.DrawMap(-1);
					DrawMainMenu(j1);
					Video.VideoUpdate();
				}
				else
				if (j1 == 3)
				{
					Input.ClearKeyStatus();
					if(SystemMenu() != 0)
					{
						Gmud.sMap.DrawMap(-1);
						Gmud.sMap.DrawMap(-1);
						Video.VideoUpdate();
						return;
					}
					Gmud.sMap.DrawMap(-1);
					Gmud.sMap.DrawMap(-1);
					DrawMainMenu(j1);
					Video.VideoUpdate();
				}
			}
			else
			if((Input.inputstatus & Input.kKeyExit)!=0)
			{
				Gmud.sMap.DrawMap(-1);
				Video.VideoUpdate();
				Gmud.GmudDelay(50);
				return;
			}
			Input.ClearKeyStatus();
			Gmud.GmudDelay(80);
		}
	}

	static void Fly()
	{
		int i1 = -1;
		for (int j1 = 0; j1 < 19; j1++)
			if(GmudData.flyable_map[j1] == Gmud.sMap.map_id)
				i1 = j1;

		if (i1 < 0)  //室内
			return;
		if (Gmud.sPlayer.GetFlySkillLevel() < 30)  //等级不够
			return;
		if (Gmud.sPlayer.fp < 200)
		{
//			wstring aa("你的内力不足，无法施展轻功。");
//			UI.DrawDialog(&aa);
			UI.DrawDialog("你的内力不足，无法施展轻功。");
			return;
		}
		Gmud.sPlayer.fp -= 200;
		int k1 = 0;
		int l1 = 0;
		boolean flag = false;
		DrawFly(0, 0);
		Video.VideoUpdate();
		//*/
		Input.ClearKeyStatus();
		Gmud.GmudDelay(100);
		while (true)
		{
			Input.ProcessMsg();
			if((Input.inputstatus & Input.kKeyUp)!=0)
			{
				if (k1 > 0)
					k1--;
				else
				if (l1 > 0)
				{
					l1--;
				} else
				{
					l1 = 5;
					k1 = 3;
				}
				DrawFly(l1, k1);
				Video.VideoUpdate();
			} else
			if ((Input.inputstatus & Input.kKeyDown)!=0)
			{
				if (k1 < 3)
					k1++;
				else
				if (l1 < 5)
				{
					l1++;
				} else
				{
					l1 = 0;
					k1 = 0;
				}
				DrawFly(l1, k1);
				Video.VideoUpdate();
			}
			else
			{
				if ((Input.inputstatus & Input.kKeyEnt)!=0)
				{
					int j2 = l1 + k1;
					int k2 = GmudData.fly_dest_map[j2];   //read map
					Gmud.sMap.LoadMap(k2);
					Gmud.sMap.stack_pointer = 0;
					Gmud.sMap.SetPlayerLocation(0, 4);
					Gmud.sMap.DrawMap(0);
					Input.ClearKeyStatus();
					return;
				}
				if ((Input.inputstatus & Input.kKeyExit)!=0)
				{
					Gmud.GmudDelay(120);
					return;
				}
			}
			Input.ClearKeyStatus();
			Gmud.GmudDelay(80);
		}
		//*/
	}

	static void DrawSysMenu(int id)
	{
		int j1 = 13;
		int l1 = 39;
		int i2 = 16 + 40;
		int j2 = 13 * 5;
		int k2;
		if ((k2 = 8 + 3 * i2) + l1 >= Gmud.WQX_ORG_WIDTH)
			k2 = Gmud.WQX_ORG_WIDTH - l1 - 2;
		Video.VideoClearRect(k2, 4, l1, j2);
		Video.VideoDrawRectangle(k2, 4, l1, j2);
		for (int i3 = 0; i3 < 5; i3++)
		{
			Video.VideoDrawStringSingleLine(sys_menu_words[i3], k2 + 10, 5 + j1 * i3);
			if (i3 == id)
				DrawCursor(k2 + 2, 5 + j1 * i3);
		}
	}

	static int SystemMenu()
	{
		int j1 = 0;
		DrawSysMenu(0);
		Video.VideoUpdate();
		Input.ClearKeyStatus();
		Gmud.GmudDelay(100);
		while (true)
		{
			Input.ProcessMsg();
			if ((Input.inputstatus & Input.kKeyUp)!=0)
			{
				if (j1 > 0)
					j1--;
				else
					j1 = 4;
				DrawSysMenu(j1);
				Video.VideoUpdate();
			} else
			if ((Input.inputstatus & Input.kKeyDown)!=0)
			{
				if (j1 < 4)
					j1++;
				else
					j1 = 0;
				DrawSysMenu(j1);
				Video.VideoUpdate();
			} else
			if ((Input.inputstatus & Input.kKeyEnt)!=0)
			{
				if (j1 == 0)
				{
					FPMenu();
					return 1;
				}
				if (j1 == 1)
				{
					MPMenu();
					return 1;
				}
				if (j1 == 2)
				{
					PractMenu();
					return 1;
				}
				if (j1 == 3)  //save
				{
					Input.ClearKeyStatus();
					if(task.temp_tasks_data[30] >= 100)
					{
						//save file
						String str = "存档成功!";
						if(Gmud.WriteSave())
							DrawTip(str);
						else
						{
							str = "存档失败!";
							DrawTip(str);
						}
					}
					else
					{
						String str = "请稍后再存";
						DrawTip(str);
					}
					Video.VideoUpdate();
					Gmud.GmudDelay(100);
					while(Input.inputstatus != Input.kKeyExit)
					{
						Input.ProcessMsg();
						Gmud.GmudDelay(100);
					}
					return 0;
				}
				if (j1 == 4)  //exit
				{
					Gmud.GmudDelay(100);
					Input.ClearKeyStatus();
					int l1 = Gmud.WQX_ORG_WIDTH / 2 / 2 - 10;
					String str = "您确定要离开游戏吗?\n([输入]确认 [跳出]放弃)";
					int i2 = DialogBx(str, 10, l1);
					while(true)
					{
						if ((Input.inputstatus & Input.kKeyEnt)!=0)
						{
							if (task.temp_tasks_data[30] > 360)
							{
								Gmud.GmudDelay(100);
								Input.ClearKeyStatus();
								str = "您已经很久没有存档了,存档吗?\n([输入]确认 [跳出]放弃)";
								i2 = DialogBx(str, 10, l1 + 10);
								while (true)
								{
									Input.ProcessMsg();
									if ((i2 & Input.kKeyEnt)!=0)
									{
										Gmud.WriteSave();
										Gmud.exit();
									} else
									if ((i2 & Input.kKeyExit)!=0)
										Gmud.exit();
									i2 = Input.inputstatus;
									Input.ClearKeyStatus();
									Gmud.GmudDelay(100);
								}
							}
							Gmud.exit();
						}
						else
						if ((Input.inputstatus & Input.kKeyExit)!=0)
							break;
						i2 = Input.inputstatus;
						Input.ClearKeyStatus();
						Gmud.GmudDelay(100);	
					}
				}
			} else
			if ((Input.inputstatus & Input.kKeyExit)!=0)
			{
				Gmud.GmudDelay(50);
				return 0;
			}
			Input.ClearKeyStatus();
			Gmud.GmudDelay(80);
		}
	}

	static int DialogBx(String s1, int i1, int j1)
	{
		int k1 = 13;
		int l1 = Gmud.WQX_ORG_WIDTH - 8 - i1;
		ArrayList<String> as;
		int i2 = (as = Video.SplitString(s1, l1 - 4)).size() * k1 + 4;
		Video.VideoClearRect(i1, j1, l1, i2);
		Video.VideoDrawRectangle(i1, j1, l1, i2);
		int j2 = 0;
		for (int l2 = 0; j2 < as.size() && l2 < i2; l2 += k1)
		{
			Video.VideoDrawStringSingleLine(as.get(j2)/*[j2].c_str()*/, i1 + 1, j1 + 1 + j2 * k1);
			j2++;
		}

		Video.VideoUpdate();
		Input.ClearKeyStatus();
		while(Input.inputstatus == 0)
		{
			Gmud.GmudDelay(100);
		}
		return Input.inputstatus;
	}

	static void DrawTip(String s1)
	{
		int i1 = s1.length() * 13;
		int j1 = 13 + 4;
		int k1;
		if ((k1 = (Gmud.WQX_ORG_WIDTH - i1) / 2) < 0)
			k1 = 0;
		int l1;
		if ((l1 = (Gmud.WQX_ORG_WIDTH / 2 - j1) / 2 - 10) < 0)
			l1 = 0;
		Video.VideoClearRect(k1, l1, i1, j1);
		Video.VideoDrawRectangle(k1, l1, i1, j1);
		Video.VideoDrawStringSingleLine(s1, k1 + 2, l1 + 2);
	}

	static void DrawStringFromY(String s1, int i1)
	{
		int j1 = 13;
		int k1 = 41;
		Video.VideoClearRect(0, k1, Gmud.WQX_ORG_WIDTH, j1 * 3);
		Video.VideoDrawString(s1, 0, k1);
	}

	/**
	 * 绘制数字加减对话框，用于 {加力} 等。
	 * @param cur
	 * @param max 最大值，[0-999]
	 * @param x
	 * @param y
	 */
	static void DrawNumberBox(int cur, int max, int x, int y)
	{
		final int num_w = 27; // 数字区的宽度
		final int nw = 6; // 每个数字的宽度
		final int h = 13 + 4; // 总高度，假定小号文本高度固定为 13
		final int w = num_w + 4 + 10; // 总宽度 
		if (cur > max)
			cur = max;
		if (cur < 0)
			cur = 0;
		
		// 绘制外边框
		Video.VideoClearRect(x, y, w, h);
		Video.VideoDrawRectangle(x, y, w, h);
		
		// 分隔线
		Video.VideoDrawLine(x + num_w + 3, y, x + num_w + 3, (y + h));
		Video.VideoDrawLine(x + num_w + 3, y + h / 2, (x + w), y + h / 2);
		
		// 绘制上下箭头
		final int arrow_w = w - 3 - num_w - 4;
		final int arrow_h = h / 2 - 3;
		Video.VideoDrawArrow(x + num_w + 5, (y + h / 2) - 2, arrow_w, -arrow_h, cur > 0 ? 2 : 0);
		Video.VideoDrawArrow(x + num_w + 5, (y + h / 2) + 2, arrow_w, arrow_h, cur < max ? 2 : 0);
		// 		int i3 = w - 3 - num_w - 5
		//		for (int j3 = 0; j3 < i3 / 2 + 1; j3++)
		//		{
		//			Video.VideoDrawLine(x + num_w + 5 + j3, (y + h / 2) - 2 - j3, (x + w) - 2 - j3, (y + h / 2) - 2 - j3);
		//			Video.VideoDrawLine(x + num_w + 5 + j3, y + h / 2 + 2 + j3, (x + w) - 2 - j3, y + h / 2 + 2 + j3);
		//		}
		
		// 右对齐绘制数字
		int k3 = 100;
		boolean flag = false;
		for (int l3 = 0; l3 < 3; l3++)
		{
			int i4;
			if ((i4 = cur / k3) != 0 || l3 == 2 || flag)
			{
				flag = true;
//				wchar_t a[2];
//				_itow(i4, a, 10);
				String a = String.valueOf(i4);
				Video.VideoDrawStringSingleLine(a, x + 3 + (nw + 1) * l3, y + 2);
			}
			cur -= i4 * k3;
			k3 /= 10;
		}
	}

	static void DrawProgressBox(int i1, int j1, int k1, int l1)
	{
		i1 *= 1000;
		j1 *= 1000;
		int j2 = 32;
		int k2 = 8;
		int l2 = (Gmud.WQX_ORG_WIDTH * 2) / 5;
		int i3 = 39;
		int j3;
		if ((j3 = l2 + i3 + 12) > Gmud.WQX_ORG_WIDTH)
			j3 = Gmud.WQX_ORG_WIDTH;
		if (Gmud.WQX_ORG_WIDTH - j3 < 32)
			j2 = Gmud.WQX_ORG_WIDTH - j3;
		int k3 = k2 / 4;
		Video.VideoClearRect(j2, 0, j3, k2 + 2);
		Video.VideoDrawRectangle(j2, k3, l2, k2);
		int l3;
		if (i1 <= l2)
		{
			if (i1 <= 0)
				i1 = 1;
			int i4;
			l3 = (i4 = l2 / i1) * j1;
		} else
		{
			int j4 = i1 / l2;
			if (i1 % l2 > 0)
				j4++;
			if (j4 <= 0)
				j4 = 1;
			l3 = j1 / j4;
		} 
		if (l3 > l2)
			l3 = l2;
		if (j1 >= i1)
			l3 = l2;
		Video.VideoFillRectangle(j2, k3, l3, k2);

//		wchar_t aa[15];
//		wstring num = _itow(k1, aa, 10);
//		num += "/";
//		num +=  _itow(l1, aa, 10);
		String num = String.format("%d/%d", k1, l1);
		Video.VideoDrawNumberData(num, j2 + l2 + 3, k2/2);
	}

	/**
	 * 绘制 <b>GmudTemp.temp_array_20_2</b> 从start起num 个技能列表
	 * @param x x坐标起点
	 * @param y y坐标起点
	 * @param num 技能数量
	 * @param start 技能起点
	 * @param selPos 当前已选中的序号[0,2]
	 * @param drawLevel 是否显示技能等级（如   ×180）	 */
	static void DrawSkillList(int x, int y, int num, int start, int selPos, int drawLevel) {
		if (num <= 0)
			return;
		
		int k2 = 13;
		int l2 = 0;
		int i3 = 0;
		int j3 = 13;
		if (num >= 3)
			l2 = k2 * 3 + 6;
		else
			l2 = num * k2 + 6;
		if (drawLevel == 0)
			i3 = j3 * 5 + 16 + 4;
		else
			i3 = j3 * 7 + 16 + 10;
		Video.VideoClearRect(x, y, i3, l2);
		Video.VideoDrawRectangle(x, y, i3, l2);
		int k3 = (k2 - 12) / 2;
		for (int l3 = 0; l3 < num && l3 < 3; l3++) {
			int i4 = GmudTemp.temp_array_20_2[start + l3][0];
			int j4 = GmudTemp.temp_array_20_2[start + l3][1];
			if (i4 == 255)
				continue;
			Video.VideoDrawArc(x + 4 + 6, y + k3 + 2 + 6 + l3 * (k2 + 1), 6);
			if (l3 == selPos)
				Video.VideoFillArc(x + 4 + 6, y + k3 + 2 + 6 + l3 * (k2 + 1),
						4);
			String s1 = Skill.skill_name[i4];
			if (drawLevel != 0) {
				s1 += "×";
				// wchar_t num[4];
				// s1 += _itow(j4, num, 10);
				s1 += String.valueOf(j4);
			}
			Video.VideoDrawStringSingleLine(s1/* .c_str() */, x + 4 + 16, y + 2
					+ l3 * (k2 + 1));
		}
	}
	
	
	//*********** UI-Player ************//
	static final String player_menu_words[] = new String[] {"状态", "描述", "属性", "婚姻"};

	static final String item_menu_words[] = new String[] {"食物", "药物", "武器", "装备", "其它", "接收"};

	static final String useitem_menu_words[] = new String[] {"使用", "丢弃", "发送"};

	static final String skill_menu_words[] = new String[] {"拳脚", "兵刃", "轻功", "内功", "招架", "知识", "法术"};

	static final String sys_menu_words[] = new String[] {"内力", "法力", "练功", "存档", "结束"};

	static final String fp_menu_words[] = new String[] {"打坐", "加力", "吸气", "疗伤"};

	static final String mp_menu_words[] = new String[] {"冥思", "法点"};

	static final String player_attrib_menu_words[] = new String[] {"膂力", "敏捷", "根骨", "悟性"};

	static void DrawViewPlayer(int id)
	{
		int j1 = 160 - 8;
		int k1 = 80 - 4;
		Video.VideoClearRect(4, 2, j1, k1);
		Video.VideoDrawRectangle(4, 2, j1, k1);
		int l1 = 13 * 2;
		int i2 = 13;
		Video.VideoDrawStringSingleLine(player_menu_words[id], 25, 5);
		for (int j2 = 0; j2 < 4; j2++)
			if (j2 != id)
				Video.VideoDrawRectangle(32 + l1 + j2 * 14, (i2 - 10) / 2 + 5, 8, 8);
			else
				Video.VideoFillRectangle(32 + l1 + j2 * 14, (i2 - 10) / 2 + 5, 9, 9, 0);

		switch(id)
		{
		case 0:
			DrawPlayerStatus();
			return;
		case 1:
			DrawPlayerDesc();
			return;
		case 2:
			DrawPlayerAttrib();
			return;
		case 3:
			DrawPlayerMerry();
			break;
		}
	}

	static void ViewPlayer()
	{
		int j1 = 0;
		DrawViewPlayer(0);
		Video.VideoUpdate();
		Input.ClearKeyStatus();
		Gmud.GmudDelay(100);
		while(Input.Running)
		{
			Input.ProcessMsg();
			Video.VideoUpdate();
			if ((Input.inputstatus & Input.kKeyLeft)!=0)
			{
				if (j1 > 0)
					j1--;
				else
					j1 = 3;
				DrawViewPlayer(j1);
				Video.VideoUpdate();
			}
			else
			if ((Input.inputstatus & Input.kKeyRight)!=0)
			{
				if (j1 < 3)
					j1++;
				else
					j1 = 0;
				DrawViewPlayer(j1);
				Video.VideoUpdate();
			}
			else
			{
				if ((Input.inputstatus & Input.kKeyEnt)!=0)
				{
					Gmud.GmudDelay(100);
					return;
				}
				if ((Input.inputstatus & Input.kKeyExit)!=0)
				{
					Gmud.GmudDelay(100);
					return;
				}
			}
			Input.ClearKeyStatus();
			Gmud.GmudDelay(80);
		}
	}

	static void DrawPlayerStatus()
	{
		/*/
		wchar_t number[10];
		wstring as[6];
		int i = 0;
		as[i] = "食物:";
		as[i] += _itow(Gmud.sPlayer.food, number, 10);
		as[i] += "/";
		as[i] += _itow(Gmud.sPlayer.GetFoodMax(), number, 10);
		if(0 >= Gmud.sPlayer.mp)
			i++;
		as[i] += "饮水:";
		as[i] += _itow(Gmud.sPlayer.water, number, 10);
		as[i] += "/";
		as[i] += _itow(Gmud.sPlayer.GetWaterMax(), number, 10);

		i++;
		int i1;
		if ((i1 = (Gmud.sPlayer.hp_max * 1000) / ((Gmud.sPlayer.hp_full * 1000) / 100)) == 100 && Gmud.sPlayer.hp_max < Gmud.sPlayer.hp_full)
			i1 = 99;
		as[i] = "生命:";
		as[i] += _itow(Gmud.sPlayer.hp, number, 10);
		as[i] += "/";
		as[i] += _itow(Gmud.sPlayer.hp_max, number, 10);
		as[i] += "(";
		as[i] += _itow(i1, number, 10);
		as[i] += "%)";
		i++;
		as[i] = "内力:";
		as[i] += _itow(Gmud.sPlayer.fp, number, 10);
		as[i] += "/";
		as[i] += _itow(Gmud.sPlayer.fp_level, number, 10);
		as[i] += "(+";
		as[i] += _itow(Gmud.sPlayer.fp_plus, number, 10);
		as[i] += ")";
		i++;
		if(0 >= Gmud.sPlayer.mp)
		{
			as[i] = "经验:";
			as[i] += _itow(Gmud.sPlayer.exp, number, 10);
			as[i] += " 潜能:";
			as[i] += _itow(Gmud.sPlayer.potential, number, 10);
			as[++i] = "";
		}
		else
		{
			as[i] = "法力:"; 
			as[i] += _itow(Gmud.sPlayer.mp, number, 10);
			as[i] += "/";
			as[i] += _itow(Gmud.sPlayer.mp_level, number, 10);
			as[i] += "(+";
			as[i] += _itow(Gmud.sPlayer.mp_plus, number, 10);
			as[i] += ")";
			i++;
			as[i] = "经验:";
			as[i] += _itow(Gmud.sPlayer.exp, number, 10);
			as[i] += " 潜能:";
			as[i] += _itow(Gmud.sPlayer.potential, number, 10);
		}
		/*/
		
		String as[] = new String[6];
		int i = 0;
		
		as[i] = String.format("食物:%d/%d", Gmud.sPlayer.food, Gmud.sPlayer.GetFoodMax());
		if(0 >= Gmud.sPlayer.mp) {
			i++;
			as[i] = "";
		}
		as[i] += String.format("饮水:%d/%d", Gmud.sPlayer.water, Gmud.sPlayer.GetWaterMax());
		i++;
		
		int i1;
		if ((i1 = (Gmud.sPlayer.hp_max * 1000) / ((Gmud.sPlayer.hp_full * 1000) / 100)) == 100 && Gmud.sPlayer.hp_max < Gmud.sPlayer.hp_full)
			i1 = 99;
		as[i] = String.format("生命:%d/%d(%d%%)", Gmud.sPlayer.hp, Gmud.sPlayer.hp_max, i1);
		i++;
		
		as[i] = String.format("内力:%d/%d(+%d)", Gmud.sPlayer.fp, Gmud.sPlayer.fp_level, Gmud.sPlayer.fp_plus);
		i++;
		
		if(0 >= Gmud.sPlayer.mp)
		{
			as[i] = String.format("经验:%d 潜能:%d", Gmud.sPlayer.exp, Gmud.sPlayer.potential);
			i++;
			
			as[i] = "";
		}
		else
		{
			as[i] = String.format("法力:%d/%d(+%d)", Gmud.sPlayer.mp, Gmud.sPlayer.mp_level, Gmud.sPlayer.mp_plus);
			i++;

			as[i] = String.format("经验:%d 潜能:%d ", Gmud.sPlayer.exp, Gmud.sPlayer.potential);
			i++;
			
			as[i] = "";
		}
		
		for (int k1 = 0; k1 < 6; k1++)
			Video.VideoDrawStringSingleLine(as[k1]/*.c_str()*/, 8, 17 + k1 * 12);
	}

	static void DrawPlayerDesc()
	{
		//*/
//		wchar_t number[5];
//		wstring as[5];
		String as[] = new String[5];
		int i1;
		if ((i1 = Gmud.sPlayer.sex) < 0 || i1 > 1)
			i1 = 0;
		as[0] = "[";
		as[0] += GmudData.class_name[Gmud.sPlayer.class_id];
		as[0] += "]";
		as[0] += Gmud.sPlayer.player_name;

		as[1] = "你是一位";
//		as[1] += _itow(14 + Gmud.sPlayer.GetAge(), number, 10);
		as[1] += 14 + Gmud.sPlayer.GetAge();
		as[1] += "岁的";
		as[1] += i1 != 0 ? "女性" : "男性";

		int j1;
		if ((j1 = Gmud.sPlayer.GetFaceLevel()) < 0)
			as[2] = "你看起来一脸稚气";
		else
		{
			as[2] = "你长得";
			as[2] += GmudData.face_level_name[j1][i1];
			as[2] += ",";
			as[2] += GmudData.face_level_name[j1 + 1][i1];
		}
		as[3] = "武艺看起来";
		as[3] += GmudData.level_name[Gmud.sPlayer.GetPlayerLevel() / 5];

		as[4] = "出手似乎";
		as[4] += GmudData.attack_level_name[Gmud.sPlayer.GetAttackLevel()];

		for (int l1 = 0; l1 < 5; l1++)
			Video.VideoDrawStringSingleLine(as[l1]/*.c_str()*/, 8, 17 + l1 * 12);
		//*/
	}

	static void DrawPlayerAttrib()
	{
//		wchar_t number[10];
//		wstring as[5];
		String as[] = new String[5];
		as[0] = "金钱:";
//		as[0] += _itow(Gmud.sPlayer.money, number, 10);
		as[0] += Gmud.sPlayer.money;

		as[1] = "膂力  [";
//		as[1] += _itow(Gmud.sPlayer.GetForce(), number, 10);
		as[1] += Gmud.sPlayer.GetForce();
		as[1] += "/";
//		as[1] += _itow(Gmud.sPlayer.pre_force, number, 10);
		as[1] += Gmud.sPlayer.pre_force;
		as[1] += "]";

		as[2] = "敏捷  [";
//		as[2] += _itow(Gmud.sPlayer.GetAgility(), number, 10);
		as[2] += Gmud.sPlayer.GetAgility();
		as[2] += "/";
//		as[2] += _itow(Gmud.sPlayer.pre_agility, number, 10);
		as[2] += Gmud.sPlayer.pre_agility;
		as[2] += "]";

		as[3] = "根骨  [";
//		as[3] += _itow(Gmud.sPlayer.GetAptitude(), number, 10);
		as[3] += Gmud.sPlayer.GetAptitude();
		as[3] += "/";
//		as[3] += _itow(Gmud.sPlayer.pre_aptitude, number, 10);
		as[3] += Gmud.sPlayer.pre_aptitude;
		as[3] += "]";
		
		as[4] = "悟性  [";
//		as[4] += _itow(Gmud.sPlayer.GetSavvy(), number, 10);
		as[4] += Gmud.sPlayer.GetSavvy();
		as[4] += "/";
//		as[4] += _itow(Gmud.sPlayer.pre_savvy, number, 10);
		as[4] += Gmud.sPlayer.pre_savvy;
		as[4] += "]";

		for (int j1 = 0; j1 < 5; j1++)
			Video.VideoDrawStringSingleLine(as[j1]/*.c_str()*/, 8, 18 + j1 * 12);
	}

	static void DrawPlayerMerry()
	{
		Video.VideoDrawStringSingleLine(Gmud.sPlayer.GetConsortName()/*.c_str()*/, 8, 18);
	}

	static void DrawFly(int i1, int j1)
	{
		if (i1 > 5)
			return;
		int k1 = 13;
		int l1 = k1 * 4;
		int i2 = 13;
		int j2 = i2 * 4;
		int k2 = 4;
		int l2 = 4;
		Video.VideoClearRect(4, 4, j2, l1);
		Video.VideoDrawRectangle(4, 4, j2, l1);
		int i3 = 0;
		int j3;
		if ((j3 = (k1 - 12) / 2) < 0)
			j3 = 0;
		for (; i3 < 4; i3++)
		{
			Video.VideoDrawStringSingleLine(GmudData.map_name[i3 + i1], k2 + 10, (int)(4 + 1 + i3 * 12.5));
			if (i3 == j1)
				DrawCursor(k2 + 3, l2 + 1 + i3 * k1 + j3);
		}
	}

	static int DrawDeleteItem()
	{
		int i1 = 26;
		int j1 = 16 + i1;
		int k1 = i1 + 6;
		int l1 = j1 + k1 + 10;
		int i2 = 5 + i1 + 16;
		String s1 = "确认删除吗？";
		int j2 = 13 * 5;
		int k2 = 13;
		Video.VideoClearRect(l1, i2, j2, k2);
		Video.VideoDrawStringSingleLine(s1/*.c_str()*/, l1, i2);
		Input.ClearKeyStatus();
		Video.VideoUpdate();
		Gmud.GmudDelay(100);
		while (true)
		{
			Input.ProcessMsg();
			if((Input.inputstatus & Input.kKeyEnt)!=0)
			{
				Gmud.GmudDelay(80);
				return 1;
			}
			if ((Input.inputstatus & Input.kKeyExit)!=0)
			{
				Gmud.GmudDelay(80);
				return 0;
			}
			Input.ClearKeyStatus();
			Gmud.GmudDelay(80);
		}
//		return 0;
	}

	static void DrawList(String as[], int i1, int j1, int k1, int l1, int i2, int j2)
	{
		int length = (j2 == 0) ? 6 : 7;
		int k2 = 12;
		int l2 = 26;
		int i3 = 32 + l2;
		int j3 = Gmud.WQX_ORG_WIDTH - i3 * 2;
		int k3 = k2 * 5;
		int l3 = l2 + 6;
		if (j3 < l2 * 4 + l2 / 2)
			j3 = l2 * 4 + l2 / 2;
		if ((i3 = Gmud.WQX_ORG_WIDTH - j3 - 4) < 0)
			i3 = 0;
		Video.VideoClearRect(i3, 4, j3, k3 + 2);
		Video.VideoDrawRectangle(i3, 4, j3, k3 + 2);
		Video.VideoDrawLine(i3 + l3, 4, i3 + l3, ((5 + k3) - 1) + 1);
		if (i1 + j1 <= length - 1)
		{
			int k4;
			for (int i4 = 0; i4 < 5 && (k4 = i4 + i1) <= length - 1; i4++)
				if (i4 == j1)
				{
					Video.VideoFillRectangle(i3 + 3, 5 + i4 * k2, l2, k2);
					Video.VideoDrawStringSingleLine(as[k4], i3 + 3, 5 + i4 * k2, 2);
				} else
				{
					Video.VideoDrawStringSingleLine(as[k4], i3 + 3, 5 + i4 * k2);
				}
		}
		for (int j4 = 0; j4 < 5; j4++)
		{
			int l4 = GmudTemp.temp_array_32_2[j4 + k1][0];
			int i5 = GmudTemp.temp_array_32_2[j4 + k1][1];
			if (l4 == 255)
				return;
			int j5 = (k2 - 10) / 2;
			Video.VideoDrawRectangle(i3 + l3 + 4, j4 * k2 + 5 + 3 + j5, 5, 5);
			if (i5 == 1)
			{
				Video.VideoDrawStringSingleLine("√", i3 + l3, j4 * k2 + 3 + j5);
				/*
				Video.VideoDrawLine(i3 + l3 + 4 + 4, j4 * k2 + 5 + 3 + j5 + 9, i3 + l3 + 8 + 4, j4 * k2 + 5 + 2 + 4 + 9);
				Video.VideoDrawLine(i3 + l3 + 4 + 4, j4 * k2 + 5 + 3 + j5 + 9, i3 + l3 + 8 + 13, j4 * k2 + 5 + 2 + 4);
				*/
			}
			if (i2 == 1 && l1 == j4)
				Video.VideoFillRectangle(i3 + l3 + 4, j4 * k2 + 5 + 3 + j5, 5, 5);
			if (j2 == 0)
			{
				int k5 = Gmud.sPlayer.item_package[l4][0];
				String s1 = Items.item_names[k5];
				if (Items.item_repeat[k5] == 1)
				{
					s1 += " ×";
					s1 += Gmud.sPlayer.item_package[l4][2];
				}
				Video.VideoDrawStringSingleLine(s1/*.c_str()*/, i3 + l3 + 12, j4 * k2 + 4 + 2);
			} else
			{
				int l5 = Gmud.sPlayer.skills[l4][0];
				Video.VideoDrawStringSingleLine(Skill.skill_name[l5], i3 + l3 + 12, j4 * k2 + 4 + 2);
			}
		}
	}

	static void PlayerItem()
	{
		int j1 = 0;
		int m = 0;
		int n = 0;
		int i2 = 0;
		int j2 = 0;
		int k2 = Gmud.sPlayer.CopyItemData(-1, 0);
		int k3 = 12;
		ArrayList<String> as = null;
		int l3 = 0;
		int i4 = 0;
		int k1 = k2 & 0xff;
		DrawList(item_menu_words, 0, 0, 0, 0, 0, 0);
		Video.VideoUpdate();
		Input.ClearKeyStatus();
		Gmud.GmudDelay(100);
		while(true)
		{
			Input.ProcessMsg();
			if ((Input.inputstatus & Input.kKeyUp)!=0)
			{
				if (j1 == 0)
				{
					if (m > 0)
						m--;
					else
					if (n > 0)
					{
						n--;
					} else
					{
						n = 1;
						m = 4;
					}
					int l2;
					k1 = (l2 = Gmud.sPlayer.CopyItemData(-1, m + n)) & 0xff;
					i2 = 0;
					j2 = 0;
					DrawList(item_menu_words, n, m, 0, 0, j1, 0);
				}
				else
				{
					if (j2 > 0)
						j2--;
					else
					if (i2 > 0)
					{
						i2--;
					} else
					{
						if ((i2 = k1 - 5) < 0)
							i2 = 0;
						j2 = 4;
						if (4 > k1 - 1)
							j2 = k1 - 1;
					}
					DrawList(item_menu_words, n, m, i2, j2, j1, 0);
					int j4 = GmudTemp.temp_array_32_2[i2 + j2][0];
					l3 = (as = Video.SplitString(Items.GetItemDesc(Gmud.sPlayer.item_package[j4][0]), Gmud.WQX_ORG_WIDTH - 13)).size();
					i4 = 0;
					if (l3 > 0)
					{
						Video.VideoClearRect(0, Gmud.WQX_ORG_WIDTH / 2 - k3, Gmud.WQX_ORG_WIDTH, k3);
						if (l3 == 1)
							Video.VideoDrawStringSingleLine(as.get(0)/*.c_str()*/, 2, Gmud.WQX_ORG_WIDTH / 2 - k3);
						else
						{
//							wstring ss(as[0].c_str());
							String ss = as.get(0);
							ss += " >";
							Video.VideoDrawStringSingleLine(ss/*.c_str()*/, 2, Gmud.WQX_ORG_WIDTH / 2 - k3);
						}	
					}
				}
				Video.VideoUpdate();
			}
			else
			if ((Input.inputstatus & Input.kKeyDown)!=0)
			{
				if (j1 == 0)
				{
					if (m < 4)
						m++;
					else
					if (n < 1)
						n++;
					else
						n = m = 0;
					int i3;
					k1 = (i3 = Gmud.sPlayer.CopyItemData(-1, m + n)) & 0xff;
					i2 = 0;
					j2 = 0;
					DrawList(item_menu_words, n, m, 0, 0, j1, 0);
				}
				else
				{
					if (j2 < 4 && i2 + j2 < k1 - 1)
						j2++;
					else
					if (i2 < k1 - 5)
						i2++;
					else
						i2 = j2 = 0;
					DrawList(item_menu_words, n, m, i2, j2, j1, 0);
					int k4 = GmudTemp.temp_array_32_2[i2 + j2][0];
					l3 = (as = Video.SplitString(Items.GetItemDesc(Gmud.sPlayer.item_package[k4][0]), Gmud.WQX_ORG_WIDTH - 13)).size();
					i4 = 0;
					if (l3 > 0)
					{
						Video.VideoClearRect(0, Gmud.WQX_ORG_WIDTH / 2 - k3, Gmud.WQX_ORG_WIDTH, k3);
						if (l3 == 1)
							Video.VideoDrawStringSingleLine(as.get(0)/*[0].c_str()*/, 2, Gmud.WQX_ORG_WIDTH / 2 - k3);
						else
						{
//							wstring ss(as[0].c_str());
							String ss = as.get(0);
							ss += " >";
							Video.VideoDrawStringSingleLine(ss/*.c_str()*/, 2, Gmud.WQX_ORG_WIDTH / 2 - k3);
						}
					}
				}
				Video.VideoUpdate();
			}
			else
			if ((Input.inputstatus & Input.kKeyRight)!=0)
			{
				if (j1 != 0 && l3 > 1 && i4 < l3 - 1)
				{
					i4++;
					Video.VideoClearRect(0, Gmud.WQX_ORG_WIDTH / 2 - k3, Gmud.WQX_ORG_WIDTH, k3);
					if (i4 < l3 - 1)
					{
//						wstring ss(as[i4].c_str());
						String ss = as.get(i4);
						ss += " >";
						Video.VideoDrawStringSingleLine(ss/*.c_str()*/, 2, Gmud.WQX_ORG_WIDTH / 2 - k3);
					}
					else
						Video.VideoDrawStringSingleLine(as.get(i4)/*.c_str()*/, 2, Gmud.WQX_ORG_WIDTH / 2 - k3);
					Video.VideoUpdate();
				}
			}
			else
			if ((Input.inputstatus & Input.kKeyExit)!=0)
				if (j1 != 0)
				{
					j1 = 0;
					DrawList(item_menu_words, n, m, i2, j2, 0, 0);
					Video.VideoClearRect(0, Gmud.WQX_ORG_WIDTH / 2 - k3, Gmud.WQX_ORG_WIDTH, k3);
					Video.VideoUpdate();
					Input.ClearKeyStatus();
				} else
				{
					Gmud.GmudDelay(200);
					Input.ClearKeyStatus();
					return;
				}
			if ((Input.inputstatus & Input.kKeyEnt)!=0)
			{
				if (j1 == 0 && k1 > 0)
					j1 = 1;
				else
				if (j1 != 0)
				{
					ItemMenu(i2, j2);
					int j3;
					k1 = (j3 = Gmud.sPlayer.CopyItemData(-1, m + n)) & 0xff;
					j1 = 0;
					Video.VideoClearRect(0, Gmud.WQX_ORG_WIDTH / 2 - k3, Gmud.WQX_ORG_WIDTH, k3);
				}
				i2 = j2 = 0;
				DrawList(item_menu_words, n, m, 0, 0, j1, 0);
				if (k1 > 0 && j1 != 0)
				{
					int l4 = GmudTemp.temp_array_32_2[0][0];
					l3 = (as = Video.SplitString(Items.GetItemDesc(Gmud.sPlayer.item_package[l4][0]), Gmud.WQX_ORG_WIDTH - 13)).size();
					i4 = 0;
					if (l3 > 0)
					{
						Video.VideoClearRect(0, Gmud.WQX_ORG_WIDTH / 2 - 13, Gmud.WQX_ORG_WIDTH, 13);
						if (l3 == 1)
							Video.VideoDrawStringSingleLine(as.get(0)/*.c_str()*/, 2, Gmud.WQX_ORG_WIDTH / 2 - k3);
						else
						{
//							wstring ss(as[0].c_str());
							String ss = as.get(0);
							ss += " >";
							Video.VideoDrawStringSingleLine(ss/*.c_str()*/, 2, Gmud.WQX_ORG_WIDTH / 2 - k3);
						}
					}
				}
				Video.VideoUpdate();
			}
			Input.ClearKeyStatus();
			Gmud.GmudDelay(80);
		}
	}

	static void DrawItemMenu(int i1)
	{
		int j1 = 12;
		int k1 = 26;
		int l1 = 32;
		int i2 = k1 + 10;
		int j2 = j1 * 3;
		int k2 = k1 + 6;
		int l2 = l1 + k2 + k1;
		int i3 = 5 + k1;
		Video.VideoClearRect(l2, i3, i2, j2 + 2);
		Video.VideoDrawRectangle(l2, i3, i2, j2 + 2);
		int j3 = (j1 - 10) / 2;
		for (int k3 = 0; k3 < 3; k3++)
		{
			Video.VideoDrawRectangle(l2 + 2, i3 + k3 * j1 + j3 + 3, 5, 5);
			if (i1 == k3)
				Video.VideoFillRectangle(l2 + 2, i3 + k3 * j1 + j3 + 3, 5, 5);
			Video.VideoDrawStringSingleLine(useitem_menu_words[k3], l2 + 10, i3 + k3 * j1 + 1);
		}
	}

	static void ItemMenu(int i1, int j1)
	{
		int l1 = 0;
		DrawItemMenu(0);
		Video.VideoUpdate();
		Input.ClearKeyStatus();
		Gmud.GmudDelay(100);
		while (true)
		{
			Input.ProcessMsg();
			if ((Input.inputstatus & Input.kKeyUp)!=0)
			{
				if (l1 == 0)
					l1 = 2;
				else
					l1--;
				DrawItemMenu(l1);
				Video.VideoUpdate();
			} else
			if ((Input.inputstatus & Input.kKeyDown)!=0)
			{
				if (l1 == 2)
					l1 = 0;
				else
					l1++;
				DrawItemMenu(l1);
				Video.VideoUpdate();
			} else
			{
				if ((Input.inputstatus & Input.kKeyEnt)!=0)
				{
					if (l1 == 0)
					{
						String s1;  //use item
						if ((s1 = Gmud.sPlayer.UseItem(GmudTemp.temp_array_32_2[i1 + j1][0])).length() > 0)
						{
							int l2 = 13;
							Video.VideoClearRect(0, Gmud.WQX_ORG_WIDTH / 2 - l2, Gmud.WQX_ORG_WIDTH, l2);
							Video.VideoDrawStringSingleLine(s1/*.c_str()*/, 2, Gmud.WQX_ORG_WIDTH / 2 - l2);
							Video.VideoUpdate();
							Gmud.GmudDelay(100);
							Input.ClearKeyStatus();
							while(Input.inputstatus == 0)
								Gmud.GmudDelay(100);
						}
						return;
					}
					if (l1 == 1) {
						int j2 = DrawDeleteItem(); // delete item
						if (j2 == 1) {
							Gmud.sPlayer.DeleteOneItem(GmudTemp.temp_array_32_2[i1 + j1][0]);
						}
						return;
					}
					if (l1 == 2) {
						// TODO send item
						DrawTip("不能发送!");
						break;
					}
				}
				if ((Input.inputstatus & Input.kKeyExit)!=0)
					return;
			}
			Input.ClearKeyStatus();
			Gmud.GmudDelay(80);
		} 
	}

	static void PlayerSkill()
	{
		int i1 = 13;
		boolean flag = false;
		int k1 = 0;
		int l1 = 26;
		int i2 = 32 + l1 + 2;
		int k2 = 0;
		int l2 = 0;
		int i3 = 0;
		int j3 = 0;
		int k3;
		int j2 = (k3 = Gmud.sPlayer.CopySkillData(-1, 0)) & 0xff;
		DrawList(skill_menu_words, 0, 0, 0, 0, 0, 1);
		Video.VideoUpdate();
		Input.ClearKeyStatus();
		Gmud.GmudDelay(100);
		do
		{
			Input.ProcessMsg();
			if ((Input.inputstatus & Input.kKeyUp)!=0)
			{
				if (k1 == 0)
				{
					if (l2 > 0)
						l2--;
					else
					if (k2 > 0)
					{
						k2--;
					} else
					{
						k2 = 2;
						l2 = 4;
					}
					int l3;
					j2 = (l3 = Gmud.sPlayer.CopySkillData(-1, k2 + l2)) & 0xff;
					i3 = 0;
					j3 = 0;
					DrawList(skill_menu_words, k2, l2, 0, 0, k1, 1);
					Video.VideoUpdate();
				} else
				{
					if (j3 > 0)
						j3--;
					else
					if (i3 > 0)
					{
						i3--;
					} else
					{
						if ((i3 = j2 - 5) < 0)
							i3 = 0;
						j3 = 4;
						if (4 > j2 - 1)
							j3 = j2 - 1;
					}
					DrawList(skill_menu_words, k2, l2, i3, j3, k1, 1);
					int k4 = i3 + j3;
					int k5 = Gmud.sPlayer.skills[GmudTemp.temp_array_32_2[k4][0]][2];
					int k6;
					if ((k6 = Gmud.sPlayer.skills[GmudTemp.temp_array_32_2[k4][0]][1]) > 255)
						k6 = 255;
					if (k6 < 0)
						k6 = 0;
					String s1 = GmudData.level_name[k6 / 5];
					Video.VideoClearRect(0, Gmud.WQX_ORG_WIDTH / 2 - i1, Gmud.WQX_ORG_WIDTH, 12);
//					wchar_t num[15];
					s1 += " ";
//					s1 += _itow(k6, num, 10);
					s1 += k6;
					s1 += " /";
//					s1 += _itow(k5, num, 10);
					s1 += k5;
					Video.VideoDrawStringSingleLine(s1/*.c_str()*/, 35, Gmud.WQX_ORG_WIDTH / 2 - i1);
					Video.VideoUpdate();
				}
			} else
			if ((Input.inputstatus & Input.kKeyDown)!=0)
			{
				if (k1 == 0)
				{
					if (l2 < 4)
						l2++;
					else
					if (k2 < 2)
						k2++;
					else
						k2 = l2 = 0;
					int i4;
					j2 = (i4 = Gmud.sPlayer.CopySkillData(-1, k2 + l2)) & 0xff;
					i3 = 0;
					j3 = 0;
					DrawList(skill_menu_words, k2, l2, 0, 0, k1, 1);
					Video.VideoUpdate();
				} else
				{
					if (j3 < 4 && i3 + j3 < j2 - 1)
						j3++;
					else
					if (i3 < j2 - 5)
						i3++;
					else
						i3 = j3 = 0;
					DrawList(skill_menu_words, k2, l2, i3, j3, k1, 1);
					int l4 = i3 + j3;
					int l5 = Gmud.sPlayer.skills[GmudTemp.temp_array_32_2[l4][0]][2];
					int l6;
					if ((l6 = Gmud.sPlayer.skills[GmudTemp.temp_array_32_2[l4][0]][1]) > 255)
						l6 = 255;
					if (l6 < 0)
						l6 = 0;
					String s2 = GmudData.level_name[l6 / 5];
					Video.VideoClearRect(0, Gmud.WQX_ORG_WIDTH / 2 - i1, Gmud.WQX_ORG_WIDTH, 12);
					s2 += " ";
//					wchar_t num[15];
//					s2 += _itow(l6, num, 10);
					s2 += l6;
					s2 += " /";
//					s2 += _itow(l5, num, 10);
					s2 += l5;
					Video.VideoDrawStringSingleLine(s2/*.c_str()*/, 35, Gmud.WQX_ORG_WIDTH / 2 - i1);
					Video.VideoUpdate();
				}
			} else
			if ((Input.inputstatus & Input.kKeyExit)!=0)
			{
				if (k1 != 0)
				{
					k1 = 0;
					DrawList(skill_menu_words, k2, l2, i3, j3, 0, 1);
					Video.VideoClearRect(0, Gmud.WQX_ORG_WIDTH / 2 - i1, Gmud.WQX_ORG_WIDTH, i1);
					Video.VideoUpdate();
					Gmud.GmudDelay(200);
					Input.ClearKeyStatus();
				} else
				{
					Gmud.GmudDelay(200);
					Input.ClearKeyStatus();
					return;
				}
			} else
			if ((Input.inputstatus & Input.kKeyEnt)!=0)
			{
				if (k1 == 0 && j2 > 0)
					k1 = 1;
				else
				if (k1 != 0)
				{
					Gmud.GmudDelay(200);
					int i5 = i3 + j3;
					int i6 = Gmud.sPlayer.skills[GmudTemp.temp_array_32_2[i5][0]][0];
					int i7;
					if ((i7 = GmudTemp.temp_array_32_2[i5][1]) == 0)
						Gmud.sPlayer.SelectSkill(i6, k2 + l2);
					else
						Gmud.sPlayer.UnselectSkill(k2 + l2);
					int j4;
					j2 = (j4 = Gmud.sPlayer.CopySkillData(-1, l2 + k2)) & 0xff;
					k1 = 0;
					i3 = j3 = 0;
					Video.VideoClearRect(0, Gmud.WQX_ORG_WIDTH / 2 - i1, Gmud.WQX_ORG_WIDTH, i1);
					if(Battle.sBattle!=null)
						Battle.sBattle.c();
				}
				DrawList(skill_menu_words, k2, l2, i3, j3, k1, 1);
				if (j2 > 0 && k1 != 0)
				{
					int j5 = i3 + j3;
					int j6 = Gmud.sPlayer.skills[GmudTemp.temp_array_32_2[j5][0]][2];
					int j7;
					if ((j7 = Gmud.sPlayer.skills[GmudTemp.temp_array_32_2[j5][0]][1]) > 255)
						j7 = 255;
					if (j7 < 0)
						j7 = 0;
					String s3 = GmudData.level_name[j7 / 5];
					Video.VideoClearRect(0, Gmud.WQX_ORG_WIDTH / 2 - i1, Gmud.WQX_ORG_WIDTH, 12);
//					wchar_t num[15];
					s3 += " ";
//					s3 += _itow(j7, num, 10);
					s3 += j7;
					s3 += " /";
//					s3 += _itow(j6, num, 10);
					s3 += j6;
					Video.VideoDrawStringSingleLine(s3/*.c_str()*/, 35, Gmud.WQX_ORG_WIDTH / 2 - i1);
				}
				Video.VideoUpdate();
			}
			Input.ClearKeyStatus();
			Gmud.GmudDelay(80);
		} while (true);
	}

	static void DrawFPMenu(int id)
	{
		int j1 = 13;
		int l1 = 39;
		int i2 = 16 + 40;
		int j2 = j1 * 4 + 2;
		int k2;
		if ((k2 = 8 + 3 * i2) + l1 > Gmud.WQX_ORG_WIDTH)
			k2 = Gmud.WQX_ORG_WIDTH - l1 - 2;
		Video.VideoClearRect(k2 - 5, 15, l1, j2);
		Video.VideoDrawRectangle(k2 - 5, 15, l1, j2);
		int l2 = (j1 - 8) / 2;
		for (int i3 = 0; i3 < 4; i3++)
		{
			Video.VideoDrawStringSingleLine(UI.fp_menu_words[i3], k2 + 5, 17 + j1* i3);
			if (i3 == id)
				DrawCursor(k2 - 3, 16 + j1 * i3 + l2);
		}
	}

	static void FPMenu()
	{
		int j1 = 0;
		Gmud.sMap.DrawMap(-1);
		DrawFPMenu(0);
		Video.VideoUpdate();
		Input.ClearKeyStatus();
		Gmud.GmudDelay(100);
		String s1;
		while (true) {
			Input.ProcessMsg();
			if ((Input.inputstatus & Input.kKeyUp) != 0) {
				if (j1 > 0)
					j1--;
				else
					j1 = 3;
				DrawFPMenu(j1);
				Video.VideoUpdate();
			} else if ((Input.inputstatus & Input.kKeyDown) != 0) {
				if (j1 < 3)
					j1++;
				else
					j1 = 0;
				DrawFPMenu(j1);
				Video.VideoUpdate();
			} else if ((Input.inputstatus & Input.kKeyExit) != 0) {
				Input.ClearKeyStatus();
				Gmud.GmudDelay(50);
				return;
			} else if ((Input.inputstatus & Input.kKeyEnt) != 0) {
				if (j1 == 0) {
					if (RecoverFP() == 1)
						return;
				} else if (j1 == 1) {
					if (FPPlusMenu() == 1)
						return;
				} else if (j1 == 2) {
					s1 = Gmud.sPlayer.Breathing();
					DrawStringFromY(s1, 660);
					Video.VideoUpdate();
					Gmud.GmudDelay(1500);
				} else if (j1 == 3) {
					if ((s1 = Gmud.sPlayer.Recovery()).substring(0, 3) == "你摧动")
					{
						String str="你全身放松，坐下来开始运功疗伤！";
						DrawStringFromY(str, 660);
						Video.VideoUpdate();
						Gmud.GmudDelay(1500);
						DrawStringFromY(s1, 660);
						Video.VideoUpdate();
						Gmud.GmudDelay(1500);
					}
					DrawStringFromY(s1, 660);
					Video.VideoUpdate();
					Gmud.GmudDelay(1500);
				}
				Gmud.sMap.DrawMap(-1);
				DrawFPMenu(j1);
				Video.VideoUpdate();
			}
			Input.ClearKeyStatus();
			Gmud.GmudDelay(80);
		}
	}


	static int RecoverFP()
	{
		Gmud.GmudDelay(100);
		int j1 = Gmud.sPlayer.GetFPSpeed();  //get speed
		int k1 = 0;
	    while (k1 != 3)
		{
			Input.ProcessMsg();
			while(true)
			{
				Input.ProcessMsg();
				DrawProgressBox(Gmud.sPlayer.fp_level * 2, Gmud.sPlayer.fp, Gmud.sPlayer.fp, Gmud.sPlayer.fp_level);
				Video.VideoUpdate();
				if ((Input.inputstatus & Input.kKeyExit)!=0)
				{
					Gmud.GmudDelay(150);
					return 0;
				}
				if ((k1 = Gmud.sPlayer.Meditation()) != 0)
					break;
				Input.ClearKeyStatus();
				Gmud.GmudDelay(600 / j1);
			}
			if (k1 == 1)
			{
				String str = "你必须选择你要用的内功心法";
				DrawStringFromY(str, 660);
				Input.ClearKeyStatus();
				Video.VideoUpdate();
				Gmud.GmudDelay(1500);
				return 0;
			}
			if (k1 == 2)
			{
				String str = "你的内功等级不够";
				DrawStringFromY(str, 660);
				Input.ClearKeyStatus();
				Video.VideoUpdate();
				Gmud.GmudDelay(1500);
				return 0;
			}
		}
		String str="你的内功等级不够";
		DrawStringFromY(str, 660);
		Input.ClearKeyStatus();
		Video.VideoUpdate();
		Gmud.GmudDelay(1500);
		return 0;
	}

	static int FPPlusMenu()
	{
		if(255 == Gmud.sPlayer.select_skills[3])
		{
			String str = "你必须选择你要用的内功心法";
			DrawStringFromY(str, 660);
			Video.VideoUpdate();
			Input.ClearKeyStatus();
			Gmud.GmudDelay(1500);
			return 0;
		}
		int i1 = Gmud.sPlayer.GetPlusFPMax();
		int j1;
		int k1 = j1 = Gmud.sPlayer.fp_plus;
		boolean flag = false;
		int i2 = 26;
		int j2 = 16 + i2;
		int k2 = (8 + 3 * j2) - i2 - 14 - 32;
		DrawNumberBox(j1, i1, k2, 60);
		Video.VideoUpdate();
		Input.ClearKeyStatus();
		Gmud.GmudDelay(100);
		while(true)
		{
			Input.ProcessMsg();
			if((Input.inputstatus & Input.kKeyUp)!=0)
			{
				if (j1 > 0)
					j1--;
				DrawNumberBox(j1, i1, k2, 60);
				Video.VideoUpdate();
				Gmud.GmudDelay(50);
			} else
			if ((Input.inputstatus & Input.kKeyLeft)!=0)
			{
				j1+=10;
				if (j1 > i1)
					j1 = i1;
				DrawNumberBox(j1, i1, k2, 60);
				Video.VideoUpdate();
				Gmud.GmudDelay(50);
			} else
			if ((Input.inputstatus & Input.kKeyRight)!=0)
			{
				if (j1 > 10)
					j1-=10; 
				else 
					j1 = 0;
				DrawNumberBox(j1, i1, k2, 60);
				Video.VideoUpdate();
				Gmud.GmudDelay(50);
			} else
			if ((Input.inputstatus & Input.kKeyDown)!=0)
			{
				if (j1 < i1)
					j1++;
				DrawNumberBox(j1, i1, k2, 60);
				Video.VideoUpdate();
				Gmud.GmudDelay(50);
			}
			if ((Input.inputstatus & Input.kKeyEnt)!=0)
			{
				Gmud.sPlayer.fp_plus = j1;
				if (j1 == i1)
				{
					String str = "你目前的加力上限为";
//					wchar_t nu[4];
//					str += _itow(i1, nu, 10);
					str += i1;
					DrawStringFromY(str, 660);
					Input.ClearKeyStatus();
					Video.VideoUpdate();
					Gmud.GmudDelay(1500);
				} else
				{
					Gmud.GmudDelay(50);
				}
				return 0;
			}
			if ((Input.inputstatus & Input.kKeyExit)!=0)
			{
				if (i1 == k1)
				{
					String str = "你目前的加力上限为";
//					wchar_t nu[4];
//					str += _itow(i1, nu, 10);
					str += i1;
					DrawStringFromY(str, 660);
					Input.ClearKeyStatus();
					Video.VideoUpdate();
					Gmud.GmudDelay(1500);
				} else
				{
					Input.ClearKeyStatus();
					Gmud.GmudDelay(50);
				}
				return 0;
			}
			Input.ClearKeyStatus();
			Gmud.GmudDelay(80);
		}
//		return 0;
	}

	static int RecoverMP()
	{
		Gmud.GmudDelay(100);
		int j1 = Gmud.sPlayer.GetMPSpeed();  //get speed
		int k1 = 0;
		while(k1 != 3)
		{
			Input.ProcessMsg();
			while(true)
			{
				Input.ProcessMsg();
				DrawProgressBox(Gmud.sPlayer.mp_level * 2, Gmud.sPlayer.mp, Gmud.sPlayer.mp, Gmud.sPlayer.mp_level);
				Video.VideoUpdate();
				Input.ProcessMsg();
				if ((Input.inputstatus & Input.kKeyExit)!=0)
					return 0;
				if ((k1 = Gmud.sPlayer.Think()) != 0)
					break;
				Input.ClearKeyStatus();
				Gmud.GmudDelay(600 / j1);
			}
			if (k1 == 1)
			{
				String str = "你必须选择你要用的法术";
				DrawStringFromY(str, 660);
				Video.VideoUpdate();
				Input.ClearKeyStatus();
				Gmud.GmudDelay(1500);
				return 0;
			}
			if (k1 == 2)
			{
				String str = "你的法术等级不够";
				DrawStringFromY(str, 660);
				Video.VideoUpdate();
				Input.ClearKeyStatus();
				Gmud.GmudDelay(1500);
				return 0;
			}
		}
		String str = "你的法术等级不够";
		DrawStringFromY(str, 660);
		Video.VideoUpdate();
		Input.ClearKeyStatus();
		Gmud.GmudDelay(1500);
		return 0;
	}

	static int MPPlusMenu()
	{
		if (255 == Gmud.sPlayer.select_skills[6])
		{
			String str = "你必须选择你要用的法术";
			DrawStringFromY(str, 660);
			Video.VideoUpdate();
			Input.ClearKeyStatus();
			Gmud.GmudDelay(1500);
			return 0;
		}
		int i1 = Gmud.sPlayer.GetPlusMPMax();
		int j1;
		int k1 = j1 = Gmud.sPlayer.mp_plus;
		int i2 = 26;
		int j2 = 16 + i2;
		int k2 = (8 + 3 * j2) - i2 - 14 - 32;
		DrawNumberBox(j1, i1, k2, 60);
		Video.VideoUpdate();
		Input.ClearKeyStatus();
		Gmud.GmudDelay(100);
		while(true)
		{
			Input.ProcessMsg();
			if ((Input.inputstatus & Input.kKeyUp)!=0)
			{
				if (j1 > 0)
					j1--;
				DrawNumberBox(j1, i1, k2, 60);
				Video.VideoUpdate();
				Gmud.GmudDelay(50);
			} else
			if ((Input.inputstatus & Input.kKeyLeft)!=0)
			{
				j1+=10;
				if (j1 > i1)
					j1 = i1;
				DrawNumberBox(j1, i1, k2, 60);
				Video.VideoUpdate();
				Gmud.GmudDelay(50);
			} else
			if ((Input.inputstatus & Input.kKeyRight)!=0)
			{
				if (j1 > 10)
					j1-=10; 
				else 
					j1 = 0;
				DrawNumberBox(j1, i1, k2, 60);
				Video.VideoUpdate();
				Gmud.GmudDelay(50);
			} else
			if((Input.inputstatus & Input.kKeyDown)!=0)
			{
				if (j1 < i1)
					j1++;
				DrawNumberBox(j1, i1, k2, 60);
				Video.VideoUpdate();
				Gmud.GmudDelay(50);
			}
			if((Input.inputstatus & Input.kKeyEnt)!=0)
			{
				Gmud.sPlayer.mp_plus = j1;
				if (j1 == i1)
				{
					String str = "你目前的法点上限为";
//					wchar_t nu[4];
//					str += _itow(i1, nu, 10);
					str += i1;
					DrawStringFromY(str, 660);
					Video.VideoUpdate();
					Gmud.GmudDelay(1500);
				} else
				{
					Gmud.GmudDelay(50);
				}
				return 0;
			}
			if((Input.inputstatus & Input.kKeyExit)!=0)
			{
				if (i1 == k1)
				{
					String str = "你目前的法点上限为";
//					wchar_t nu[4];
//					str += _itow(i1, nu, 10);
					str += i1;
					DrawStringFromY(str, 660);
					Video.VideoUpdate();
					Gmud.GmudDelay(1500);
				} else
				{
					Gmud.GmudDelay(50);
				}
				return 0;
			}
			Input.ClearKeyStatus();
			Gmud.GmudDelay(80);
		}
//		return 0;
	}

	static void DrawMPMenu(int id)
	{
		int j1 = 13;
		int l1 = 39;
		int i2 = 11 + 40;
		int j2 = j1 * 2 + 2;
		int k2;
		if ((k2 = 8 + 3 * i2) + l1 > Gmud.WQX_ORG_WIDTH)
			k2 = Gmud.WQX_ORG_WIDTH - l1 - 2;
		Video.VideoClearRect(k2 - 5, 20, l1, j2);
		Video.VideoDrawRectangle(k2 - 5, 20, l1, j2);
		int l2 = (j1 - 8) / 2;
		for (int i3 = 0; i3 < 2; i3++)
		{
			Video.VideoDrawStringSingleLine(mp_menu_words[i3], k2 + 4, 22 + j1 * i3);
			if (i3 == id)
				DrawCursor(k2 - 3, 20 + j1 * i3 + l2);
		}
	}

	static void MPMenu()
	{
		int j1 = 0;
		Gmud.sMap.DrawMap(-1);
		DrawMPMenu(0);
		Video.VideoUpdate();
		Input.ClearKeyStatus();
		Gmud.GmudDelay(100);
		while(true)
		{
			Input.ProcessMsg();
			if ((Input.inputstatus & Input.kKeyUp)!=0)
			{
				if (j1 > 0)
					j1--;
				else
					j1 = 1;
				DrawMPMenu(j1);
				Video.VideoUpdate();
				Gmud.GmudDelay(50);
			} else
			if ((Input.inputstatus & Input.kKeyDown)!=0)
			{
				if (j1 < 1)
					j1++;
				else
					j1 = 0;
				DrawMPMenu(j1);
				Video.VideoUpdate();
				Gmud.GmudDelay(50);
			} else
			if ((Input.inputstatus & Input.kKeyEnt)!=0)
			{
				int k1;
				if (j1 == 0)
				{
					if ((k1 = RecoverMP()) == 1)
						return;
				} else
				if (j1 == 1 && (k1 = MPPlusMenu()) == 1)
					return;
				Gmud.sMap.DrawMap(-1);
				DrawMPMenu(j1);
				Video.VideoUpdate();
			} else
			if ((Input.inputstatus & Input.kKeyExit)!=0)
			{
				Gmud.GmudDelay(50);
				return;
			}
			Input.ClearKeyStatus();
			Gmud.GmudDelay(80);
		}
	}
	
	/**
	 * 自练技能菜单
	 */
	static void PractMenu()
	{
		// 计算可用于练习的技能数量
		final int i1 = Gmud.sPlayer.GetPracticeSkillNumber();
		if (i1 == 0)
			return;
		
		int j1;
		int k1 = (j1 = 13) * 5 + 20;
		int l1 = Gmud.WQX_ORG_WIDTH - 6 - k1;
		Gmud.GmudDelay(100);
		int i2 = 0;
		int j2 = 0;
		Gmud.sMap.DrawMap(-1);
		DrawSkillList(l1, 40, i1, 0, 0, 0);
		Video.VideoUpdate();
		Input.ClearKeyStatus();
		Gmud.GmudDelay(100);
		while (true)
		{
			Input.ProcessMsg();
			if ((Input.inputstatus & Input.kKeyUp)!=0)
			{
				if (j2 > 0)
					j2--;
				else
				if (i2 > 0)
				{
					i2--;
				} else
				{
					if ((i2 = i1 - 3) < 0)
						i2 = 0;
					if (i1 - 1 < 2)
						j2 = i1 - 1;
					else
						j2 = 2;
				}
				DrawSkillList(l1, 40, i1, i2, j2, 0);
				Video.VideoUpdate();
			} else
			if ((Input.inputstatus & Input.kKeyDown)!=0)
			{
				if (j2 < 2 && j2 < i1 - 1)
					j2++;
				else
				if (i2 < i1 - 3)
				{
					i2++;
				} else
				{
					i2 = 0;
					j2 = 0;
				}
				DrawSkillList(l1, 40, i1, i2, j2, 0);
				Video.VideoUpdate();
			} else
			if ((Input.inputstatus & Input.kKeyEnt)!=0)
			{
				Gmud.GmudDelay(100);
				int l2 = 0;
				int j3 = 0;
				l2 = i2 + j2;
				int i3;
				if ((j3 = Gmud.sPlayer.SetNewSkill(i3 = GmudTemp.temp_array_20_2[l2][0])) == -1)
					return;
				int k3 = 0;
				auto_confirm = 0;
				while (k3 == 0)
					while (true)
					{
						if ((k3 = DrawPractice(j3)) == 1)
							return;
						if (k3 != 2)
							break;
						Gmud.sMap.DrawMap(-1);
						DrawSkillList(l1, 40, i1, i2, j2, 0);
						Input.ClearKeyStatus();
						Video.VideoUpdate();
					}
				Gmud.sMap.DrawMap(-1);
				DrawSkillList(l1, 40, i1, i2, j2, 0);
				Video.VideoUpdate();
			} else
			if ((Input.inputstatus & Input.kKeyExit)!=0)
			{
				Input.ClearKeyStatus();
				Gmud.GmudDelay(100);
				return;
			}
			Input.ClearKeyStatus();
			Gmud.GmudDelay(80);
		}
	}

	static int DrawPractice(int i1)
	{
		Gmud.GmudDelay(100);
		int k1 = Gmud.sPlayer.GetPracticeSpeed(i1);
		int l1 = 0;
		String str;
		//*/
		while (l1 != 6)
		{
			while(true)
			{
				Input.ProcessMsg();
				DrawProgressBox(Gmud.sPlayer.skills[i1][4], Gmud.sPlayer.skills[i1][2], Gmud.sPlayer.skills[i1][2], Gmud.sPlayer.skills[i1][1]);  //draw
				Video.VideoUpdate();
				if ((Input.inputstatus & Input.kKeyExit)!=0)
					return 1;
				if (auto_confirm>0 && (Input.inputstatus&Input.kKeyEnt)!=0) {
					--auto_confirm;
				}
				
				if ((l1 = Gmud.sPlayer.PracticeSkill(i1)) != 0)
					break;
				Gmud.GmudDelay(960 / k1);
			}
			if (l1 == 1)
			{
				str = "你的功夫很难再有所提高了,还是向师傅请教一下吧";
				DrawStringFromY(str, 660);
				Video.VideoUpdate();
				Input.ClearKeyStatus();
				Gmud.GmudDelay(1500);
				return 3;
			}
			if (l1 == 2)
			{
				str = "你的内功等级不够";
				DrawStringFromY(str, 660);
				Video.VideoUpdate();
				Input.ClearKeyStatus();
				Gmud.GmudDelay(1500);
				return 3;
			}
			if (l1 == 3)
			{
				str = "你的内力修为不足,要勤修内功!";
				DrawStringFromY(str, 660);
				Video.VideoUpdate();
				Input.ClearKeyStatus();
				Gmud.GmudDelay(1500);
				return 3;
			}
			if (l1 == 4)
			{
				str = "趁手的兵器都没有一把,瞎比划什么!";
				DrawStringFromY(str, 660);
				Video.VideoUpdate();
				Input.ClearKeyStatus();
				Gmud.GmudDelay(1500);
				return 3;
			}
			if (l1 == 5)
			{
				str = "你受伤了,还是先治疗要紧.";
				DrawStringFromY(str, 660);
				Video.VideoUpdate();
				Input.ClearKeyStatus();
				Gmud.GmudDelay(1500);
				return 3;
			}
			if (l1 != 6)
				continue;
			str = "你的功夫进步了!";
			DrawStringFromY(str, 660);
			Video.VideoUpdate();
			Gmud.GmudDelay(1500);
			Gmud.sMap.DrawMap(-1);
			
			if (auto_confirm==0) {
				return 0;
			}
			
			int i2;
			str = "继续练功吗？\n([输入]确认 [跳出]放弃)";
			while((i2 = DialogBx(str, 16, (Gmud.WQX_ORG_WIDTH / 2 / 3) * 2)) != Input.kKeyExit) 
				if ((i2 & Input.kKeyEnt)!=0)
				{
					Input.ClearKeyStatus();
					Gmud.sMap.DrawMap(-1);
					return 0;
				}
			Input.ClearKeyStatus();
			return 3;
		}
		str = "你的武学经验不足,无法领会更深的功夫!";
		DrawStringFromY(str, 660);
		Video.VideoUpdate();
		Input.ClearKeyStatus();
		Gmud.GmudDelay(1500);
		return 3;
	}
	
	//********** UI-NPC **********//
	static int npc_menu_type = 0;
	static int npc_id = 0;
	static int npc_image_id = 0;
	static String npc_name;
	static int npc_menu_x = 0;
	static int npc_menu_y = 0;
	static final String npc_menu_words[] = new String[] {"交谈", "查看", "战斗", "切磋", "交易", "拜师", "请教"};

	static void DrawNPCMainMenu(int i1)
	{
		int j1 = 13;
		int k1 = 26;
		int l1 = (j1 + 1) * 4 + 4;
		if (npc_menu_type > 0)
			l1 += j1 + 1;
		npc_menu_y = (Gmud.WQX_ORG_WIDTH / 2 - l1) / 2;
		if (npc_menu_x < 4)
			npc_menu_x = 4;
		Video.VideoClearRect(4, 4, k1 * 2, j1);
		Video.VideoDrawStringSingleLine(npc_name/*.c_str()*/, 4, 4);
		int i2 = 6;
		int j2 = k1 + 10 + i2;
		if (npc_menu_x + j2 + 4 > Gmud.WQX_ORG_WIDTH)
			npc_menu_x = Gmud.WQX_ORG_WIDTH - 4 - j2;
		Video.VideoClearRect(npc_menu_x, npc_menu_y, j2, l1);
		Video.VideoDrawRectangle(npc_menu_x, npc_menu_y, j2, l1);
		int k2;
		if ((k2 = (j1 - i2) / 2) < 0)
			k2 = 0;
		int l2;
		for (l2 = 0; l2 < 4; l2++)
		{
			Video.VideoDrawRectangle(npc_menu_x + 3, npc_menu_y + l2 * (j1 + 1) + 2 + k2, i2, i2);
			if (l2 == i1)
				Video.VideoFillRectangle(npc_menu_x + 3, npc_menu_y + l2 * (j1 + 1) + 2 + k2, i2, i2);
			Video.VideoDrawStringSingleLine(npc_menu_words[l2], npc_menu_x + 8 + i2, npc_menu_y + l2 * (j1 + 1) + 2);
		}

		if (npc_menu_type > 0)
		{
			Video.VideoDrawRectangle(npc_menu_x + 3, npc_menu_y + l2 * (j1 + 1) + 2 + k2, i2, i2);
			if (l2 == i1)
				Video.VideoFillRectangle(npc_menu_x + 3, npc_menu_y + l2 * (j1 + 1) + 2 + k2, i2, i2);
			Video.VideoDrawStringSingleLine(npc_menu_words[npc_menu_type], npc_menu_x + 8 + i2, npc_menu_y + l2 * (j1 + 1) + 2);
		}
	}


	static void TalkWithNPC(int id)
	{
		task.Talk(id);
	}

	static void EnterBattle(int id)
	{
		Battle.sBattle = new Battle(id, UI.npc_image_id, 0);
		Battle.sBattle.BattleMain();
//		delete glpBattle;
//		glpBattle = 0;
		Battle.sBattle = null;

		/*
		aa.f = 0;
		aa.i = o.e;
		aa.j = id;
		aa.k = 0;
		aa.h = 0;
		aa.d();
		*/
	}

	static void EnterTryBattle(int id)
	{
		if (NPC.NPC_attrib[id][11] < NPC.NPC_attrib[id][15] / 2)
		{
			Gmud.sMap.DrawMap(-1);
			Gmud.GmudDelay(120);
			String str = "对方看起来并不想你和切磋";
			UI.DrawDialog(str);
			return;
		} else
		{
			Battle.sBattle = new Battle(id, UI.npc_image_id, 1);
			Battle.sBattle.BattleMain();
//			delete glpBattle;
//			glpBattle = 0;
			Battle.sBattle = null;
			/*
			aa.f = 0;
			aa.i = o.e;
			aa.j = i1;
			aa.k = 1;
			aa.h = 0;
			aa.d();
			*/
			return;
		}
	}

	static void Trade(int id)
	{
		if (NPC.NPC_sell_list[id][0] == 0)  //卖
		{
			int j1 = Gmud.sPlayer.CopyItemList();
			TradeWithNPC(1, j1);
			return;
		}
		else
		{
			int k1 = NPC.CopyItemList(id);    //买
			TradeWithNPC(0, k1);
			return;
		}
	}

	static void TradeWithNPC(int i1, int j1)
	{
		if (j1 <= 0)
			return;
		Gmud.sMap.DrawMap(-1);
		DrawTalk(Res.readtext(5, dialog_point[6 + i1], dialog_point[1 + 6 + i1]));
		int k1 = 10;
		int l1 = 13 + 4;
		int i2 = 0;
		int j2 = 0;
		DrawItemList(k1, l1, j1, 0, 0, i1);
		Video.VideoUpdate();
		Gmud.GmudDelay(200);
		while(true)
		{
			Input.ProcessMsg();
			if ((Input.inputstatus & Input.kKeyUp)!=0)
			{
				if (j2 > 0)
					j2--;
				else
				if (i2 > 0)
				{
					i2--;
				} else
				{
					if ((i2 = j1 - 3) < 0)
						i2 = 0;
					if (j1 - 1 < 2)
						j2 = j1 - 1;
					else
						j2 = 2;
				}
				DrawItemList(k1, l1, j1, i2, j2, i1);
				Video.VideoUpdate();
			} else
			if ((Input.inputstatus & Input.kKeyDown)!=0)
			{
				if (j2 < 2 && j2 < j1 - 1)
					j2++;
				else
				if (i2 < j1 - 3)
				{
					i2++;
				} else
				{
					i2 = 0;
					j2 = 0;
				}
				DrawItemList(k1, l1, j1, i2, j2, i1);
				Video.VideoUpdate();
			} else
			if ((Input.inputstatus & Input.kKeyEnt)!=0)
			{
				Gmud.GmudDelay(100);
				int l2 = i2 + j2;
				int i3 = GmudTemp.temp_array_32_2[l2][0];
				if (i1 == 0)
				{
					int limit_money = (Items.item_attribs[i3][6]!=0 && Gmud.sPlayer.GainOneItem(i3))?1:0;
					if (Gmud.sPlayer.money >= limit_money)
					{
						Gmud.sPlayer.money -= Items.item_attribs[i3][6];
						Gmud.GmudDelay(200);
						return;
					}
				} else
				{
					int j3 = (Items.item_attribs[i3][6] * 7) / 10;
					Gmud.sPlayer.money += j3;
					
					// TODO: 用 序号 代替 ID
//					Gmud.sPlayer.LoseItem(i3, 1);
					if ((j3 = Gmud.sPlayer.ExistItem(i3, 1)) >= 0) {
						Gmud.sPlayer.LoseItem(j3, 1);
					}
					return;
				}
			} else
			if ((Input.inputstatus & Input.kKeyExit)!=0)
			{
				Gmud.GmudDelay(100);
				return;
			}
			Input.ClearKeyStatus();
			Gmud.GmudDelay(80);
		}
	}

	static void DrawItemList(int i1, int j1, int k1, int l1, int i2, int j2)
	{
		if (k1 <= 0)
			return;
		int k2 = 13;
		int l2 = 0;
		int i3 = 0;
		int j3 = 13;
		if (k1 >= 3)
			l2 = k2 * 3 + 6;
		else
			l2 = k1 * k2 + 6;
		if (j2 == 0)
			i3 = j3 * 5 + 16 + 4;
		else
			i3 = j3 * 7 + 16 + 10;
		Video.VideoClearRect(i1, j1, i3, l2);
		Video.VideoDrawRectangle(i1, j1, i3, l2);
		int k3 = (k2 - 12) / 2;
		for (int l3 = 0; l3 < k1 && l3 < 3; l3++)
		{
			int i4 = GmudTemp.temp_array_32_2[l1 + l3][0];
			int j4 = GmudTemp.temp_array_32_2[l1 + l3][1];
			if (i4 == 255)
				continue;
			Video.VideoDrawArc(i1 + 4 + 6, j1 + k3 + 2 + 6 + l3 * (k2 + 1), 6);
			if (l3 == i2)
			{
				Video.VideoFillArc(i1 + 4 + 6, j1 + k3 + 2 + 6 + l3 * (k2 + 1), 4);
				int k4 = j1 + l2 + 4;
				int l4 = Items.item_attribs[i4][6];
				if (j2 == 1)
					l4 = (l4 * 7) / 10;
//				String s2="金钱：";
//				wchar_t num[10];
//				s2 += _itow(Gmud.sPlayer.money, num, 10);
//				s2 += " 价格：";
//				s2 += _itow(l4, num, 10);
				String s2 = String.format("金钱：%d 价格：%d", Gmud.sPlayer.money, l4);
				int i5 = Gmud.WQX_ORG_WIDTH - 8 - i1;
				Video.VideoClearRect(i1, k4, i5, k2);
				Video.VideoDrawStringSingleLine(s2/*.c_str()*/, i1, k4);
			}
			String s1 = Items.item_names[i4];
			if(j2 != 0)
			{
//				s1 += "×";
//				wchar_t num[4];
//				s1 += _itow(j4, num, 10);
				s1 += String.format("x%d", j4);
			}
			Video.VideoDrawStringSingleLine(s1/*.c_str()*/, i1 + 4 + 16, j1 + 2 + l3 * (k2 + 1));
		}
	}

	/**
	 * 计算技能等级 
	 * @param i 门派功夫ID
	 * @param j 基本功夫ID
	 * @return
	 */
	static int GetSkillTypeLevel(int i, int j)
	{
		return (Gmud.sPlayer.GetSkillLevel(i) + Gmud.sPlayer.GetSkillLevel(j) / 2);
	}

//	extern wstring ReplaceStr(wstring*, LPCWSTR, LPCWSTR);

	static void ApprenticeWords(int i)
	{
//		UI.DrawDialog(&ReplaceStr(&ReplaceStr(&gmud_readtext(5, UI.dialog_point[i], UI.dialog_point[1 + i]), "$o", UI.npc_name.c_str()), "m", Gmud.sPlayer.player_name));
		String str = Res.readtext(5, UI.dialog_point[i], UI.dialog_point[1 + i]);
		String s1 = str.replaceAll("\\$o", UI.npc_name);
		UI.DrawDialog( s1.replaceAll("m", Gmud.sPlayer.player_name));
	}

	static void Apprentice(int id)
	{
		if (Gmud.sPlayer.class_id != 0 && Gmud.sPlayer.class_id != NPCINFO.NPC_attribute[id][1])
		{
			Gmud.sMap.DrawMap(-1);
			DrawDialog(Res.readtext(5, dialog_point[9], dialog_point[1 + 9]));
			return;
		}
		Gmud.sMap.DrawMap(-1);
		switch (id)
		{
		case 96: //谷虚道长
			ApprenticeWords(182);
			ApprenticeWords(10);
			Gmud.sPlayer.class_id = 5;
			break;

		case 97: //古松道长
			ApprenticeWords(182);
			ApprenticeWords(10);
			Gmud.sPlayer.class_id = 5;
			break;

		case 101: //清虚道长
			if (Gmud.sPlayer.fp_level < 1500)
			{
				ApprenticeWords(183);
				return;
			}
			if (GetSkillTypeLevel(32, 0) < 180)
			{
				ApprenticeWords(184);
				return;
			}
			if (GetSkillTypeLevel(31, 1) < 150)
			{
				ApprenticeWords(185);
				return;
			}
			if (Gmud.sPlayer.GetSavvy() < 28)
			{
				ApprenticeWords(186);
				return;
			}
			ApprenticeWords(187);
			ApprenticeWords(10);
			
			Gmud.sPlayer.class_id = 5;
			break;

		case 122: //雪山教头
			if (Gmud.sPlayer.GetAgility() < 22)
			{
				ApprenticeWords(188);
				return;
			}
			ApprenticeWords(189);
			ApprenticeWords(10);
			
			Gmud.sPlayer.class_id = 6;
			break;

		case 118: //封万剑
			if (Gmud.sPlayer.GetAgility() < 23)
			{
				ApprenticeWords(188);
				return;
			}
			if (GetSkillTypeLevel(36, 0) < 60)
			{
				ApprenticeWords(193);
				return;
			}
			ApprenticeWords(192);
			ApprenticeWords(10);
			
			Gmud.sPlayer.class_id = 6;
			break;

		case 110: //白瑞德
			if (Gmud.sPlayer.fp_level < 1200)
			{
				ApprenticeWords(190);
				return;
			}
			if (Gmud.sPlayer.GetAptitude() < 32)
			{
				ApprenticeWords(191);
				return;
			}
			ApprenticeWords(194);
			ApprenticeWords(10);
			
			Gmud.sPlayer.class_id = 6;
			break;

		case 38: //商宝震
			ApprenticeWords(163);
			ApprenticeWords(10);
			
			Gmud.sPlayer.class_id = 1;
			break;

		case 43: //商剑鸣
			if (Gmud.sPlayer.fp_level < 500)
			{
				ApprenticeWords(159);
				return;
			}
			if (GetSkillTypeLevel(14, 0) < 75)
			{
				ApprenticeWords(160);
				return;
			}
			if (GetSkillTypeLevel(11, 3) < 75)
			{
				ApprenticeWords(161);
				return;
			}
			ApprenticeWords(162);
			ApprenticeWords(10);
			
			Gmud.sPlayer.class_id = 1;
			break;

		case 47: //王维扬
			if (Gmud.sPlayer.fp_level < 800)
			{
				ApprenticeWords(159);
				return;
			}
			if (GetSkillTypeLevel(14, 0) < 150)
			{
				ApprenticeWords(160);
				return;
			}
			if (GetSkillTypeLevel(11, 3) < 150)
			{
				ApprenticeWords(164);
				return;
			}
			ApprenticeWords(165);
			ApprenticeWords(10);
			
			Gmud.sPlayer.class_id = 1;
			break;

		case 90: //腾王丸
			ApprenticeWords(181);
			ApprenticeWords(10);
			
			Gmud.sPlayer.class_id = 4;
			break;

		case 87: //花十郎
			if (GetSkillTypeLevel(27, 1) < 90)
			{
				ApprenticeWords(180);
				return;
			}
			ApprenticeWords(181);
			ApprenticeWords(10);
			
			Gmud.sPlayer.class_id = 4;
			break;

		case 94: //和仲阳
			if (Gmud.sPlayer.GetForce() < 22)
			{
				ApprenticeWords(178);
				return;
			}
			if (GetSkillTypeLevel(26, 0) < 180)
			{
				ApprenticeWords(180);
				return;
			}
			ApprenticeWords(179);
			ApprenticeWords(10);
			
			Gmud.sPlayer.class_id = 4;
			break;

		case 73: //方长老
			ApprenticeWords(174);
			ApprenticeWords(10);
			
			Gmud.sPlayer.class_id = 3;
			break;

		case 80: //余鸿儒
			if (GetSkillTypeLevel(25, 0) < 180)
			{
				ApprenticeWords(175);
				return;
			}
			if (Gmud.sPlayer.GetForce() < 30)
			{
				ApprenticeWords(176);
				return;
			}
			ApprenticeWords(174);
			ApprenticeWords(10);
			
			Gmud.sPlayer.class_id = 3;
			break;

		case 56: //平婆婆
			if (Gmud.sPlayer.sex == 0)
			{
				ApprenticeWords(166);
				return;
			}
			ApprenticeWords(171);
			ApprenticeWords(10);
			
			Gmud.sPlayer.class_id = 2;
			break;

		case 57: //桑轻虹
			if (Gmud.sPlayer.sex == 0)
			{
				ApprenticeWords(166);
				return;
			}
			if (GetSkillTypeLevel(19, 1) < 90)
			{
				ApprenticeWords(172);
				return;
			}
			ApprenticeWords(171);
			ApprenticeWords(10);
			
			Gmud.sPlayer.class_id = 2;
			break;

		case 66: //唐晚词
			if (Gmud.sPlayer.sex == 0)
			{
				ApprenticeWords(166);
				return;
			}
			if (GetSkillTypeLevel(19, 1) < 75)
			{
				ApprenticeWords(172);
				return;
			}
			ApprenticeWords(171);
			ApprenticeWords(10);
			Gmud.sPlayer.class_id = 2;
			// fall through

		case 58: //李青照
			if (Gmud.sPlayer.sex == 0)
			{
				ApprenticeWords(166);
				return;
			}
			if (Gmud.sPlayer.GetSkillLevel(9) < 100)
			{
				ApprenticeWords(168);
				return;
			}
			if (Gmud.sPlayer.GetFaceLevel() < 9)
			{
				ApprenticeWords(167);
				return;
			}
			ApprenticeWords(170);
			ApprenticeWords(10);
			Gmud.sPlayer.class_id = 2;
			break;

		case 127: //华佗
			if (Gmud.sPlayer.pre_savvy > 18)
			{
				ApprenticeWords(151);
				return;
			}
			ApprenticeWords(150);
			ApprenticeWords(10);
			Gmud.sPlayer.class_id = 7;
			break;

		case 126: //北海鳄神
			if (Gmud.sPlayer.pre_savvy > 18)
			{
				ApprenticeWords(151);
				return;
			}
			if (GetSkillTypeLevel(40, 7) < 90)
			{
				ApprenticeWords(152);
				return;
			}
			ApprenticeWords(149);
			ApprenticeWords(10);
			
			Gmud.sPlayer.class_id = 7;
			break;

		case 129:   //娜可露露
			if (Gmud.sPlayer.pre_savvy > 18)
			{
				ApprenticeWords(151);
				return;
			}
			if (GetSkillTypeLevel(40, 7) < 165)
			{
				ApprenticeWords(153);
				return;
			}
			if (GetSkillTypeLevel(45, 0) < 165)
			{
				ApprenticeWords(153);
				return;
			}
			if (GetSkillTypeLevel(41, 3) < 165)
			{
				ApprenticeWords(153);
				return;
			}
			if (GetSkillTypeLevel(43, 1) < 165)
			{
				ApprenticeWords(153);
				return;
			}
			ApprenticeWords(148);
			ApprenticeWords(10);
			
			Gmud.sPlayer.class_id = 7;
			break;

		case 135:   //葛洪
			if (Gmud.sPlayer.pre_savvy < 28)
			{
				ApprenticeWords(154);
				return;
			}
			ApprenticeWords(145);
			ApprenticeWords(10);
			
			Gmud.sPlayer.class_id = 8;
			break;

		case 138:   //留孙真人
			if (Gmud.sPlayer.GetSavvy() < 28)
			{
				ApprenticeWords(154);
				return;
			}
			if (GetSkillTypeLevel(49, 9) < 120)
			{
				ApprenticeWords(155);
				return;
			}
			ApprenticeWords(147);
			ApprenticeWords(10);
			
			Gmud.sPlayer.class_id = 8;
			break;

		case 141:    //茅盈
			if (Gmud.sPlayer.GetSavvy() < 40)
			{
				ApprenticeWords(156);
				return;
			}
			if (Gmud.sPlayer.mp_level < 1200)
			{
				ApprenticeWords(157);
				return;
			}
			ApprenticeWords(146);
			ApprenticeWords(10);
			Gmud.sPlayer.class_id = 8;
			break;

		default:
			return;
		}
		Gmud.sPlayer.teacher_id = id;
	}

	static int DrawConsult(int i1, int j1)
	{
		Gmud.GmudDelay(100);
		int l1 = Gmud.sPlayer.GetStudySpeed();
		int i2 = 0;
		String str;
		while (i2 != 5)
		{
			while (true)
			{
				Input.ProcessMsg();
				DrawProgressBox(Gmud.sPlayer.skills[i1][4], Gmud.sPlayer.skills[i1][2], Gmud.sPlayer.skills[i1][2], Gmud.sPlayer.skills[i1][1]);
				Video.VideoUpdate();
				if ((Input.inputstatus & Input.kKeyExit)!=0)
					return 1;
				if (auto_confirm>0 && (Input.inputstatus&Input.kKeyEnt)!=0) {
					--auto_confirm;
				}
				if ((i2 = Gmud.sPlayer.StudySkill(i1, j1)) != 0)
					break;
				Input.ClearKeyStatus();
				Gmud.GmudDelay(600 / l1);
			};
			if (i2 == 1)
			{
				str = "你的武学经验不足,无法领会更深的功夫";
				DrawStringFromY(str, 660);
				Video.VideoUpdate();
				Gmud.GmudDelay(1500);
				return 1;
			}
			if (i2 == 2)
			{
				str = "你的潜能已经发挥到极限了,没有办法再成长了";
				DrawStringFromY(str, 660);
				Video.VideoUpdate();
				Gmud.GmudDelay(1500);
				return 2;
			}
			if (i2 == 3)
			{
				str = "没钱读什么书啊，回去准备够学费再来吧!";
				DrawStringFromY(str, 660);
				Video.VideoUpdate();
				Gmud.GmudDelay(1500);
				return 3;
			}
			if (i2 == 4)
			{
				str = "你的功夫已经不输为师了，真是可喜可贺呀";
				DrawStringFromY(str, 660);
				Video.VideoUpdate();
				Gmud.GmudDelay(1500);
				return 4;
			}
		};
		str = "你的功夫进步了！";
		DrawStringFromY(str, 660);
		Video.VideoUpdate();
		Gmud.GmudDelay(1500);
		Input.ClearKeyStatus();
		Gmud.sMap.DrawMap(-1);
		
		if (auto_confirm==0) {
			return 0;
		}
		
		int j2;
		str = "继续学习吗？\n([输入]确认 [跳出]放弃)";
		while((j2 = DialogBx(str, 16, (Gmud.WQX_ORG_WIDTH / 2 / 3) * 2)) != Input.kKeyExit) 
			if (j2 == Input.kKeyEnt)
			{
				Gmud.sMap.DrawMap(-1);
				return 0;
			}
		Gmud.GmudDelay(120);
		return 5;
	}

	static void ConsultWithNPC(int i1)
	{
		Gmud.sMap.DrawMap(-1);
		UI.DrawTalk(Res.readtext(5, UI.dialog_point[8], UI.dialog_point[1 + 8]));
		int j1 = 10;
		int k1 = 13 + 4;
		int l1 = 0;
		int i2 = 0;
		UI.DrawSkillList(j1, k1, i1, 0, 0, 1);
		Video.VideoUpdate();
		Gmud.GmudDelay(200);
		while (true)
		{
			Input.ProcessMsg();
			if ((Input.inputstatus & Input.kKeyUp)!=0)
			{
				if (i2 > 0)
					i2--;
				else
				if (l1 > 0)
				{
					l1--;
				} else
				{
					if ((l1 = i1 - 3) < 0)
						l1 = 0;
					if (i1 - 1 < 2)
						i2 = i1 - 1;
					else
						i2 = 2;
				}
				UI.DrawSkillList(j1, k1, i1, l1, i2, 1);
				Video.VideoUpdate();
			} else
			if ((Input.inputstatus & Input.kKeyDown)!=0)
			{
				if (i2 < 2 && i2 < i1 - 1)
					i2++;
				else
				if (l1 < i1 - 3)
				{
					l1++;
				} else
				{
					l1 = 0;
					i2 = 0;
				}
				UI.DrawSkillList(j1, k1, i1, l1, i2, 1);
				Video.VideoUpdate();
			} else
			if ((Input.inputstatus & Input.kKeyEnt)!=0)
			{
				Gmud.GmudDelay(100);
				int k2 = l1 + i2;
				int l2 = GmudTemp.temp_array_20_2[k2][0];
				int i3 = GmudTemp.temp_array_20_2[k2][1];
				int j3;
				if ((j3 = Gmud.sPlayer.SetNewSkill(l2)) >= 0)
				{
					int k3 = 0;
					auto_confirm = 3;
					while (k3 == 0)
						if ((k3 = DrawConsult(j3, i3)) == 1)
							return;
					Gmud.sMap.DrawMap(-1);
					DrawSkillList(j1, k1, i1, l1, i2, 1);
					Video.VideoUpdate();
				}
			} else
			if ((Input.inputstatus & Input.kKeyExit)!=0)
			{
				Gmud.GmudDelay(100);
				return;
			}
			Input.ClearKeyStatus();
			Gmud.GmudDelay(80);
		}
	}

	static void Consult(int id)
	{
		if (6 == id && Gmud.sPlayer.exp < 0x30d40) //exp > 20W
		{
			Gmud.sMap.DrawMap(-1);
			String str = "去去去，赚够经验再来学吧！";
			DrawDialog(str);
			return;
		}
		int j1;
		if (0 == (j1 = NPC.CopyNCPSkillList(id)))  //招式数 == 0
		{
			return;
		} else
		{
			ConsultWithNPC(j1);  //start
			return;
		}
	}

	static void NPCMainMenu()
	{
		int j1 = 0;
		int k1 = 4;
		if (npc_menu_type > 0)
			k1++;
		DrawNPCMainMenu(0);
		Video.VideoUpdate();
		Input.ClearKeyStatus();
		Gmud.GmudDelay(150);
		while(true)
		{
			Input.ProcessMsg();
			if ((Input.inputstatus &Input.kKeyUp)!=0)  //up key
			{
				if (j1 == 0)
					j1 = k1 - 1;
				else
					j1--;
				DrawNPCMainMenu(j1);
				Video.VideoUpdate();
			} else
			if ((Input.inputstatus &Input.kKeyDown)!=0)   //down key
			{
				if (j1 < k1 - 1)
					j1++;
				else
					j1 = 0;
				DrawNPCMainMenu(j1);
				Video.VideoUpdate();
			} else
			{
				if ((Input.inputstatus & Input.kKeyEnt)!=0)   //enter key
				{
					if (j1 == 0)
						TalkWithNPC(npc_id);  //交谈
					else
					if (j1 == 1)
						ViewNPC(npc_id);  //查看
					else
					if (j1 == 2)
						EnterBattle(npc_id);  //战斗
					else
					if (j1 == 3)
						EnterTryBattle(npc_id);  //切磋
					else
					if (j1 == 4)
						if (npc_menu_type == 4)
							Trade(npc_id);   //交易
						else
						if (npc_menu_type == 5)
							Apprentice(npc_id);   //拜师
						else
						if (npc_menu_type == 6)
							Consult(npc_id);   //请教
					Gmud.GmudDelay(100);
					return;
				}
				if ((Input.inputstatus &Input.kKeyExit)!=0)  //esc
				{
					Gmud.GmudDelay(100);
					return;
				}
			}
			Input.ClearKeyStatus();
			Gmud.GmudDelay(80);
		}
	}

	static void DrawTalk(String s1)
	{
		int i1 = 13 * 2 + 2;
		ArrayList<String> as = Video.SplitString(s1, Gmud.WQX_ORG_WIDTH - 3);
		int j1 = 0;
		while(true)
		{
			if (j1 >= as.size())
				return;
			Video.VideoClearRect(0, 0, Gmud.WQX_ORG_WIDTH - 1, i1 + 6);
			Video.VideoDrawRectangle(0, 0, Gmud.WQX_ORG_WIDTH - 1, i1 + 6);
			Video.VideoDrawStringSingleLine(as.get(j1), 10, 4);
			if (++j1 < as.size())
			{
				Video.VideoDrawStringSingleLine(/*as[j1].c_str()*/as.get(j1), 10, 7 + 12);
				j1++;
			}
		}
//		Video.VideoUpdate();
	}

	static void ViewNPC(int i1)
	{
		String s1 = NPC.GetNPCDesc(i1);  //get desc
		int j1;
		if ((j1 = NPCINFO.NPC_attribute[i1][2]) < 10)
			j1 = 10;
		j1 = (j1 / 10) * 10;

		Battle.sBattle = new Battle(i1, 0, 0);
		Battle.sBattle.CopyNPCData(i1);
		Battle.sBattle.CalcFighterLevel(1);
		int k1 = Battle.sBattle.fighter_data[1][62] / 5;  //level
		int l1 = Battle.sBattle.CalcAttackLevel(1);  //attack level
//		delete glpBattle;
//		glpBattle = 0;
		Battle.sBattle = null;

		String str = new String(npc_name);
		str += "看起来约";
//		wchar_t num[6];
//		str += _itow(j1, num, 10);
		str += j1;
		str += "多岁\n";
		str += "武艺看起来";
		str += GmudData.level_name[k1];
		str += "\n";
		str += "出手似乎";
		str += GmudData.attack_level_name[l1];
		str += "\n";
		str += "带着:";
		for (int i2 = 0; i2 < 4; i2++)
		{
			int k2 = NPC.NPC_item[i1][i2];
			if ((k2 < 77 || k2 > 86) && (k2 < 88 || k2 > 91) && (k2 < 68 || k2 > 71) && k2 != 10 && k2 > 0)
			{
				str += Items.item_names[k2];
				str += " ";
			}
		}

		str += "\n";
		str += s1/*.c_str()*/;  //add desc
		int j2 = Gmud.WQX_ORG_WIDTH - 4;
		int l2 = Gmud.WQX_ORG_WIDTH / 2 - 4;
		ArrayList<String> as  = Video.SplitString(str, j2);
		int i3 = 13;
		int j3 = 6;
		int k3 = as.size() / j3 + (as.size() % j3 <= 0 ? 0 : 1);
		for (int l3 = 0; l3 < k3;)
		{
			Video.VideoClearRect(2, 2, j2, l2);
			Video.VideoDrawRectangle(2, 2, j2, l2);
			for (int i4 = 0; i4 < j3 && i4 + l3 * j3 < as.size(); i4++)
				Video.VideoDrawStringSingleLine(as.get(i4 + l3 * j3)/*.c_str()*/, 2, 3 + i4 * 12);
			
			Video.VideoUpdate();
			Input.ClearKeyStatus();
			Gmud.GmudDelay(120);
			int j4;
			if (++l3 < k3)
			{
				while(Input.inputstatus != Input.kKeyDown)
					if ((j4 = UI.DrawFlashCursor(Gmud.WQX_ORG_WIDTH - 16, 5, 8)) == Input.kKeyExit)
					{
						Gmud.GmudDelay(100);
						return;
					}
			} else
			{
				while(Input.inputstatus != Input.kKeyExit)
					Gmud.GmudDelay(100);
				Input.ClearKeyStatus();
				Gmud.GmudDelay(100);
				return;
			}
		}
	}

	
}
