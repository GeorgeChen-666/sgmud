/**
 * Copyright (C) 2011, FMSoft.GMUD.
 * 
 * GmudMain: the main thread.
 * 
 * @author nxliao
 */
package cn.fmsoft.lnx.gmud.simple.core;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.widget.EditText;

public class GmudMain extends Thread {

	static final int STATE_INVALID = 0x00;
	static final int STATE_UNINITALIZE = 0x01;
	static final int STATE_WAIT_UI = 0x02;
	static final int STATE_WAIT_NEW_NAME = 0x08;
	static final int STATE_RUNNING = 0x10;

	static int sStaus = STATE_INVALID;

	private Context mContext;

	static private GmudMain sInstance;
	
	static private String sInputString;

	public GmudMain(Context context) {
		super("GmudMain-thread");
		mContext = context;
		sInstance = this;
		sStaus = STATE_INVALID;
	}

	@Override
	public void run() {
		sStaus = STATE_UNINITALIZE;
		Gmud.prepare();

		// 等待UI方面准备好
		synchronized (sInstance) {
			sStaus = STATE_WAIT_UI;
			while (!Video.HasBound()) {
				try {
					sInstance.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		// 启动游戏主线程
		if (Video.HasBound()) {
			sStaus = STATE_RUNNING;

			Input.InitInput();
			Input.ProcessMsg();
			Video.VideoUpdate();

			gamemain();
		}
		
		sStaus = STATE_INVALID;
	}

	static void Resume() {
		if (sInstance != null) {
			synchronized (sInstance) {
				try {
					sInstance.notifyAll();
				} catch (IllegalMonitorStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	synchronized void SetName(String name) {
		if ((sStaus & STATE_WAIT_NEW_NAME) != 0) {
			sInputString = name;
			sStaus &= ~(STATE_WAIT_NEW_NAME);
			Resume();
		}
	}

	void EnterNewName(int type) {
		Context context = Gmud.sActivity;
		AlertDialog.Builder alert = new AlertDialog.Builder(context);

		alert.setTitle("Gmud");
		alert.setMessage("Please Enter new NAME: ");

		// Set an EditText view to get user input
		final EditText input = new EditText(context);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String name = input.getText().toString();
				SetName(name);
			}
		});
		alert.show();
	}
	
	static String WaitForNewName(final int type) {
		sStaus |= STATE_WAIT_NEW_NAME;

		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				sInstance.EnterNewName(type);
			}
		});

		synchronized (sInstance) {
			while ((sStaus & STATE_WAIT_NEW_NAME) != 0) {
				try {
					sInstance.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return sInputString;
	}

	/**
	 * 重新开始游戏，创建一个新的人物 
	 */
	static synchronized void NewGame() {

		if ((sStaus & STATE_RUNNING) == 0) {
			return;
		}

		Player player = Player.getInstance();

		NewGame ng = new NewGame();
		ng.ShowStory();
		int id = ng.SelectChar();
		player.sex = (id > 1) ? 1 : 0;
		player.image_id = id;

		// ng.EnterName(hwnd);
		player.player_name = WaitForNewName(0);
		
		ng.AllocPoint(player);
	}
	
	static synchronized void Restart() {
		final Map map = Map.getInstance();
		final Player player = Player.getInstance();
		player.reset();
		
		Input.ClearKeyStatus();
		
		GmudMain.NewGame();

		// initialize & load
		task.reset();
		NPC.InitData();
		Battle.sBattle = null;
		map.LoadMap(0);
		map.stack_pointer = 0;
		map.LoadPlayer(player.image_id);

		// enter map
		Video.VideoClear();
		map.SetPlayerLocation(0, 4);
		map.DrawMap(0);
		Video.VideoUpdate();
	}

	void SetWeapon(Player player) {
		int i1 = 0;
		int j1 = 0;
		boolean flag = false;
		int attack = player.lasting_tasks[7];
		String s1 = GmudData.weapon_first_name[attack - 1]; // first word
		if (player.lasting_tasks[8] / 256 > 0) { 
			// 自身属性: 天渊 → 合灵
			i1 = player.lasting_tasks[8] / 256 - 1;
			j1 = player.lasting_tasks[8] & 255;
			s1 += GmudData.weapon_last_name[i1 * 4 + j1];
			flag = true;
		} else if (player.lasting_tasks[8] >= 24) {
			// 未定属性：绝世 → 蟠桃
			int l1 = player.lasting_tasks[8];
			s1 += GmudData.weapon_last_name[l1];
		}
		int i2 = (player.lasting_tasks[7] * player.GetSavvy()) / 10;
		Items.item_names[77] = s1.toString(); // set Desc word
		Items.item_attribs[77][0] = 2; // set item type
		Items.item_attribs[77][1] = player.lasting_tasks[5]; // set weapon type
		Items.item_attribs[77][2] = i2; // set attack
		if (flag)
			if (i1 == 1)
				Items.item_attribs[77][3] = 20 - j1 * 5; // +命中
			else if (i1 == 0)
				Items.item_attribs[77][4] = 20 - j1 * 5; // +回避
		if (player.lasting_tasks[8] >= 24 && !flag) // +附属属性  该属性暂时无用 
			Items.item_attribs[77][5] = player.lasting_tasks[8] - 10;
		else
			Items.item_attribs[77][5] = 20 - j1 * 5;
		if (player.ExistItem(77, 1) < 0) // exist?
			player.GainOneItem(77); // add one
	}

	void gamemain() {
		final Map map = Map.getInstance();
		final Player player = Player.getInstance();

		if (!Gmud.LoadSave()) {
			NewGame();
		}

		if (1 == player.lasting_tasks[4]) // new 自制武器
		{
			// enter name
			player.weapon_name = WaitForNewName(1);

			// 属性
			int k1 = 0;
			if (util.RandomInt(100) < 5 + player.bliss) // rand < q.A +5
				k1 = (util.RandomInt(6) + 1) * 256 + util.RandomInt(4);
			else if (util.RandomInt(100) < player.bliss) // rand < q.A
				k1 = 24 + util.RandomInt(13);
			
			player.lasting_tasks[4] = 0;
			player.lasting_tasks[7] = util.RandomInt(52) + 1; // attack
			player.lasting_tasks[8] = k1;
			player.lasting_tasks[9] = 1;
			// WriteSave(); // savedata
			Input.ClearKeyStatus();
			Video.VideoClear();
			Video.VideoDrawString("您的武器铸造成功！", 20, 35);
			Video.VideoUpdate();
			while (Input.inputstatus == 0)
				Gmud.GmudDelay(100);
		}
		if (player.lasting_tasks[9] == 1)
			SetWeapon(player); // 自制武器调整
		if (player.lasting_tasks[1] > 0 && player.lasting_tasks[1] < 8)
			player.GainOneItem(79 + player.lasting_tasks[1]); // +坛地图
		// initialize & load
		task.reset();
		NPC.InitData();
		Battle.sBattle = null;
		map.LoadMap(0);
		map.stack_pointer = 0;
		map.LoadPlayer(player.image_id);

		// enter map
		Video.VideoClear();
		map.SetPlayerLocation(0, 4);
		map.DrawMap(0);
		Video.VideoUpdate();
		
		// GmudTemp.timer_thread_handle = CreateThread(0, 0, StartTimer, 0, 0, 0);

		Input.ClearKeyStatus();

		// 开启自动回血线程
		new Thread("Timer") {
			@Override
			public void run() {
				while (Input.Running) {
					GmudTemp.TimerFunc();
					try {
						sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();

		while (Input.Running) {
			Input.ProcessMsg();
			if ((Input.inputstatus & Input.kKeyUp) != 0) // press up
			{
				Input.ClearKeyStatus();
				if (map.player_dir == 0) {
					map.DirUp();
				} else {
					map.SetPlayerLocation(-1, 0);
					map.DrawMap(-1);
				}
				Video.VideoUpdate();
				Gmud.GmudDelay(100);
			} else if ((Input.inputstatus & Input.kKeyDown) != 0) // press down
			{
				Input.ClearKeyStatus();
				if (map.player_dir == 1) {
					map.DirDown();
				} else {
					map.SetPlayerLocation(-1, 1);
					map.DrawMap(-1);
				}
				Video.VideoUpdate();
				Gmud.GmudDelay(100);
			} else if ((Input.inputstatus & Input.kKeyPgUp) != 0) // left++
			{
				Input.ClearKeyStatus();
				while ((Input.inputstatus & Input.kKeyPgUp) != 0
						|| Input.inputstatus == 0) {
					Input.ProcessMsg();
					map.DirLeft(4);
					Video.VideoUpdate();
					Gmud.GmudDelay(60);
				}
				Video.VideoUpdate();
				Gmud.GmudDelay(50);
			} else if ((Input.inputstatus & Input.kKeyPgDn) != 0) // right++
			{
				Input.ClearKeyStatus();
				while ((Input.inputstatus & Input.kKeyPgDn) != 0
						|| Input.inputstatus == 0) {
					Input.ProcessMsg();
					map.DirRight(4);
					Video.VideoUpdate();
					Gmud.GmudDelay(60);
				}
				Video.VideoUpdate();
				Gmud.GmudDelay(50);
			} else if ((Input.inputstatus & Input.kKeyLeft) != 0) // press left
			{
				Input.ClearKeyStatus();
				map.DirLeft(4);
				Video.VideoUpdate();
				Gmud.GmudDelay(130);
			} else if ((Input.inputstatus & Input.kKeyRight) != 0) // press
																	// right
			{
				Input.ClearKeyStatus();
				map.DirRight(4);
				Video.VideoUpdate();
				Gmud.GmudDelay(130);
			} else if ((Input.inputstatus & Input.kKeyEnt) != 0) // press enter
			{
				Input.ClearKeyStatus();
				map.KeyEnter();
				map.DrawMap(-1);
				Video.VideoUpdate();
			} else if ((Input.inputstatus & Input.kKeyExit) != 0) // press Esc
			{
				Input.ClearKeyStatus();
				UI.MainMenu(); // main menu
			} else if ((Input.inputstatus & Input.kKeyFly) != 0) // press fly
			{
				Input.ClearKeyStatus();
				UI.Fly();
				map.DrawMap(-1);
				Video.VideoUpdate();
			}
			Gmud.GmudDelay(60);
		}
		// CloseHandle(GmudTemp.timer_thread_handle);
		// if(glpBattle)
		// delete glpBattle;
		// delete glPlayer;
		// delete sMap;
	}
}