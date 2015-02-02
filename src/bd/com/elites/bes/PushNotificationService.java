package bd.com.elites.bes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import bd.com.elites.bes.utils.AsyncTaskHandler;
import bd.com.elites.bes.utils.Constants;
import bd.com.elites.bes.utils.FetcherListener;
import bd.com.elites.bes.utils.UtilityFunctions;

public class PushNotificationService extends Service implements FetcherListener {
	private static final int DELAY_MILLIS = 1000 * 60 * 5;// 5 min
	private static final String MESSAGE_POLICE = "message_police";
	private static final String MESSAGE_FIRE_SERVICE = "message_fire_service";
	Handler handler;
	Runnable runable;
	boolean isFetcherRunning = false;
	int fallBack = 0;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		getPushNotification();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY; // Run until explicitly stopped.
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("MyService", "Service Stopped.");
		if (handler != null) {
			handler.removeCallbacks(runable);
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	private void getPushNotification() {

		final String url = getResources().getString(
				R.string.check_push_notification_url);
		handler = new Handler();
		runable = new Runnable() {
			@Override
			public void run() {
				try {
					if (UtilityFunctions
							.isNetworkAvailable(PushNotificationService.this)) {
						if (isFetcherRunning && fallBack < 10) {
							fallBack++;
						} else {
							AsyncTaskHandler asyncTask = new AsyncTaskHandler(
									PushNotificationService.this,
									Constants.ASYNCTASK_ACTIONS.GET_PUSH_NOTIFICATION);
							Log.d("tonmoy", "url : " + url);
							asyncTask.execute(new String[] { url, "" });
							isFetcherRunning = true;
							fallBack = 0;
						}

					}
				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					handler.postDelayed(this, DELAY_MILLIS);
				}
			}

		};

		handler.postDelayed(runable, DELAY_MILLIS);

	}

	@Override
	public void onStartFetcher() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFinishFetcher(Object responseStr) {
		String res = (responseStr) == null ? "null" : (String) responseStr;
		Log.d("tonmoy", res);
		isFetcherRunning = false;
		// Toast.makeText(this, res, 1).show();
		try {
			JSONArray jArray = new JSONArray(res);
			for (int i = 0; i < jArray.length(); i++) {
				JSONArray msg1Array = jArray.getJSONArray(i);
				for (int j = 0; j < msg1Array.length(); j++) {
					JSONObject msgObj = msg1Array.getJSONObject(j);
					String from = msgObj.getString("from");
					if (storeMessage(from, msgObj.toString())) {
						String id = msgObj.optString("id");
						String message = msgObj.optString("message");
						String time = msgObj.optString("time");
						sendNotification(System.currentTimeMillis(), message,
								from);
					}

				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void sendNotification(long when, String msg, String from) {
		NotificationManager mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent i = new Intent(this, NotificationDetailsViewActivity.class);
		i.setData(Uri.parse("content://" + when));
		Log.d("GCM_PUSH_NOTIFICATION", "message is : " + msg);
		i.putExtra("msg", msg);
		i.putExtra("from", from);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, i, 0);
		Uri uri = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Emergency Service Notification")
				.setWhen(when)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg).setSound(uri).setAutoCancel(true)
				.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify((int) when, mBuilder.build());
	}

	@Override
	public void onErrorFetcher(Exception e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNoInternetConnection() {
		// TODO Auto-generated method stub

	}

	private boolean storeMessage(String from, String msg) {
		boolean flag = false;
		SharedPreferences prefs = getSharedPreferences(
				"bd.com.elites.bes.messages", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		if (from.equals("1"))// police
		{
			if (!prefs.getString(MESSAGE_POLICE, "n/a").equals(msg)) { // not
																		// found
				editor.putString(MESSAGE_POLICE, msg);
				flag = true;
			}

		} else if (from.equals("2")) {
			if (!prefs.getString(MESSAGE_FIRE_SERVICE, "n/a").equals(msg)) { // not
																				// found
				editor.putString(MESSAGE_FIRE_SERVICE, msg);
				flag = true;
			}
		}
		editor.commit();
		return flag;
	}

}
