package cn.fmsoft.lnx.gmud.simple.core;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class uibattle {

	static int hit_id = -1;
	static int menu_id = 0;
	static Bitmap player_img;
	static Bitmap NPC_img;
	static Bitmap hp_img;
	static Bitmap fp_img;
	static Bitmap mp_img;
	static Bitmap hit_img;
	static final String menu_title[] =new String[] {
		"普通攻击", "绝招攻击", "使用内力", "使用物品", "调整招式", "逃跑"
	};
	
	
//	vector<wstring> desc_words;
	static ArrayList<String> desc_words;

	static int weapon_id[] = new int[2];

	/**
	 * 绘制内力菜单，只有  {加力} 和 {吸气}
	 * @param i 当前选择
	 */
	static void DrawFPMenu(int i) {
		Video.VideoClearRect(60, 50, 39, 28);
		Video.VideoDrawRectangle(60, 50, 39, 28);
		for (int i3 = 0; i3 < 2; i3++) {
			Video.VideoDrawStringSingleLine(UI.fp_menu_words[i3 + 1], 72, 51 + 13 * i3);
			if (i3 == i)
				UI.DrawCursor(60 + 4, 52 + 12 * i3);
		}
	}

	/**
	 * 加力菜单
	 */
	static void DrawPlusFp() {
		
		final int id_active = Battle.sBattle.active_id;
		
		// 无内功，直接返回
		if (Battle.sBattle.fighter_data[id_active][36] == 255) {
			UI.DrawStringFromY(Res.STR_NO_INNER_KONGFU_STRING, 660);
			Video.VideoUpdate();
			Gmud.GmudDelay(1500);
			return;
		}
		
		// 加力上限
		final int max = Gmud.sPlayer.GetPlusFPMax();
		// 当前加力
		final int cur_base = Battle.sBattle.fighter_data[id_active][0];
		
		int cur = cur_base;
		
		int k1 = 80 - 70 - 4;
		int l1;
		l1 = (l1 = (l1 = 13) + (16 + 2)) + 12 * 5;
		int i2;
		if ((i2 = 80 - l1) > 0)
			i2 /= 2;
		else
			i2 = 0;
		i2 += 13 + 16 + 16 + 16;
		UI.DrawNumberBox(cur, max, k1, i2);
		Video.VideoUpdate();
		Input.ClearKeyStatus();
		Gmud.GmudDelay(100);
		while (true) {
			if ((Input.inputstatus & Input.kKeyUp) != 0) // up
			{
				if (cur > 0)
					cur--;
				UI.DrawNumberBox(cur, max, k1, i2);
				Video.VideoUpdate();
				Gmud.GmudDelay(50);
			} else if ((Input.inputstatus & Input.kKeyDown) != 0) // down
			{
				if (cur < max)
					cur++;
				UI.DrawNumberBox(cur, max, k1, i2);
				Video.VideoUpdate();
				Gmud.GmudDelay(50);
			} else {
				if ((Input.inputstatus & Input.kKeyEnt) != 0) // enter
				{
					Battle.sBattle.fighter_data[id_active][0] = cur;
					Battle.sBattle.a(id_active, 0, cur);
					if (cur == max) {
						String str = String.format(Res.STR_FP_PLUS_LIMIT_STRING, max);
						UI.DrawStringFromY(str, 660);
						Video.VideoUpdate();
						Input.ClearKeyStatus();
						Gmud.GmudDelay(1500);
						return;
					} else {
						Input.ClearKeyStatus();
						Gmud.GmudDelay(50);
						return;
					}
				}
				if ((Input.inputstatus & Input.kKeyExit) != 0) // Esc
					if (max == cur_base) {
						String str = String.format(Res.STR_FP_PLUS_LIMIT_STRING, max);
						UI.DrawStringFromY(str, 660);
						Video.VideoUpdate();
						Input.ClearKeyStatus();
						Gmud.GmudDelay(1500);
						return;
					} else {
						Gmud.GmudDelay(50);
						return;
					}
			}
			Input.ClearKeyStatus();
			Gmud.GmudDelay(80);
		}
	}

	static void UseFPMenu()
	{
		int l = 0;
		DrawMain();
		DrawFPMenu(0);
		Video.VideoUpdate();
		Input.ClearKeyStatus();
		Gmud.GmudDelay(100);
		while (true)
		{
			Input.ProcessMsg();
			if ((Input.inputstatus & Input.kKeyUp)!=0)  //up key
			{
				if (l > 0)
					l--;
				else
					l = 1;
				DrawFPMenu(l);
				Video.VideoUpdate();
				Gmud.GmudDelay(50);
			} else
			if ((Input.inputstatus & Input.kKeyDown)!=0)   //down
			{
				if (l < 1)
					l++;
				else
					l = 0;
				DrawFPMenu(l);
				Video.VideoUpdate();
				Gmud.GmudDelay(50);
			} else
			if ((Input.inputstatus & Input.kKeyEnt)!=0)  //enter
			{
				if (l == 0)
				{
					DrawPlusFp();
					DrawMain();
					DrawFPMenu(l);
					Video.VideoUpdate();
					Gmud.GmudDelay(50);
				} else
				if (l == 1)
				{
//					wstring str(Battle.sBattle.Breath());
					String str = Battle.sBattle.Breath();
					UI.DrawStringFromY(str, 660);
					Video.VideoUpdate();
					Gmud.GmudDelay(1500);
					DrawMain();
					DrawFPMenu(l);
					Video.VideoUpdate();
					Gmud.GmudDelay(50);
				}
			} else
			if ((Input.inputstatus & Input.kKeyExit)!=0)  //esc
			{
				Input.ClearKeyStatus();
				Gmud.GmudDelay(50);
				return;
			}
			Input.ClearKeyStatus();
			Gmud.GmudDelay(80);
		}
	}

	static void Main()
	{
		DrawMain();
		Video.VideoUpdate();
		Gmud.GmudDelay(100);
		while (true)
		{
			Input.ProcessMsg();
			if ((Input.inputstatus & Input.kKeyLeft)!=0)  //left
			{
				if (menu_id == 0)
					menu_id = 5;
				else
					menu_id--;
				DrawMain();
				Video.VideoUpdate();
				Gmud.GmudDelay(50);
			} else
			if ((Input.inputstatus & Input.kKeyRight)!=0)    //right
			{
				if (menu_id == 5)
					menu_id = 0;
				else
					menu_id++;
				DrawMain();
				Video.VideoUpdate();
				Gmud.GmudDelay(50);
			} else
			if ((Input.inputstatus & Input.kKeyExit)!=0)  //esc
			{
				menu_id = 0;
				DrawMain();
				Video.VideoUpdate();
				Gmud.GmudDelay(50);
			}
			else
			if ((Input.inputstatus & Input.kKeyEnt)!=0)   //enter
			{
				if (menu_id == 0)  //"普通攻击",
				{
					Battle.sBattle.PhyAttack(false);
					Input.ClearKeyStatus();
					return;
				}
				if (menu_id == 1)  // "绝招攻击"
				{
					int i1 = MagicMenu();
					DrawMain();
					Video.VideoUpdate();
					Input.ClearKeyStatus();
					Gmud.GmudDelay(80);
					if (i1 > 0)
						return;
				} else
				if (menu_id == 2)  //"使用内力"
				{
					UseFPMenu();
					DrawMain();
					Video.VideoUpdate();
					Input.ClearKeyStatus();
					Gmud.GmudDelay(50);
				} else
				if (menu_id == 3)  //"使用物品"
				{
					UI.PlayerItem();
					DrawMain();
					Video.VideoUpdate();
					Gmud.GmudDelay(80);
				} else
				if (menu_id == 4)  //"调整招式"
				{
					UI.PlayerSkill();
					DrawMain();
					Video.VideoUpdate();
					Gmud.GmudDelay(80);
				} else
				if (menu_id == 5)  //"逃跑"
				{
					if (Battle.sBattle.CalcActOrder() == 0)
					{
						Battle.sBattle.b(2, 36, 1);
						Input.ClearKeyStatus();
						return;
					}
					Battle.sBattle.d_int_static = 1;
					//Battle.sBattle.b_int_static = -1;
					Input.ClearKeyStatus();
					return;
				}
			}
			Input.ClearKeyStatus();
			Gmud.GmudDelay(80);
		}
	}

	/**
	 * 绘制血条
	 * @param cur 当前血量
	 * @param max 最大血量
	 * @param full 满血
	 * @param x 坐标
	 * @param y 
	 * @param show_percent 是否输出数字，如  99/100
	 */
	static void DrawHPRect(int cur, int max, int full, int x, int y, boolean show_percent)
	{
		int length = Gmud.WQX_ORG_WIDTH / 4;
		full *= 1000;
		int per = full / length;
		if (full % length > 0)
			per++;
		if (per <= 0)
			per = 1;
		
		int len;
		
		len = (cur*1000) / per;
		if (len > length)
			len = length;
		Video.VideoFillRectangle(x, y, len, 5);
		
		len = (max*1000) / per;
		if (len > length)
			len = length;
		Video.VideoFillRectangle(x, y + 7, len, 2);
		
		if(show_percent)
		{
//			wchar_t num[8];
//			wstring str(_itow(i / 1000, num, 10));
//			str += "/";
//			str += _itow(l / 1000, num, 10);
			String str = String.format("%d/%d", cur, max);
			Video.VideoDrawNumberData(str, x + length + 2, y + 2);
		}
	}

	static void DrawMain()
	{
		Video.VideoClear();
		int l;
		int i = l = 12;
		int i1 = Battle.sBattle.player_id==0?1:0;
		l = (l += 16 + 2) + 12 * 5;
		int j1;
		if ((j1 = 80 - l) > 0)
			j1 /= 2;
		else
			j1 = 0;
		int k1 = (160 - 8) / 2 + 30;
		Video.VideoDrawStringSingleLine(Battle.sBattle.player_name/*.c_str()*/, 4, j1);
		Video.VideoDrawStringSingleLine(Battle.sBattle.NPC_name/*.c_str()*/, k1, j1);
		j1 += i + 2;
		if (hit_id == Battle.sBattle.player_id)
			Video.VideoDrawImage(hit_img, 18, j1);    //draw hit
		else
			Video.VideoDrawImage(player_img, 18, j1);
		if (hit_id == i1)
			Video.VideoDrawImage(hit_img, k1 + 10, j1);   //draw hit
		else
			Video.VideoDrawImage(NPC_img, k1 + 10, j1);
		hit_id = -1;
		j1 += 19;
		Video.VideoDrawImage(hp_img, 4, j1);
		int l1 = 4 + 20;
		DrawHPRect(Battle.sBattle.fighter_data[Battle.sBattle.player_id][1], Battle.sBattle.fighter_data[Battle.sBattle.player_id][2], Battle.sBattle.fighter_data[Battle.sBattle.player_id][3], l1, j1 + 4, true);
		DrawHPRect(Battle.sBattle.fighter_data[i1][1], Battle.sBattle.fighter_data[i1][2], Battle.sBattle.fighter_data[i1][3], k1 + 2, j1 + 4, false);
		j1 += 16;
		Video.VideoDrawImage(fp_img, 4, j1);
		DrawHPRect(Battle.sBattle.fighter_data[Battle.sBattle.player_id][4], Battle.sBattle.fighter_data[Battle.sBattle.player_id][5], Battle.sBattle.fighter_data[Battle.sBattle.player_id][5], l1, j1 + 4, true);
		DrawHPRect(Battle.sBattle.fighter_data[i1][4], Battle.sBattle.fighter_data[i1][5], Battle.sBattle.fighter_data[i1][5], k1 + 2, j1 + 4, false);
		j1 += 16;
		if(Battle.sBattle.fighter_data[Battle.sBattle.player_id][42] != 255)
		{
			Video.VideoDrawImage(mp_img, 4, j1);
			DrawHPRect(Battle.sBattle.fighter_data[Battle.sBattle.player_id][6], Battle.sBattle.fighter_data[Battle.sBattle.player_id][7], Battle.sBattle.fighter_data[Battle.sBattle.player_id][7], l1, j1 + 4, true);
			j1 += 16;
		}
		//u.a(0);
		int i2 = 61;
		int j2 = 50;
		for (int k2 = 0; k2 < 6; k2++)
			if (k2 == menu_id)
			{
				Video.VideoFillRectangle(j2 + k2 * 8, i2, 7, 7);
				Video.VideoDrawStringSingleLine(menu_title[k2], j2 - 2, i2 + 7);
			} else
			{
				Video.VideoDrawRectangle(j2 + k2 * 8, i2, 6, 6);
			}
	}

	
	/**
	 * 描述过招内容
	 * @param attack_type 出招类型 0:物理攻击  1:技能攻击 2:攻击结果 3:??自定义描述文本于desc_words中 4:伤害描述
	 * @param attack_desc 描述文本ID
	 */
	static void PhyAttack(int attack_type, int attack_desc)
	{
		String str_desc;
		int id_active = Battle.sBattle.active_id;
		int id_player = Battle.sBattle.player_id;
		switch (attack_type)
		{
		case 0: //物理攻击
			str_desc = Skill.GetAttackDesc(attack_desc);
			if (id_active == id_player)
			{
				str_desc = util.ReplaceStr(str_desc, "N", "你");
				str_desc = util.ReplaceStr(str_desc, "SB", Battle.sBattle.NPC_name);
				str_desc = util.ReplaceStr(str_desc, "SW", Items.item_names[weapon_id[id_active]]);
			} else
			{
				str_desc = util.ReplaceStr(str_desc, "N", Battle.sBattle.NPC_name);
				str_desc = util.ReplaceStr(str_desc, "SB", "你");
				str_desc = util.ReplaceStr(str_desc, "SW", Items.item_names[weapon_id[id_active]]);
			}
			
			// 攻击部位
			String s;
			int hit_point = Battle.sBattle.a_int_array1d_static[3];
			if (hit_point != -1)
				s = GmudData.hit_point_name[hit_point];
			else {
				s = "";
			}
			str_desc = util.ReplaceStr(str_desc, "SP", s/*.c_str()*/);
			break;
		case 1:
			{
				str_desc = Magic.GetMagicDesc(attack_desc);
				int k2 = Battle.sBattle.a_int_array1d_static[13];
				if (id_active == id_player)
				{
					str_desc = util.ReplaceStr(str_desc, "SB", Battle.sBattle.NPC_name/*.c_str()*/);
					str_desc = util.ReplaceStr(str_desc, "SW", Items.item_names[weapon_id[id_active]]);
				} else
				{
					str_desc = util.ReplaceStr(str_desc, "你", Battle.sBattle.NPC_name/*.c_str()*/);
					str_desc = util.ReplaceStr(str_desc, "SB", "你");
					str_desc = util.ReplaceStr(str_desc, "SW", Items.item_names[weapon_id[id_active]]);
				}
				if (k2 != -1)
					str_desc = util.ReplaceStr(str_desc, "~", Magic.magic_name[k2]);
				break;
			}
		case 2: //hit result
			{
				int j3;
				str_desc = Skill.GetHitDesc(j3 = attack_desc & 0xff);
				int k3 = attack_desc / 256;
//				wstring s2("");
				String s2="";
				if (k3 > 36)
					s2 = Skill.GetHitDesc(k3);
				if (id_active == id_player)
				{
					str_desc = util.ReplaceStr(str_desc, "SB", Battle.sBattle.NPC_name/*.c_str()*/);
					if (j3 != 36)
					{
						str_desc += "(";
						str_desc += Battle.sBattle.NPC_name;
						str_desc += s2/*.c_str()*/;
						str_desc += ")";
					}
					break;
				}
				str_desc = util.ReplaceStr(str_desc, "你", Battle.sBattle.NPC_name/*.c_str()*/);
				str_desc = util.ReplaceStr(str_desc, "SB", "你");
				if (j3 != 36)
				{
					str_desc += "(你";
					str_desc += s2/*.c_str()*/;
					str_desc += ")";
				}
				break;
			}
		case 3: //？？？
//			s1 = desc_words[l];
			str_desc = desc_words.get(attack_desc);
			if (id_active == id_player)
			{
				str_desc = util.ReplaceStr(str_desc, "N", "你");
				str_desc = util.ReplaceStr(str_desc, "SB", Battle.sBattle.NPC_name/*.c_str()*/);
			} else
			{
				str_desc = util.ReplaceStr(str_desc, "N", Battle.sBattle.NPC_name/*.c_str()*/);
				str_desc = util.ReplaceStr(str_desc, "SB", "你");
			}
			break;

		case 4: //伤害描述
			str_desc = Skill.GetHitDesc(attack_desc);
			if (id_active == id_player)
			{
				str_desc = util.ReplaceStr(str_desc, "SB", Battle.sBattle.NPC_name/*.c_str()*/);
			} else
			{
				str_desc = util.ReplaceStr(str_desc, "你", Battle.sBattle.NPC_name/*.c_str()*/);
				str_desc = util.ReplaceStr(str_desc, "SB", "你");
			}
			break;
			
		default:
			str_desc = "";
			break;
		}
		DrawMain();
		UI.DrawStringFromY(str_desc, 660);
		Video.VideoUpdate();
	}

	static void DrawMagicMenu(int i, int l, int i1)
	{
		int j1;
		if ((j1 = Gmud.WQX_ORG_WIDTH / 2 - 84 - 4) <= 0)
			j1 = 10;
		int k1;
		int l1;
		l1 = (l1 = (l1 = k1 = 12) + (16 + 2)) + 12 * 5;
		int i2;
		if ((i2 = Gmud.WQX_ORG_WIDTH /2 - l1) > 0)
			i2 /= 2;
		else
			i2 = 0;
		i2 += 12 + 16 + 16;
		int j2;
		int k2 = (j2 = 13) * 6 + 8;
		int l2 = k1 + 4;
		if (Battle.sBattle.a_int_array2d_static[i][l + 1] >= 0 && Battle.sBattle.a_int_array2d_static[i][l + 1] < 39)
			l2 += k1;
		Video.VideoClearRect(j1, i2, k2, l2);
		Video.VideoDrawRectangle(j1, i2, k2, l2);
		int j3;
		for (int i3 = 0; i3 < 2 && (j3 = Battle.sBattle.a_int_array2d_static[i][l + i3]) >= 0; i3++)
		{
			if (j3 > 38)
				return;
			Video.VideoDrawStringSingleLine(Magic.magic_name[j3], j1 + 18, i2 + 1 + i3 * (k1 + 1));
			if (i3 == i1)
				UI.DrawCursor(j1 + (j2 - 8) / 2, i2 + i3 * (k1 + 1) + (k1 - 9) / 2);
		}
	}

	static int MagicMenu()
	{
		int l = 0;
		int i1 = 0;
		int j1;
		Magic.Effect(j1 = Battle.sBattle.player_id);
		int k1;
		if ((k1 = Battle.sBattle.g(j1)) < 1)  //get Battle skill number
			return 0;
		DrawMagicMenu(Battle.sBattle.player_id, 0, 0);
		Video.VideoUpdate();
		Input.ClearKeyStatus();
		Gmud.GmudDelay(150);
		while(true)
		{
			Input.ProcessMsg();
			if ((Input.inputstatus & Input.kKeyUp)!=0)  //UP
			{	//goto label1;
				if (i1 <= 0)
				{
					if (l > 0)
					{
						l--;
						Input.ClearKeyStatus();
						continue;
					}
					if ((l = k1 - 2) < 0)
						l = 0;
					if (k1 >= 2)
					{
						i1 = 1;
						Input.ClearKeyStatus();
						continue;
					}
				}
				i1 = 0;
				DrawMagicMenu(Battle.sBattle.player_id, l, i1);
				Video.VideoUpdate();
				Input.ClearKeyStatus();
				Gmud.GmudDelay(50);
				continue;
			}
				if ((Input.inputstatus & Input.kKeyDown)!=0)   //down 
				{
					if (i1 < 1 && l + i1 < k1 - 1)
						i1 = 1;
					else
					if (l < k1 - 2)
					{
						l++;
					} else
					{
						l = 0;
						i1 = 0;
					}
					DrawMagicMenu(Battle.sBattle.player_id, l, i1);
					Video.VideoUpdate();
					Gmud.GmudDelay(50);
				} else
				{
					int l1;
					String s;
					if ((Input.inputstatus & Input.kKeyEnt)!=0)  //enter
						if ((s = Magic.UseMagic(l1 = Battle.sBattle.a_int_array2d_static[Battle.sBattle.player_id][l + i1])).length() == 0)
						{
							return 1;
						} else
						{
							UI.DrawStringFromY(s, 660);
							Video.VideoUpdate();
							Gmud.GmudDelay(1200);
							return 0;
						}
					if ((Input.inputstatus & Input.kKeyExit)!=0)  //Esc
						return 0;
				}
			Input.ClearKeyStatus();
			Gmud.GmudDelay(80);
		}
	}
}
