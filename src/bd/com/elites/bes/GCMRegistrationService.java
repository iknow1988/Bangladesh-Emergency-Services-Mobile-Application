package bd.com.elites.bes;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import bd.com.elites.bes.utils.AsyncTaskHandler;
import bd.com.elites.bes.utils.Constants;
import bd.com.elites.bes.utils.UtilityFunctions;

public class GCMRegistrationService extends Service {

	String SENDER_ID = "863577510613";
	String regid;
	String regId;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// DEVICE_ID = Utility.imei(this);
		GCMSetUp();
		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private void GCMSetUp() {
		// GCM set up
		if (UtilityFunctions.checkPlayServices(this)) {
			regid = UtilityFunctions.getRegistrationId(this);
			Log.d("tonmoy", regid);

			if (regid.length() == 0) {
				registerInBackground();
			}
		} else {
			Log.i("tonmoy", "No valid Google Play Services APK found.");
		}
	}

	private void registerInBackground() {
		AsyncTaskHandler asyncTask = new AsyncTaskHandler(
				GCMRegistrationService.this,
				Constants.ASYNCTASK_ACTIONS.GET_GCM_REG_ID);
		asyncTask.execute(new String[] { SENDER_ID });
	}

	public void onGCMIDFound(String responseStr) {
		regId = responseStr;
		if (responseStr != null && responseStr.length() > 0) {
			sendRegistrationIdToBackend(responseStr);
			// AppUtilities.storeRegistrationId(this, regid);
			// stopSelf();
		}

	}

	public void onGCMIDPosted(String resultCode) {
		if (resultCode.equals("200") || resultCode.equals("201")) {
			storeRegistrationId();
		} else {
			Log.d("tonmoy", "invalid response");
		}
		stopSelf();

	}

	private void sendRegistrationIdToBackend(String gcmID) {
		String url = getResources().getString(
				R.string.check_push_notification_url);
		int appVersion = UtilityFunctions.getAppVersion(this);
		int databaseVersion = Constants.DB_INFO.DATABASE_VERSION;
		AsyncTaskHandler asyncTask = new AsyncTaskHandler(
				GCMRegistrationService.this,
				Constants.ASYNCTASK_ACTIONS.SEND_GCM_REG_ID);
		Log.d("tonmoy", gcmID);
		asyncTask.execute(new String[] { url, gcmID, appVersion + "",
				databaseVersion + "" });
	}

	private void storeRegistrationId() {
		SharedPreferences prefs = getSharedPreferences("bd.com.elites.bes",
				Context.MODE_PRIVATE);
		int appVersion = UtilityFunctions.getAppVersion(this);
		Log.i("tonmoy", "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(Constants.SHARED_PREFERENCE.PROPERTY_REG_ID, regId);
		editor.putInt(Constants.SHARED_PREFERENCE.PROPERTY_APP_VERSION,
				appVersion);
		editor.commit();
	}

}
