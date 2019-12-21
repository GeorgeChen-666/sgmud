/**
 * Copyright (C) 2011, FMSoft.GMUD.
 * 
 * @author nxliao
 */
package cn.fmsoft.lnx.gmud.simple;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import cn.fmsoft.lnx.gmud.simple.core.Gmud;
import cn.fmsoft.lnx.gmud.simple.core.Input;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.NotificationType;
import com.umeng.fb.UMFeedbackService;

public class GmudActivity extends Activity {
	// final static int MENU_HOOKGAME = 0;
	// final static int MENU_EXITAPPLICATION = 1;
	// final static int MENU_ABOUT = 2;

	private int mRequestOritation = Configuration.ORIENTATION_SQUARE;
	private boolean bLockScreen = false;
	private boolean bHideSoftKey = false;

	private static PendingIntent sMainIntent;

	protected static PendingIntent getPendingIntent(Context ctx) {
		if (sMainIntent != null)
			return sMainIntent;

		Intent intent = new Intent(ctx, GmudActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return PendingIntent.getActivity(ctx, 0, intent, 0);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		configUpdate();
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.main);

		sMainIntent = PendingIntent.getActivity(getBaseContext(), 0,
				new Intent(getIntent()), getIntent().getFlags());

		// new Gmud(this);
		Gmud.bind(this);

		mRequestOritation = getRequestedOrientation();
		Log.i("lnx", "Orientation = " + mRequestOritation);
	}

	/**
	 * 配置自动更新和错误统计功能。
	 */
	private void configUpdate() {
		UMFeedbackService.enableNewReplyNotification(this,
				NotificationType.AlertDialog);
		MobclickAgent.setDebugMode(false);
		MobclickAgent.onError(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		Gmud.unbind(this);

		if (!Gmud.Running) {
			System.exit(0);
		}
		// �����������ַ�ʽ
		// android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		// Video.VideoUpdate();

		loadConfig();
	}

	private void loadConfig() {
		SharedPreferences p = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		boolean lockScreen = p.getBoolean(getString(R.string.key_lock_screen),
				bLockScreen);
		if (lockScreen != bLockScreen) {
			bLockScreen = lockScreen;
			lock_screen();
		}
		boolean hideSoftKey = p.getBoolean(
				getString(R.string.key_hide_softkey), bHideSoftKey);
		if (hideSoftKey != bHideSoftKey) {
			bHideSoftKey = hideSoftKey;
			hide_softkey();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		return true;
		//
		// menu.add(0, MENU_HOOKGAME, 0, "Hook");
		// menu.add(0, MENU_EXITAPPLICATION, 0, "Exit Game");
		// menu.add(0, MENU_ABOUT, 0, "About");
		// return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// case MENU_EXITAPPLICATION:
		case R.id.item_exit:
			Gmud.exit();
			break;

		// case R.id.item_lockscreen:
		// bLockScreen = !item.isChecked();
		// item.setChecked(bLockScreen);
		// lock_screen();
		// break;
		//
		// case R.id.item_hidekey:
		// bHideSoftKey = !item.isChecked();
		// item.setChecked(bHideSoftKey);
		// hide_softkey();
		// break;
		// case R.id.item_hook:
		// break;

		case R.id.item_setting:
			Intent intent = new Intent(getBaseContext(), SettingActivity.class);
			startActivity(intent);
			break;

		case R.id.item_about:
			Dialog dialog = new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.btn_star)
					.setTitle(R.string.title).setMessage(R.string.about)
					.setPositiveButton("OK", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					}).create();
			dialog.show();
			break;

		case R.id.item_feedback:
			UMFeedbackService.openUmengFeedbackSDK(this);
			break;

		// case R.id.item_backup:
		// backup();
		// break;
		//
		// case R.id.item_restore:
		// restore();
		// break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void hide_softkey() {
		Gmud.setMinScale(!bHideSoftKey);
		final Control control = (Control) findViewById(R.id.control);
		control.hide(bHideSoftKey);
		final View show = findViewById(R.id.show);
		show.requestLayout();
	}

	private void lock_screen() {
		// 如果还没有横竖屏的设置,按宽高比计算
		if (bLockScreen
				&& (mRequestOritation == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)) {
			final Display display = getWindowManager().getDefaultDisplay();
			if (display.getWidth() > display.getHeight()) {
				mRequestOritation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
			} else {
				mRequestOritation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
			}
		}

		setRequestedOrientation(bLockScreen ? mRequestOritation
				: ActivityInfo.SCREEN_ORIENTATION_USER);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (Input.Running) {
			if (KeyEvent.KEYCODE_ENTER == keyCode
					|| (KeyEvent.KEYCODE_0 <= keyCode && keyCode <= KeyEvent.KEYCODE_Z)) {
				Input.onKey(keyCode, event);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);

		if (!bLockScreen) {
			if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
				mRequestOritation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
			} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
				mRequestOritation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
			}
		}

		// if (this.getResources().getConfiguration().orientation ==
		// Configuration.ORIENTATION_LANDSCAPE) {
		// } else if (this.getResources().getConfiguration().orientation ==
		// Configuration.ORIENTATION_PORTRAIT) {
		// }
	}

	public final static String BACK_UP_DIR = "xtulnx/gmud/backup/";

	private void backup() {
		Context ctx = getBaseContext();
		File dir = new File(Environment.getExternalStorageDirectory(),
				BACK_UP_DIR);
		if (!dir.exists() || !dir.isDirectory()) {
			dir.delete();
			dir.mkdirs();
		}
		Log.d("lnx", "path =" + dir.getPath());
		File back = new File(dir, "backup.xml");
		if (dir.exists()) {
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.btn_star)
					.setTitle(R.string.title)
					.setMessage("Backup to " + back.getPath())
					.setPositiveButton(android.R.string.yes,
							new OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							})
					.setNeutralButton(android.R.string.cancel,
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).create().show();
		}
	}

	private void restore() {

		new AlertDialog.Builder(this).setIcon(android.R.drawable.btn_star)
				.setTitle(R.string.title).setMessage(R.string.about)
				.setPositiveButton("OK", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).create().show();
	}
}
