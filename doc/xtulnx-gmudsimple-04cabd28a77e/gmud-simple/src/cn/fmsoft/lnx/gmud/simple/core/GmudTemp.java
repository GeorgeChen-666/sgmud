package cn.fmsoft.lnx.gmud.simple.core;

public class GmudTemp {
	static private int a = 0;
	static Object timer_thread_handle;
	
	static int temp_array_32_2[][] = new int[32][2];
	static int temp_array_20_2[][] = new int[20][2];
	
	static void ClearAllData() {
		int i;
		for(i = 0; i<32; i++)
		{
			temp_array_32_2[i][0] = 255;
			temp_array_32_2[i][1] = 0;
		}
		for(i = 0; i<20; i++)
		{
			temp_array_20_2[i][0] = 255;
			temp_array_20_2[i][1] = 0;
		}
		/*
		for(i = 0; i<40; i++)
		{
			temp_task_data[i] = 0;
		}
		*/
	}
	
	static void Clear32Data() {
		for(int i = 0; i<32; i++)
		{
			temp_array_32_2[i][0] = 255;
			temp_array_32_2[i][1] = 0;
		}
	}
	
	static void Clear20Data() {
		for(int i = 0; i<20; i++)
		{
			temp_array_20_2[i][0] = 255;
			temp_array_20_2[i][1] = 0;
		}
	}
	
	static void TimerFunc() {		
		task.temp_tasks_data[5] += 5;  //捕快time
		task.temp_tasks_data[29] += 5;  //石料time
		task.temp_tasks_data[30] += 5;  //存档时间
		task.temp_tasks_data[31] += 5;  //年龄周期
		if (task.temp_tasks_data[31] > 300)
		{
			++Gmud.sPlayer.played_time;  //++play time
			task.temp_tasks_data[31] = 0;
		}
		task.temp_tasks_data[32] += 5;
		for (int i = 0; i < 2; i++)
		{
			if (a < 0 || a > 147)  //npc recover
			{
				Map.NPC_flag[156] = 0;
				NPC.ResetData(156);
				Map.NPC_flag[157] = 0;
				NPC.ResetData(157);
				a = 0;
			}
			if (Map.NPC_flag[a] == 1)
			{
				Map.NPC_flag[a] = 0;
				NPC.ResetData(a);
			}
			a++;
			if (a > 147)
				a = 0;
		}

		if (Battle.sBattle == null)
		{
			task.temp_tasks_data[33] += 5;  //recover Hp fp mp time
			if (task.temp_tasks_data[33] > 15)
			{
				// 更新最大生命值（与年龄和内力上限有关）
				Gmud.sPlayer.hp_full = Gmud.sPlayer.GetHPMax(); 
				
				if (Gmud.sPlayer.food > 0 && Gmud.sPlayer.water > 0)
				{
					if (Gmud.sPlayer.hp < Gmud.sPlayer.hp_max - Gmud.sPlayer.GetAptitude() / 10)
						Gmud.sPlayer.hp += Gmud.sPlayer.GetAptitude() / 10;
					else
						Gmud.sPlayer.hp = Gmud.sPlayer.hp_max;

					if (Gmud.sPlayer.hp_max < Gmud.sPlayer.hp_full - Gmud.sPlayer.GetAptitude() / 10)
						Gmud.sPlayer.hp_max += Gmud.sPlayer.GetAptitude() / 10;
					else
						Gmud.sPlayer.hp_max = Gmud.sPlayer.hp_full;

//					if (Gmud.sPlayer.fp < Gmud.sPlayer.fp_level - Gmud.sPlayer.GetAptitude() / 10)
//						Gmud.sPlayer.fp += Gmud.sPlayer.GetAptitude() / 10;
//					else
//						Gmud.sPlayer.fp = Gmud.sPlayer.fp_level;
					if (Gmud.sPlayer.fp<Gmud.sPlayer.fp_level) {
						Gmud.sPlayer.fp+=Gmud.sPlayer.GetAptitude()/10;
					}

//					if (Gmud.sPlayer.mp < Gmud.sPlayer.mp_level - Gmud.sPlayer.GetAptitude() / 10)
//						Gmud.sPlayer.mp += Gmud.sPlayer.GetAptitude() / 10;
//					else
//						Gmud.sPlayer.mp = Gmud.sPlayer.mp_level;
					if (Gmud.sPlayer.mp < Gmud.sPlayer.mp_level) {
						Gmud.sPlayer.mp += Gmud.sPlayer.GetAptitude() / 10;
					}
				}
				if (Gmud.sPlayer.food > 0)
					--Gmud.sPlayer.food;
				if (Gmud.sPlayer.water > 0)
					--Gmud.sPlayer.water;
				task.temp_tasks_data[33] = 0;
			}
		}
	}

}
