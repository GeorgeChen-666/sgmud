package cn.fmsoft.lnx.gmud.simple.core;

import android.graphics.Bitmap;
import android.util.Log;

public class Map {

	/* keep single instance */
	static private Map sInstance;
	static synchronized Map getInstance() {
		if (sInstance == null) {
			sInstance = new Map();
		}
		return sInstance;
	}
	
	
	final static int ELEM_BASE_Y = 51; 
	
	int map_id;
	int player_dir;
	int stack_pointer;
	
	Bitmap []player_image = new Bitmap[6]; // player image 6
	static MapInfo.MAPINFO map_image_data = new MapInfo.MAPINFO(); // element image
	static MapInfo.MAPINFO map_event_data = new MapInfo.MAPINFO(); // event data
	
	static byte NPC_flag[] = new byte[185];
	
	static int d_int_static = 0; 
	static int e = 0;
	
//	private:
		int player_x;
		int player_image_id;
		int player_walk_status;
		int i;  //
		int j;
		int k;
		int l;
		boolean in_room;  //室内?
		boolean b_boolean;
		int m;
		int n;
		int o;
		int p;
		byte [] b_byte_array1d = new byte[244];
		Bitmap map_image[] = new Bitmap[244];    //[244]; //map img

		static int player_last_location[] = new int[64]; //player location stack
		
		// 93
		static int d_int_array1d_static[] = new int[] {
			15, 16, 16, 16, 13, 14, 17, 16, 16, 18, 
			24, 27, 19, 15, 22, 20, 13, 18, 21, 15, 
			24, 56, 22, 20, 57, 10, 27, 58, 25, 17, 
			60, 16, 16, 61, 16, 16, 62, 14, 16, 63, 
			16, 16, 64, 16, 16, 65, 16, 16, 66, 16, 
			13, 67, 12, 16, 68, 16, 16, 69, 16, 16, 
			121, 16, 16, 122, 16, 16, 123, 16, 16, 124, 
			16, 16, 125, 13, 16, 136, 11, 23, 137, 16, 
			16, 138, 16, 16, 139, 16, 18, 156, 26, 16, 
			158, 24, 18
		};
	
	
		
	public Map(){
		reset();
	}
	
	void reset() {
		map_id = 0;
		stack_pointer = 0;
		
		in_room = true;
		b_boolean = true; // xx
		
		m = 72;  //出口线起始X
		n = 20;  //出口线X间距
		p = 8;   //出口线Y长
		o = 71;  //出口线Y
		
//		memset(Map::NPC_flag, 0, 185 * sizeof(unsigned char));
//		memset(map_image, 0, 244 * sizeof(Image*));
		java.util.Arrays.fill(NPC_flag, (byte) 0);
		java.util.Arrays.fill(map_image, null);
	}
	
	public void recycle() {
//		if(map_image_data.data)
//			delete[] map_image_data.data;
//		if(map_event_data.data)
//			delete[] map_event_data.data;
//		for(int i = 0; i < 244; i++)
//			if(map_image[i])
//				DeleteObject(map_image[i]);
//		delete[] map_image;
//		for(i = 0; i < 6; i++)
//		{
//			if(player_image[i])
//				DeleteObject(player_image[i]);
//		}
//		delete[] player_image;
//		delete[] b_byte_array1d;
	}
	
	public boolean LoadMap(int id) {
		if (b_boolean)   //map ready?
		{
			b_boolean = false;
		}
		boolean flag = false;
		for (int i1 = 0; i1 < 11; i1++)  //root 地图
		{
			if (GmudData.fly_dest_map[i1] == id)
			{
				stack_pointer = 0;
				flag = true;
			}
	}

		boolean flag1 = in_room;
		int i2 = map_id;
		if (stack_pointer < 60)
		{
			player_last_location[stack_pointer] = i2;
			player_last_location[stack_pointer + 1] = i;
			player_last_location[stack_pointer + 2] = player_x;
			player_last_location[stack_pointer + 3] = player_dir;
			stack_pointer += 4;
		}
		
		map_id = id;

//		if(map_image_data.data)
//			delete[] map_image_data.data;
		MapInfo.GetMapElem(map_image_data, id);   //load elem image

//		if(map_event_data.data)
//			delete[] map_event_data.data;
		MapInfo.GetMapEvent(map_event_data, id);   //load map event 

		// XXX: 屏蔽武馆里面那面墙的地图属性。
		if (id == 5 && map_event_data.data[11] == -321) {
			map_event_data.data[11] = 255;
		}

		if (Gmud.DEBUG) {
			Log.i("lnx", "map id=" + id + " image-size=" + map_image_data.data.length + " event-size="+map_event_data.data.length);
		}
		
		// TODO: 
		if(task.bad_man_mapid == id)
		{
			// TODO: 
//			*(map_image_data.data + map_image_data.size - 1) = 186;
//			*(map_event_data.data + map_event_data.size - 1) = 179;
			map_image_data.data[map_image_data.size-1] = 186;
			map_event_data.data[map_event_data.size-1] = 179;
		}
		else
		{
			--map_event_data.size;
			--map_image_data.size;
		}

		byte [] abyte0 = new byte[244];
		int j2 = 0;
		in_room = false;
		int k2 = 0;
		while(k2 < 64)
		{
			if (id == MapInfo.Map_in_room[k2])
			{
				in_room = true;
				break;
			}
			k2++;
		}
		byte [] abyte1 = new byte[244];
		for (int l2 = 0; l2 < map_image_data.size; l2++)
			if (map_image_data.data[l2] >= 0 && map_image_data.data[l2] < 244)
				abyte1[map_image_data.data[l2]] = 1;

		if (flag && !flag1)
		{
			for (int i3 = 0; i3 < 244; i3++)
			{
				boolean flag2 = true;
				if (abyte1[i3] == 1)
					flag2 = false;
				if (flag2)
				{
//					DeleteObject(map_image[i3]);
					map_image[i3] = null;
					abyte0[i3] = 0;
				}
			}

		}
		for (int j3 = 0; j3 < map_image_data.size; j3++)
		{
			// TODO: 
//			Input.ProcessMsg();
			int l3;
			if (((l3 = map_image_data.data[j3]) & 0x8000000) != 0)
			{
				j2 += l3 & 0xffff;
				continue;
			}
			if(map_image[l3] == null)
				map_image[l3] = Res.loadimage(l3);
			abyte0[l3] = 1;
			j2 += map_image[l3].getWidth();
		}

		l = j2;
		for (int k3 = 0; k3 < 244; k3++)
		{
			b_byte_array1d[k3] = abyte0[k3];
			
			// TODO:
//			if (abyte0[k3] == 0)
//				DeleteObject(map_image[k3]);
			if (abyte0[k3] == 0) {
				map_image[k3] = null;
			}
		}
		
//		delete[] abyte0;
//		delete[] abyte1;
		
		return true;
	}


	public void LoadPlayer(int id)
	{
		if (id > 2)
			id = 2;
		id *= 6;
		player_image_id = id + 74;
		for (int j1 = 0; j1 < 6; j1++)
			player_image[j1] = Res.loadimage(player_image_id + j1);
	}

	static void SetNPCDead(int i1, byte byte0)   //set npc dead
	{
		NPC_flag[i1] = byte0;
	}

	public void SetPlayerLocation(int in_x, int dir)
	{
		if (in_x >= 0) // -1 keep
		{
			if (in_x < 0)
				in_x = 0;
			if (in_x > 144)
				in_x = 144;
			player_x = in_x;
		}
		player_dir = dir;
		player_walk_status = 0;
	}

	private void DrawPlayer()
	{
		// TODO: 
		Video.VideoClearRect(0, ELEM_BASE_Y, 160, 79);
		if (player_dir < 2)  //draw player
		{
			//draw player up/down
			Video.VideoDrawImage(player_image[player_dir], player_x, 1 + ELEM_BASE_Y);
		} else
		{
			//draw player left/right
			Video.VideoDrawImage(player_image[player_dir + player_walk_status], player_x, 1 + ELEM_BASE_Y);
		}
		if(in_room)  //draw 室内方框
		{
			Video.VideoDrawRectangle(0, 0, 159, 79);
			Video.VideoDrawLine(m, o, m, o + p);  //draw 出口1
			Video.VideoDrawLine(m + n, o, m + n, o + p);  //draw 出口1
		}
	}

	private void ReturnUpLevelMap()
	{
		if (stack_pointer >= 4)
		{
			boolean flag = in_room;
			int i1 = stack_pointer - 4;
			int j1 = player_last_location[i1];
			int k1 = player_last_location[i1 + 1];
			int l1 = player_last_location[i1 + 2];
			int i2 = player_last_location[i1 + 3];
			LoadMap(j1);  //load map
			stack_pointer = i1; //stack --
			if(flag)
				i2 = 1;
			DrawMap(k1);  //draw map
			SetPlayerLocation(l1, i2);  //set x & dir
			DrawPlayer();
		}
	}

	public void DirDown()
	{
		if (in_room &&  n / 2 > Math.abs(player_x - m))
			ReturnUpLevelMap();
	}

	public void DirUp()
	{
		int i1;
		if ((i1 = GetEvent()) < 0)
			return;
		int j1;
		if ((j1 = map_event_data.data[i1]) == 255)  //nothing
			return;
		if (j1 < 0)
		{
			if (j1 == -255)   //go back parent map
			{
				ReturnUpLevelMap();
				return;
			}
			if (j1 == -65535)  //goto map[0]
			{
				LoadMap(0);
				SetPlayerLocation(0, 4);
				DrawMap(0);
				return;
			}
			boolean flag = false;
			if (j1 < -255)  //goto map -[j1+256]
			{
				Log.e("lnx", "Map.DirUp, event = " + j1);
				flag = true;
				j1 = -(j1 + 256);
			}
			else
			{
				j1 = -j1;
			}
			int k1 = j1;
			LoadMap(k1);
			if(flag)
			{
				i = l - Gmud.WQX_ORG_WIDTH;
				if (i < 0)
					i = 0;
				SetPlayerLocation(Gmud.WQX_ORG_WIDTH, 2);
			}
			else
			{
				SetPlayerLocation(0, 4);
				i = 0;
			}
			if(in_room)
				SetPlayerLocation(m, 0);
			DrawMap(-1);
		}
	}

	public void DirLeft(int i1)
	{
		player_walk_status = 1-player_walk_status;  //player walk status
		player_dir = 2;   //set dir
		int j1 = 32;
		if (i == 0 && player_x < Gmud.WQX_ORG_WIDTH)
		{
			player_x -= 5;
			if (player_x < 0)
				player_x = 0;
			DrawPlayer();
			return;
		}
		if (player_x > j1)
		{
			player_x -= 4;
			if (player_x < j1)
				player_x = j1;
			DrawPlayer();
			return;
		}
		int k1 = map_image_data.size;
		int l1;
		if((l1 = i - i1) < 0)
			l1 = 0;
		int i2 = j;
		int j2 = k;
		while(true)
		{
			Input.ProcessMsg();
			if(i2 < 0)
				break;
			int k2 = map_image_data.data[i2];
			CalcElemWidth(k2);
			if (j2 <= l1)
			{
				Video.VideoClearRect(0, 0, Gmud.WQX_ORG_WIDTH, ELEM_BASE_Y);  //clean map image
				j = i2;
				k = j2;
				int k3;
				for(; j2 < l1 + Gmud.WQX_ORG_WIDTH && i2 < k1; j2 += k3)
				{
					Input.ProcessMsg();
					int l2 = map_image_data.data[i2];
					int j3 = j2 - l1;
					k3 = DrawElem(j3, l2, i2);
					i2++;
				}

				i = l1;
				break;
			}
			if (--i2 >= 0)
			{
				int i3 = CalcElemWidth(map_image_data.data[i2]);
				j2 -= i3;
			}
		}
		DrawPlayer();
	}

	public void DirRight(int i1)
	{
		int j1 = GetEvent();
		int k1 = -1;
		if (j1 >= 0)
			k1 = map_event_data.data[j1];
		player_dir = 4;
		player_walk_status = 1-player_walk_status;
		if (k1 >= 512 && task.MapPassable(k1 - 512))   //隐藏地图可通行检查
		{
			DrawPlayer();
			return;
		}
		int l1 = 48;
		if (player_x < Gmud.WQX_ORG_WIDTH - l1)
		{
			player_x += 5;
			if (player_x > Gmud.WQX_ORG_WIDTH - l1)
				player_x = Gmud.WQX_ORG_WIDTH - l1;
			DrawPlayer();
			return;
		}
		int i2 = map_image_data.size;
		int j2 = i + i1;
		if (i >= l - Gmud.WQX_ORG_WIDTH && player_x < Gmud.WQX_ORG_WIDTH - l1 / 3)
		{
			player_x += 4;
			if (player_x > Gmud.WQX_ORG_WIDTH - l1 / 3)
				player_x = Gmud.WQX_ORG_WIDTH - l1 / 3;
			DrawPlayer();
			return;
		}
		if (j2 > l - Gmud.WQX_ORG_WIDTH)
			j2 = l - Gmud.WQX_ORG_WIDTH;
		if (j2 < 0)
			j2 = 0;
		int k2 = j;
		int l2 = k;
		while(Input.Running)
		{
			Input.ProcessMsg();
			if (k2 >= i2)
				break;
			int i3 = map_image_data.data[k2];
			int k3 = CalcElemWidth(i3);
			if(l2 + k3 > j2)
			{
				Video.VideoClearRect(0, 0, Gmud.WQX_ORG_WIDTH, ELEM_BASE_Y);
				j = k2;
				k = l2;
				int i4;
				for (; l2 < j2 + Gmud.WQX_ORG_WIDTH && k2 < i2; l2 += i4)
				{
					Input.ProcessMsg();
					int j3 = map_image_data.data[k2];
					int l3 = l2 - j2;
					i4 = DrawElem(l3, j3, k2);
					k2++;
				}
				i = j2;
				break;
			}
			l2 += k3;
			k2++;
		}
		DrawPlayer();
	}

	public void KeyEnter()
	{
		if (player_dir != 0)
			return;
		int i1;
		if ((i1 = GetEvent()) < 0)
			return;
		int j1;
		if ((j1 = map_event_data.data[i1]) == 255)  //nothing
			return;
		if (j1 >= 0)
		{
			if (j1 >= 512)
				j1 -= 512;
			if (j1 >= 256)
			{
				MapEvent(j1 - 256);     //map event
				return;
			}
			int l1 = j1;    //event id < 256 NPC
			if (NPC_flag[l1] == 1)
				return;
			UI.npc_name = NPC.NPC_names[l1];  //npc name
			UI.npc_menu_x = d_int_static + 2;
			UI.npc_image_id = e;
			UI.npc_id = l1;
			ReadNPCMenu(l1);
			UI.NPCMainMenu();  //npc main menu
			Input.ClearKeyStatus();
			DrawMap(-1);
			Video.VideoUpdate();
		}
	}

//	extern void GmudDelay(unsigned int);
//	extern bool util.RandomBool(int);
//	extern int util.RandomInt(int);
//	extern String ReplaceStr(wstring* ,LPCWSTR, LPCWSTR);

	private void MapEvent(int k)
	{
		switch (k)
		{
		default:
			break;

		case 0:  //draw map name
			{
				for (int k1 = 0; k1 < 11; k1++)
					if(GmudData.fly_dest_map[k1] == map_id)
					{
						Gmud.GmudDelay(200);
						String str= GmudData.map_name[k1];
						UI.DrawMapTip(str);
						Video.VideoUpdate();
					}
				return;
			}
		case 1:
			Gmud.GmudDelay(150);
			if (task.temp_tasks_data[0] == 0)
			{
				String str=task.bad_man_nothing;
				UI.DrawMapTip(str);  //本镇治安良好
				Video.VideoUpdate();
				return;
			} else
			{
				String str=task.bad_man_doing;
				UI.DrawMapTip(str.replaceAll("o", NPC.NPC_names[179]));   //本镇正在缉拿人犯
				Video.VideoUpdate();
				return;
			}

		case 2: //扫地
			{
				if (task.temp_tasks_data[23] != 0 || task.temp_tasks_data[24] == 0)
					return;
				if (Gmud.sPlayer.hp < 40)
				{
					UI.ShowDialog(136);
					task.temp_tasks_data[24] = 0;
					return;
				}
				for (int l1 = 0; l1 < 4; l1++)
				{
					UI.DrawTalk(Res.readtext(5, UI.dialog_point[123 + l1], UI.dialog_point[1 + 123 + l1]));
					Video.VideoUpdate();
					Gmud.GmudDelay(900);
				}

				UI.ShowDialog(135);
				Gmud.sPlayer.exp += 20;
				Gmud.sPlayer.potential += 10;
				Gmud.sPlayer.money += 50;
				Gmud.sPlayer.hp -= 30;
				String str=task.old_woman_award;
				UI.DrawDialog(str);
				task.temp_tasks_data[24] = 0;
				return;
			}
		case 3: //挑水
			{
				if (task.temp_tasks_data[23] != 1 || task.temp_tasks_data[24] == 0)
					return;
				if (Gmud.sPlayer.hp < 50)
				{
					UI.ShowDialog(136);
					task.temp_tasks_data[24] = 0;
					return;
				}
				for (int i2 = 0; i2 < 4; i2++)
				{
					UI.DrawTalk(Res.readtext(5, UI.dialog_point[127 + i2], UI.dialog_point[1 + 127 + i2]));
					Video.VideoUpdate();
					Gmud.GmudDelay(900);
				}

				UI.ShowDialog(135);
				Gmud.sPlayer.exp += 20;
				Gmud.sPlayer.potential += 10;
				Gmud.sPlayer.money += 50;
				Gmud.sPlayer.hp -= 40;
				String str=task.old_woman_award;
				UI.DrawDialog(str);
				task.temp_tasks_data[24] = 0;
				return;
			}
		case 4: //劈柴
			{
				if (task.temp_tasks_data[23] != 2 || task.temp_tasks_data[24] == 0)
					return;
				if (Gmud.sPlayer.hp < 60)
				{
					UI.ShowDialog(136);
					task.temp_tasks_data[24] = 0;
					return;
				}
				for (int j2 = 0; j2 < 4; j2++)
				{
					UI.DrawTalk(Res.readtext(5, UI.dialog_point[131 + j2], UI.dialog_point[1 + 131 + j2]));
					Video.VideoUpdate();
					Gmud.GmudDelay(900);
				}

				UI.ShowDialog(135);
				Gmud.sPlayer.exp += 20;
				Gmud.sPlayer.potential += 10;
				Gmud.sPlayer.money += 50;
				Gmud.sPlayer.hp -= 50;
				String str=task.old_woman_award;
				UI.DrawDialog(str);
				task.temp_tasks_data[24] = 0;
				return;
			}
		case 5: //井
			{
				Gmud.GmudDelay(200);
				int k2 = Gmud.sPlayer.GetWaterMax();
				if (Gmud.sPlayer.water < k2)
				{
					Gmud.sPlayer.water += 20;
					if (Gmud.sPlayer.water > k2)
						Gmud.sPlayer.water = k2;
					String str="你在井边用杯子舀起井水喝";
					UI.DrawMapTip(str);
					return;
				} else
				{
					String str="你已经再也喝不下一滴水了";
					UI.DrawMapTip(str);
					return;
				}
			}
		case 6: //钓鱼

			if (Gmud.sPlayer.equips[9] != 73)  //无钓竿
			{
				UI.ShowDialog(137);
				return;
			}
			if (Gmud.sPlayer.hp < 40)
			{
				UI.ShowDialog(138);
				return;
			}
			UI.ShowDialog(139);
			UI.ShowDialog(140);
			Gmud.sPlayer.hp -= 40;  
			if(util.RandomBool(Gmud.sPlayer.GetAgility() * 2))
			{
				UI.ShowDialog(142);
				if (Gmud.sPlayer.ExistItem(76, 1) >= 0)  //检查鱼篓
				{
					UI.ShowDialog(144);
					Gmud.sPlayer.GainOneItem(74);
					return;
				} else
				{
					UI.ShowDialog(143);
					return;
				}
			} else
			{
				UI.ShowDialog(141);
				return;
			}

		case 7: //draw 坛 name
			{
				Gmud.GmudDelay(200);
				for (int l2 = 0; l2 < 8; l2++)
					if (GmudData.boss_map_id[l2] == map_id)
					{
						String str=GmudData.boss_map_name[l2];
						UI.DrawMapTip(str);
						return;
					}
				return;
			}
		case 8: //游戏厅入口
			{
				Gmud.GmudDelay(200);
				String s4 = Res.readtext(5, UI.dialog_point[110], UI.dialog_point[1 + 110]);
				int i3;
				s4 += task.yes_no;
				for (i3 = UI.DialogBx(s4, 8, 8); i3 != Input.kKeyEnt && i3 != Input.kKeyExit;)
					while(0 == (i3 = Input.inputstatus))
						Gmud.GmudDelay(100);
				Gmud.GmudDelay(300);
				if (i3 == Input.kKeyEnt)
				{//*/
					String s5 = Res.readtext(5, UI.dialog_point[111], UI.dialog_point[1 + 111]);
					int j3;
					if ((j3 = UI.DialogBx(s5, 8, 8)) == Input.kKeyLeft)
					{
						if (Gmud.sPlayer.GetSkillLevel(7) <= 0)
							return;
						mini_game_2.GameMain();    //dance
						int l3 = mini_game_2.score;
						if (Gmud.sPlayer.GetSkillLevel(7) > 60)
							l3 = 0;
						Gmud.GmudDelay(300);
						GameReturnMap(7, l3);   //基本轻功
						return;
					}
					if (j3 == Input.kKeyRight)
					{
						if (Gmud.sPlayer.GetSkillLevel(8) <= 0)
							return;
						mini_game_1.GameMain();    //投球
						int i4 = mini_game_1.score;
						if (Gmud.sPlayer.GetSkillLevel(8) > 60)
							i4 = 0;
						Gmud.GmudDelay(300);
						GameReturnMap(8, i4);  //基本招架
						return;
					}//*/
				}
				break;
			}
		case 9: //桃花源入口
			{
				Gmud.GmudDelay(200);
				String s6 = Res.readtext(5, UI.dialog_point[87], UI.dialog_point[1 + 87]);
				int k3;
				s6 += task.yes_no;
				for (k3 = UI.DialogBx(s6, 8, 8); k3 != Input.kKeyEnt && k3 != Input.kKeyExit;)
					while(0 == (k3 = Input.inputstatus))
						Gmud.GmudDelay(100);
				Gmud.GmudDelay(300);
				if (k3 != Input.kKeyEnt)
					break;
				if (Gmud.sPlayer.lasting_tasks[6] != 0)
				{
					UI.ShowDialog2(82);
					Gmud.GmudDelay(3000);
					UI.ShowDialog2(83);
					Gmud.GmudDelay(1000);
					LoadMap(89);
					SetPlayerLocation(0, 4);
					DrawMap(0);
					return;
				}
				UI.ShowDialog2(78);
				Gmud.GmudDelay(3000);
				if (util.RandomBool(Gmud.sPlayer.GetAgility() * 2))
				{
					while (true)
					{
						UI.ShowDialog2(80);
						Input.ClearKeyStatus();
						while(Input.inputstatus == 0)
							Gmud.GmudDelay(100);
						UI.ShowDialog2(197);
						Gmud.GmudDelay(2000);
						if (util.RandomBool(50))
						{
							if (util.RandomBool(30))
							{
								UI.ShowDialog2(84);
								Gmud.GmudDelay(1500);
								UI.ShowDialog2(85);
								Battle.sBattle = new Battle(147, 187, 0);
								Battle.sBattle.BattleMain();
//								delete glpBattle;
//								glpBattle = 0;
								Battle.sBattle = null;

								if (NPC.NPC_attrib[147][11] <= 0)
								{
									UI.ShowDialog2(86);
									Input.ClearKeyStatus();
									while(Input.inputstatus == 0)
										Gmud.GmudDelay(100);
									Gmud.sPlayer.lasting_tasks[6] = 1;
									LoadMap(89);
									SetPlayerLocation(0, 4);
									DrawMap(0);
									return;
								} else
								{
									return;
								}
							}
						} else
						{
							UI.ShowDialog2(81);
						}
					}
				} else
				{
					UI.ShowDialog2(79);
					Gmud.GmudDelay(15000);
					return;
				}
			}
		case 10: //铸武器
			{
				if (Gmud.sPlayer.lasting_tasks[4] == 1)  //on init
				{
					UI.ShowDialog(107);
					return;
				}
				if (Gmud.sPlayer.exp - Gmud.sPlayer.lasting_tasks[3] < 0x186a0)  //need more exp
				{
					UI.ShowDialog(104);
					return;
				}
				if (Gmud.sPlayer.money < Gmud.sPlayer.lasting_tasks[3] * 2)  //need more money
				{
					UI.ShowDialog(105);
					return;
				}
				String s7 = Res.readtext(5, UI.dialog_point[102], UI.dialog_point[1 + 102]);  //select type
				Input.ClearKeyStatus();
				Gmud.GmudDelay(200);
				int j4 = UI.DialogBx(s7, 8, 8);
				DrawMap(-1);
				byte byte0 = 0;
				if (j4 == Input.kKeyLeft)
					byte0 = 7;
				else
				if (j4 == Input.kKeyRight)
					byte0 = 9;
				else
				if (j4 == Input.kKeyUp)
					byte0 = 1;
				else
				if (j4 == Input.kKeyDown)
					byte0 = 6;
				if (byte0 == 0)
					return;
				Input.ClearKeyStatus();
				Gmud.GmudDelay(200);
				if (Gmud.sPlayer.ExistItem(77, 1) >= 0 && Items.item_attribs[77][1] != byte0)  //exist one
				{
					UI.ShowDialog(97);
					return;
				}
				Gmud.sPlayer.money -= Gmud.sPlayer.lasting_tasks[3] * 2;
				if (Gmud.sPlayer.GetForce() < 25 && util.RandomBool(50))
				{
					int k4;
					UI.ShowDialog(k4 = util.RandomInt(3) + 98);
					return;
				} else
				{
					UI.ShowDialog(108);  //init
					Gmud.sPlayer.lasting_tasks[3] = Gmud.sPlayer.exp;
					Gmud.sPlayer.lasting_tasks[4] = 1;
					Gmud.sPlayer.lasting_tasks[5] = byte0;
					return;
				}
			}
		case 11: //上吊
			Gmud.GmudDelay(200);
			Input.ClearKeyStatus();
			if (Gmud.sPlayer.ExistItem(19, 1) < 0)
			{
				String str="活得太没意思了\n真想找根绳子上吊自杀！\n";
				UI.DialogBx(str, 10, 16);
				return;
			}
			String str="如果您选择上吊自杀，那么角色存档记录将被删除！\n请务必考虑清楚！";
			str += task.yes_no;
			int l4 = UI.DialogBx(str, 10, 16);
			while (true)
			{
				Input.ProcessMsg();
				if (Input.inputstatus == Input.kKeyEnt)
				{/*/
					fclose(fopen("Gmud.sav", "w"));
					//exit
					exit(0);//*/
					GmudMain.Restart();
					return ;
				} else
				if (Input.inputstatus == Input.kKeyExit)
					return;
				Gmud.GmudDelay(100);
			}
		}
	}

	private void GameReturnMap(int k, int i1)   //mini game return map
	{
		int j1;
		if ((j1 = Gmud.sPlayer.SetNewSkill(k)) == -1)
			return;
		int k1 = 0;
		if (Gmud.sPlayer.skills[j1][4] <= 0)
			Gmud.sPlayer.SetSkillUpgrate(j1);
		Gmud.sPlayer.skills[j1][2] += i1;

		while (Gmud.sPlayer.skills[j1][2] >= Gmud.sPlayer.skills[j1][4]) {
			i1 = Gmud.sPlayer.skills[j1][2] - Gmud.sPlayer.skills[j1][4];
			Gmud.sPlayer.skills[j1][1] += 1;
			Gmud.sPlayer.skills[j1][2] = i1;
			Gmud.sPlayer.SetSkillUpgrate(j1);
			k1++;
			if (k1 > 255) {
				break;
			}
		}
		
		String str = String.format(
				"你的%s\n进步了:%d级！\n\n等级:%d\n点数:%d",
				Skill.skill_name[k], k1, 
				Gmud.sPlayer.skills[j1][1],
				Gmud.sPlayer.skills[j1][2]);

		UI.DialogBx(str, 8, 8);
	}

	private int CalcElemWidth(int id)
	{
		if ((id & 0x8000000) == 0)
			return map_image[id].getWidth();
		else
			return (id & 0xffff);
	}

	private int DrawElem(int i1, int j1, int k1) //i1 x
	{
		Input.ProcessMsg();
		int l1 = CalcElemWidth(j1);
		if((j1 & 0x8000000)!=0)
			return l1;
		int i2;
		if ((i2 = map_event_data.data[k1]) >= 512 || i2 >= 0 && i2 < 200)
			i2 &= 0xff;
		if (i2 >= 0 && i2 < 200 && NPC_flag[i2] == 1)  //NPC dead
			return l1;

		int j2 = ELEM_BASE_Y - map_image[j1].getHeight(); //image Y
		if (i1 + map_image[j1].getWidth() >= Gmud.WQX_ORG_WIDTH)
		{
			Video.VideoDrawImage(map_image[j1], i1, j2);
			return Gmud.WQX_ORG_WIDTH - i1;
		}
		else
		{
			Video.VideoDrawImage(map_image[j1], i1, j2);
			return l1;
		}
	}

	void DrawMap(int id)
	{
		int j1 = map_image_data.size;
		if (id < 0)
			id = i;
		if (l - id < Gmud.WQX_ORG_WIDTH - 1)
			id = l - Gmud.WQX_ORG_WIDTH;
		if (id < 0)
			id = 0;
		int k1 = 0;
		int l1 = 0;
		while(Input.Running)
		{
			Input.ClearKeyStatus();
			if (k1 >= j1)
				break;
			int i2 = map_image_data.data[k1];
			int k2 = CalcElemWidth(i2);
			if (l1 + k2 > id)
			{
				Video.VideoClear();
				j = k1;
				k = l1;
				int i3;
				for (; l1 < id + Gmud.WQX_ORG_WIDTH && k1 < j1; l1 += i3)
				{
					int j2 = map_image_data.data[k1];
					int l2 = l1 - id;
					i3 = DrawElem(l2, j2, k1);
					k1++;
				}

				i = id;
				break;
			}
			l1 += k2;
			k1++;
		}
		DrawPlayer();
	}

	private int GetEvent()
	{
		int i1 = j;
		int j1 = k;
		int k1 = map_image_data.size;
		d_int_static = 0;
		int k2 = 0;
		int l2 = i1;
		for (; i1 < k1 && j1 - i < Gmud.WQX_ORG_WIDTH; i1++)
		{
			int l1 = map_image_data.data[i1];
			int j2 = CalcElemWidth(l1);
			int i3 = j1;
			int j3 = j2;
			int k3 = 0;
			while (true)
			{
				if (k3 >= 90)
					break;
				if (d_int_array1d_static[k3] == l1)
				{
					i3 += d_int_array1d_static[k3 + 1];
					j3 = d_int_array1d_static[k3 + 2];
					break;
				}
				k3 += 3;
			}
			k3 = 16;
			int l3 = 0;
			if (player_x + i < i3)
			{
				if (player_x + k3 + i > i3)
				{
					if ((l3 = k3 - (i3 - player_x - i)) > j3)
						l3 = j3;
					if (l3 > k3)
						l3 = k3;
				}
			} else
			if (player_x + i < i3 + j3 && (l3 = j3 - ((player_x + i) - i3)) > k3)
				l3 = k3;
			if (l3 > k2)
			{
				l2 = i1;
				k2 = l3;
				d_int_static = j1 - i;
				if (d_int_static < 0)
					d_int_static = 0;
			}
			j1 += j2;
		}
		if (k2 < 4)
			return -1;
		int i2;
		if ((i2 = map_image_data.data[l2]) < 255)
			e = i2;
		return l2;
	}

	private void ReadNPCMenu(int id)
	{
		//*/
		UI.npc_menu_type = -1;
		UI.npc_id = id;
		UI.npc_name = NPC.NPC_names[id];  //赋值 NPC name
		if (NPCINFO.NPC_attribute[id][1] == 0)   //class_id == 百姓
		{
			if (id == 6 || id == 30)   //独行大侠 or 顾炎武 显示“请教”
			{
				UI.npc_menu_type = 6;
				return;
			}
		}
		else
		{
			if (NPCINFO.NPC_attribute[id][1] == 255)  //可交易
			{
				UI.npc_menu_type = 4;
				return;
			}
			if (id == Gmud.sPlayer.teacher_id)   //teacher id 匹配 显示 请教
			{
				UI.npc_menu_type = 6;
				return;
			}
			UI.npc_menu_type = 5;
		}
		//*/
	}	
}
