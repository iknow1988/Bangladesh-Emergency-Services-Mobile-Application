package bd.com.elites.bes.utils;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import bd.com.elites.bes.model.District;
import bd.com.elites.bes.model.PoliceThana;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.Gson;

public class UtilityFunctions {

	public UtilityFunctions(Context context) {

	}

	public static Intent getFacebookPageIntent(Context context, String pageId,
			String pageUserName) {

		Intent fbIntent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("fb://profile/" + pageId));

		if (context.getPackageManager().queryIntentActivities(fbIntent, 0)
				.size() > 0) {
			return fbIntent;
		} else {
			return new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://www.facebook.com/" + pageUserName));

		}
	}

	public static Intent getPlayStoreIntentForDeveloper(Context context,
			String developerId) {

		Intent playStoreIntent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("market://search?q=pub:" + developerId));
		if (context.getPackageManager()
				.queryIntentActivities(playStoreIntent, 0).size() > 0) {
			return playStoreIntent;
		} else {
			return new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://play.google.com/store/apps/developer?id="
							+ developerId));

		}

	}

	public static Intent getPlayStoreIntentForThisApp(Context context) {

		Intent playStoreIntent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("market://details?id=" + context.getPackageName()));
		if (context.getPackageManager()
				.queryIntentActivities(playStoreIntent, 0).size() > 0) {
			return playStoreIntent;
		} else {
			return new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://play.google.com/store/apps/details?id="
							+ context.getPackageName()));

		}

	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public static int getLocationServiceStatus(Context context) {
		LocationManager lManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		if (lManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			return 1;
		else if (lManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
			return 2;
		else
			return 0;
	}

	public static void goToElitesFacebook(Context context) {

		Intent facebookIntent = getFacebookPageIntent(context,
				"115662305228449", "elitesbd");
		context.startActivity(facebookIntent);
	}

	public static void goToElitesPlaystore(Context context) {

		Intent playStoreIntent = getPlayStoreIntentForDeveloper(context,
				"eLITEs");
		context.startActivity(playStoreIntent);
	}

	public static void goToPlaystoreLinkOfThisApp(Context context) {

		Intent playStoreIntent = getPlayStoreIntentForThisApp(context);
		context.startActivity(playStoreIntent);
	}

	public static void callElites(Context context) {
		Intent call = new Intent(Intent.ACTION_CALL,
				Uri.parse("tel: +8801917060123"));
		context.startActivity(call);
	}

	// public static void goToAapFacebook(Context context) {
	//
	// Intent facebookIntent = getFacebookPageIntent(context,
	// "123366347812540", "airportarmedpolice");
	// context.startActivity(facebookIntent);
	// }

	public static void showLongToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	public static void showShortToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static boolean checkPlayServices(Context ct) {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(ct);
		if (resultCode != ConnectionResult.SUCCESS) {
			return false;
		}
		return true;
	}

	public static String getRegistrationId(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(
				"bd.com.elites.bes", Context.MODE_PRIVATE);
		String registrationId = prefs.getString(
				Constants.SHARED_PREFERENCE.PROPERTY_REG_ID, "");
		if (registrationId.length() == 0) {
			Log.i("tonmoy", "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(
				Constants.SHARED_PREFERENCE.PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i("tonmoy", "App version changed.");
			return "";
		}
		Log.i("tonmoy", registrationId);
		return registrationId;
	}

	public static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	public static int getLanguageFromSharedPreference(Context context) {
		SharedPreferences pref = context.getSharedPreferences(
				Constants.SHARED_PREFERENCE.SHARED_PREFERENCE_NAME,
				Context.MODE_PRIVATE);

		return pref.getInt(Constants.SHARED_PREFERENCE.LANGUAGE_PREFERENCE,
				Constants.LANGUAGE.ENGLISH);
	}

	public static District getPresentDistrictFromSharedPreference(Context context) {
		District district = null;
		SharedPreferences pref = context.getSharedPreferences(
				Constants.SHARED_PREFERENCE.SHARED_PREFERENCE_NAME,
				Context.MODE_PRIVATE);

		String present_dis_json = pref.getString(
				Constants.SHARED_PREFERENCE.PRESENT_DISTRICT, "");

		try {
			Gson gson = new Gson();
			district = gson.fromJson(present_dis_json, District.class);
		} catch (Exception e) {

		}
		return district;
	}
	
	public static PoliceThana getPresentThanaFromSharedPreference(Context context) {
		PoliceThana thana = null;
		SharedPreferences pref = context.getSharedPreferences(
				Constants.SHARED_PREFERENCE.SHARED_PREFERENCE_NAME,
				Context.MODE_PRIVATE);

		String present_thana_json = pref.getString(
				Constants.SHARED_PREFERENCE.PRESENT_THANA, "");

		try {
			Gson gson = new Gson();
			thana = gson.fromJson(present_thana_json, PoliceThana.class);
		} catch (Exception e) {

		}
		return thana;
	}
	public static String getResponseString(HttpResponse response) {
		int charRead;
		int BUFFER_SIZE = 4000;
		char[] inputBuffer = new char[BUFFER_SIZE];

		String returnString = "";

		try {
			// Log.d("responseString in Apicaller.getResponseString()",
			// response.toString());
			HttpEntity responseEntity = response.getEntity();
			InputStream responseStream = responseEntity.getContent();

			InputStreamReader isr = new InputStreamReader(responseStream);
			while ((charRead = isr.read(inputBuffer)) > 0) {
				// ---convert the chars to a String---
				String readString = String
						.copyValueOf(inputBuffer, 0, charRead);
				returnString += readString;
				inputBuffer = new char[BUFFER_SIZE];
			}
		} catch (Exception e) {
			Log.d("error in Apicaller.getResponseString()", e.toString());
			// DebugLog.log(e.toString());
			return null;
		}
		return returnString;
	}
}
