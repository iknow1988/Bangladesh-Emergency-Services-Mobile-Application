package bd.com.elites.bes.utils;

import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

public class BaseActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		removeTitleBar();

		// set language
		SharedPreferences pref = getSharedPreferences(
				Constants.SHARED_PREFERENCE.SHARED_PREFERENCE_NAME,
				Context.MODE_PRIVATE);

		int language_preference = pref.getInt(
				Constants.SHARED_PREFERENCE.LANGUAGE_PREFERENCE,
				Constants.LANGUAGE.ENGLISH);

		if (language_preference == Constants.LANGUAGE.BANGLA) {

			String languageToLoad = "bn";
			Locale locale = new Locale(languageToLoad);
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			getBaseContext().getResources().updateConfiguration(config,
					getBaseContext().getResources().getDisplayMetrics());
		} else{

			String languageToLoad = "en";
			Locale locale = new Locale(languageToLoad);
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			getBaseContext().getResources().updateConfiguration(config,
					getBaseContext().getResources().getDisplayMetrics());
		}

	}

	/*
	 * Removes the TitleBar And Create more room for the app
	 */
	protected void removeTitleBar() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

}