package cn.fmsoft.lnx.gmud.simple.core;

import cn.fmsoft.lnx.gmud.simple.core.NPC.NPCSKILLINFO;

public class Battle {
	String player_name;
	String NPC_name;
	int a_int_array2d_static[][]=new int[2][10];
	int c_int_array2d_static[][]=new int[2][9];
	int fighter_data[][] = new int[2][256];
	int is_try;
	int player_id;
	int active_id;
	int NPC_id;
	int NPC_image_id;
	short NPC_item[] = new short[5];
	int NPC_equip[] = new int[16];
	int NPC_select_skill[]= new int[16];
	
	// 3元数组，[0]- active-id 
	int a_int_array1d_static[] = new int[256];
	int b_int_array1d_static[] = new int[256];
	
	int d_int_static;  //end flag ?
	int e_int_static;

	private int f_int_array1d_static[] = new int[4];
	private boolean a_boolean_static;

	static Battle sBattle;
	
	
	public Battle(int npc, int nimg, int tryflag)
	{
		NPC_id = npc;
		NPC_image_id = nimg;
		is_try = tryflag;
		player_id = 0;
		active_id = 0;
		for (int i1 = 0; i1 < 256; i1++)
		{
			b_int_array1d_static[i1] = -1;
			fighter_data[0][i1] = fighter_data[1][i1] = 0;
		}

		e_int_static = 0;
	}

	int CalcAttackLevel(int i1)
	{
		int j1 = fighter_data[i1][8];
		int k1;
		if ((k1 = fighter_data[i1][29]) != 0)
			j1 += Items.item_attribs[k1][2];
		if ((j1 = (j1 += fighter_data[i1][0]) / 20) > 5)
			j1 = 5;
		return j1;
	}

	int g(int i1)
	{
		int j1 = 0;
		for (int k1 = 0; k1 < 10 && a_int_array2d_static[i1][k1] >= 0 && a_int_array2d_static[i1][k1] < 40; k1++)
			j1++;

		return j1;
	}

	/**
	 * 记录伤害结果？ j1:
	 * @param active_id
	 * @param j1 0=fpPlus 1=hp 2=hp_max 4=fp
	 * @param k1
	 */
	void a(int active_id, int j1, int k1)
	{
		for (int l1 = 0; l1 < e_int_static; l1 += 3)
			if (b_int_array1d_static[l1 + 32] == active_id && b_int_array1d_static[l1 + 1 + 32] == j1)
			{
				b_int_array1d_static[l1 + 2 + 32] = k1;
				return;
			}

		if (e_int_static + 3 > 256 - 32)
		{
			return;
		} else
		{
			b_int_array1d_static[e_int_static + 32] = active_id;
			b_int_array1d_static[e_int_static + 1 + 32] = j1;
			b_int_array1d_static[e_int_static + 2 + 32] = k1;
			e_int_static += 3;
			return;
		}
	}

	// TODO:
	// Bug: 0级买麻绳与卖花妞战斗，普通攻击的描述变成“闪避”类的文本
	// 相关变量：b_int_array1d_static[5] = 246  c_int_array2d_static[?][8]
	int PhyAttack(boolean flag)
	{
		int ai[]=new int[2];
		b_int_array1d_static[3] = util.RandomInt(16);
		ai[0] = ai[1] = 0;
		int i1 = active_id;
		int j1 = active_id != 0 ? 0 : 1;
//	    int i1;
//	    if ((i1 = a(n = jdField_g_of_type_Int == 0 ? 1 : 0, 1)) == 0)
//	      return 0;
		int k1 = 50;
		if (active_id == player_id)
			k1 = 60;
		int l1;
		if ((l1 = f(fighter_data[i1][64] / 100) - f(fighter_data[j1][64] / 100)) < -100)
			l1 = -100;
		if (l1 > 100)
			l1 = 100;
		int i2 = 0;
		int j2 = fighter_data[i1][29]; // 武器
		if (flag && f_int_array1d_static[3] >= 0)
			fighter_data[i1][29] = f_int_array1d_static[3]; // 暂时换了武器？
		int k2 = fighter_data[i1][9] - fighter_data[j1][9]; // 敏捷相差
		int l2;
		if ((l2 = CalaAvtiveSpeed(i1, 2, 4)) > 0 && l2 < 20)
			k2 += CalaAvtiveSpeed(i1, 1, 0);
		int i3 = 0;
		for (int j3 = 0; j3 < 2; j3++)
		{
			if ((i2 = fighter_data[j3][29]) == 0)
			{ //空手
				int i4;
				if ((i4 = fighter_data[j3][30]) == 255)
				{ // 无拳脚技能
					c_int_array2d_static[j3][8] = ib(j3, 1, 1);
				} else
				{
					c_int_array2d_static[j3][8] = ib(j3, i4, fighter_data[j3][31]);
					ai[j3] = fighter_data[j3][31];
				}
				continue;
			}
			int j4 = fighter_data[j3][32]; // [30+2] 已选择的兵刃技能
			int i5 = Items.item_attribs[i2][1]; // 武器类型
			int j6 = 0;
			if (j4 != 255)
				j6 = Skill.skill_weapon_type[j4];
			if (j6 == i5) // 武器类型相同？
			{
				int k5 = j4;
				c_int_array2d_static[j3][8] = ib(j3, k5, fighter_data[j3][33]);
				ai[j3] = fighter_data[j3][33];
			} else
			{ // XXX: 鞭类武器(9)时，描述文本变成闪避
				int l5 = Skill.c[i5];
				c_int_array2d_static[j3][8] = ib(j3, l5, 1);
			}
		}

		if (flag && f_int_array1d_static[0] >= 0)
		{
			c_int_array2d_static[i1][8] = f_int_array1d_static[0];
			int k3 = f_int_array1d_static[0];
			for (int k4 = 0; k4 < 8; k4++)
				c_int_array2d_static[i1][k4] = Skill.a[k3][k4];

		}
		i3 = 0 + (c_int_array2d_static[i1][4] <= 127 ? c_int_array2d_static[i1][4] : -(256 - c_int_array2d_static[i1][4]));
		int l3;
		int l4;
		if ((l3 = (l4 = fighter_data[i1][62] / 5 - fighter_data[j1][62] / 5) * 12) < -60)
			l3 = -60;
		if (l3 > 60)
			l3 = 60;
		int j5 = CalcHit(i1);   //命中率
		if ((k1 += j5 + l3 + i3 + k2 + l1) > 99)
			k1 = 99;
		if (k1 < 1)
			k1 = 1;
		int i6;
		if ((i6 = CalaAvtiveSpeed(j1, 4, 4)) > 0 && i6 < 20)
			k1 = 100;
		if (util.RandomBool(k1))
		{
			int k6 = CalcAvoid(j1);   //回避率
			int j7 = 0;
			int j8;
			if ((j8 = fighter_data[j1][34]) > 0 && j8 < 94)
				j7 = f(fighter_data[j1][35]);
			k6 += j7;
			if (i6 > 0 && i6 < 20)
				k6 = 0;
			if (util.RandomBool(k6))
			{
				int l8 = c_int_array2d_static[i1][8];
				int k9 = 0;
				b(0, l8, 1);
				if (fighter_data[j1][34] != 255)
					k9 = ib(j1, fighter_data[j1][34], fighter_data[j1][35]);
				else
					k9 = util.RandomInt(5) + 243;
				d(0, k9, 1);
				fighter_data[i1][29] = j2;
				return 0;
			}
			int i9 = 0;
			i9 = 0 + fighter_data[j1][8];
			if (fighter_data[j1][54] != 255)
				i9 += fighter_data[j1][55] / 2;
			if (fighter_data[j1][38] != 255)
				i9 += fighter_data[j1][39];
			i9 = f(i9 += c_int_array2d_static[j1][5] <= 127 ? c_int_array2d_static[j1][5] : -(255 - c_int_array2d_static[j1][5]));
			if (i6 > 0 && i6 < 20)
				i9 = 0;
			if (util.RandomBool(i9))
			{
				int l9 = c_int_array2d_static[i1][8];
				b(0, l9, 1);
				int k10;
				if (i2 == 0)
					k10 = 252;
				else
					k10 = util.RandomInt(4) + 248;
				d(0, k10, 1);
				fighter_data[i1][29] = j2;
				return 0;
			}
		} else
		{
			int l6 = c_int_array2d_static[i1][8];
			if (util.RandomBool(65))
			{ // ? 如果击中了，加入攻击描述
				b(0, l6, 1);
				int k7;
				if (i2 == 0)
					k7 = 252;
				else
					k7 = util.RandomInt(4) + 248;
				d(0, k7, 1);
			} else
			{
				int l7 = 0;
				b(0, l6, 1);
				if (fighter_data[j1][34] != 255)
					l7 = ib(j1, fighter_data[j1][34], fighter_data[j1][35]);
				else
					l7 = util.RandomInt(5) + 243;
				d(0, l7, 1);
			}
			fighter_data[i1][29] = j2; // 恢复武器
			return 0;
		}
		if ((i6 = CalaAvtiveSpeed(j1, 5, 4)) > 0 && i6 < 20 && util.RandomBool(CalaAvtiveSpeed(j1, 5, 0)))
		{
			b(0, c_int_array2d_static[i1][8], 1);
			d(1, CalaAvtiveSpeed(j1, 5, 2), 1);
			fighter_data[i1][29] = j2;
			return 0;
		}
		int i8 = CalcAttack(i1);  //计算攻击
		int i7 = 0 + i8;
		fighter_data[i1][29] = j2;
		int k8 = c_int_array2d_static[i1][6];
		i7 += c_int_array2d_static[i1][7];
		// XXX: 不同？！
		if (fighter_data[i1][29] > 800 || fighter_data[i1][30] > 800)
			Video.exit(1);   //error exit
		int j9 = fighter_data[i1][0];
		if (fighter_data[i1][4] > 0)
		{
			int i10;
			if ((i10 = b(i1, 4)) < 0)
				i10 = fighter_data[i1][4];
			if (i10 >= j9)
			{
				i10 -= j9;
			} else
			{
				j9 = i10;
				i10 = 0;
			}
			a(i1, 4, i10);
		} else
		{
			j9 = 0;
		}
		i7 += i2 == 0 ? j9 : j9 / 2;
		if (flag)
			i7 += f_int_array1d_static[1];
		if ((i7 = (i7 = (i7 -= CalcDefenseB(j1)) - c_int_array2d_static[j1][1] * ai[j1]) - fighter_data[j1][4] / 10) < 0)
			i7 = 1;
		if (i7 < 8)
			i7 += 8;
		int j10 = 10 + (c_int_array2d_static[i1][3] <= 127 ? c_int_array2d_static[i1][3] : -(256 - c_int_array2d_static[i1][3]));
		if (flag)
			j10 += f_int_array1d_static[2];
		if (j10 < 1)
			j10 = 1;
		if (j10 > 70)
			j10 = 70;
		if (util.RandomBool(j10))
		{
			i7 += util.RandomInt(fighter_data[i1][8]);
			if (i2 == 0)
				k8 = i7;
		}
		int l10;
		if (util.RandomBool((l10 = fighter_data[j1][11]) * 2) && (i7 -= i7 / 3) > k8)
			k8 = i7;
		int i11;
		if (util.RandomBool(i11 = CalcDefense(j1)))
			k8 = 0;
		if (k8 > i7)
			k8 = i7;
		if (i7 <= i8 / 2)
			i7 = i8 / 2 + util.RandomInt(i8 / 2);
		int j11 = c_int_array2d_static[i1][8];
		b(0, j11, 1);
		int k11 = 0;
		int l11 = 0;
		if ((i2 = fighter_data[i1][29]) == 0)
		{
			int i12 = 1;
			if (fighter_data[i1][30] != 255)
				i12 = fighter_data[i1][30];
			k11 = ia(0, i7, fighter_data[j1][3], i12);
		} else
		{
			int j12 = 1;
			if (fighter_data[i1][32] != 255)
				j12 = fighter_data[i1][32];
			int l12;
			k11 = ia(l12 = Items.item_attribs[i2][1], i7, fighter_data[j1][3], j12);
		}
		int k12 = b(j1, 1);
		int i13 = b(j1, 2);
		if (k12 < 0)
			k12 = fighter_data[j1][1];
		if (i13 < 0)
			i13 = fighter_data[j1][2];
		int j13 = k12 - i7;
		int k13 = i13 - k8;
		if (j13 < 0)
			j13 = 0;
		if (k13 < 0)
			k13 = 0;
		a(j1, 1, j13);
		a(j1, 2, k13);
		if ((l11 = 9 - j13 / (fighter_data[j1][3] / 10)) < 0)
			l11 = 0;
		if (i2 != 0)
		{
			if (l11 == 9 && util.RandomInt(2) == 1)
				l11 = 10;
			l11 += 47;
		} else
		{
			l11 += 37;
		}
		k11 = l11 * 256 + k11;
		d(2, k11, 1);
		return i7;
	}

	boolean BattleIsEnd()
	{
		if (d_int_static == 1)
			return false;   //逃跑成功
		if (is_try == 0)
		{
			if (fighter_data[0][1] <= 0 || fighter_data[1][1] <= 0)
				return false;  //退出
		} else
		if (is_try == 1 && (fighter_data[0][1] < fighter_data[0][3] / 2 || fighter_data[1][1] < fighter_data[1][3] / 2))  //切磋
			return false;   //退出
		return true;
	}

	void CopyData()
	{
		CopyPlayerData();
		CopyNPCData(NPC_id);
	}

	void CopyPlayerData()
	{
		int i1 = player_id;
		player_name = Gmud.sPlayer.player_name;
		for (int j1 = 0; j1 < 128; j1++)
			fighter_data[i1][j1] = 0;

		fighter_data[i1][66] = Gmud.sPlayer.class_id; //class id
		fighter_data[i1][0] = Gmud.sPlayer.fp_plus; //加力
		fighter_data[i1][1] = Gmud.sPlayer.hp; //hp
		fighter_data[i1][2] = Gmud.sPlayer.hp_max; //hp-max
		fighter_data[i1][3] = Gmud.sPlayer.hp_full; //hp_full
		fighter_data[i1][4] = Gmud.sPlayer.fp; //fp
		fighter_data[i1][5] = Gmud.sPlayer.fp_level; //fp-level
		fighter_data[i1][6] = Gmud.sPlayer.mp; //mp
		fighter_data[i1][7] = Gmud.sPlayer.mp_level; //mp-level
		fighter_data[i1][8] = Gmud.sPlayer.GetForce(); //后天膂力
		fighter_data[i1][9] = Gmud.sPlayer.GetAgility(); //后天敏捷
		fighter_data[i1][10] = Gmud.sPlayer.GetSavvy(); //后天悟性
		fighter_data[i1][11] = Gmud.sPlayer.GetAptitude(); //后天根骨
		fighter_data[i1][12] = Gmud.sPlayer.unknow2; //??
		fighter_data[i1][13] = Gmud.sPlayer.unknow3; //??

		fighter_data[i1][67] = Gmud.sPlayer.money;  //money
		fighter_data[i1][68] = Gmud.sPlayer.sex;  //sex
		fighter_data[i1][69] = (int) (Gmud.sPlayer.played_time / Player.AGE_TIME);  //age

		for (int j2 = 0; j2 < 8; j2++)     //reset skill
		{
			fighter_data[i1][30 + j2 * 2] = 255;
			fighter_data[i1][30 + j2 * 2 + 1] = 0;
			fighter_data[i1][46 + j2 * 2] = 255;
			fighter_data[i1][46 + j2 * 2 + 1] = 0;
		}

		for (int k1 = 0; k1 < 16; k1++)    //copy equip data
			fighter_data[i1][14 + k1] = Gmud.sPlayer.equips[k1];

		for (int l1 = 0; l1 < 8; l1++)     //copy selected skill data
		{
			fighter_data[i1][30 + l1 * 2] = Gmud.sPlayer.select_skills[l1];
			fighter_data[i1][30 + l1 * 2 + 1] = Gmud.sPlayer.GetSkillLevel(Gmud.sPlayer.select_skills[l1]);
		}

		fighter_data[i1][46] = 1;
		fighter_data[i1][47] = Gmud.sPlayer.GetSkillLevel(1);
		int k2;
		if ((k2 = fighter_data[i1][32]) >= 0 && k2 < 54)
		{
			int l2 = Skill.skill_weapon_type[k2];
			int i3 = Skill.c[l2];
			fighter_data[i1][48] = Skill.c[l2];
			fighter_data[i1][49] = Gmud.sPlayer.GetSkillLevel(i3);
		} else
		{
			fighter_data[i1][48] = 255;
			fighter_data[i1][49] = 0;
		}
		for (int i2 = 2; i2 < 8; i2++)
		{
			fighter_data[i1][46 + i2 * 2] = Skill.skill_weapon_type[i2];
			fighter_data[i1][46 + i2 * 2 + 1] = Gmud.sPlayer.GetSkillLevel(Skill.b[i2]);
		}

		fighter_data[i1][62] = Gmud.sPlayer.GetPlayerLevel();  //global level
		fighter_data[i1][63] = Gmud.sPlayer.mp_plus;  //法点
		fighter_data[i1][64] = Gmud.sPlayer.exp;  //exp
		fighter_data[i1][65] = Gmud.sPlayer.unknow1;  //??
		CalcFighterLevel(i1);
	}

	void CopyNPCData(int i1)
	{
		if (i1 < 0 || i1 > 179)
			return;
		int[] tpdata = NPC.NPC_attrib[i1];
		NPC_name = NPC.NPC_names[i1];   //npc name
		for (int j1 = 0; j1 < 128; j1++) //clean data
			fighter_data[1][j1] = 0;

		for (int k1 = 0; k1 < 8; k1++)  //reset npc skill
		{
			fighter_data[1][30 + k1 * 2] = 255;
			fighter_data[1][30 + k1 * 2 + 1] = 0;
			fighter_data[1][46 + k1 * 2] = 255;
			fighter_data[1][46 + k1 * 2 + 1] = 0;
		}

		fighter_data[1][66] = tpdata[1]; //class id
		if (fighter_data[1][66] > 8 || fighter_data[1][66] < 0)
			fighter_data[1][66] = 0;
		fighter_data[1][0] = tpdata[4]; //加力
		fighter_data[1][1] = tpdata[11]; //hp
		fighter_data[1][2] = tpdata[12]; //hp-max
		fighter_data[1][3] = tpdata[15]; //hp-full
		fighter_data[1][4] = tpdata[13]; //fp
		fighter_data[1][5] = tpdata[14]; //fp-level
		fighter_data[1][6] = tpdata[13]; //mp=fp
		fighter_data[1][7] = tpdata[14]; //mp-level=fp
		fighter_data[1][8] = tpdata[5]; //膂力
		fighter_data[1][9] = tpdata[6]; //敏捷
		fighter_data[1][10] = tpdata[7]; //悟性
		fighter_data[1][11] = tpdata[8]; //根骨
		fighter_data[1][12] = tpdata[9]; //?
		fighter_data[1][13] = tpdata[10]; //?
		fighter_data[1][63] = 0;
		fighter_data[1][64] = tpdata[3]; //??
		fighter_data[1][65] = 0;  //?
		NPCEquip(i1);  //npc equipt
		for (int l1 = 0; l1 < 16; l1++) //copy equip data
			fighter_data[1][14 + l1] = NPC_equip[l1];

		NPCSetSkill(i1);  //npc select skill
		for (int i2 = 0; i2 < 16; i2++)   //copy selected skill data
			fighter_data[1][30 + i2] = NPC_select_skill[i2];

		fighter_data[1][46] = 1;
		fighter_data[1][47] = NPC.GetNPCSkillLevel(i1, 1);
		int j2 = fighter_data[1][29];
		if (j2 > 0)
		{
			int k2 = Items.item_attribs[j2][1];
			int l2 = NPC.GetNPCSkillLevel(i1, Skill.c[k2]);
			fighter_data[1][48] = Skill.c[k2];
			fighter_data[1][49] = l2;
		}
		for (int i3 = 2; i3 < 8; i3++)
		{
			fighter_data[1][46 + i3 * 2] = Skill.b[i3];
			fighter_data[1][46 + i3 * 2 + 1] = NPC.GetNPCSkillLevel(i1, Skill.b[i3]);
		}
	}

	void NPCEquip(int i)
	{
		if (i < 0 || i > 179)
			return;
		for (int j = 0; j < 16; j++)
			NPC_equip[j] = 0;

		for (int k = 0; k < 5; k++)
		{
			int l;
			if ((l = NPC.NPC_item[i][k]) == 0)
				continue;
			if (Items.item_attribs[l][0] == 2)
			{
				NPC_equip[15] = l;
				continue;
			}
			if (Items.item_attribs[l][0] == 3)
			{
				int i1 = Items.item_attribs[l][1];
				NPC_equip[i1] = l;
			}
		}
	}

	void NPCSetSkill(int i)
	{
		if (i < 0 || i > 179)
			return;
		
		final int[] skills;
		if (i == 179) {
			skills = task.bad_man_skill;
		} else {
			NPCSKILLINFO nsk = new NPCSKILLINFO();
			NPC.GetNPCSkill(nsk, i);
			skills = nsk.data;
		}
		
		int j = skills[0];
		if (j < 0)
		{
//			free(nsk.data);
			return;
		}
		for (int k = 0; k < 8; k++)  //clear aa.e[]
		{
			NPC_select_skill[k * 2] = 255;
			NPC_select_skill[k * 2 + 1] = 0;
		}

		int l = NPC_equip[15];
		for (int i1 = 0; i1 < j; i1++)
		{
			int j1 = skills[1 + i1 * 2];
			int k1 = skills[1 + i1 * 2 + 1];
			if (j1 < 10)
				continue;
			if (Skill.skill_type[j1] < 2)
			{
				if (Skill.skill_type[j1] == 0)
				{
					NPC_select_skill[0] = j1;
					NPC_select_skill[1] = k1;
					if (l == 0)
					{
						NPC_select_skill[8] = j1;
						NPC_select_skill[9] = k1;
					}
					continue;
				}
				if (Skill.skill_type[j1] == 1 && l > 0 && Items.item_attribs[l][1] == Skill.skill_weapon_type[j1])
				{
					NPC_select_skill[2] = j1;
					NPC_select_skill[3] = k1;
					NPC_select_skill[8] = j1;
					NPC_select_skill[9] = k1;
				}
			} else
			{
				int l1 = Skill.skill_type[j1];
				NPC_select_skill[l1 * 2] = j1;
				NPC_select_skill[l1 * 2 + 1] = k1;
			}
		}
//		free(nsk.data);
	}

	int GetWeaponID(int i1)
	{
		return fighter_data[i1][29];
	}

	int CalcSkill(int i1, int j1)
	{
		if (j1 < 0 || j1 > 94)
			return 0;
		if (j1 < 10)
		{
			for (int k1 = 0; k1 < 8; k1++)
				if (fighter_data[i1][46 + k1 * 2] == j1)
					return fighter_data[i1][46 + k1 * 2 + 1];

			return 0;
		}
		for (int l1 = 0; l1 < 8; l1++)
			if (fighter_data[i1][30 + l1 * 2] == j1)
				return fighter_data[i1][30 + l1 * 2 + 1];

		return 0;
	}

	void CalcFighterLevel(int i1)
	{
		int j1 = GetWeaponID(i1);
		int k1 = Items.item_attribs[j1][1];
		int l1 = 0;
		if (j1 == 0)
		{
			l1 = (l1 = (l1 = (l1 = (l1 = (l1 = 0 + CalcSkill(i1, fighter_data[i1][30]) * 2) + CalcSkill(i1, 1)) + CalcSkill(i1, fighter_data[i1][36]) * 2) + CalcSkill(i1, 0)) + CalcSkill(i1, fighter_data[i1][34]) * 2) + CalcSkill(i1, 7);
			int i2;
			if ((i2 = fighter_data[i1][38]) != 255 && Skill.skill_type[i2] == 0)
				l1 += CalcSkill(i1, fighter_data[i1][38]) * 2;
			l1 = (l1 = (l1 += CalcSkill(i1, 8)) / 3) / 4;
		} else
		{
			int j2;
			if ((j2 = fighter_data[i1][32]) != 255)
			{
				int k2 = Skill.skill_weapon_type[j2];
				if (k1 == k2)
					l1 = 0 + CalcSkill(i1, j2) * 2;
			}
			int l2 = Skill.c[k1];
			l1 = (l1 = (l1 = (l1 = (l1 += CalcSkill(i1, l2)) + CalcSkill(i1, fighter_data[i1][36]) * 2) + CalcSkill(i1, 0)) + CalcSkill(i1, fighter_data[i1][34]) * 2) + CalcSkill(i1, 7);
			int i3;
			if ((i3 = fighter_data[i1][38]) != 255 && Skill.skill_type[i3] == 1)
				l1 += CalcSkill(i1, fighter_data[i1][38]) * 2;
			l1 = (l1 = (l1 += CalcSkill(i1, 8)) / 3) / 4;
		}
		if (l1 % 5 > 2)
			l1 = (l1 / 5) * 5 + 5;
		if (l1 > 255)
			l1 = 255;
		if (l1 < 0)
			l1 = 0;
		fighter_data[i1][62] = l1;
	}

	void CostFP(int i1, int j1)
	{
		int k1;
		if ((k1 = b(i1, 4)) < 0)
			k1 = fighter_data[i1][4];
		if ((k1 -= j1) < 0)
			k1 = 0;
		a(i1, 4, k1);
	}

	void CostMP(int i1, int j1)
	{
		int k1;
		if ((k1 = b(i1, 6)) < 0)
			k1 = fighter_data[i1][6];
		if ((k1 -= j1) < 0)
			k1 = 0;
		a(i1, 6, k1);
	}

//	extern void GmudDelay(unsigned int);
//	extern Image* gmud_loadimage(int);

	void BattleMain()
	{
		uibattle.weapon_id[0] = uibattle.weapon_id[1] = 0;
		a_boolean_static = false;
		CopyData();
		d_int_static = 0;
		uibattle.menu_id = 0;
		active_id = CalcActOrder();     //计算出招先后
		boolean flag = true;

		uibattle.player_img = Res.loadimage(Gmud.sPlayer.image_id * 6 + 74 + 4);
		uibattle.NPC_img = Res.loadimage(NPC_image_id);
		uibattle.hp_img = Res.loadimage(244);
		uibattle.fp_img = Res.loadimage(245);
		uibattle.mp_img = Res.loadimage(246);
		uibattle.hit_img = Res.loadimage(247);

		uibattle.weapon_id[0] = fighter_data[0][29];
		uibattle.weapon_id[1] = fighter_data[1][29];
		uibattle.hit_id = -1;
		uibattle.DrawMain();
		Video.VideoUpdate();
		while(true)
		{
			Input.ClearKeyStatus();
			if (!flag)
				break;
			PlayerActive();
			if(!(flag = BattleIsEnd()))
				break;
			NPCActive();
			flag = BattleIsEnd();
		};
		BattleEnd();  //win

//		DeleteObject(uibattle.hit_img);  //free data
//		DeleteObject(uibattle.mp_img);
//		DeleteObject(uibattle.fp_img);
//		DeleteObject(uibattle.hp_img);
//		DeleteObject(uibattle.NPC_img);
//		DeleteObject(uibattle.player_img);

		player_name = "";
		NPC_name = "";
		Gmud.GmudDelay(200);

	}

	void ClearActiveQueue()
	{
		e_int_static = 0;
		for (int i1 = 0; i1 < 256; i1++)
		{
			a_int_array1d_static[i1] = -1;
			b_int_array1d_static[i1] = -1;
		}
	}

	int CalaAvtiveSpeed(int i1, int j1, int k1)
	{
		if (k1 < 5)
			return fighter_data[i1][70 + j1 * 5 + k1];
		else
			return 0;
	}

	/**
	 * 设置出招的描述文本 ??
	 * @param i1
	 * @param j1 id (Magic.txt)
	 * @param k1
	 */
	void b(int i1, int j1, int k1)
	{
		b_int_array1d_static[4] = i1;
		b_int_array1d_static[5] = j1;
		b_int_array1d_static[6] = k1;
	}

	void h()
	{
		b_int_array1d_static[0] = e_int_static;
		if (e_int_static > 0)
			b_int_array1d_static[1] = a(b_int_array1d_static);
		else
			b_int_array1d_static[1] = 0;
		b_int_array1d_static[2] = active_id;
	}

	void e(int i1, int j1, int k1)
	{
		int l1;
		if ((l1 = k1) > 5 || l1 < 0)
			l1 = 1;
		if (i1 == -1 || j1 == -1)
			return;
		for (int i2 = 0; i2 < l1;)
		{
			uibattle.PhyAttack(i1, j1 + i2);
			i2++;
			Video.VideoUpdate();
			Gmud.GmudDelay(900);
		}
	}

	void f()
	{
		int i1 = a_int_array1d_static[1];
		if (a(a_int_array1d_static) != i1)
			return;
		int j1;
		e(a_int_array1d_static[4], a_int_array1d_static[5], a_int_array1d_static[6]);
		if ((j1 = a_int_array1d_static[0]) > 256 - 32)
			j1 = 256 - 32;
		for (int k1 = 0; k1 < j1; k1 += 3)
		{
			int i2 = a_int_array1d_static[k1 + 32];
			int k2 = a_int_array1d_static[k1 + 1 + 32];
			int i3;
			if ((i3 = a_int_array1d_static[k1 + 2 + 32]) == -123 && k2 >= 14 && k2 - 14 < 16)
			{
				int k3 = fighter_data[i2][k2];
				if (i2 == player_id)
				{
					if (k2 == 29)
						Gmud.sPlayer.UnEquipWeapon();
					else
						Gmud.sPlayer.UnEquipArmor(k2 - 14);
					int i4;
					if ((i4 = Gmud.sPlayer.ExistItem(k3, 1)) >= 0)
						Gmud.sPlayer.DeleteOneItem(i4);
				}
				fighter_data[i2][k2] = 0;
			} else
			{
				fighter_data[i2][k2] = i3;
			}
		}

		int j2;
		if (a(j2 = active_id != 0 ? 0 : 1, 1) >= 0)
			uibattle.hit_id = j2;
		e(a_int_array1d_static[7], a_int_array1d_static[8], a_int_array1d_static[9]);
		Video.VideoUpdate();
		e(a_int_array1d_static[10], a_int_array1d_static[11], a_int_array1d_static[12]);
		Video.VideoUpdate();
		int l2 = active_id;
		for (int j3 = 0; j3 < 8; j3++)
		{
			for (int l3 = 0; l3 < 2; l3++)
			{
				active_id = l3;
				int j4;
				if ((j4 = CalaAvtiveSpeed(l3, j3, 4)) <= 0 || j4 >= 20)
					continue;
				if (j3 == 7)
				{
					int k4;
					if ((k4 = CalaAvtiveSpeed(l3, j3, 2)) >= 0)
						e(1, k4, 1);
					int i5 = CalaAvtiveSpeed(l3, j3, 0);
					fighter_data[l3][1] -= i5;
					if (fighter_data[l3][1] < 0)
						fighter_data[l3][1] = 0;
				}
				int l4;
				if ((--j4) == 0 && (l4 = CalaAvtiveSpeed(l3, j3, 3)) >= 0)
					e(1, l4, 1);
				d(l3, j3, 4, j4);
			}

		}

		active_id = l2;
		uibattle.DrawMain();
		Video.VideoUpdate();
	}

	void i()
	{
		h();
		int i1;
		for (i1 = 0; i1 < 256; i1++)
			a_int_array1d_static[i1] = b_int_array1d_static[i1];

		f();

		for (i1 = 0; i1 < 256; i1++)
			b_int_array1d_static[i1] = -1;
		e_int_static = 0;

		a(0, 0, 0, -1);
		uibattle.weapon_id[0] = fighter_data[0][29];
		uibattle.weapon_id[1] = fighter_data[1][29];
		active_id = active_id==0?1:0;
	}

	void a(int i1, int j1, int k1, int l1)
	{
		f_int_array1d_static[0] = i1;
		f_int_array1d_static[1] = j1;
		f_int_array1d_static[2] = k1;
		f_int_array1d_static[3] = l1;
	}

	void PlayerActive()
	{
		if (player_id == active_id)
		{
			ClearActiveQueue();
			int i1;
			if ((i1 = CalaAvtiveSpeed(active_id, 4, 4)) > 0 && i1 < 20)
				b(1, 99, 1);
			else
			{
				uibattle.menu_id = 0;
				uibattle.Main();
			}
			i();
		}
	}

	boolean ba()
	{
		int i1 = fighter_data[active_id][9];
		int j1 = fighter_data[active_id][62];
		int k1 = active_id != 0 ? 0 : 1;
		int l1 = fighter_data[k1][62];
		int i2;
		if ((i2 = f(fighter_data[active_id][64] / 100) - f(fighter_data[k1][64] / 100)) > 50)
			i2 = 50;
		if (i2 < -50)
			i2 = -50;
		int j2;
		if ((j2 = (j2 = (j2 = (i1 - fighter_data[k1][9]) + 50) + (j1 - l1)) + i2) < 1)
			j2 = 10;
		if (j2 > 90)
			j2 = 90;
		return util.RandomBool(j2);
	}

	void c(int i1, int j1, int k1)
	{
		b_int_array1d_static[7] = i1;
		b_int_array1d_static[8] = j1;
		b_int_array1d_static[9] = k1;
	}

	void d(int i1, int j1, int k1)
	{
		b_int_array1d_static[10] = i1;
		b_int_array1d_static[11] = j1;
		b_int_array1d_static[12] = k1;
	}
	// j1:技能ID k1:技能等级  产生攻击描述？
	int ib(int i1, int j1, int k1)
	{
		for (int l1 = 0; l1 < 8; l1++)
			c_int_array2d_static[i1][l1] = 0;

		for (int i3 = 0; i3 < 52; i3 += 2)
			if (Skill.g[i3] == j1)
			{
				int i2 = Skill.e[Skill.g[i3 + 1]];
				int k2 = Skill.e[Skill.g[i3 + 1] + 1] - Skill.e[Skill.g[i3 + 1]];
				int k3 = 0;
				do
				{
					if (k3 >= k2)
						break;
					int i4 = Skill.a[i2 + k3][0];
					if (k1 < i4)
						break;
					k3++;
				} while (true);
				int j4;
				int i5 = (j4 = util.RandomInt(k3)) + i2;
				for (int k5 = 0; k5 < 8; k5++)
					c_int_array2d_static[i1][k5] = Skill.a[i5][k5];

				return i5;
			}

		for (int j3 = 0; j3 < 16; j3 += 2)
			if (Skill.f[j3] == j1)
			{
				int j2 = Skill.e[Skill.f[j3 + 1]];
				int l2 = Skill.e[Skill.f[j3 + 1] + 1] - Skill.e[Skill.f[j3 + 1]];
				int l3 = 0;
				do
				{
					if (l3 >= l2)
						break;
					int k4 = Skill.a[j2 + l3][0];
					if (k1 < k4)
						break;
					l3++;
				} while (true);
				int l4;
				int j5 = (l4 = util.RandomInt(l3)) + j2;
				for (int l5 = 0; l5 < 8; l5++)
					c_int_array2d_static[i1][l5] = Skill.a[j5][l5];

				return j5;
			}

		return -1;
	}

	int CalcHit(int i1)
	{
	int j1 = fighter_data[i1][12];
		int k1;
		if ((k1 = CalaAvtiveSpeed(i1, 0, 4)) > 0 && k1 < 20)
			j1 += CalaAvtiveSpeed(i1, 0, 0);
		for (int l1 = 0; l1 < 16; l1++)
		{ // 遍历所有装备
			int i2;
			if ((i2 = fighter_data[i1][14 + l1]) > 0 && i2 < 92 && (Items.item_attribs[i2][0] == 2 || Items.item_attribs[i2][0] == 3))
			{ // 如果是“武器”或“装备”，则加上“命中率”
				int j2 = Items.item_attribs[i2][3] <= 127 ? Items.item_attribs[i2][3] : -(256 - Items.item_attribs[i2][3]);
				j1 += j2;
			}
		}

		return j1;
	}

	int CalcAvoid(int i1)
	{
		int j1 = fighter_data[i1][13];
		int k1;
		if ((k1 = CalaAvtiveSpeed(i1, 3, 4)) > 0 && k1 < 20)
			j1 += CalaAvtiveSpeed(i1, 3, 0);
		for (int l1 = 0; l1 < 16; l1++)
		{
			int i2;
			if ((i2 = fighter_data[i1][14 + l1]) > 0 && i2 < 92 && (Items.item_attribs[i2][0] == 2 || Items.item_attribs[i2][0] == 3))
			{
				int j2 = Items.item_attribs[i2][4] <= 127 ? Items.item_attribs[i2][4] : -(256 - Items.item_attribs[i2][4]);
				j1 += j2;
			}
		}

		return j1;
	}

	int CalcDefense(int i1)
	{
		int j1 = 0;
		for (int k1 = 0; k1 < 16; k1++)
		{
			int l1;
			if ((l1 = fighter_data[i1][14 + k1]) > 0 && l1 < 92 && Items.item_attribs[l1][0] == 3)
			{
				int i2 = Items.item_attribs[l1][2] <= 127 ? Items.item_attribs[l1][2] : -(256 - Items.item_attribs[l1][2]);
				j1 += i2;
			}
		}

		return j1;
	}

	int CalcDefenseB(int i1)
	{
		int j1 = 0;
		int k1;
		if ((k1 = CalaAvtiveSpeed(i1, 6, 4)) > 0 && k1 < 20)
			j1 = 0 + CalaAvtiveSpeed(i1, 6, 0);
		for (int l1 = 0; l1 < 16; l1++)
		{
			int i2;
			if ((i2 = fighter_data[i1][14 + l1]) > 0 && i2 < 92 && Items.item_attribs[i2][0] == 3)
			{
				int j2 = Items.item_attribs[i2][2] <= 127 ? Items.item_attribs[i2][2] : -(256 - Items.item_attribs[i2][2]);
				j1 += j2;
			}
		}

		return j1;
	}

	int CalcAttack(int i1)
	{
		int j1 = 0;
		if ((j1 = (j1 = 0 + fighter_data[i1][8]) + fighter_data[i1][4] / 10) < 10)
			j1 = 10;
		int k1;
		if ((k1 = CalaAvtiveSpeed(i1, 1, 4)) > 0 && k1 < 20)
			j1 += CalaAvtiveSpeed(i1, 1, 0);
		for (int l1 = 0; l1 < 16; l1++)
		{
			int i2;
			if ((i2 = fighter_data[i1][14 + l1]) > 0 && i2 < 92 && Items.item_attribs[i2][0] == 2)
			{
				int j2 = Items.item_attribs[i2][2] <= 127 ? Items.item_attribs[i2][2] : -(256 - Items.item_attribs[i2][2]);
				j1 += j2;
			}
		}

		if (Gmud.sPlayer.lasting_tasks[9] != 0 && Gmud.sPlayer.lasting_tasks[8] / 256 == 0 && fighter_data[i1][29] == 77)
			j1 += Items.item_attribs[77][4];
		return j1;
	}

	void d(int i1, int j1, int k1, int l1)
	{
		if (k1 < 5)
			fighter_data[i1][70 + j1 * 5 + k1] = l1;
	}

	boolean a(int i1, int j1, int k1, int l1, int i2, int j2, int k2)
	{
		if (fighter_data[i1][70 + j1 * 5 + 4] < 0)
			fighter_data[i1][70 + j1 * 5 + 4] = 0;
		if (fighter_data[i1][70 + j1 * 5 + 4] != 0)
			return false;
		fighter_data[i1][70 + j1 * 5] = k1;
		fighter_data[i1][70 + j1 * 5 + 1] = l1;
		fighter_data[i1][70 + j1 * 5 + 2] = i2;
		fighter_data[i1][70 + j1 * 5 + 3] = j2;
		fighter_data[i1][70 + j1 * 5 + 4] = k2;
		if (k2 >= 20)
			fighter_data[i1][70 + j1 * 5 + 4] = 19;
		for (int l2 = 0; l2 < 5; l2++)
			a(i1, 70 + j1 * 5 + l2, fighter_data[i1][70 + j1 * 5 + l2]);

		return true;
	}

	String Breath()
	{
		int i1 = active_id;
		int j1 = Gmud.sPlayer.GetSkillLevel(0);
		if (Gmud.sPlayer.select_skills[3] == 255 || j1 == 0)
			return "你必须选择你要用的内功心法.";
		if (fighter_data[i1][1] >= fighter_data[i1][2])
		{
			fighter_data[i1][1] = fighter_data[i1][2];
			return "你现在体力充沛.";
		}
		if (Gmud.sPlayer.fp < 50 || Gmud.sPlayer.fp_level < 50)
			return "你的内力不够.";
		int k1;
		if ((k1 = (fighter_data[i1][2] - fighter_data[i1][1]) * 2) > fighter_data[i1][4])
			k1 = fighter_data[i1][4];
		fighter_data[i1][4] -= k1;
		fighter_data[i1][1] += k1;
		if (fighter_data[i1][1] > fighter_data[i1][2])
			fighter_data[i1][1] = fighter_data[i1][2];
		a(i1, 4, fighter_data[i1][4]);
		a(i1, 1, fighter_data[i1][1]);
		return "你深深吸了几口气,脸色看起来好多了.";
	}

	int a(int i1, int j1)
	{
	    int m = -1;
		for (int k1 = 0; k1 < e_int_static; k1 += 3) {
			if (a_int_array1d_static[k1 + 32] == i1 && a_int_array1d_static[k1 + 1 + 32] == j1) {
				m = k1;
			}
		}
//				return a_int_array1d_static[k1 + 2 + 32];
//		return -1;
		if (m==-1) 
			return -1000000;
		return a_int_array1d_static[m + 2 + 32];
	}
	  public void a1(int paramInt1, int paramInt2)
	  {
	    int m;
	    if ((m = a(paramInt1, 4)) < 0)
	      m = fighter_data[paramInt1][4];
	    m -= paramInt2;
	    if (m < 0)
	      m = 0;
	    a(paramInt1, 4, m);
	  }
	  public void b1(int paramInt1, int paramInt2)
	  {
		    int m;
		    if ((m = a(paramInt1, 6)) < 0)
		      m = fighter_data[paramInt1][6];
		    m -= paramInt2;
		    if (m < 0)
		      m = 0;
		    a(paramInt1, 6, m);
		  }
	int b(int i1, int j1)
	{
		for (int k1 = 0; k1 < e_int_static; k1 += 3)
			if (b_int_array1d_static[k1 + 32] == i1 && b_int_array1d_static[k1 + 1 + 32] == j1)
				return b_int_array1d_static[k1 + 2 + 32];

		return -1;
	}

	void b(int i1, int j1, int k1, int l1)
	{
		if (k1 <= 0)
		{
			d(0, 247, 1);
			return;
		}
		int i2 = 0;
		if ((i2 = b(l1, 1)) < 0)
			i2 = fighter_data[l1][1];
		if (k1 < 0)
			k1 = 0;
		if ((i2 -= k1) < 0)
			i2 = 0;
		a(l1, 1, i2);
		int j2 = 0;
		if ((j2 = i2 / (fighter_data[l1][3] / j1)) >= j1)
			j2 = j1 - 1;
		if (j2 < 0)
			j2 = 0;
		j2 += i1;
		d(1, j2, 1);
	}

	void c(int i1, int j1, int k1, int l1)
	{
		if (j1 <= 0)
		{
			d(0, 247, 1);
			return;
		}
		int i2 = ia(i1, j1, fighter_data[k1][3], l1);
		int j2 = 0;
		int k2 = 0;
		if ((k2 = b(k1, 1)) < 0)
			k2 = fighter_data[k1][1];
		if ((j2 = 9 - k2 / (fighter_data[k1][3] / 10)) < 0)
			j2 = 0;
		if (j2 > 9)
			j2 = 9;
		if (i1 != 0)
		{
			if (j2 == 9 && util.RandomInt(2) == 1)
				j2 = 10;
			j2 += 47;
		} else
		{
			j2 += 37;
		}
		i2 = j2 * 256 + i2;
		d(2, i2, 1);
	}

	int ia(int i1, int j1, int k1, int l1)
	{
		j1 *= 1000;
		if ((k1 *= 10) == 0)
			k1 = 10;
		int i2;
		if ((i2 = j1 / k1) > 100)
			i2 = 100;
		if (i2 < 0)
			i2 = 0;
		int l3;
		int i4;
		switch (i1)
		{
		case 0: // '\0'
			{
				if (l1 == 44)
				{
					int j2;
					if ((j2 = i2 / 12) > 6)
						j2 = 6;
					return j2 + 12;
				}
				if (util.RandomBool(65))
					return 7;
				int k2;
				if ((k2 = i2 / 12) > 6)
					k2 = 6;
				int i3 = k2 != 6 ? k2 : 5;
				int k3;
				if ((k3 = util.RandomInt(4)) == 0)
					return i3;
				if (k3 == 1)
					return 6 + i3;
				if (k3 == 2)
					return 12 + k2;
				if (k3 == 3)
					return 19 + k2;
				else
					return 7;
			}
		case 1: // '\001'
			{
				int l2 = i2 / 25;
				return 26 + l2;
			}
		case 6: // '\006'
			{
				int j3 = i2 / 25;
				if (util.RandomBool(85))
					return 30 + j3;
				else
					return 26 + j3;
			}
		case 7: // '\007'
			return l3 = i2 / 16;

		case 9: // '\t'
			return i4 = i2 / 16;

		case 2: // '\002'
		case 3: // '\003'
		case 4: // '\004'
		case 5: // '\005'
		case 8: // '\b'
		default:
			return 7;
		}
	}

	void b()
	{
		a(player_id, 2, fighter_data[player_id][2]);
		for (int i1 = 0; i1 < 16; i1++)
			if (Gmud.sPlayer.equips[i1] != fighter_data[player_id][14 + i1])
			{
				fighter_data[player_id][14 + i1] = Gmud.sPlayer.equips[i1];
				a(player_id, 14 + i1, Gmud.sPlayer.equips[i1]);
			}
	}

	void c()
	{
		for (int i1 = 0; i1 < 8; i1++)
		{
			if (Gmud.sPlayer.select_skills[i1] == fighter_data[player_id][30 + i1 * 2])
				continue;
			fighter_data[player_id][30 + i1 * 2] = Gmud.sPlayer.select_skills[i1];
			fighter_data[player_id][30 + i1 * 2 + 1] = Gmud.sPlayer.GetSkillLevel(Gmud.sPlayer.select_skills[i1]);
			a(player_id, 30 + i1 * 2, Gmud.sPlayer.select_skills[i1]);
			a(player_id, 30 + i1 * 2 + 1, fighter_data[player_id][30 + i1 * 2 + 1]);
			if (i1 == 1)
			{
				int j1 = player_id;
				int k1;
				if ((k1 = fighter_data[j1][32]) >= 0 && k1 < 36)
				{
					int l1 = Skill.skill_weapon_type[k1];
					int i2 = Skill.c[l1];
					fighter_data[j1][48] = Skill.c[l1];
					fighter_data[j1][49] = Gmud.sPlayer.GetSkillLevel(i2);
				} else
				{
					fighter_data[j1][48] = 255;
					fighter_data[j1][49] = 0;
				}
				a(player_id, 48, fighter_data[j1][48]);
				a(player_id, 49, fighter_data[j1][49]);
			}
			CalcFighterLevel(player_id);
			a(player_id, 62, fighter_data[player_id][62]);
		}
	}

	void NPCActive() {
		if (player_id != active_id) {
			int i1 = CalaAvtiveSpeed(active_id, 4, 4);
			if (i1 > 0 && i1 < 20) {
				// "你现在呆若木鸡！"
				b(1, 99, 1);
			} else {
				Magic.Effect(active_id);
				int k1 = g(active_id);
				boolean flag = util.RandomBool(20);
				if (k1 > 0 && flag) {
					int j2 = a_int_array2d_static[active_id][util.RandomInt(k1)];
					String s = Magic.UseMagic(j2);
					if (s.length() > 0)
						PhyAttack(false);
				} else {
					PhyAttack(false);
				}
			}
			i();
		}
	}

	int a(int ai[])
	{
		int i1 = 0;
		int j1 = ai[0];
		for (int k1 = 32; k1 < 32 + j1; k1++)
			i1 += 0xef ^ ai[k1];

		return i1;
	}

	int CalcActOrder() // 计算出招先后
	{
		int agility = fighter_data[active_id][9];
		int level = fighter_data[active_id][62];
		int id_rival = active_id != 0 ? 0 : 1;
		int level_rival = fighter_data[id_rival][62];
		int i2 = f(fighter_data[active_id][64] / 100) - f(fighter_data[id_rival][64] / 100);
		if (i2 > 50)
			i2 = 50;
		if (i2 < -50)
			i2 = -50;
		int j2 = (agility - fighter_data[id_rival][9]) + 50 + (level - level_rival) + i2;
		if (j2 < 1)
			j2 = 10;
		if (j2 > 95)
			j2 = 95;
		int k2 = 1;
		if (util.RandomBool(j2))
			k2 = 0;
		return k2;
	}

	int f(int i1) {
		int j1 = 0;
		if (i1 < 1)
			return 0;
		for (int k1 = 0x40000000; k1 > 0; k1 >>= 2)
			if (i1 >= j1 + k1) {
				i1 -= j1 + k1;
				j1 = (j1 >>= 1) + k1;
			} else {
				j1 >>= 1;
			}

		return j1;
	}

//	extern BOOL WriteSave();

	void BattleEnd() {
		int i1 = NPC_id;
		int k1 = 0;
		for (int i2 = 0; i2 < 5; i2++)
			NPC_item[i2] = 0;

		for (int j2 = 0; j2 < 5; j2++) {
			int i3;
			if ((i3 = NPC.NPC_item[i1][j2]) == 0)
				continue;
			if (Items.item_attribs[i3][0] == 2) {
				if (fighter_data[1][29] != i3)
					continue;
			} else if (Items.item_attribs[i3][0] == 3) {
				int k3 = Items.item_attribs[i3][1];
				if (fighter_data[1][14 + k3] == i3)
					NPC_item[k1++] = (short) i3;
				continue;
			}
			NPC_item[k1++] = (short) i3;
		}

		int k2 = NPC.NPC_attrib[i1][16];
		
		// 如果不是“切磋”且不是“逃跑”，刷新战斗结果
		if (is_try == 0 && d_int_static == 0)
			if (fighter_data[0][1] <= 0) // player Hp < 0
			{
				Gmud.sPlayer.PlayerDead(); // player dead
				Gmud.WriteSave(); // save
				UI.DrawDead();
				Gmud.exit();
			} else {
				TaskEnd(i1); // 特殊人物
				UI.BattleWin(k2); // draw win
				for (int j3 = 0; j3 < 5; j3++)
					// get item
					Gmud.sPlayer.GainOneItem(NPC_item[j3]);

				Gmud.sPlayer.money += k2; // +money
				Gmud.sMap.SetNPCDead(i1, (byte) 1); // set npc dead
			}
		RollBackData();
	}

	void TaskEnd(int i1)
	{
		if (i1 == 179)   //恶人
		{
			/*  //delete map data
			int j1 = j.a_z_static.a;
			int k1 = x.a[j1].length - 1;
			x.a[j1][k1] = p.b[6];
			ac.a[j1][k1] = 255;   //delete event
			*/
			task.bad_man_mapid = -1;
			--Gmud.sMap.map_event_data.size;
			--Gmud.sMap.map_image_data.size;
			task.temp_tasks_data[0] = 0;
			int l3 = task.temp_tasks_data[3];
			int l4 = task.temp_tasks_data[4];
			Gmud.sPlayer.exp += l3;
			Gmud.sPlayer.potential += l4;
//			wstring s1("你被奖励了：\n");
//			wchar_t num[10];
//			s1 += _itow(l3, num, 10);
//			s1 += " 点实战经验\n";
//			s1 += _itow(l4, num, 10);
//			s1 += " 点潜能\n ";
			String s1 = String.format("你被奖励了：\n%d 点实战经验\n%d 点潜能\n ", l3,l4);
			UI.DialogBx(s1, 14, 12);
			return;
		}
		if (task.temp_tasks_data[19] == 1 && task.temp_tasks_data[18] == i1)  //平一指任务
		{
			task.temp_tasks_data[20] = 1;
			return;
		}
		if (i1 >= 148 && i1 <= 178 && i1 != 156 && i1 != 157)   //坛主
		{
			String s = "获得了：\n";
			switch (i1)
			{
			case 148: 
				{
					int l1;
					if ((l1 = Gmud.sPlayer.ExistItem(79, 1)) >= 0)
						Gmud.sPlayer.LoseItem(l1, 1);
					Gmud.sPlayer.exp += 50000;
					s += "50000点经验\n";
					Gmud.sPlayer.lasting_tasks[1] = 1;
					UI.DialogBx(s, 14, 12);
					return;
				}
			case 155: 
				{
					int i2;
					if ((i2 = Gmud.sPlayer.ExistItem(80, 1)) >= 0)
						Gmud.sPlayer.LoseItem(i2, 1);
					Gmud.sPlayer.exp += 50000;
					Gmud.sPlayer.money += 50000;
					s += "50000点经验\n50000金钱";
					Gmud.sPlayer.lasting_tasks[1] = 2;
					UI.DialogBx(s, 14, 12);
					return;
				}
			case 150: 
				{
					int j2;
					if ((j2 = Gmud.sPlayer.ExistItem(81, 1)) >= 0)
						Gmud.sPlayer.LoseItem(j2, 1);
					Gmud.sPlayer.fp_level += 150;
					s += "150点内力修为\n";
					Gmud.sPlayer.lasting_tasks[1] = 3;
					UI.DialogBx(s, 14, 12);
					return;
				}
			case 154: 
				{
					int k2;
					if ((k2 = Gmud.sPlayer.ExistItem(82, 1)) >= 0)
						Gmud.sPlayer.LoseItem(k2, 1);
					Gmud.sPlayer.fp_level += 200;
					Gmud.sPlayer.money += 60000;
					s += "200点内力修为\n60000金钱\n";
					Gmud.sPlayer.lasting_tasks[1] = 4;
					UI.DialogBx(s, 14, 12);
					return;
				}
			case 151: 
				{
					int l2;
					if ((l2 = Gmud.sPlayer.ExistItem(83, 1)) >= 0)
						Gmud.sPlayer.LoseItem(l2, 1);
					Gmud.sPlayer.fp_level += 200;
					Gmud.sPlayer.exp += 60000;
					s += "200点内力修为\n60000点经验\n";
					Gmud.sPlayer.lasting_tasks[1] = 5;
					UI.DialogBx(s, 14, 12);
					return;
				}
			case 153: 
				{
					int i3;
					if ((i3 = Gmud.sPlayer.ExistItem(84, 1)) >= 0)
						Gmud.sPlayer.LoseItem(i3, 1);
					for (int i4 = 0; i4 < 32; i4++)
						if (Gmud.sPlayer.skills[i4][0] != 255)
							Gmud.sPlayer.skills[i4][1] += 3;

					Gmud.sPlayer.money += 60000;
					s += "武功等级上升3级\n60000金钱\n";
					Gmud.sPlayer.lasting_tasks[1] = 6;
					UI.DialogBx(s, 14, 12);
					return;
				}
			case 152: 
				{
					int j3;
					if ((j3 = Gmud.sPlayer.ExistItem(85, 1)) >= 0)
						Gmud.sPlayer.LoseItem(j3, 1);
					for (int j4 = 0; j4 < 32; j4++)
						if (Gmud.sPlayer.skills[j4][0] != 255)
							Gmud.sPlayer.skills[j4][1] += 3;

					Gmud.sPlayer.exp += 60000;
					s += "武功等级上升3级\n60000点经验\n";
					Gmud.sPlayer.lasting_tasks[1] = 7;
					UI.DialogBx(s, 14, 12);
					return;
				}
			case 149: 
				{
					int k3;
					if ((k3 = Gmud.sPlayer.ExistItem(86, 1)) >= 0)
						Gmud.sPlayer.LoseItem(k3, 1);
					for (int k4 = 0; k4 < 32; k4++)
						if (Gmud.sPlayer.skills[k4][0] != 255)
							Gmud.sPlayer.skills[k4][1] += 3;

					Gmud.sPlayer.fp_level += 200;
					s += "武功等级上升3级\n200点内力修为\n";
					Gmud.sPlayer.lasting_tasks[1] = 8;
					UI.DialogBx(s, 14, 12);
					return;
				}
			}
		}
	}

	void RollBackData()
	{
		int i1 = player_id;
		int j1 = (i1) != 0 ? 0 : 1;
		for (int k1 = 0; k1 < 16; k1++)
			if (fighter_data[i1][14 + k1] != Gmud.sPlayer.equips[k1])
				Gmud.sPlayer.LoseOneItem(fighter_data[i1][14 + k1]);
		int l1 = NPC_id;
		for (int i2 = 0; i2 < 5; i2++)
		{
			int j2;
			if ((j2 = NPC.NPC_item[l1][i2]) == 0)
				continue;
			if (Items.item_attribs[j2][0] == 2)
			{
				// 如果武器不同，是临时的？需要去掉？
				if (fighter_data[1][29] != j2)
					NPC.NPC_item[l1][i2] = 0;
				continue;
			}
			if (Items.item_attribs[j2][0] != 3)
				continue;
			int k2 = Items.item_attribs[j2][1];
			if (fighter_data[1][14 + k2] != j2)
				NPC.NPC_item[l1][i2] = 0;
		}
		NPC.NPC_attrib[l1][11] = fighter_data[j1][1];
		NPC.NPC_attrib[l1][12] = fighter_data[j1][2];
		
		// TODO: FP 与  MP 共用了 [13]/[14]，暂时只能更新一个，取较为常用的 FP
		// if (fighter_data[j1][6] > 0)
		// NPC.NPC_attrib[l1][13] = fighter_data[j1][6];
		// else
		NPC.NPC_attrib[l1][13] = fighter_data[j1][4];

		Gmud.sPlayer.hp = fighter_data[i1][1];
		Gmud.sPlayer.hp_max = fighter_data[i1][2];
		Gmud.sPlayer.fp = fighter_data[i1][4];
		Gmud.sPlayer.mp = fighter_data[i1][6];
	}
}
