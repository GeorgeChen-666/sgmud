package cn.fmsoft.lnx.gmud.simple.core;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;

public class Player {

	/* keep single instance */
	static private Player sInstance;
	static synchronized Player getInstance() {
		if (sInstance == null) {
			sInstance = new Player();
		}
		return sInstance;
	}

	static final int AGE_TIME = 240;

	int image_id; // 人物形象
	int sex; // 性别
	int hp, hp_max, hp_full; // 生命
	int fp, fp_level, fp_plus; // 内力
	int mp, mp_level, mp_plus; // 魔法
	int money, exp, potential; // 金钱,经验,潜能
	int water, food; // 水，食物
	int pre_force; // 先天力量
	int pre_agility; // 先天敏捷
	int pre_savvy; // 先天悟性
	int pre_aptitude; // 先天根骨
	int face_level; // 外貌
	int class_id; // 门派
	int teacher_id; // 师傅
	int bliss; // 福缘
	int married; // 婚姻
	int unknow1, unknow2, unknow3;

	/**
	 * .[][0]:技能ID[0,53] .[][1]:技能等级 .[][2]:已学习的点数 .[][3]:未知  
	 * .[][4]:当前级别技能提升技能的点数=(Level+1)*(Level+1)/(GetSavvy()*2*10 + Rnd(16))
	 */
	int skills[][] = new int[32][5];

	int lasting_tasks[] = new int[40];

	long played_time;
	int/*char*/ equips[] = new int[16];
	
	/**
	 * [0]:拳脚 [1]:兵刃 [2]:轻功 [3]:内功(255表示没有) [4]:招架 [5]:知识 [6]:法术
	 */
	int select_skills[] = new int[8];
	
	/**
	 * [0]:ID [1]:是否已装备 [2]:数量
	 */
	int item_package[][] = new int[32][3];
	
//	byte player_name[] = new byte[16];
//	char weapon_name[] = new char[16]; // wchar_t
	// char consort_name[] = new char[16];

	String player_name;
	String weapon_name;
	String consort_name;

	String intArray2String(int num[], int size) {
		StringBuilder builder = new StringBuilder();
//		builder.append(size);
		for (int i = 0; i < size; i++) {
			builder.append(num[i]);
			builder.append(',');
		}
		return builder.toString();
	}
	String intArray2String(int num[][], int size_v, int size_h) {
		StringBuilder builder = new StringBuilder();
//		builder.append(size_v);
//		builder.append(size_h);
		for (int i = 0; i < size_v; i++) {
			for (int j = 0; j < size_h; j++) {
				builder.append(num[i][j]);
				builder.append(',');
			}
		}
		return builder.toString();
	}

	void String2IntArray(String str, int num[], int size) {
		int start = 0, cur;
		for (int i = 0; i < size; i++) {
			cur = str.indexOf(',', start + 1);
			num[i] = Integer.parseInt(str.substring(start, cur));
			start = cur + 1;
		}
	}

	void String2IntArray(String str, int num[][], int size_v, int size_h) {
		int start = 0, cur;
		for (int i = 0; i < size_v; i++) {
			for (int j = 0; j < size_h; j++) {
				cur = str.indexOf(',', start + 1);
				num[i][j] = Integer.parseInt(str.substring(start, cur));
				start = cur + 1;
			}
		}
	}
	
	boolean Clean(Context context) {

		SharedPreferences preferences; 
		preferences = context.getSharedPreferences(Gmud.SAVE_PATH, Context.MODE_PRIVATE); //其中fileName表示存放配置的配置文件名称 

		Editor editor = preferences.edit();
		editor.putString("key", "");
		
		editor.commit();
		
		return true;
	}
	
	boolean Save(Context context) {
		SharedPreferences preferences; 
		preferences = context.getSharedPreferences(Gmud.SAVE_PATH, Context.MODE_PRIVATE); //其中fileName表示存放配置的配置文件名称

		Editor editor = preferences.edit();
				
		editor
		.putString("player_name", player_name)
		.putString("weapon_name", weapon_name)
		.putString("consort_name", consort_name)
		.putInt("image_id",image_id)
		.putInt("sex",sex)
		.putInt("hp",hp)
		.putInt("hp_max",hp_max)
		.putInt("hp_full",hp_full)
		.putInt("fp",fp)
		.putInt("fp_level",fp_level)
		.putInt("fp_plus",fp_plus)
		.putInt("mp",mp)
		.putInt("mp_level",mp_level)
		.putInt("mp_plus",mp_plus)
		.putInt("money",money)
		.putInt("exp",exp)
		.putInt("potential",potential)
		.putInt("water",water)
		.putInt("food",food)
		.putInt("pre_force",pre_force)
		.putInt("pre_agility",pre_agility)
		.putInt("pre_savvy",pre_savvy)
		.putInt("pre_aptitude",pre_aptitude)
		.putInt("face_level",face_level)
		.putInt("class_id",class_id)
		.putInt("teacher_id",teacher_id)
		.putInt("bliss",bliss)
		.putInt("married",married)
		.putInt("unknow1",unknow1)
		.putInt("unknow2",unknow2)
		.putInt("unknow3",unknow3)
		.putString("skills", intArray2String(skills, 32, 5))
		.putString("lasting_tasks", intArray2String(lasting_tasks, 40))
		.putString("equips", intArray2String(equips, 16))
		.putString("select_skills", intArray2String(select_skills, 8))
		.putString("item_package", intArray2String(item_package, 32, 3))
		.putLong("played_time", played_time);
		
		editor.putString("key", "lnx");
		
		editor.commit();
		return true;
	}
	
	boolean load(Context context) {
		SharedPreferences preferences; 
		preferences = context.getSharedPreferences(Gmud.SAVE_PATH, Context.MODE_PRIVATE);
		
		String key = preferences.getString("key", "");
		if (key == "") {
			return false;
		}

		player_name = preferences.getString("player_name", player_name);
		weapon_name = preferences.getString("weapon_name", weapon_name);
		consort_name = preferences.getString("consort_name", consort_name);
		
		image_id = preferences.getInt("image_id",image_id);
		sex = preferences.getInt("sex",sex);
		hp = preferences.getInt("hp",hp);
		hp_max = preferences.getInt("hp_max",hp_max);
		hp_full = preferences.getInt("hp_full",hp_full);
		fp = preferences.getInt("fp",fp);
		fp_level = preferences.getInt("fp_level",fp_level);
		fp_plus = preferences.getInt("fp_plus",fp_plus);
		mp = preferences.getInt("mp",mp);
		mp_level = preferences.getInt("mp_level",mp_level);
		mp_plus = preferences.getInt("mp_plus",mp_plus);
		money = preferences.getInt("money",money);
		exp = preferences.getInt("exp",exp);
		potential = preferences.getInt("potential",potential);
		water = preferences.getInt("water",water);
		food = preferences.getInt("food",food);
		pre_force = preferences.getInt("pre_force",pre_force);
		pre_agility = preferences.getInt("pre_agility",pre_agility);
		pre_savvy = preferences.getInt("pre_savvy",pre_savvy);
		pre_aptitude = preferences.getInt("pre_aptitude",pre_aptitude);
		face_level = preferences.getInt("face_level",face_level);
		class_id = preferences.getInt("class_id",class_id);
		teacher_id = preferences.getInt("teacher_id",teacher_id);
		bliss = preferences.getInt("bliss",bliss);
		married = preferences.getInt("married",married);
		unknow1 = preferences.getInt("unknow1",unknow1);
		unknow2 = preferences.getInt("unknow2",unknow2);
		unknow3 = preferences.getInt("unknow3",unknow3);
		

		String2IntArray(preferences.getString("skills",""), skills, 32, 5);
		String2IntArray(preferences.getString("lasting_tasks",""), lasting_tasks, 40);
		String2IntArray(preferences.getString("equips",""), equips, 16);
		String2IntArray(preferences.getString("select_skills",""), select_skills, 8);
		String2IntArray(preferences.getString("item_package",""), item_package, 32, 3);
		played_time = preferences.getLong("played_time", played_time);
		
		GmudTemp.ClearAllData();
		
		return true;
	}

	void reset() {
		water = food = hp_full = hp_max = hp = 100;
		pre_force = pre_agility = pre_savvy = pre_aptitude = 20;
		fp = fp_level = fp_plus = mp = mp_level = mp_plus = 0;
		money = potential = 100;
		exp = 0;

		image_id = sex = face_level = class_id = teacher_id = 0;
		bliss = married = unknow1 = unknow2 = unknow3 = 0;
		
		item_package[0][0] = 42;
		item_package[0][1] = 0;
		item_package[0][2] = 1;
		int i;
		for(i = 0; i < 16; i++)
			equips[i] = 0;
		for(i = 1; i < 32; i++)
			item_package[i][0] = item_package[i][1] = item_package[i][2] = 0;

		for(i = 0; i < 8; i++)
			select_skills[i] = 255;
		for(i = 0; i < 32; i++)
		{
			skills[i][0] = 255;
			skills[i][1] = skills[i][2] = skills[i][3] = skills[i][4] = 0;
		}

		for(i = 0; i< 40;i++)
			lasting_tasks[i] = 0;
		GmudTemp.ClearAllData();
		played_time = 0;
//		for(i = 0; i<16; i++)
//			player_name[i] = 0;
//		for(i = 0; i<16; i++)
//			weapon_name[i] = 0;
//		for(i = 0; i<16; i++)
//			consort_name[i] = 0;
		
		// initialization
		player_name = "[No Name]";
		weapon_name = null;
		consort_name = null;
	}
	
	public Player() {
		reset();
	}

	// dead
	void PlayerDead() {
		potential /= 2;
		exp -= (exp / 10);
		money -= (money / 5);
		bliss -= util.RandomInt(60);
		if (bliss < 0) {
			bliss = 0;
		}

		// skill--
		for (int i = 0; i < 32; i++) {
			if (skills[i][0] == 255 || skills[i][1] == 0)
				continue;
			--skills[i][1];
			if (skills[i][1] <= 0) {
				skills[i][1] = skills[i][2] = skills[i][3] = skills[i][4] = 0;
			}
		}
	}

	// 门派id
	int GetPlayerClassID() {
		return (class_id > 9) ? 0 : class_id;
	}

	/**
	 * 
	 * @return 食物最大
	 */
	int GetFoodMax() {
		return 75 + 15 * pre_force;
	}

	/**
	 * 饮水最大
	 * @return
	 */
	int GetWaterMax() {
		return 60 + 15 * pre_force;
	}

	/**
	 * 取生命值(Health Point)上限，与内力、年龄有关
	 * @return HP Max
	 */
	int GetHPMax() {
		return 100 + (fp_level / 4) + (GetAge() * 20);
	}

	/**
	 * 年龄增量（14起）
	 * @return
	 */
	int GetAge() {
		return (int) (played_time / AGE_TIME);
	}

	/**
	 * 计算技能的等级，如果此技能无效或未学，返回0
	 * @param id 技能ID
	 * @return 技能等级
	 */
	int GetSkillLevel(int id) {
		if (id > 53 || id < 0)
			return 0;
		for (int j = 0; j < 32; j++) {
			if (skills[j][0] >= 0 && skills[j][0] <= 53 && skills[j][0] == id)
				return skills[j][1];
		}
		return 0;
	}

	/**
	 * @return 招式总数
	 */
	int GetSkillNumber() {
		int i1 = 0;
		for (int j1 = 0; j1 < 32; j1++)
			if (skills[j1][0] != 255)
				i1++;

		return i1;
	}

	/**
	 * @return 角色等级
	 */
	int GetPlayerLevel() {
		int i1 = GetWeaponID();
		int j1 = Items.item_attribs[i1][1];
		int k1 = 0;
		if (i1 == 0) {
			k1 = (k1 = (k1 = (k1 = (k1 = (k1 = 0 + (GetSkillLevel(select_skills[0]) * 2) / 3)
					+ GetSkillLevel(1) / 3)
					+ (GetSkillLevel(select_skills[3]) * 2) / 3)
					+ GetSkillLevel(0) / 3)
					+ (GetSkillLevel(select_skills[2]) * 2) / 3)
					+ GetSkillLevel(7) / 3;
			int l1;
			if ((l1 = select_skills[4]) != 255 && Skill.skill_type[l1] == 0)
				k1 += (GetSkillLevel(select_skills[4]) * 2) / 3;
			k1 = (k1 += GetSkillLevel(8) / 3) / 4;
		} else {
			int i2;
			if ((i2 = select_skills[1]) != 255) {
				int j2 = Skill.skill_weapon_type[i2];
				if (j1 == j2) {
					k1 = 0 + (GetSkillLevel(i2) * 2) / 3;
					int k2 = Skill.skill_weapon_type[j1];
					k1 += GetSkillLevel(k2) / 3;
				}
			}
			k1 = (k1 = (k1 = (k1 += (GetSkillLevel(select_skills[3]) * 2) / 3) + GetSkillLevel(0)
					/ 3)
					+ (GetSkillLevel(select_skills[2]) * 2) / 3)
					+ GetSkillLevel(7) / 3;
			int l2;
			if ((l2 = select_skills[4]) != 255 && Skill.skill_type[l2] == 1)
				k1 += (GetSkillLevel(select_skills[4]) * 2) / 3;
			k1 = (k1 += GetSkillLevel(8) / 3) / 4;
		}
		if (k1 > 255)
			k1 = 255;
		if (k1 < 0)
			k1 = 0;
		return k1;
	}

	// 平均技能等级
	int GetSkillAverageLevel() {
		int i1 = 0;
		int j1 = 0;
		for (int j = 0; j < 32; j++)
			if (skills[j][0] != 255) {
				i1++;
				j1 += skills[j][1];
			}

		if (i1 == 0)
			return 0;
		else
			return j1 / i1;
	}

	/**
	 * 
	 * @return 轻功等级
	 */
	int GetFlySkillLevel() {
		return (GetSkillLevel(select_skills[2]) + GetSkillLevel(7) / 2);
	}

	/**
	 * 设置相貌等级,含技能提升 {驻颜术} {扶桑忍术}
	 * @return 
	 */
	int SetFaceLevel() {
		int i = GetSkillLevel(10) / 15 + GetSkillLevel(26) / 15 + face_level;
		return (i > 12) ? 12 : i;
	}

	/**
	 * 
	 * @return 相貌等级 [0-13]
	 */
	int GetFaceLevel() {
		if (GetAge() < 2)
			return -1;
		if (face_level > 13)
			return 13;
		else
			return face_level;
	}

	/**
	 *  
	 * @return 加力max
	 */
	int GetPlusFPMax() {
		int i1 = GetSkillLevel(0);
		if (0 == i1) {
			return 0;
		} else {
			int j1 = GetSkillLevel(select_skills[3]);
			return (i1 / 2 + j1) / 2;
		}
	}

	/**
	 * Mana Point
	 * @return 法点max
	 */
	int GetPlusMPMax() {
		int i1;
		if (0 == (i1 = GetSkillLevel(4))) {
			return 0;
		} else {
			int j1 = GetSkillLevel(select_skills[6]);
			return (i1 / 2 + j1) / 2;
		}
	}

	/**
	 * 
	 * @return 武器ID
	 */
	int GetWeaponID() {
		return equips[15];
	}

	/**
	 * 
	 * @return 后天膂力
	 */
	int GetForce() {
		int i1 = GetSkillLevel(1);
		int j1 = 0;
		if (lasting_tasks[9] != 0 && lasting_tasks[8] / 256 == 4 && equips[15] == 77)
			j1 = 20-(lasting_tasks[8]&3)*5/*Items.item_attribs[77][4]*/; // 自制武器加成，下同
		return i1 / 10 + pre_force + j1;
	}

	/**
	 * 
	 * @return 敏捷
	 */
	int GetAgility() {
		int i1 = GetSkillLevel(7);
		int j1 = 0;
		if (lasting_tasks[9] != 0 && lasting_tasks[8] / 256 == 3 && equips[15] == 77)
			j1 = 20-(lasting_tasks[8]&3)*5/*Items.item_attribs[77][4]*/;
		return i1 / 10 + pre_agility + j1;
	}

	/**
	 * 
	 * @return 悟性
	 */
	int GetSavvy() {
		int i1 = GetSkillLevel(9);
		int j1 = 0;
		if (lasting_tasks[9] != 0 && lasting_tasks[8] / 256 == 6 && equips[15] == 77)
			j1 = 20-(lasting_tasks[8]&3)*5/*Items.item_attribs[77][4]*/;
		return i1 / 10 + pre_savvy + j1;
	}

	// 根骨
	int GetAptitude() {
		int i1 = GetSkillLevel(0);
		int j1 = 0;
		if (lasting_tasks[9] != 0 && lasting_tasks[8] / 256 == 5 && equips[15] == 77)
			j1 = 20-(lasting_tasks[8]&3)*5/*Items.item_attribs[77][4]*/;
		return i1 / 10 + pre_aptitude + j1;
	}

	// 婚姻
	String GetConsortName() {
		final String str;
		if (null == consort_name) {
			if (0 == sex)
				str = "你还是光棍一条";
			else
				str = "你还待字闺中";
		} else {
			if (0 == sex) {
				str = "你老婆是" + consort_name;
			} else {
				str = "你老公是" + consort_name;
			}
		}
		return str;
	}


	/**
	 * 重置 技能升级 需要的点数 = (当前等级+1)*(当前等级+1) / 悟性 * 2 * 10 + Rnd(16)
	 * 
	 * @param id
	 */
	void SetSkillUpgrate(int id) {
		int j1 = skills[id][1] + 1;
		int k1;
		if ((k1 = (((j1 + 1) * (j1 + 1)) / GetSavvy()) * 2 * 10) < 10)
			k1 = 10 + util.RandomInt(16);
		else
			k1 += util.RandomInt(16);
		skills[id][4] = k1;
//		skills[id][2] = 0;
	}

	// 出手等级
	int GetAttackLevel() {
		int i1 = GetForce();
		int j1;
		if ((j1 = GetWeaponID()) != 0)
			i1 += Items.item_attribs[j1][2];
		if ((i1 = (i1 += fp_plus) / 20) > 5)
			i1 = 5;
		return i1;
	}

	// 打坐速度
	int GetFPSpeed() {
		int i1 = GetSkillLevel(0);
		int j1;
		int k1 = GetSkillLevel(j1 = select_skills[3]);
		int l1 = GetAptitude();
		int i2;
		if ((i2 = (i1 / 2 + k1) / 10 + l1 / 5) <= 0) // 基本内功/20 + 门派内功/20 + 后天根骨 / 5
			i2 = 1;
		if (i2 > 60)
			i2 = 60;
		return i2;
	}

	// 冥思速度
	int GetMPSpeed() {
		int i1 = GetSkillLevel(4);
		int j1;
		int k1 = GetSkillLevel(j1 = select_skills[6]);
		int l1 = GetSavvy();
		int i2;
		if ((i2 = (i1 / 2 + k1) / 10 + l1 / 5) <= 0) // 基本法术/20 + 选择法术/10 + 后天悟性 / 5
			i2 = 1;
		if (i2 > 60)
			i2 = 60;
		return i2;
	}

	// 学习速度
	int GetStudySpeed() {
		int i1;
		if ((i1 = (i1 = GetSavvy()) / 2) <= 0) // 后天悟性 / 2
			i1 = 1;
		if (i1 > 60)
			i1 = 60;
		return i1;
	}

	// 练功速度
	int GetPracticeSpeed(int id) {
		int j1 = skills[id][0];
		int k1 = 0;
		int l1;
		switch (l1 = Skill.skill_weapon_type[j1]) {
		case 0:
			k1 = GetSkillLevel(1);
			break;

		case 1:
			k1 = GetSkillLevel(3);
			break;

		case 6:
			k1 = GetSkillLevel(2);
			break;

		case 7:
			k1 = GetSkillLevel(5);
			break;

		case 9:
			k1 = GetSkillLevel(6);
			break;
		}
		if (j1 == select_skills[2])
			k1 = GetSkillLevel(7);
		int i2 = GetSkillLevel(0);
		int j2;
		int k2;
		if ((j2 = select_skills[3]) == 255)
			k2 = 0;
		else
			k2 = GetSkillLevel(j2);
		int l2;
		int i3;
		if ((i3 = (l2 = i2 / 2 + k2) / 5 + k1 / 5) <= 0) // 基本内功/10 + 门派内功/5 + 对应基本招式 / 5
			i3 = 1;
		if (i3 > 60)
			i3 = 60;
		return i3;
	}


	/**
	 * 可练功招式数, [0,2] [拳脚，兵刃，轻功]
	 * @return 招式数量
	 * GmuTemp.temp_array_20_2[0,2] 用于保存技能等级
	 */
	int GetPracticeSkillNumber() {
		int i1 = 0;
		for (int j1 = 0; j1 < 3; j1++) {
			int k1;
			int l1;
			if (select_skills[j1] != 255 && (l1 = GetSkillLevel(k1 = select_skills[j1])) != 0) {
				GmudTemp.temp_array_20_2[i1][0] = k1; // 技能ID
				GmudTemp.temp_array_20_2[i1][1] = l1; // 等级
				i1++;
			}
		}
		return i1;
	}

	// 存在id物品num个，返回物品栏编号
	int ExistItem(int id, int num) {
		for (int k1 = 0; k1 < 32; k1++)
			if (item_package[k1][0] == id && item_package[k1][2] >= num)
				return k1;

		return -1;
	}

	// 增加一个物品
	boolean GainOneItem(int id) {
		if (id <= 0)
			return false;
		int j1;
		if ((j1 = Items.item_repeat[id]) > 0) // item 可叠加
		{
			for (int k1 = 0; k1 < 32; k1++)
				if (item_package[k1][0] == id && item_package[k1][2] < 255) {
					item_package[k1][2]++;
					return true;
				}

		}
		for (int l1 = 0; l1 < 32; l1++)
			// 不可叠加处理
			if (item_package[l1][0] == 0) {
				item_package[l1][0] = id;
				item_package[l1][1] = 0;
				item_package[l1][2] = 1;
				return true;
			}

		return false;
	}

	void LoseOneItem(int i1) {
		for (int j1 = 0; j1 < 32; j1++)
			if (item_package[j1][0] == i1)
				DeleteOneItem(j1);
	}

	/**
	 * 删除物品
	 * @param i1 当前包裹中的编号
	 * @param j1 需要删除的数量
	 */
	void LoseItem(int i1, int j1) {
		int k1 = -1;
		
		// TODO: 其实应该不用下面的循环，待分析 @lnx
		if (0 <= i1 && i1 < 32) {
			i1 = item_package[i1][0];
		}
		
		for (int l1 = 0; l1 < 32; l1++) {
			if (item_package[l1][0] != i1)
				continue;
			if (k1 >= 0 && item_package[l1][1] == 0) {
				k1 = l1;
				continue;
			}
			if (k1 < 0)
				k1 = l1;
		}

		if (k1 >= 0) {
			item_package[k1][2] -= j1;
			if (item_package[k1][2] <= 0) {
				if (item_package[k1][1] == 1) {
					for (int i2 = 0; i2 < 16; i2++)
						if (equips[i2] == i1)
							equips[i2] = 0;

				}
				item_package[k1][0] = item_package[k1][1] = item_package[k1][2] = 0;
			}
			return;
		} else {
			return;
		}

	}

	void DeleteOneItem(int i1) {
		if (i1 == -1)
			return;
		int j1 = item_package[i1][0];
		if (item_package[i1][1] == 1)
			return;
		if (j1 < 0 || j1 > 91)
			return;
		int k1 = Items.item_attribs[j1][0];
		int l1 = Items.item_attribs[j1][1];
		if (k1 == 2) {
			if (item_package[i1][1] != 0)
				UnEquipWeapon();
		} else if (k1 == 3 && item_package[i1][1] != 0)
			UnEquipArmor(l1);
		item_package[i1][0] = item_package[i1][1] = item_package[i1][2] = 0;
	}

	boolean a(int i1, int j1) {
		if (item_package[i1][2] >= j1) {
			item_package[i1][2] -= j1;
			if (item_package[i1][2] == 0) {
				int k1 = item_package[i1][0];
				for (int l1 = 0; l1 < 16; l1++)
					if (equips[l1] == k1)
						equips[l1] = 0;

				item_package[i1][0] = 0;
				item_package[i1][1] = 0;
				DeleteOneItem(i1);
			}
			return true;
		} else {
			return false;
		}
	}

	String UseItem(int i1)
{
	int j1;
	if ((j1 = item_package[i1][0]) < 0 || j1 > 91)
		return "";
	int k1 = Items.item_attribs[j1][0];  //type
	int l1 = Items.item_attribs[j1][1];
	switch(k1)  //switch type
	{
	case 5:
	default:
		break;

	case 0: 
		{ // 吃食物
			if (Battle.sBattle!=null)
				break;
			int i2 = Items.item_attribs[j1][2];
			int j2 = Items.item_attribs[j1][3];
			if (food >= GetFoodMax())
				return "你再也吃不下任何东西了";
			food += i2;
			water += j2;
			item_package[i1][2]--;
			if (item_package[i1][2] <= 0)
			{
				item_package[i1][0] = 0;
				item_package[i1][1] = 0;
				item_package[i1][2] = 0;
			}
			break;
		}
	case 1: // 用药
		if (Battle.sBattle != null && l1 == 0) {
			int player_id = Battle.sBattle.player_id;
			int hp_max_tmp = Battle.sBattle.fighter_data[player_id][2];
			int hp_full_tmp = Battle.sBattle.fighter_data[player_id][3];
			if (hp_max_tmp >= hp_full_tmp)
				break;
			hp_max_tmp += Items.item_attribs[j1][2];
			if (hp_max_tmp > hp_full_tmp)
				hp_max_tmp = hp_full_tmp;
			item_package[i1][2]--;
			if (item_package[i1][2] <= 0) {
				item_package[i1][0] = 0;
				item_package[i1][1] = 0;
				item_package[i1][2] = 0;
			}
			Battle.sBattle.fighter_data[player_id][2] = hp_max_tmp;
			Battle.sBattle.a(player_id, 2, hp_max_tmp);
			break;
		}
		if (l1 == 0) {
			if(hp_max == hp_full)
				break;
			hp_max += Items.item_attribs[j1][2];
			if (hp_max > hp_full)
				hp_max = hp_full;
			item_package[i1][2]--;
			if (item_package[i1][2] <= 0) {
				item_package[i1][0] = 0;
				item_package[i1][1] = 0;
				item_package[i1][2] = 0;
			}
			break;
		}
		if (l1 != 1 || Battle.sBattle != null)
			break;
		if (lasting_tasks[13] < 10000)
		{
			fp_level += item_package[i1][2];
			lasting_tasks[13] += 1;
		}
		item_package[i1][2]--;
		if (item_package[i1][2] <= 0)
		{
			item_package[i1][2] = 0;
			item_package[i1][0] = 0;
			item_package[i1][1] = 0;
		}
		break;

	case 2: //weapon
      if ((Battle.sBattle != null) && (item_package[i1][0] == 77))
    	  break; // 战斗的时候不换自制武器（影响玩家属性计算）
      
		if (item_package[i1][1] == 0)
			EquipWeapon(i1);
		else
			UnEquipWeapon();
		if(Battle.sBattle!=null)
			Battle.sBattle.b();
		break;

	case 3: //equip
		{
			if (item_package[i1][1] == 0)
				EquipArmor(l1, i1);
			else
				UnEquipArmor(l1);
			if(Battle.sBattle!=null)
				Battle.sBattle.b();
			break;
		}
	case 4: //read book
		{
			if (Battle.sBattle!=null)
				break;
			
			int k2 = GetSkillLevel(9);
			if (k2 == 0)
				return "你还是个文盲!";
			int l2 = k2 / 2;
			if (l2 == 0)
				l2 = 1;
			int skill_id = Items.item_attribs[j1][2];
			int hp_expend = Items.item_attribs[j1][3];
			int max_level = Items.item_attribs[j1][4];
			int require_EXP = Items.item_attribs[j1][5];
			int skill_pos;
			if (GetSkillLevel(skill_id) == 0) {
				skill_pos = AddNewSkill(skill_id);
				if (skill_pos == -1) {
					return "";
				}
				
				skills[skill_pos][1] = 1;
				SetNewSkill(skill_id);
				/*
				 * wchar_t str[9];
				 * wcscpy(str, "你学会了");
				 * wcscat(str, Skill.skill_name[i3]);
				 * return str;
				 */
				return "你埋头研读,似乎有点心得";
			}
			skill_pos = SetNewSkill(skill_id);
			if (skills[skill_pos][4] <= 0) {
				SetSkillUpgrate(skill_pos);
			}
			if (max_level < skills[skill_pos][1])
				return "书上所说的对你太浅了";
			int k4 = (((require_EXP * 1000) / max_level) * (skills[skill_pos][1] + 1)) / 1000;
			if (exp < k4)
				return "你的实战经验不足";
			if (hp < hp_expend)
				return "你现在太疲倦了,没法研读";
			skills[skill_pos][2] += l2;
			hp -= hp_expend;
			if (skills[skill_pos][2] > skills[skill_pos][4])
			{
				skills[skill_pos][4] = 0;
				skills[skill_pos][1] += 1;
				skills[skill_pos][2] = 0;
				SetSkillUpgrate(skill_pos);
				return "你的功夫进步了";
			} else
			{
				return "你埋头研读,似乎有点心得";
			}
		}
	}
	return "";
}

	void EquipArmor(int i1, int j1) {
		int k1;
		if ((k1 = item_package[j1][0]) < 0 || k1 > 91)
			return;
		int l1;
		if ((l1 = Items.item_attribs[k1][0]) != 3)
			return;
		if (equips[i1] != 0)
			UnEquipArmor(i1);
		item_package[j1][1] = 1;
		equips[i1] = k1;
	}

	void UnEquipArmor(int i1) {
		int j1;
		if ((j1 = equips[i1]) == 0)
			return;
		int k1 = 0;
		while (true) {
			if (k1 >= 32)
				break;
			if (item_package[k1][0] == j1 && item_package[k1][1] != 0) {
				item_package[k1][1] = 0;
				break;
			}
			k1++;
		}
		equips[i1] = 0;
	}

	void EquipWeapon(int i1) {
		int j1 = item_package[i1][0];
		if (equips[15] != 0)
			UnEquipWeapon();
		if (j1 < 0 || j1 > 91)
			return;
		int k1;
		if ((k1 = Items.item_attribs[j1][0]) != 2) {
			return;
		} else {
			item_package[i1][1] = 1;
			equips[15] = j1;
			return;
		}
	}

	void UnEquipWeapon() {
		int i1;
		if ((i1 = equips[15]) == 0)
			return;
		int j1 = 0;
		do {
			if (j1 >= 32)
				break;
			if (item_package[j1][0] == i1 && item_package[j1][1] != 0) {
				item_package[j1][1] = 0;
				break;
			}
			j1++;
		} while (true);
		equips[15] = 0;
	}

	int CopyItemList() {
		int i1 = 0;
		for (int j1 = 0; j1 < 32; j1++)
			if (item_package[j1][0] != 0 && item_package[j1][1] == 0) {
				GmudTemp.temp_array_32_2[i1][0] = item_package[j1][0];
				GmudTemp.temp_array_32_2[i1++][1] = item_package[j1][2];
			}

		return i1;
	}

	/**
	 * 添加新技能，如果技能已存在，则返回序号，否则新建一个等级为1的技能。
	 * @param id
	 * @return
	 */
	int AddNewSkill(int id) {
		if (GetSkillLevel(id) > 0)
			return SetNewSkill(id);
		for (int j1 = 0; j1 < 32; j1++)
			if (skills[j1][0] == 255) {
				skills[j1][0] = id;
				skills[j1][1] = 1;
				skills[j1][2] = 0;
				skills[j1][3] = 0;
				SetSkillUpgrate(j1);
				return j1;
			}

		return -1;
	}

	/**
	 * 添加新技能，如果该技能已经存在，直接返回它的索引序号
	 * @param id 技能ID
	 * @return 0 无效技能
	 * -1 异常 
	 * [0,32) 技能序号
	 */
	int SetNewSkill(int id) {
		if (id < 0 || id > 53)
			return 0;
		for (int j1 = 0; j1 < 32; j1++)
			if (skills[j1][0] >= 0 && skills[j1][0] <= 53 && skills[j1][0] == id)
				return j1;

		for (int k1 = 0; k1 < 32; k1++)
			if (skills[k1][0] > 53) {
				skills[k1][0] = id;
				skills[k1][1] = 0;
				skills[k1][2] = skills[k1][3] = skills[k1][4] = 0;
				return k1;
			}

		return -1;
	}

	// 勾选技能
	void SelectSkill(int id, int type) {
		if (id < 10)
			return;
		int k1;
		if ((k1 = select_skills[type]) != 255)
			UnselectSkill(type);
		select_skills[type] = id;
	}

	// 取消选择技能
	void UnselectSkill(int type) {
		select_skills[type] = 255;
	}

	// 请教
	int StudySkill(int id, int maxlevel) {
		int k1 = skills[id][0];
		int l1;
		if ((l1 = skills[id][1]) >= maxlevel)
			return 4;
		if (exp < l1 * 2 * (l1 * 2))
			return 1;
		if (skills[id][4] <= 0)
			SetSkillUpgrate(id);
		int i2;
		if ((i2 = skills[id][2]) > skills[id][4]) {
			skills[id][2] = skills[id][4];
			return 0;
		}
		if (i2 == skills[id][4]) {
			skills[id][2] = 0;
			skills[id][4] = 0;
			skills[id][1] += 1;
			return 5;
		}
		if (potential <= 0)
			return 2;
		if (k1 == 9 && money <= 0)
			return 3;
		if ((i2 += 4) / 10 != skills[id][2] / 10) {
			potential -= 1;
			if (k1 == 9)
				money -= 1;
		}
		skills[id][2] = i2;
		return 0;
	}


	/**
	 * 练功
	 * @param id 技能的序号
	 * @return 0 正常学习
	 * 1 很难提高，需要向师傅请教 
	 * 2 需要提升内功
	 * 3 打坐不够
	 * 4 没有趁手兵器
	 * 5 有伤
	 * 6 升级
	 */
	int PracticeSkill(int id) {
		
		// 要先疗伤
		if (hp_full != hp_max)
			return 5;

		// 计算基本功的等级
		final int k1;
		
		final int j1 = skills[id][0]; // 实际技能ID
		if (j1 == select_skills[2]) {
			// 如果是已选择的轻功
			k1 = GetSkillLevel(7);
		} else {
			final int l1 = Skill.skill_weapon_type[j1];
			switch (l1) {
			case 0: // 拳脚类
				k1 = GetSkillLevel(1);
				break;

			case 1: // 刀法类
				k1 = GetSkillLevel(3);
				break;

			case 6: // 剑法类
				k1 = GetSkillLevel(2);
				break;

			case 7: // 杖法类
				k1 = GetSkillLevel(5);
				break;

			case 9: // 鞭法类
				k1 = GetSkillLevel(6);
				break;
			default:
				k1 = 0;
				break;
			}

			// 检查武器
			if (l1 > 0) {
				int i2 = GetWeaponID();
				int j2 = Items.item_attribs[i2][1];
				if (j2 != l1)
					return 4;
			}
		}
		
		// 已超过基本功
		int k2;
		if ((k2 = skills[id][1]) > k1)
			return 1;
		
		// 查检内功修为是否足够
		int l2 = GetSkillLevel(0);
		int i3;
		int j3;
		if ((i3 = select_skills[3]) == 255)
			j3 = 0;
		else
			j3 = GetSkillLevel(i3);
		int k3 = l2 / 2 + j3; // (基本内功/2 + 门派内功)
		if (k2 > k3)
			return 2;
		
		int l3 = k3 * 10;
		if (class_id == 8)
			l3 += exp / 1000;
		int i4;
		if ((i4 = ((k2 + 1) * ((l3 * 1000) / k3)) / 1000) > fp_level)
			return 3;
		
		
		int j4;
		if ((j4 = skills[id][4]) <= 0)
			SetSkillUpgrate(id);
		
		int k4;
		if ((k4 = skills[id][2]) > skills[id][4]) {
			skills[id][2] = skills[id][4];
			return 0;
		}
		if (k4 == skills[id][4]) {
			skills[id][2] = 0;
			skills[id][4] = 0;
			skills[id][1] += 1;
			return 6;
		} else {
			k4+=1+k1/5+GetSavvy()/10;
			skills[id][2] = k4;
			return 0;
		}
	}

	// 所学技能最高等级
	int GetMaxSkillLevel() {
		int i1 = 0;
		for (int k1 = 0; k1 < 32; k1++) {
			int j1;
			if (skills[k1][0] != 255 && (j1 = skills[k1][1]) > i1)
				i1 = j1;
		}
		return i1;
	}

	// 吸气
	String Breathing() {
		int i1 = GetSkillLevel(0);
		if (255 == select_skills[3] || i1 == 0) {
			return "你必须选择你要用的内功心法.";
		}
		if (hp >= hp_max) {
			hp = hp_max;
			return "你现在体力充沛.";
		}
		if (fp_level < 50 || fp < 50) {
			return "你的内力不够.";
		}
		int j1;
		if ((j1 = (hp_max - hp) * 2) > fp)
			j1 = fp;
		fp -= j1;
		hp += j1 / 2;
		if (hp > hp_max)
			hp = hp_max;
		return "你深深吸了几口气,脸色看起来好多了.";
	}

	// 疗伤
	String Recovery() {
		int j1 = GetSkillLevel(0);
		if (select_skills[3] == 255 || j1 == 0) {
			return "你必须选择你要用的内功心法.";
		}
		if (hp_full == hp_max) {
			return "你并没有受伤.";
		}
		int i1 = 0;
		int k1 = GetSkillLevel(select_skills[3]);
		if ((i1 = j1 / 2 + k1) < 45) {
			return "你运功良久,一抖衣袖,长叹一声站起身来.";
		}
		if (hp_max <= 0 || hp_full / hp_max > 4) {
			return "你已经受伤过重,只怕一运真气就会有生命危险.";
		}
		if (fp_level < 150 || fp < 150) {
			return "你的真气不够,还不能用来疗伤.";
		}
		fp -= 50;
		int l1 = (i1 * 2) / 3;
		hp_max += l1;
		if (hp_max > hp_full)
			hp_max = hp_full;
		return "你摧动真气,脸上一阵红一阵白,哇的一声吐出一口淤血,脸色看起来好多了.";
	}

	// 打坐
	int Meditation() {
		int i1;
		if ((i1 = GetSkillLevel(0)) == 0) // 基本内功 ==0
			return 2;
		int j1;
		int k1;
		if ((k1 = GetSkillLevel(j1 = select_skills[3])) == 0 || j1 == 255) // no 门派内功
			return 1;
		if (fp >= fp_level * 2) {
			int l1;
			l1 = (l1 = (i1 / 2 + k1) * 10) + exp / 1000; // max = 基本内功*5+门派内功*10+（年龄-14）*10+经验/1000
			if (fp_level < l1) {
				fp = 0;
				fp_level += 1;
				return 0;
			} else {
				fp = fp_level;
				return 3;
			}
		} else {
			fp += 2+(i1+k1*2)/40; // @lnx: 增加一个与技能相关的量，让打坐加快
			return 0;
		}
	}

	/**
	 * 冥思
	 * @return 0:正常 
	 * 1:没有选择
	 * 2:基本法术不够
	 * 3:已达到上限
	 */
	int Think() {
		int i1;
		if ((i1 = GetSkillLevel(4)) == 0) // basic == 0
			return 2;
		int j1;
		int k1;
		if ((k1 = GetSkillLevel(j1 = select_skills[6])) == 0) // no selected
			return 1;
		if (mp >= mp_level * 2) {
			int l1 = (i1 / 2 + k1) * 10;
			if (class_id == 8)
				l1 += exp / 1000;
			if (mp_level < l1) {
				mp = 0;
				mp_level += 1;
				return 0;
			} else {
				mp = mp_level;
				return 3;
			}
		} else {
			mp += 3+(i1+k1*2)/40; // @lnx: 增加一个与技能相关的量，让打坐加快
			return 0;
		}
	}

	int CopyItemData(int i1, int j1) {
		GmudTemp.Clear32Data();
		i1++;
		int k1 = 0;
		int l1 = 0;
		for (int i2 = i1; i2 < 32; i2++) {
			int j2;
			if ((j2 = item_package[i2][0]) == 0 || j2 > 91)
				continue;
			int k2;
			if ((k2 = Items.item_attribs[j2][0]) == 5)
				k2 = 4;
			if (k2 != j1)
				continue;
			if (l1 == 0) {
				GmudTemp.temp_array_32_2[l1][0] = i2;
				GmudTemp.temp_array_32_2[l1][1] = item_package[i2][1];
				l1++;
				k1 = i2;
			} else {
				GmudTemp.temp_array_32_2[l1][0] = i2;
				GmudTemp.temp_array_32_2[l1][1] = item_package[i2][1];
				l1++;
			}
		}

		if (l1 == 0)
			return 0;
		else
			return (k1 << 8) + l1;
	}

	int CopySkillData(int i1, int j1) {
		GmudTemp.Clear32Data();
		++i1;
		int k1 = 0;
		int l1 = 0;
		for (int i2 = i1; i2 < 32; i2++) {
			if (skills[i2][0] < 0 || skills[i2][0] > 53 || Skill.skill_type[skills[i2][0]] != j1
					&& (j1 != 4 || Skill.skill_type[skills[i2][0]] >= 2 || skills[i2][0] <= 9))
				continue;
			if (l1 == 0) {
				GmudTemp.temp_array_32_2[l1][0] = i2;
				GmudTemp.temp_array_32_2[l1][1] = (select_skills[j1] != skills[i2][0] ? 0 : 1);
				l1++;
				k1 = i2;
				continue;
			}
			if (l1 < 32) {
				GmudTemp.temp_array_32_2[l1][0] = i2;
				GmudTemp.temp_array_32_2[l1][1] = (select_skills[j1] != skills[i2][0] ? 0 : 1);
			}
			l1++;
		}

		if (l1 == 0)
			return 0;
		else
			return (k1 << 8) + l1;
	}
}
