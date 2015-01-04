package bd.com.elites.bes;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import bd.com.elites.bes.model.District;
import bd.com.elites.bes.model.Division;
import bd.com.elites.bes.utils.AsyncTaskHandler;
import bd.com.elites.bes.utils.BaseActivity;
import bd.com.elites.bes.utils.Constants;
import bd.com.elites.bes.utils.Globals;
import bd.com.elites.bes.utils.TransparentProgressDialog;

public class SplashActivity extends BaseActivity {

	TransparentProgressDialog dialog = null;
	boolean isAsynctaskFinished = false, isAnimationFinished = false,
			isPreferenceSet = false;

	LinearLayout policeText, fireText, hospitalText, organizationTextContainer;

	RelativeLayout a2iText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		startService(new Intent(this, GCMRegistrationService.class));
		
		init();
	}

	
	private void init() {

		dialog = new TransparentProgressDialog(SplashActivity.this);

		AsyncTaskHandler asyncTask = new AsyncTaskHandler(SplashActivity.this,
				Constants.ASYNCTASK_ACTIONS.GET_DISTRICT_LIST);
		asyncTask.execute();

		a2iText = (RelativeLayout) findViewById(R.id.a2iText);
		a2iText.setVisibility(View.GONE);

		organizationTextContainer = (LinearLayout) findViewById(R.id.organizationTextContainer);
		policeText = (LinearLayout) findViewById(R.id.policeText);
		policeText.setVisibility(View.INVISIBLE);
		fireText = (LinearLayout) findViewById(R.id.fireText);
		fireText.setVisibility(View.INVISIBLE);
		hospitalText = (LinearLayout) findViewById(R.id.hospitalText);
		hospitalText.setVisibility(View.INVISIBLE);

		startAnimations();
		isPreferenceSet = checkSharedPreferences();

	}

	private boolean checkSharedPreferences() {
		SharedPreferences pref = getSharedPreferences(
				Constants.SHARED_PREFERENCE.SHARED_PREFERENCE_NAME,
				Context.MODE_PRIVATE);

		/*
		 * Globals.PREFERRED_LANGUAGE = pref.getInt(
		 * Constants.SHARED_PREFERENCE.LANGUAGE_PREFERENCE,
		 * Constants.LANGUAGE.ENGLISH); Globals.PRESENT_DISTRICT_ID =
		 * pref.getInt( Constants.SHARED_PREFERENCE.PRESENT_DISTRICT_ID, -1);
		 * Globals.PRESENT_THANA_ID = pref.getInt(
		 * Constants.SHARED_PREFERENCE.PRESENT_THANA_ID, -1);
		 * 
		 * Globals.PRESENT_DISTRICT_NAME = pref.getString(
		 * Constants.SHARED_PREFERENCE.PRESENT_DISTRICT_NAME, "");
		 * Globals.PRESENT_THANA_NAME = pref.getString(
		 * Constants.SHARED_PREFERENCE.PRESENT_THANA_NAME, "");
		 * 
		 * Globals.PRESENT_THANA_QUICK_NUMBER = pref.getString(
		 * Constants.SHARED_PREFERENCE.PRESENT_THANA_NAME, "");
		 */

		if (pref.getString(Constants.SHARED_PREFERENCE.PRESENT_DISTRICT,
						null) == null
				|| pref.getString(Constants.SHARED_PREFERENCE.PRESENT_THANA,
						null) == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
	}

	public void onPreExecuteAsyncTask() {
		// dialog.show();
	}

	public void onPostExecuteAsyncTask(ArrayList<District> districts) {

		// Globals.DISTRICT_LIST.clear();
		// Globals.DISTRICT_LIST.addAll(districts);

		Globals.DIVISION_LIST.clear();
		for (District district : districts) {
			if (Globals.DIVISION_LIST.size() == 0
					|| !Globals.DIVISION_LIST
							.get(Globals.DIVISION_LIST.size() - 1).name
							.equals(district.division)) {
				Globals.DIVISION_LIST.add(new Division(district.division,
						district.division_bn));
				Globals.DIVISION_LIST.get(Globals.DIVISION_LIST.size() - 1)
						.addDistrict(district);
			} else {
				Globals.DIVISION_LIST.get(Globals.DIVISION_LIST.size() - 1)
						.addDistrict(district);
			}
		}

		isAsynctaskFinished = true;
		moveToHome();
	}

	private void startAnimations() {

		Animation slideInFromLeftPolice = AnimationUtils.loadAnimation(
				SplashActivity.this, R.anim.slide_in_from_left);
		slideInFromLeftPolice.setDuration(1000);
		slideInFromLeftPolice.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				Animation slideInFromLeftFire = AnimationUtils.loadAnimation(
						SplashActivity.this, R.anim.slide_in_from_left);
				slideInFromLeftFire.setDuration(1000);
				slideInFromLeftFire
						.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation animation) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onAnimationRepeat(Animation animation) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onAnimationEnd(Animation animation) {
								Animation slideInFromLeftHos = AnimationUtils
										.loadAnimation(SplashActivity.this,
												R.anim.slide_in_from_left);
								slideInFromLeftHos.setDuration(1000);
								slideInFromLeftHos
										.setAnimationListener(new AnimationListener() {

											@Override
											public void onAnimationStart(
													Animation animation) {
												// TODO Auto-generated method
												// stub

											}

											@Override
											public void onAnimationRepeat(
													Animation animation) {
												// TODO Auto-generated method
												// stub

											}

											@Override
											public void onAnimationEnd(
													Animation animation) {
												showA2iTextAnimation();

											}
										});

								hospitalText.setVisibility(View.VISIBLE);
								hospitalText.startAnimation(slideInFromLeftHos);

							}
						});
				fireText.setVisibility(View.VISIBLE);
				fireText.startAnimation(slideInFromLeftFire);
			}
		});

		policeText.setVisibility(View.VISIBLE);
		policeText.startAnimation(slideInFromLeftPolice);

	}

	private void moveToHome() {

		if (isAnimationFinished && !isAsynctaskFinished) {
			dialog.show();
		} else if (!isAnimationFinished && isAsynctaskFinished) {

		} else if (isAsynctaskFinished && isAnimationFinished) {

			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}

			if (isPreferenceSet) {
				Intent home_activity = new Intent(SplashActivity.this,
						HomeActivity.class);
				startActivity(home_activity);
				SplashActivity.this.finish();
			} else {

				Toast.makeText(SplashActivity.this,
						R.string.please_set_your_preference_, Toast.LENGTH_LONG)
						.show();

				Intent pref_activity = new Intent(SplashActivity.this,
						SettingsActivity.class);
				startActivity(pref_activity);
				SplashActivity.this.finish();
			}

		}
	}

	private void showA2iTextAnimation() {
		Animation slideOutToRight = AnimationUtils.loadAnimation(
				SplashActivity.this, R.anim.slide_out_to_right);
		slideOutToRight.setDuration(500);
		slideOutToRight.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {

				organizationTextContainer.setVisibility(View.GONE);

				Animation fadeIn = AnimationUtils.loadAnimation(
						SplashActivity.this, R.anim.fade_in);
				fadeIn.setDuration(1500);
				fadeIn.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						isAnimationFinished = true;
						moveToHome();

					}
				});
				a2iText.setVisibility(View.VISIBLE);
				a2iText.startAnimation(fadeIn);

			}
		});
		organizationTextContainer.startAnimation(slideOutToRight);

	}

}
