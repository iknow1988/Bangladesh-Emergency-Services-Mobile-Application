package bd.com.elites.bes;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import bd.com.elites.bes.adapter.DistrictListAdapter;
import bd.com.elites.bes.adapter.MainMenuAdapter;
import bd.com.elites.bes.map.MapViewActivity;
import bd.com.elites.bes.model.District;
import bd.com.elites.bes.model.Division;
import bd.com.elites.bes.model.PoliceThana;
import bd.com.elites.bes.utils.AsyncTaskHandler;
import bd.com.elites.bes.utils.BaseActivity;
import bd.com.elites.bes.utils.Constants;
import bd.com.elites.bes.utils.Globals;
import bd.com.elites.bes.utils.TransparentProgressDialog;
import bd.com.elites.bes.utils.UtilityFunctions;

public class HomeActivity extends BaseActivity implements OnClickListener {

	GridView main_menu;
	ExpandableListView district_listview;
	LinearLayout district_listview_container;
	LinearLayout district_selector;

	TransparentProgressDialog dialog = null;
	boolean isShowingDistrictList = false;

	District present_district = null;
	PoliceThana present_thana = null;
	String present_thana_name = "", present_dis_name = "";
	int language_preference, present_dis_id = -1, present_thana_id = -1;
	String police_quick_call_number = "";
	String fire_control_room = "029555555";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		overridePendingTransition(R.anim.slide_in_from_left,
				R.anim.slide_in_from_left);
		setContentView(R.layout.activity_home);
		startService(new Intent(this, PushNotificationService.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return false;
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		dialog = new TransparentProgressDialog(HomeActivity.this);

		if (Globals.DIVISION_LIST == null
				|| !(Globals.DIVISION_LIST.size() > 0)) {
			AsyncTaskHandler asyncTask = new AsyncTaskHandler(
					HomeActivity.this,
					Constants.ASYNCTASK_ACTIONS.GET_DISTRICT_LIST_FROM_HOME);
			asyncTask.execute();
		}

		district_selector = (LinearLayout) findViewById(R.id.district_selector);
		district_selector.setOnClickListener(this);

		findViewById(R.id.ic_facebook).setOnClickListener(this);
		findViewById(R.id.ic_contribute).setOnClickListener(this);
		findViewById(R.id.ic_user_guide).setOnClickListener(this);
		findViewById(R.id.ic_settings).setOnClickListener(this);
		findViewById(R.id.fire_control_room).setOnClickListener(this);
		findViewById(R.id.police_quick_call).setOnClickListener(this);

		main_menu = (GridView) findViewById(R.id.main_menu);
		main_menu.setAdapter(new MainMenuAdapter(HomeActivity.this));
		main_menu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> av, View view,
					final int position, long id) {

				Animation animation = AnimationUtils.loadAnimation(
						HomeActivity.this, R.anim.zoom_out);
				animation.setFillAfter(false);

				animation.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {

						if (position == 0) {
							Intent police_activity = new Intent(
									HomeActivity.this, PoliceActivity.class);
							startActivity(police_activity);
							HomeActivity.this.finish();
						} else if (position == 1) {
							Intent fire_service_activity = new Intent(
									HomeActivity.this,
									FireServiceActivity.class);
							startActivity(fire_service_activity);
							HomeActivity.this.finish();
						} else if (position == 2) {
							Intent hospital_activity = new Intent(
									HomeActivity.this, HospitalActivity.class);
							startActivity(hospital_activity);
							HomeActivity.this.finish();
						} else if (position == 3) {
							// Get Location Manager and check for GPS & Network
							// location services
							LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
							if (!UtilityFunctions
									.isNetworkAvailable(HomeActivity.this)) {
								showSimpleDialogWithMessage(getResources()
										.getString(
												R.string.internet_connection_required));
							} else if (!(lm
									.isProviderEnabled(LocationManager.GPS_PROVIDER)
									|| lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
								// Build the alert dialog
								AlertDialog.Builder builder = new AlertDialog.Builder(
										HomeActivity.this);
								builder.setTitle(R.string.location_services_not_active);
								builder.setMessage(R.string.please_enable_location_services_and_gps);
								builder.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialogInterface,
													int i) {
												// Show location settings when
												// the user acknowledges the
												// alert dialog
												Intent intent = new Intent(
														Settings.ACTION_LOCATION_SOURCE_SETTINGS);
												startActivity(intent);
											}
										});
								Dialog alertDialog = builder.create();
								alertDialog.setCanceledOnTouchOutside(false);
								alertDialog.show();
							} else {
								Intent map_activity = new Intent(
										HomeActivity.this,
										MapViewActivity.class);
								startActivity(map_activity);
								HomeActivity.this.finish();
							}
						}

					}
				});

				view.startAnimation(animation);

			}

		});

		district_listview = (ExpandableListView) findViewById(R.id.district_listview);
		district_listview_container = (LinearLayout) findViewById(R.id.district_listview_container);

		SharedPreferences pref = getSharedPreferences(
				Constants.SHARED_PREFERENCE.SHARED_PREFERENCE_NAME,
				Context.MODE_PRIVATE);

		language_preference = pref.getInt(
				Constants.SHARED_PREFERENCE.LANGUAGE_PREFERENCE,
				Constants.LANGUAGE.ENGLISH);

		present_district = UtilityFunctions
				.getPresentDistrictFromSharedPreference(HomeActivity.this);

		if (present_district != null) {
			present_dis_id = present_district.id;
			present_dis_name = present_district.getName(language_preference);
		}

		present_thana = UtilityFunctions
				.getPresentThanaFromSharedPreference(HomeActivity.this);

		if (present_thana != null) {
			present_thana_id = present_thana.id;
			present_thana_name = present_thana.getName(language_preference);
			police_quick_call_number = present_thana.getQuickContactNumber();
		}

	}

	@Override
	public void onBackPressed() {

		if (isShowingDistrictList) {
			hideDistrictList();
		} else {
			HomeActivity.this.finish();
		}
	}

	@Override
	public void onClick(View v) {

		int id = v.getId();
		switch (id) {
		case R.id.district_selector:
			toggleDistrictList();
			break;

		case R.id.ic_facebook:
			showFacebookLinks();
			break;

		case R.id.ic_contribute:

			if (UtilityFunctions.isNetworkAvailable(HomeActivity.this)) {
				Intent contributionIntent = new Intent(HomeActivity.this,
						ContributionActivity.class);
				startActivity(contributionIntent);
				HomeActivity.this.finish();

			} else {
				showSimpleDialogWithMessage(getResources().getString(
						R.string.internet_connection_required));
			}

			break;

		case R.id.ic_user_guide:

			Intent userGuide = new Intent(HomeActivity.this,
					UserguideActivity.class);
			startActivity(userGuide);
			HomeActivity.this.finish();
			break;

		case R.id.ic_settings:
			Intent settingsActivity = new Intent(HomeActivity.this,
					SettingsActivity.class);
			startActivity(settingsActivity);
			HomeActivity.this.finish();
			break;

		case R.id.fire_control_room:
			callFireControlRoom();
			break;

		case R.id.police_quick_call:
			callQuickPolice();
			break;

		default:
			break;
		}

	}

	private void showDistrictList() {

		DistrictListAdapter dla = new DistrictListAdapter(HomeActivity.this);
		district_listview.setAdapter(dla);
		district_listview.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView elv, View v,
					int groupPosition, int childPosition, long id) {

				Intent districtIntent = new Intent(HomeActivity.this,
						DistrictActivity.class);
				districtIntent.putExtra("district_id", Globals.DIVISION_LIST
						.get(groupPosition).districts.get(childPosition).id);
				districtIntent.putExtra(
						"district_name",
						Globals.DIVISION_LIST.get(groupPosition).districts.get(
								childPosition).getName(language_preference));
				startActivity(districtIntent);
				HomeActivity.this.finish();
				return true;
			}
		});

		district_listview.setOnGroupClickListener(dla);
		district_listview_container.setVisibility(View.VISIBLE);
		isShowingDistrictList = true;
		((ImageView) district_selector.findViewById(R.id.ic_expand))
				.setImageResource(R.drawable.ic_action_collapse);
	}

	private void hideDistrictList() {

		district_listview_container.setVisibility(View.GONE);
		isShowingDistrictList = false;
		((ImageView) district_selector.findViewById(R.id.ic_expand))
				.setImageResource(R.drawable.ic_action_expand);

	}

	private void toggleDistrictList() {

		if (isShowingDistrictList) {
			hideDistrictList();
		} else {
			showDistrictList();
		}

	}

	public void onPreExecuteAsyncTask() {
		dialog.show();
	}

	public void onPostExecuteAsyncTask(ArrayList<District> districts) {

		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}

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

	}

	private void showFacebookLinks() {
		final Dialog detailsDialog = new Dialog(this);
		detailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		View fb_links = ((LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.facebook_links_dialog, null);

		fb_links.findViewById(R.id.fb_police).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						detailsDialog.dismiss();
						Intent fb_police = UtilityFunctions
								.getFacebookPageIntent(HomeActivity.this,
										"387950311308200", "dmp.dhaka");
						startActivity(fb_police);
					}
				});

		fb_links.findViewById(R.id.fb_mis).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						detailsDialog.dismiss();
						Intent fb_mis = UtilityFunctions.getFacebookPageIntent(
								HomeActivity.this, "189823517785353",
								"mis.dghs");
						startActivity(fb_mis);
					}
				});

		detailsDialog.setContentView(fb_links);
		detailsDialog.show();
	}

	private void callFireControlRoom() {
		AlertDialog.Builder dialogBuilder = new Builder(HomeActivity.this);
		dialogBuilder
				.setTitle(getResources().getString(R.string.making_phone_call))
				.setMessage(
						getResources().getString(
								R.string.call_fire_control_room))
				.setPositiveButton(getResources().getString(R.string.yes),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String url = "tel:"
										+ HomeActivity.this.fire_control_room
												.trim();
								Intent call = new Intent(Intent.ACTION_CALL,
										Uri.parse(url));
								HomeActivity.this.startActivity(call);
								dialog.dismiss();

							}
						})
				.setNegativeButton(getResources().getString(R.string.no),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).show();
	}

	private void callQuickPolice() {

		if (police_quick_call_number.equals("")) {
			showSimpleDialogWithMessage(getResources().getString(
					R.string.select_nearest_police_station_in_settings));
		} else {
			AlertDialog.Builder dialogBuilder = new Builder(HomeActivity.this);
			dialogBuilder
					.setTitle(
							getResources()
									.getString(R.string.making_phone_call))
					.setMessage(
							getResources().getString(R.string.call_to)
									+ present_thana_name + "?")
					.setPositiveButton(getResources().getString(R.string.yes),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String url = "tel:"
											+ HomeActivity.this.police_quick_call_number
													.trim();
									Intent call = new Intent(
											Intent.ACTION_CALL, Uri.parse(url));
									HomeActivity.this.startActivity(call);
									dialog.dismiss();

								}
							})
					.setNegativeButton(getResources().getString(R.string.no),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).show();
		}
	}

	private void showSimpleDialogWithMessage(String message) {
		Dialog dialog = new Dialog(HomeActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		View view = ((LayoutInflater) HomeActivity.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.simple_dialog_with_message, null);

		((TextView) view.findViewById(R.id.message)).setText(message);

		dialog.setContentView(view);
		dialog.show();
		return;
	}
}
