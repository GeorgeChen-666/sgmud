package cn.fmsoft.lnx.gmud.simple;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.coolnx.lib.XmlUtils;
import org.xmlpull.v1.XmlPullParserException;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.widget.Toast;
import cn.fmsoft.lnx.gmud.simple.core.Gmud;

public class SettingActivity extends PreferenceActivity {
	final static boolean DEBUG = true;
	final static String DBG_TAG = SettingActivity.class.getName();

	public final static String BACK_UP_DIR = "xtulnx/gmud/backup/";
	public final static String BACK_UP_FILE = "default.xml";

	private File mBackupDir;
	private File mBackupFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// PreferenceManager.setDefaultValues(this, R.xml.pre_settings, false);
		addPreferencesFromResource(R.xml.pre_settings);

		init();
		setupViews();
	}

	private void init() {
		mBackupDir = new File(Environment.getExternalStorageDirectory(),
				BACK_UP_DIR);
		mBackupFile = new File(mBackupDir, BACK_UP_FILE);
	}

	private void setupViews() {
		boolean bExist = mBackupFile.isFile();

		PreferenceScreen ps = getPreferenceScreen();

		Preference backup = ps.findPreference(getString(R.string.key_backup));
		if (bExist) {
			long time = mBackupFile.lastModified();
			Date modify = new Date(time);
			String s = getString(R.string.summary_backup_last);
			String summary = String.format(s, modify.toLocaleString());
			backup.setSummary(summary);
		}
		backup.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				tryBackup();
				return false;
			}
		});

		Preference restore = ps.findPreference(getString(R.string.key_restore));
		if (bExist) {
			restore.setEnabled(true);
			String format = getString(R.string.summary_resotre_last);
			String summary = String.format(format, mBackupFile.getPath());
			restore.setSummary(summary);
			restore.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					tryRestore();
					return false;
				}
			});
		}
	}

	/** 判断是否有存档 */
	private int hasSave() {
		String key = getSharedPreferences(Gmud.SAVE_PATH, Context.MODE_PRIVATE)
				.getString("key", null);
		if (key == null || key == "") {
			return -1;
		}
		return 0;
	}

	private void tryBackup() {
		if (hasSave() == -1) {
			new AlertDialog.Builder(this).setIcon(android.R.drawable.btn_star)
					.setTitle(R.string.title)
					.setMessage(R.string.tip_need_save).create().show();
			return;
		}

		File dir = mBackupDir;
		if (!dir.exists() || !dir.isDirectory()) {
			dir.delete();
			dir.mkdirs();
		}

		if (mBackupFile.exists()) {
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.btn_star_big_on)
					.setTitle(R.string.title)
					.setMessage(R.string.tip_backup_overwrite)
					.setPositiveButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									backup();
								}
							})
					.setNeutralButton(android.R.string.cancel,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).create().show();
		} else {
			backup();
		}
	}

	private void backup() {
		SharedPreferences preferences;
		preferences = getSharedPreferences(Gmud.SAVE_PATH, Context.MODE_PRIVATE);
		Map<String, ?> all = preferences.getAll();
		if (all != null) {
			try {
				OutputStream os = new FileOutputStream(mBackupFile);
				XmlUtils.writeMapXml(all, os);
				os.close();
				Toast.makeText(getBaseContext(), R.string.tip_backup_success,
						Toast.LENGTH_SHORT).show();
				setupViews();
				return;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Toast.makeText(getBaseContext(), R.string.tip_backup_failed,
				Toast.LENGTH_SHORT).show();
	}

	private void tryRestore() {
		if (hasSave() != -1) {
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.btn_star_big_on)
					.setTitle(R.string.title)
					.setMessage(R.string.tip_restore_overwrite)
					.setPositiveButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									restore();
								}
							})
					.setNeutralButton(android.R.string.cancel,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).create().show();
		} else {
			restore();
		}

		SharedPreferences preferences;
		preferences = getSharedPreferences(Gmud.SAVE_PATH, Context.MODE_PRIVATE);

		Editor editor = preferences.edit();

		InputStream is;
		try {
			is = new FileInputStream(mBackupFile);
			HashMap map = XmlUtils.readMapXml(is);
			is.close();
			Set s = map.entrySet();
			Iterator i = s.iterator();
			while (i.hasNext()) {
				Map.Entry e = (Map.Entry) i.next();
				String k = (String) e.getKey();
				Object v = e.getValue();
				if (v == null) {
					editor.putString(k, null);
				} else if (v instanceof Boolean) {
					editor.putBoolean(k, (Boolean) v);
				} else if (v instanceof Float) {
					editor.putFloat(k, (Float) v);
				} else if (v instanceof Integer) {
					editor.putInt(k, (Integer) v);
				} else if (v instanceof Long) {
					editor.putLong(k, (Long) v);
				} else if (v instanceof String) {
					editor.putString(k, (String) v);
				}
			}
			editor.commit();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void restore() {

		SharedPreferences preferences;
		preferences = getSharedPreferences(Gmud.SAVE_PATH, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		InputStream is;
		try {
			is = new FileInputStream(mBackupFile);
			HashMap map = XmlUtils.readMapXml(is);
			is.close();
			Set s = map.entrySet();
			Iterator i = s.iterator();
			while (i.hasNext()) {
				Map.Entry e = (Map.Entry) i.next();
				String k = (String) e.getKey();
				Object v = e.getValue();
				if (v == null) {
					editor.putString(k, null);
				} else if (v instanceof Boolean) {
					editor.putBoolean(k, (Boolean) v);
				} else if (v instanceof Float) {
					editor.putFloat(k, (Float) v);
				} else if (v instanceof Integer) {
					editor.putInt(k, (Integer) v);
				} else if (v instanceof Long) {
					editor.putLong(k, (Long) v);
				} else if (v instanceof String) {
					editor.putString(k, (String) v);
				}
			}

			Toast.makeText(getBaseContext(), R.string.tip_restore_failed,
					Toast.LENGTH_SHORT).show();

			editor.commit();

			finish();

			AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 200,
					GmudActivity.getPendingIntent(getBaseContext()));

			Gmud.exit();
			return;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Toast.makeText(getBaseContext(), R.string.tip_restore_failed,
				Toast.LENGTH_SHORT).show();
	}
}