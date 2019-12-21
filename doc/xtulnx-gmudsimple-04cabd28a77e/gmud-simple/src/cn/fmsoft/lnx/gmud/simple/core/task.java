package cn.fmsoft.lnx.gmud.simple.core;

import java.util.Arrays;
import cn.fmsoft.lnx.gmud.simple.core.NPC.NPCSKILLINFO;

public class task {
	
	// 40
	static final int temp_tasks_data[] = new int[40];

	static final String yes_no = "\n([输入]确认 [跳出]放弃)\n";
	static final String award = "你被奖励了：\n";
	static final String award_exp = " 点经验 ";
	static final String award_potential = " 点潜能 ";
	static final String no_award = "努力吧，干活去吧！";

	// 27
	static final int item_to_find[] = new int[] {
			1, 2, 3, 4, 5, 6, 7, 8, 9, 11,
			12, 13, 16, 17, 18, 19, 35, 40, 42, 43, 
			46, 48, 49, 72, 73, 74, 76
		};
	static final String item_start = "今天妾身正准备请人去找o,能否帮个忙？";
	static final String item_wait = "妾身还盼着您的o呢！";
	static final String old_woman_task_name[] = new String[]{
			"扫地", "挑水", "劈柴"
		};
	static final String old_woman_start = "老身年事已高，有好心人帮帮我『o』吗？";
	static final String old_woman_wait = "老身吩咐你的事做完了么？";
	static final String old_woman_unable = "唉！你也小有名气了，老身使唤不动你了！";
	static final String old_woman_award = "你被奖励了：20点实战经验 10点潜能 50金钱";
	static final String kill_start = "老夫夜观天象，o阳寿已尽，你去解决他!";
	static final String kill_wait = "老夫不是让你解决o吗？";
	static final String exist_award = "看你红光满面，还是先去顾炎武处领赏吧！";
	static final String talk_start = "请速去拜见o";
	static final String talk_wait = "老夫不是说过请去拜见o吗！";
	static final String task_finish = "你完成了任务，去顾炎武处领赏吧！";
	static final String bad_man_start = "近有恶人『o』在m为非作歹，请速去为民除害！";
	static final String bad_man_wait = "在下不是叫你去收服o吗？";
	static final String bad_man_nothing = "本镇治安良好";
	static final String bad_man_doing = "本镇正在缉拿人犯『o』";
	static final String bad_man_family_name[] = new String[]
	{"赵", "钱", "孙", "李", "周", "吴", "郑", "王"};
	static final String bad_man_name[] = new String []
	{"一", "二", "三", "四", "五", "六", "七", "八"};
	static final int bad_man_skill[] = new int[50];
	static int bad_man_mapid = -1;
	
	static void reset() {
		Arrays.fill(bad_man_skill, 0);
		bad_man_mapid = -1;
	}
	
	static void CommonDialog()
	{
		Gmud.sMap.DrawMap(-1);
		int i1 = util.RandomInt(5);
//		wstring s1(gmud_readtext(5, UI.dialog_point[i1], UI.dialog_point[1+i1]));
//		s1 = ReplaceStr(&s1, L"\\$o", UI.npc_name.c_str());
//		UI.DrawDialog(&ReplaceStr(&s1, L"\\$n", Gmud.sPlayer.player_name));
		
		final String s1 = Res.readtext(5, UI.dialog_point[i1], UI.dialog_point[1+i1]);
		final String s2 = s1.replaceAll("\\$o", UI.npc_name);
		UI.DrawDialog(s2.replaceAll("\\$n", Gmud.sPlayer.player_name));
	}

	static boolean MapPassable(int i)
	{
		switch(i)
		{
		case 138: 
			if (Gmud.sPlayer.ExistItem(91, 1) < 0)
				return true;
			break;

		case 126:
			if (Gmud.sPlayer.ExistItem(90, 1) < 0)
				return true;
			break;

		case 41:
			if (Gmud.sPlayer.ExistItem(79, 1) < 0)
				return true;
			break;

		case 137: 
			if (Gmud.sPlayer.ExistItem(80, 1) < 0)
				return true;
			break;

		case 77:
			if (Gmud.sPlayer.ExistItem(81, 1) < 0)
				return true;
			break;

		case 128: 
			if (Gmud.sPlayer.ExistItem(82, 1) < 0)
				return true;
			break;

		case 92: 
			if (Gmud.sPlayer.ExistItem(83, 1) < 0)
				return true;
			break;

		case 106:
			if (Gmud.sPlayer.ExistItem(84, 1) < 0)
				return true;
			break;

		case 100:
			if (Gmud.sPlayer.ExistItem(85, 1) < 0)
				return true;
			break;

		case 64:
			if (Gmud.sPlayer.ExistItem(86, 1) < 0)
				return true;
			break;
		}
		return i >= 158 && i <= 178 && Map.NPC_flag[i] == 0;
	}

	static void Talk(int i1)
	{
		if (temp_tasks_data[9] == 1 && temp_tasks_data[10] == 0 && i1 == temp_tasks_data[8])
		{
			Gmud.sMap.DrawMap(-1);
			UI.ShowDialog(5);  //“我知道了，多谢来访”
			temp_tasks_data[10] = 1;
			return;
		}
		if(i1 == 179)
		{
			Gmud.sMap.DrawMap(-1);
//			wstring str(L"小兔崽子儿，我看你是不想活了！！");
//			UI.DrawDialog(&str);
			UI.DrawDialog("小兔崽子儿，我看你是不想活了！！");
			return;
		}
		int j1;
		if ((j1 = NPCINFO.NPC_attribute[i1][0]) == 0)
		{
			CommonDialog();
			Input.ClearKeyStatus();
			return;
		}
		if (j1 > 0)
		{
			int k1 = j1;
			Gmud.sMap.DrawMap(-1);
//			wstring s1(gmud_readtext(5, UI.dialog_point[k1], UI.dialog_point[1+k1]));
//			s1 = ReplaceStr(&s1, L"\$o", UI.npc_name.c_str());
//			UI.DrawDialog(&ReplaceStr(&s1, L"\$n", Gmud.sPlayer.player_name));
			final String s1 = Res.readtext(5, UI.dialog_point[k1], UI.dialog_point[1+k1]);
			String s2 = s1.replaceAll("\\$o", UI.npc_name);
			UI.DrawDialog(s2.replaceAll("\\$n", Gmud.sPlayer.player_name));

			Input.ClearKeyStatus();
			while(Input.inputstatus == 0)
				Gmud.GmudDelay(100);
			return;
		}
		if (j1 < 0)
		{
			Gmud.sMap.DrawMap(-1);
			Gmud.GmudDelay(120);
			SpecialDialog(i1);  //SpecialDialog
			Input.ClearKeyStatus();
		}
	}

//	extern bool RandomBool(int);

	static void SpecialDialog(int i1)
	{
		int k3;
	label0:
		switch (i1)
		{
		default:
			break;
		case 1: //小顽童
			{
				int j1;
				if ((j1 = Gmud.sPlayer.ExistItem(6, 2)) >= 0 && Gmud.sPlayer.item_package[j1][2] == 2)
				{
//					wstring s1 = gmud_readtext(5, UI.dialog_point[28], UI.dialog_point[1 + 28]);
//					UI.DrawTalk(&s1);
					String s1 = Res.readtext(5, UI.dialog_point[28], UI.dialog_point[1 + 28]);
					UI.DrawTalk(s1);
					
					s1 += yes_no;
					Gmud.GmudDelay(80);
					Input.ClearKeyStatus();
					int k4 = UI.DialogBx(s1, 8, 8);
					while(true)
					{
						if (k4 == Input.kKeyEnt)
						{
							Gmud.sPlayer.LoseItem(j1, 2);
							Gmud.sPlayer.GainOneItem(40);
							break;
						}
						if (k4 == Input.kKeyExit)
							break;
						while(0 == (k4 = Input.inputstatus))
							Gmud.GmudDelay(100);
					};
					break;
				}
				if (util.RandomBool(80))
				{
					UI.ShowDialog(26);
					return;
				} else
				{
					UI.ShowDialog(27);
					return;
				}
			}
		case 27: //小书童
			int j4;
			UI.ShowDialog(j4 = util.RandomInt(3) + 36);
			return;

		case 29: //老裁缝
			{
				int k1 = Gmud.sPlayer.ExistItem(40, 1);
				UI.ShowDialog(40);
				if (k1 < 0)
					break;
//				wstring s2 = gmud_readtext(5, UI.dialog_point[41], UI.dialog_point[1 + 41]);
//				UI.DrawTalk(&s2);
				String s2 = Res.readtext(5, UI.dialog_point[41], UI.dialog_point[1 + 41]);
				
				Input.ClearKeyStatus();
				s2 += yes_no;
				int l5 = UI.DialogBx(s2, 8, 8);
				while (true)
				{
					if (l5 == Input.kKeyEnt)
					{
						Gmud.sPlayer.LoseItem(k1, 1);
						Gmud.sPlayer.GainOneItem(43);
						Gmud.sMap.DrawMap(-1);
						UI.ShowDialog(42);
//						goto label0;
						break label0;
					}
					if (l5 == Input.kKeyExit)
//						goto label0;
						break label0;
					while(0 == (l5 = Input.inputstatus))
						Gmud.GmudDelay(100);
				}
			}
		case 19: //荷西
			{
				int l1 = Gmud.sPlayer.ExistItem(74, 30);
				UI.ShowDialog(33);
				if (l1 < 0)
					break;
//				wstring s3 = gmud_readtext(5, UI.dialog_point[34], UI.dialog_point[1 + 34]);
//				UI.DrawTalk(&s3);
				String s3 = Res.readtext(5, UI.dialog_point[34], UI.dialog_point[1 + 34]);
				UI.DrawTalk(s3);
				
				Input.ClearKeyStatus();
				s3 += yes_no;
				int i6 = UI.DialogBx(s3, 8, 8);
				while (true)
				{
					if (i6 == Input.kKeyEnt)
					{
						Gmud.sPlayer.LoseItem(l1, 30);
						Gmud.sPlayer.GainOneItem(33);
						Gmud.sMap.DrawMap(-1);
						UI.ShowDialog(35);
//						goto label0;
						break label0;
					}
					if (i6 == Input.kKeyExit)
//						goto label0;
						break label0;
					while(0 == (i6 = Input.inputstatus))
						Gmud.GmudDelay(100);
				}
			}
		case 4: // 厨师",
			{
				int i2 = Gmud.sPlayer.ExistItem(5, 1);
				UI.ShowDialog(30);
				if (i2 < 0)
					break;
//				wstring s4 = gmud_readtext(5, UI.dialog_point[31], UI.dialog_point[1 + 31]);
//				UI.DrawTalk(&s4);
				String s4 = Res.readtext(5, UI.dialog_point[31], UI.dialog_point[1 + 31]);
				UI.DrawTalk(s4);
				
				Input.ClearKeyStatus();
				s4 += yes_no;
				int j6 = UI.DialogBx(s4, 8, 8);
				while (true)
				{
					if (j6 == Input.kKeyEnt)
					{
						Gmud.sPlayer.LoseItem(i2, 1);
						Gmud.sPlayer.GainOneItem(12);
						Gmud.sMap.DrawMap(-1);
						UI.ShowDialog(32);
//						goto label0;
						break label0;
					}
					if (j6 == Input.kKeyExit)
//						goto label0;
						break label0;
					while(0 == (j6 = Input.inputstatus))
						Gmud.GmudDelay(100);
				}
			}
		case 36: //盐商
			{
				int j2 = Gmud.sPlayer.ExistItem(10, 1);
				UI.ShowDialog(45);
				if (j2 < 0)
					break;
//				wstring s5 = gmud_readtext(5, UI.dialog_point[46], UI.dialog_point[1 +46]);
//				UI.DrawTalk(&s5);
				String s5 = Res.readtext(5, UI.dialog_point[46], UI.dialog_point[1 +46]);
				UI.DrawTalk(s5);
				
				Input.ClearKeyStatus();
				s5 += yes_no;
				int k6 = UI.DialogBx(s5, 8, 8);
				while (true)
				{
					if (k6 == Input.kKeyEnt)
					{
						Gmud.sPlayer.LoseItem(j2, 1);
						Gmud.sPlayer.GainOneItem(62);
						Gmud.sMap.DrawMap(-1);
						UI.ShowDialog(47);
//						goto label0;
						break label0;
					}
					if (k6 == Input.kKeyExit)
//						goto label0;
						break label0;
					while(0 == (k6 = Input.inputstatus))
						Gmud.GmudDelay(100);
				}
			}
		case 46: //平阿四
			{
				int k2 = Gmud.sPlayer.ExistItem(69, 1);
				if (util.RandomBool(80))
					UI.ShowDialog(48);
				else
					UI.ShowDialog(49);
				if (k2 < 0)
					break;
//				wstring s6 = gmud_readtext(5, UI.dialog_point[50], UI.dialog_point[1 + 50]);
//				UI.DrawTalk(&s6);
				String s6 = Res.readtext(5, UI.dialog_point[50], UI.dialog_point[1 + 50]);
				UI.DrawTalk(s6);
				
				int l6 = UI.DialogBx((s6 += yes_no), 8, 8);
				while (true)
				{
					if (l6 == Input.kKeyEnt)
					{
						Gmud.sPlayer.LoseItem(k2, 1);
						Gmud.sPlayer.GainOneItem(68);
						Gmud.sMap.DrawMap(-1);
						UI.ShowDialog(51);
//						goto label0;
						break label0;
					}
					if (l6 == Input.kKeyExit)
//						goto label0;
						break label0;
					while(0 == (l6 = Input.inputstatus))
						Gmud.GmudDelay(100);
				}
			}
		case 89: //食野太郎
			{
				int l2 = Gmud.sPlayer.ExistItem(2, 10);
				int l4 = util.RandomInt(3);
				UI.ShowDialog(59 + l4);
				if (l2 < 0)
					break;
//				wstring s7 = gmud_readtext(5, UI.dialog_point[62], UI.dialog_point[1 + 62]);
//				UI.DrawTalk(&s7);
				String s7 = Res.readtext(5, UI.dialog_point[62], UI.dialog_point[1 + 62]);
				UI.DrawTalk(s7);
				
				int k7 = UI.DialogBx((s7 += yes_no), 8, 8);
				while (true)
				{
					if (k7 == Input.kKeyEnt)
					{
						Gmud.sPlayer.LoseItem(l2, 10);
						Gmud.sPlayer.GainOneItem(70);
						Gmud.sMap.DrawMap(-1);
						UI.ShowDialog(63);
//						goto label0;
						break label0;
					}
					if (k7 == Input.kKeyExit)
//						goto label0;
						break label0;
					while(0 == (k7 = Input.inputstatus))
						Gmud.GmudDelay(100);
				}
			}
		case 67: //小红
			{
				int i3 = Gmud.sPlayer.ExistItem(29, 1);
				int i5 = util.RandomInt(2);
				UI.ShowDialog(54 + i5);
				if (i3 < 0)
					break;
//				wstring s8 = gmud_readtext(5, UI.dialog_point[56], UI.dialog_point[1 + 56]);
//				UI.DrawTalk(&s8);
				String s8 = Res.readtext(5, UI.dialog_point[56], UI.dialog_point[1 + 56]);
				UI.DrawTalk(s8);
				
				int l7 = UI.DialogBx((s8 += yes_no), 8, 8);
				while (true)
				{
					if (l7 == Input.kKeyEnt)
					{
						Gmud.sPlayer.LoseItem(i3, 1);
						Gmud.sPlayer.GainOneItem(28);
						Gmud.sMap.DrawMap(-1);
						UI.ShowDialog(57);
//						goto label0;
						break label0;
					}
					if (l7 == Input.kKeyExit)
//						goto label0;
						break label0;
					while(0 == (l7 = Input.inputstatus))
						Gmud.GmudDelay(100);
				}
			}
		case 63: //司棋
			{
				int j3 = Gmud.sPlayer.ExistItem(72, 1);
				int j5 = util.RandomInt(2);
				UI.ShowDialog(64 + j5);
				if (j3 < 0)
					break;
//				wstring s9 = gmud_readtext(5, UI.dialog_point[66], UI.dialog_point[1 + 66]);
//				UI.DrawTalk(&s9);
				String s9 = Res.readtext(5, UI.dialog_point[66], UI.dialog_point[1 + 66]);
				UI.DrawTalk(s9);
				int i8 = UI.DialogBx((s9 += yes_no), 8, 8);
				while (true)
				{
					if (i8 == Input.kKeyEnt)
					{
						Gmud.sPlayer.LoseItem(j3, 1);
						Gmud.sPlayer.GainOneItem(71);
						Gmud.sMap.DrawMap(-1);
						UI.ShowDialog(67);
//						goto label0;
						break label0;
					}
					if (i8 == Input.kKeyExit)
//						goto label0;
						break label0;
					while(0 == (i8 = Input.inputstatus))
						Gmud.GmudDelay(100);
				}
			}
		case 0: //阿庆嫂
			UI.ShowDialog(24 + util.RandomInt(2));
			Input.ClearKeyStatus();
			return;

		case 110: //白瑞德
			{
				if ((k3 = Gmud.sPlayer.ExistItem(88, 1)) >= 0 && Gmud.sPlayer.class_id == 6)
				{
					if (Gmud.sPlayer.GetSkillLevel(38) >= 150 && Gmud.sPlayer.GetSkillLevel(2) > 60)
					{
						UI.ShowDialog(21);
						Gmud.sPlayer.LoseItem(k3, 1);
						Gmud.sPlayer.lasting_tasks[0] = 1;
						return;
					} else
					{
						CommonDialog();
						return;
					}
				} else
				{
					CommonDialog();
					return;
				}
			}
		case 2: //捕快
			KillWanted();
			return;

		case 30: //顾炎武
			{
				for (int i7 = 0; i7 < 3; i7++)
					if (temp_tasks_data[11 + i7 * 5] == 1)
					{
						int j8 = temp_tasks_data[12 + i7 * 5] + util.RandomInt(Gmud.sPlayer.bliss);
						if (i7 < 2)
							j8 = temp_tasks_data[12] + temp_tasks_data[17];
						if (util.RandomBool(75))
						{
							Gmud.sPlayer.exp += j8;
							
//							wchar_t num[8];
//							wstring str(award);
//							str += _itow(j8, num, 10);
//							str += award_exp;
							String str = award + j8 + award_exp;
							UI.DrawDialog(str);
						} else
						{
							Gmud.sPlayer.potential += j8 + util.RandomInt(Gmud.sPlayer.bliss / 2);
//							wstring str(award);
//							wchar_t num[8];
//							str += _itow(j8, num, 10);
//							str += award_potential;
							String str = award + j8 + award_potential;
							UI.DrawDialog(str);
						}
						temp_tasks_data[8 + i7 * 5] = temp_tasks_data[9 + i7 * 5] = temp_tasks_data[10 + i7 * 5] = temp_tasks_data[11 + i7 * 5] = 0;
						return;
					}
//				wstring str(no_award);
//				UI.DrawDialog(&str);
				UI.DrawDialog(no_award);
				return;
			}
		case 5: //村长
			FindPeople();
			return;

		case 9: //中年妇人
			FindItem();
			return;

		case 25: //平一指
			task.KillPeople();
			return;

		case 24: //老婆婆
			OldWoman();
			return;

		case 13: //石料管事"
			if (Gmud.sPlayer.exp < 1000)
			{
				UI.ShowDialog(13);  //less exp
				return;
			}
			if (Gmud.sPlayer.exp > 20000)
			{
				UI.ShowDialog(14);  //more exp
				return;
			}
			if (temp_tasks_data[25] == 1)
			{
				UI.ShowDialog(12);  //存在石料
				return;
			}
			if (temp_tasks_data[29] < 240 && temp_tasks_data[26] > 0)
			{
				UI.ShowDialog(16);  //不需要
				return;
			}
			if (Gmud.sPlayer.GainOneItem(87))
			{
				UI.ShowDialog(17);
				temp_tasks_data[25] = 1;
				temp_tasks_data[26] += 1;
				if (temp_tasks_data[27] < 40)
					temp_tasks_data[27] = 40;
				else // TODO: 石料奖励与悟性和福缘相关，降低难度。
					temp_tasks_data[27] += util.RandomInt(10+(Gmud.sPlayer.GetSavvy()+Gmud.sPlayer.bliss)/2) + 1;
				if (temp_tasks_data[28] < 20)
					temp_tasks_data[28] = 20;
				else
					temp_tasks_data[28] += util.RandomInt(5+(Gmud.sPlayer.GetSavvy()+Gmud.sPlayer.bliss)/2) + 1;
				temp_tasks_data[29] = 0;
				return;
			} else
			{
				UI.ShowDialog(15);
				temp_tasks_data[25] = 0;
				return;
			}

		case 14: //工地管事
			if (temp_tasks_data[25] == 0)
			{
				UI.ShowDialog(18);
				return;
			}
			int l3;
			if ((l3 = Gmud.sPlayer.ExistItem(87, 1)) >= 0)
			{
				Gmud.sPlayer.LoseItem(l3, 1);
				UI.ShowDialog(20);
				Gmud.sPlayer.exp += temp_tasks_data[27];
				Gmud.sPlayer.potential += temp_tasks_data[28];
				temp_tasks_data[25] = 0;

//				wstring str(award);
//				wchar_t num[20];
//				str += _itow(temp_tasks_data[27], num, 10);
//				str += award_exp;
//				str += _itow(temp_tasks_data[28], num, 10);
//				str += award_potential;
//				UI.DrawDialog(&str);
				String str = award + temp_tasks_data[27] + award_exp + temp_tasks_data[28] + award_potential;
				UI.DrawDialog(str);
				return;
			} else
			{
				UI.ShowDialog(19);
				temp_tasks_data[25] = 0;
				return;
			}

		case 136:  //华岳
			if (Gmud.sPlayer.money >= 0x7a120)
			{
				Gmud.sPlayer.money -= 0x7a120;
				Gmud.sPlayer.bliss += 1;
				if (Gmud.sPlayer.bliss > 90)
					Gmud.sPlayer.bliss = 90;
				UI.ShowDialog(23);
				return;
			} else
			{
				UI.ShowDialog(22);
				return;
			}

		case 145: //干匠
			if (Gmud.sPlayer.lasting_tasks[2] != 0)
			{
				UI.ShowDialog(195);
				Gmud.sMap.LoadMap(87);
				Gmud.sMap.SetPlayerLocation(0, 4);
				Gmud.sMap.DrawMap(0);
				return;
			}
			if (Gmud.sPlayer.exp < 0x186a0)
			{
				UI.ShowDialog(109);
				return;
			}
			UI.ShowDialog(196);
			UI.ShowDialog(88);
			//*/
			Battle.sBattle = new Battle(146, 231, 1);
			if (Gmud.sPlayer.GainOneItem(11))
			{
				UI.ShowDialog(90);
				NPC.ResetData(146);
				Battle.sBattle.BattleMain();  //battle main
				Input.ClearKeyStatus();
				Gmud.sMap.DrawMap(-1);
				if (Gmud.sPlayer.hp < Gmud.sPlayer.hp_max / 2)
				{
//					delete glpBattle;
//					glpBattle = 0;
					Battle.sBattle = null;
					UI.ShowDialog(95);
					return;
				}
				if (Gmud.sPlayer.equips[15] != 11)
				{
//					delete glpBattle;
//					glpBattle = 0;
					Battle.sBattle = null;
					String str = "莫邪哼了一声：我们考的是刀，你这算什么";
					UI.DrawDialog(str);
					return;
				}
				int i4;
				if ((i4 = Gmud.sPlayer.ExistItem(11, 1)) != -1)
					Gmud.sPlayer.DeleteOneItem(i4);
				UI.ShowDialog(96);
				Gmud.sPlayer.GainOneItem(17);
				UI.ShowDialog(91);
				NPC.ResetData(146);
				Battle.sBattle.BattleMain();  //battle main
				Input.ClearKeyStatus();
				Gmud.sMap.DrawMap(-1);
				if (Gmud.sPlayer.hp < Gmud.sPlayer.hp_max / 2)
				{
//					delete glpBattle;
//					glpBattle = 0;
					Battle.sBattle = null;
					UI.ShowDialog(95);
					return;
				}
				if (Gmud.sPlayer.equips[15] != 17)
				{
//					delete glpBattle;
//					glpBattle = 0;
					Battle.sBattle = null;
					String str = "莫邪哼了一声：我们考的是剑，你这算什么";
					UI.DrawDialog(str);
					return;
				}
				if ((i4 = Gmud.sPlayer.ExistItem(17, 1)) != -1)
					Gmud.sPlayer.DeleteOneItem(i4);
				UI.ShowDialog(96);
				Gmud.sPlayer.GainOneItem(21);
				UI.ShowDialog(92);
				NPC.ResetData(146);
				Battle.sBattle.BattleMain();  //battle main
				Input.ClearKeyStatus();
				Gmud.sMap.DrawMap(-1);
				if (Gmud.sPlayer.hp < Gmud.sPlayer.hp_max / 2)
				{
//					delete glpBattle;
//					glpBattle = 0;
					Battle.sBattle = null;
					UI.ShowDialog(95);
					return;
				}
				if (Gmud.sPlayer.equips[15] != 21)
				{
//					delete glpBattle;
//					glpBattle = 0;
					Battle.sBattle = null;
					String str = "莫邪哼了一声：我们考的是杖，你这算什么";
					UI.DrawDialog(str);
					return;
				}
				if ((i4 = Gmud.sPlayer.ExistItem(21, 1)) != -1)
					Gmud.sPlayer.DeleteOneItem(i4);
				UI.ShowDialog(96);
				Gmud.sPlayer.GainOneItem(22);
				UI.ShowDialog(93);
				NPC.ResetData(146);
				Battle.sBattle.BattleMain();  //battle main
				Input.ClearKeyStatus();
				Gmud.sMap.DrawMap(-1);
				if (Gmud.sPlayer.hp < Gmud.sPlayer.hp_max / 2)
				{
//					delete glpBattle;
//					glpBattle = 0;
					Battle.sBattle = null;
					UI.ShowDialog(95);
					return;
				}
				if (Gmud.sPlayer.equips[15] != 22)
				{
//					delete glpBattle;
//					glpBattle = 0;
					Battle.sBattle = null;
					String str = "莫邪哼了一声：我们考的是鞭，你这算什么";
					UI.DrawDialog(str);
					return;
				}
//				delete glpBattle;
//				glpBattle = 0;
				Battle.sBattle = null;
				if ((i4 = Gmud.sPlayer.ExistItem(22, 1)) != -1)
					Gmud.sPlayer.DeleteOneItem(i4);
				UI.ShowDialog(94);
				Gmud.sPlayer.lasting_tasks[2] = 1;
				Gmud.sMap.LoadMap(87);
				Gmud.sMap.SetPlayerLocation(0, 4);
				Gmud.sMap.DrawMap(0);
				return;
			} else
				//*/
			{
				UI.ShowDialog(89);
				return;
			}

		case 156:    //月下老人
			{
				if(8 == Gmud.sPlayer.class_id)
				{
					String str = "茅山一派属方外之士，岂能再动凡心？";
					UI.DrawDialog(str);
					return;
				}
				if(6 > Gmud.sPlayer.GetAge())
				{
					String str = "这么嫩的小伢仔也要结婚？早恋可不好哟！";
					UI.DrawDialog(str);
					return;
				}
				if(0 == Gmud.sPlayer.lasting_tasks[6])
				{
					String str = "房子都没有就想结婚？我可不能眼看你们婚后露宿街头！";
					UI.DrawDialog(str);
					return;
				}
				String str = "请选择下列选项: 结婚: 1.求婚 2.允婚 离婚: 3.分道 4.扬镳";
				UI.DrawDialog(str);
				return;
			}
			/*
			你已有家室还来结婚？重婚是犯罪的!
			你尚未婚配就来离婚，想拿老夫开涮吗？

			恭喜恭喜,你结婚了! 

			有人向你求婚『name』是一位age岁的gender性,看上去face,同意吗?y/n
			 
			请选择下列选项: 结婚: 1.求婚 2.允婚 离婚: 3.分道 4.扬镳    (请按数字键选择)  
			  
			你决定求婚，现在正怀着忐忑的心情等待对方的答复... 

			请检查线路! 

			落花有意流水无情,此事只好作罢, 
			真情不在朝朝暮暮,此事从长计议!  

			别开玩笑,你们性别相同耶!

			name同意了,此乃天作之合，祝你们白头到老
			 
			你决定与name断绝夫妻情分，现在正等待对方答复...

			唉，如此你我情断义绝，多自珍重吧。 
			如此了断倒也干脆，离婚了就别在来找我。

			一日夫妻百日恩，还是不逞一时意气了吧。
			床头吵架床尾和，日子还是要过的。

			你爱人根本就不是这个人,别人家里的事你凑什么热闹!  

			世事艰辛，这日子...还能过到一块儿去吗, 不过了? y/n  

			非法请求!  

			你拒绝了对方的请求!
			*/

		case 157: //老管家
			CommonDialog();
			//o.d("您来啦！~");  //家具 翻修 销毁
			return;
		case 6: //独行大侠
			CommonDialog();
			break;
		}	
	}

	static void FindItem()
	{
		for (int i1 = 0; i1 < 3; i1++)
			if (temp_tasks_data[11 + i1 * 5] == 1)
			{
//				wstring str(exist_award);
//				UI.DrawDialog(&str);
				UI.DrawDialog(exist_award);
				return;
			}
		int j1;
		if (temp_tasks_data[14] != 0)
			if ((j1 = Gmud.sPlayer.ExistItem(temp_tasks_data[13], 1)) >= 0)
			{
				Gmud.sPlayer.LoseItem(j1, 1);
				temp_tasks_data[15] = 1;
				temp_tasks_data[16] = 1;
//				wstring str(task_finish);
//				UI.DrawDialog(&str);
				UI.DrawDialog(task_finish);
				return;
			} else
			{
//				wstring str(item_wait);
//				UI.DrawDialog(&ReplaceStr(&str, L"o", Items::item_names[temp_tasks_data[13]]));
				String str = item_wait.replaceAll("o", Items.item_names[temp_tasks_data[13]]);
				UI.DrawDialog(str);
				
				return;
			}
		if (Gmud.sPlayer.exp < 3000)
			j1 = item_to_find[util.RandomInt(27)];
		else
			j1 = util.RandomInt(75) + 1;
		temp_tasks_data[13] = j1;
		temp_tasks_data[14] = 1;
		temp_tasks_data[15] = 0;
		temp_tasks_data[16] = 0;
		temp_tasks_data[17] = util.RandomInt(20+Gmud.sPlayer.GetSavvy() * 3);
		if (temp_tasks_data[17] < 5)
			temp_tasks_data[17] = 5;
//		wstring str(item_start);
//		UI.DrawDialog(&ReplaceStr(&str, L"o", Items::item_names[temp_tasks_data[13]]));
		String str = item_start.replaceAll("o", Items.item_names[temp_tasks_data[13]]);
		UI.DrawDialog(str);
	}

	static void FindPeople()
	{
		if (Gmud.sPlayer.exp > 0x13880 && Gmud.sPlayer.lasting_tasks[1] == 0 && Gmud.sPlayer.ExistItem(79, 1) < 0)
		{
			Gmud.sPlayer.GainOneItem(79);
			UI.ShowDialog(158);
			return;
		}
		for (int i1 = 0; i1 < 3; i1++)
			if (temp_tasks_data[11 + i1 * 5] == 1)
			{
//				wstring str(exist_award);
//				UI.DrawDialog(&str);;
				UI.DrawDialog(exist_award);
				return;
			}

		if (temp_tasks_data[9] != 0)
			if (temp_tasks_data[10] != 0)
			{
				temp_tasks_data[11] = 1;
//				wstring str(task_finish);
//				UI.DrawDialog(&str);;
				UI.DrawDialog(task_finish);
				return;
			} else
			{
//				wstring str(talk_wait);
//				UI.DrawDialog(&ReplaceStr(&str, L"o", NPC.NPC_names[temp_tasks_data[8]]));
				String str = talk_wait.replaceAll("o", NPC.NPC_names[temp_tasks_data[8]]);
				UI.DrawDialog(str);
				return;
			}
		int k1;
		if ((k1 = Gmud.sPlayer.exp) == 0)
			k1 = 100;
		int j1;
		if (Gmud.sPlayer.exp > 1000)
			j1 = 146;
		else
			j1 = 37 / (1000 / k1);
		if (j1 == 0)
			j1 = 1;
		int l1;
		for (l1 = 0; (l1 = util.RandomInt(j1)) == 5 || l1 == 9 || l1 == 30;);
		temp_tasks_data[8] = l1;
		temp_tasks_data[9] = 1;
		temp_tasks_data[10] = 0;
		temp_tasks_data[11] = 0;
		temp_tasks_data[12] = util.RandomInt(20+Gmud.sPlayer.GetSavvy() * 3);
		if (temp_tasks_data[12] < 5)
			temp_tasks_data[12] = 5;
//		wstring str(talk_start);
//		UI.DrawDialog(&ReplaceStr(&str, L"o", NPC.NPC_names[temp_tasks_data[8]]));
		String str = talk_start.replaceAll("o", NPC.NPC_names[temp_tasks_data[8]]);
		UI.DrawDialog(str);
	}

	static void OldWoman()
	{
		if (Gmud.sPlayer.exp > 5000)
		{
//			wstring str(old_woman_unable);
//			UI.DrawDialog(&str);
			UI.DrawDialog(old_woman_unable);
			return;
		}
		if (temp_tasks_data[24] == 1)
		{
//			wstring str(old_woman_wait);
//			UI.DrawDialog(&str);
			UI.DrawDialog(old_woman_wait);
			return;
		} else
		{
			temp_tasks_data[23] = util.RandomInt(3);
			temp_tasks_data[24] = 1;
//			wstring str(old_woman_start);
//			UI.DrawDialog(&ReplaceStr(&str, L"o", old_woman_task_name[temp_tasks_data[23]]));
			String str = old_woman_start.replaceAll("o", old_woman_task_name[temp_tasks_data[23]]);
			UI.DrawDialog(str);
			return;
		}
	}

	static void KillPeople()
	{
		for (int i1 = 0; i1 < 3; i1++)
			if (temp_tasks_data[11 + i1 * 5] == 1)
			{
//				wstring str(exist_award);
//				UI.DrawDialog(&str);
				UI.DrawDialog(exist_award);
				return;
			}

		if (temp_tasks_data[19] != 0)
			if (temp_tasks_data[20] != 0)
			{
				temp_tasks_data[21] = 1;
//				wstring str(task_finish);
//				UI.DrawDialog(&str);
				UI.DrawDialog(task_finish);
				return;
			} else
			{
//				wstring str(kill_wait);
//				UI.DrawDialog(&ReplaceStr(&str, L"o", NPC.NPC_names[temp_tasks_data[18]]));
				String str = kill_wait.replaceAll("o", NPC.NPC_names[temp_tasks_data[18]]);
				UI.DrawDialog(str);
				return;
			}
		int j1;
		int k1;
		if ((k1 = GmudData.a(j1 = (j1 = Gmud.sPlayer.GetSkillAverageLevel() + 5) / 5)) == 0)
		{
			return;
		} else
		{
			int l1 = k1;
			int i2 = 0;
			i2 = GmudData.kill_task_temp_table[util.RandomInt(l1)];
			temp_tasks_data[18] = i2;
			temp_tasks_data[19] = 1;
			temp_tasks_data[20] = 0;
			temp_tasks_data[21] = 0;
			temp_tasks_data[22] = util.RandomInt(Gmud.sPlayer.GetSavvy() * 4 + Gmud.sPlayer.SetFaceLevel() * 2);
//			wstring str(kill_start);
//			UI.DrawDialog(&ReplaceStr(&str, L"o", NPC.NPC_names[temp_tasks_data[18]]));
			String str = kill_start.replaceAll("o", NPC.NPC_names[temp_tasks_data[18]]);
			UI.DrawDialog(str);
			return;
		}
	}

	static void KillWanted()
	{
		// 如果有恶人并且不在25分钟时间点上，就提示有恶人
//		if (temp_tasks_data[0] != 0 && temp_tasks_data[5] / 30 != 49)
		if (temp_tasks_data[0] != 0 && temp_tasks_data[5]<600)
		{
//			wstring str(bad_man_wait);
//			UI.DrawDialog(&ReplaceStr(&str, L"o", NPC.NPC_names[179]));
			String str = bad_man_wait.replaceAll("o", NPC.NPC_names[179]);
			UI.DrawDialog(str);
			return;
		}
		//　如果时间不到300s且有杀过恶人，就提示暂无恶人
		if (temp_tasks_data[5] < 300 && temp_tasks_data[1] > 0)
		{
//			wstring str(bad_man_nothing);
//			UI.DrawDialog(&str);
			UI.DrawDialog(bad_man_nothing);
			return;
		}
		// 累计恶人难度，以10为循环
		temp_tasks_data[34] += 1;
		temp_tasks_data[34] %= 10;
		// 如果有恶人且时间在25分钟，则将恶人难度归0————25分法？
		if(49 == temp_tasks_data[5] / 30 && 1 == temp_tasks_data[0])
			temp_tasks_data[34] = 0;
		
		// 随机恶人的地图位置
		int i1 = util.RandomInt(9);    //where
		temp_tasks_data[2] = i1;   
		while((task.bad_man_mapid = GmudData.bad_man_map[i1][util.RandomInt(16)]) == 0);   //set map image&event
		// 随机恶人的名字
		int l1 = util.RandomInt(8);  //random name 1
		int i2 = util.RandomInt(8);  //random name 2
		/*/
		wstring s1(bad_man_family_name[l1]); 
		s1 += bad_man_name[i2];
		wchar_t* tp = new wchar_t[4];
		wcscpy(tp, s1.c_str());      //set obj name
		NPC.NPC_names[179] = tp;
		//*/
		NPC.NPC_names[179] = bad_man_family_name[l1] + bad_man_name[i2];
		//wcscpy(NPC.NPC_names[179], s1.c_str()); 
		
		// 随机恶人原型，用于取技能
		int j2;
		int k2 = j2 = util.RandomInt(20);
		j2 = GmudData.bad_man_based_npc[j2];
		
		// 清除物品
		//equip clean
		for (int l2 = 0; l2 < 5; l2++)
			NPC.NPC_item[179][l2] = 0;
		
		// 给布衣和武器
		NPC.NPC_item[179][0] = 42;
		NPC.NPC_item[179][1] = GmudData.bad_man_weapon[k2];  //weapon
		
		// 复制属性
		for (int i3 = 0; i3 < 18; i3++)
			NPC.NPC_attrib[179][i3] = NPCINFO.NPC_attribute[j2][i3];

		Battle.sBattle = new Battle(0, 0, 1);
		Battle.sBattle.CopyPlayerData();
		NPC.NPC_attrib[179][5] = (Gmud.sPlayer.GetForce() - 5) + temp_tasks_data[34] * temp_tasks_data[1] + temp_tasks_data[1];
		NPC.NPC_attrib[179][6] = (Gmud.sPlayer.GetAgility() - 5) + (temp_tasks_data[34] * temp_tasks_data[1]) / 2 + temp_tasks_data[1] / 2;
		NPC.NPC_attrib[179][7] = (Gmud.sPlayer.GetSavvy() - 5) + temp_tasks_data[34] * temp_tasks_data[1] + temp_tasks_data[1];
		NPC.NPC_attrib[179][8] = (Gmud.sPlayer.GetAptitude() - 5) + temp_tasks_data[34] * temp_tasks_data[1] + temp_tasks_data[1];
		NPC.NPC_attrib[179][9] = Battle.sBattle.CalcHit(0);
		NPC.NPC_attrib[179][10] = Battle.sBattle.CalcAvoid(0);
		NPC.NPC_attrib[179][13] = Gmud.sPlayer.fp_level / 2 + (temp_tasks_data[1] * Gmud.sPlayer.fp_level) / 5;
		NPC.NPC_attrib[179][14] = Gmud.sPlayer.fp_level / 2 + (temp_tasks_data[1] * Gmud.sPlayer.fp_level) / 5;
		NPC.NPC_attrib[179][16] = util.RandomInt(temp_tasks_data[1] * 300 + temp_tasks_data[34] * temp_tasks_data[1] * 200) + 90;
		
		// 累计除恶次数
		temp_tasks_data[1] += 1;
		
		// 设置恶人的经验
		NPC.NPC_attrib[179][3] = 100 + temp_tasks_data[1] * (Gmud.sPlayer.exp / 10);
		// 如果玩家有内力，则生成加力
		if (Gmud.sPlayer.fp_level > 0)
			NPC.NPC_attrib[179][4] = temp_tasks_data[1] + temp_tasks_data[34] * temp_tasks_data[1];
		else
			NPC.NPC_attrib[179][4] = 0;
		NPC.NPC_attrib[179][1] = 0;
		NPC.NPC_attrib[179][0] = 77;
		int j3 = Gmud.sPlayer.hp_full / 2 + (temp_tasks_data[1] * Gmud.sPlayer.hp_full) / 4;
		NPC.NPC_attrib[179][11] = NPC.NPC_attrib[179][12] = NPC.NPC_attrib[179][15] = j3;
		// 取玩家最猛的一个技能的等级
		int k3 = Gmud.sPlayer.GetMaxSkillLevel();
		Battle.sBattle.CalcFighterLevel(0);
		// 如果玩家整体等级大于20
		if (Battle.sBattle.fighter_data[0][62] > 20)
			k3 -= 10;
//		delete glpBattle;
//		glpBattle = 0;
		Battle.sBattle = null;
		// 计算恶人的每个技能等级（都是一样等级）
		int i4 = k3 + (temp_tasks_data[34] * temp_tasks_data[1]) / 2 + (temp_tasks_data[1] - 1) * 3;
		if (temp_tasks_data[1] > 7)
			i4 -= 5;
		if (i4 > 250)
			i4 = 250;
		if (i4 < 0)
			i4 = 0;
		// 取恶人原型的技能表
		NPCSKILLINFO nsk= new NPCSKILLINFO();
		NPC.GetNPCSkill(nsk, j2);

		int j4 = nsk.data[0];
		bad_man_skill[0] = j4;
		for (int k4 = 0; k4 < j4; k4++)
		{
			bad_man_skill[1 + k4 * 2] = nsk.data[1 + k4 * 2];
			bad_man_skill[1 + k4 * 2 + 1] = i4;
		}
//		free(nsk.data);
		// 实战经验奖励
		temp_tasks_data[3] = util.RandomInt(Gmud.sPlayer.bliss*6+Gmud.sPlayer.GetSavvy() * 2 + Gmud.sPlayer.SetFaceLevel() * 4 + temp_tasks_data[1] * 40) + temp_tasks_data[1] * 100;
		// 潜能奖励(与福缘关联起来)
		temp_tasks_data[4] = util.RandomInt(Gmud.sPlayer.bliss*3+Gmud.sPlayer.GetSavvy() + Gmud.sPlayer.SetFaceLevel() * 2 + temp_tasks_data[1] * 20) + temp_tasks_data[1] * 50;
//		wstring str(bad_man_start);
//		UI.DrawDialog(&ReplaceStr(&ReplaceStr(&str, L"o", NPC.NPC_names[179]), L"m", GmudData::map_name[temp_tasks_data[2]]));
		String str = bad_man_start.replaceAll("o", NPC.NPC_names[179]);
		UI.DrawDialog(str.replaceAll("m", GmudData.map_name[temp_tasks_data[2]]));
		// 如果是25分法，就增加一些特别奖励
		if(49 == temp_tasks_data[5] / 30 && 1 == temp_tasks_data[0])
		{
			temp_tasks_data[3] |= 0x61E;
			temp_tasks_data[4] |= 0x30F;
		}
		temp_tasks_data[5] = 0;
		temp_tasks_data[0] = 1;
		Map.NPC_flag[179] = 0;
	}



}
