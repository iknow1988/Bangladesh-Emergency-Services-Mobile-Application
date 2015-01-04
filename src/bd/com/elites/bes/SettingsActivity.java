package bd.com.elites.bes;

import java.util.ArrayList;
import com.google.gson.Gson;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import bd.com.elites.bes.adapter.DistrictListAdapter;
import bd.com.elites.bes.adapter.PoliceThanaListAdapterForSettings;
import bd.com.elites.bes.model.District;
import bd.com.elites.bes.model.Division;
import bd.com.elites.bes.model.PoliceThana;
import bd.com.elites.bes.utils.AsyncTaskHandler;
import bd.com.elites.bes.utils.BaseActivity;
import bd.com.elites.bes.utils.Constants;
import bd.com.elites.bes.utils.Globals;
import bd.com.elites.bes.utils.TransparentProgressDialog;
import bd.com.elites.bes.utils.UtilityFunctions;

public class SettingsActivity extends BaseActivity implements OnClickListener {

	ExpandableListView district_listview;
	ListView thana_listview;
	LinearLayout district_selector, district_listview_container,
			thana_selector, thana_listview_container;
	TextView district_name_textview, thana_name_textview;
	ArrayList<PoliceThana> thanasList;
	boolean isShowingDistrictList = false, isShowingThanaList = false;

	RadioGroup languageSelector;
	RadioButton languageEnglish, languageBangla;

	SharedPreferences pref;
	// String present_thana_name;
	int language_preference;// , present_thana_id;
	District present_district = null;
	PoliceThana present_thana = null;

	AsyncTaskHandler asyncTask;
	TransparentProgressDialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_in_from_right,
				R.anim.slide_out_to_left);
		setContentView(R.layout.activity_settings);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return false;
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		dialog = new TransparentProgressDialog(SettingsActivity.this);
		if (Globals.DIVISION_LIST == null
				|| !(Globals.DIVISION_LIST.size() > 0)) {
			AsyncTaskHandler asyncTask = new AsyncTaskHandler(
					SettingsActivity.this,
					Constants.ASYNCTASK_ACTIONS.GET_DISTRICT_LIST_FROM_SETTINGS);
			asyncTask.execute();
		}

		pref = getSharedPreferences(
				Constants.SHARED_PREFERENCE.SHARED_PREFERENCE_NAME,
				Context.MODE_PRIVATE);
		language_preference = UtilityFunctions
				.getLanguageFromSharedPreference(SettingsActivity.this);

		present_district = UtilityFunctions
				.getPresentDistrictFromSharedPreference(SettingsActivity.this);

		district_name_textview = ((TextView) findViewById(R.id.district_name_textview));
		if (present_district != null) {
			district_name_textview.setText(present_district
					.getName(language_preference));
		}

		// present_thana_id = pref.getInt(
		// Constants.SHARED_PREFERENCE.PRESENT_THANA_ID, -1);
		//
		// present_thana_name = pref.getString(
		// Constants.SHARED_PREFERENCE.PRESENT_THANA_NAME, "");

		present_thana = UtilityFunctions
				.getPresentThanaFromSharedPreference(SettingsActivity.this);
		thana_name_textview = ((TextView) findViewById(R.id.thana_name_textview));
		if (present_thana != null) {
			thana_name_textview.setText(present_thana
					.getName(language_preference));
		}

		languageSelector = (RadioGroup) findViewById(R.id.languageSelector);

		if (language_preference == Constants.LANGUAGE.BANGLA) {
			languageSelector.check(R.id.languageBangla);
		} else {
			languageSelector.check(R.id.languageEnglish);
		}

		district_listview = (ExpandableListView) findViewById(R.id.district_listview);
		district_selector = (LinearLayout) findViewById(R.id.district_selector);
		district_selector.setOnClickListener(this);
		district_listview_container = (LinearLayout) findViewById(R.id.district_listview_container);

		thana_listview = (ListView) findViewById(R.id.thana_listview);
		thana_selector = (LinearLayout) findViewById(R.id.thana_selector);
		thana_selector.setOnClickListener(this);
		thana_listview_container = (LinearLayout) findViewById(R.id.thana_listview_container);

		languageSelector
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {

						if (checkedId == R.id.languageBangla) {

							pref.edit()
									.putInt(Constants.SHARED_PREFERENCE.LANGUAGE_PREFERENCE,
											Constants.LANGUAGE.BANGLA).commit();
							// Toast.makeText(
							// SettingsActivity.this,
							// "Bangla has been set as your preferred language.",
							// Toast.LENGTH_SHORT).show();
						} else {
							pref.edit()
									.putInt(Constants.SHARED_PREFERENCE.LANGUAGE_PREFERENCE,
											Constants.LANGUAGE.ENGLISH)
									.commit();
							// Toast.makeText(
							// SettingsActivity.this,
							// "English has been set as your preferred language.",
							// Toast.LENGTH_SHORT).show();
						}
					}
				});

		findViewById(R.id.ok).setOnClickListener(this);

	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();

		if (isShowingDistrictList) {
			hideDistrictList();
		} else if (isShowingThanaList) {
			hideThanaList();
		} else {
			Intent home_activity = new Intent(SettingsActivity.this,
					HomeActivity.class);
			startActivity(home_activity);
			SettingsActivity.this.finish();
		}
	}

	@Override
	public void onClick(View v) {

		int id = v.getId();
		switch (id) {
		case R.id.district_selector:
			showDistrictList();
			break;

		case R.id.thana_selector:
			if (present_district == null || present_district.id == -1) {
				showSimpleDialogWithMessage(getString(R.string.please_select_your_district_first_));
			} else {

				if (isShowingThanaList) {

				} else {
					AsyncTaskHandler asyncTask = new AsyncTaskHandler(
							SettingsActivity.this,
							Constants.ASYNCTASK_ACTIONS.GET_THANA_LIST_FROM_SETTINGS);
					asyncTask.execute(new String[] { String
							.valueOf(present_district.id) });
				}
			}

			break;

		case R.id.ok:

			Intent home = new Intent(SettingsActivity.this, HomeActivity.class);
			startActivity(home);
			SettingsActivity.this.finish();
			break;

		default:
			break;
		}

	}

	public void onPreExecuteGetDistrictList() {
		dialog.show();
	}

	public void onPostExecuteGetDistrictList(ArrayList<District> districts) {

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

	private void showDistrictList() {

		hideThanaList();

		DistrictListAdapter dla = new DistrictListAdapter(SettingsActivity.this);
		district_listview.setAdapter(dla);
		district_listview.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView elv, View v,
					int groupPosition, int childPosition, long id) {

				hideDistrictList();
				District selected_district = Globals.DIVISION_LIST
						.get(groupPosition).districts.get(childPosition);

				if (present_district == null
						|| present_district.id != selected_district.id) {

					Gson gson = new Gson();
					String jsonDistrict = gson.toJson(selected_district);
					pref.edit()
							.putString(
									Constants.SHARED_PREFERENCE.PRESENT_DISTRICT,
									jsonDistrict).commit();
					present_district = selected_district;
					district_name_textview.setText(selected_district
							.getName(language_preference));

					// ///Remove these line once done/////
					/*
					 * present_dis_name = district.district_name; present_dis_id
					 * = district.id;
					 * district_name_textview.setText(present_dis_name);
					 * 
					 * pref.edit()
					 * .putInt(Constants.SHARED_PREFERENCE.PRESENT_DISTRICT_ID,
					 * present_dis_id).commit(); pref.edit() .putString(
					 * Constants.SHARED_PREFERENCE.PRESENT_DISTRICT_NAME,
					 * present_dis_name).commit();
					 */
					// ///////////////////////////////

					thana_name_textview.setText(getResources().getString(
							R.string.select_police_station));
					present_thana = null;
					pref.edit()
							.remove(Constants.SHARED_PREFERENCE.PRESENT_THANA)
							.commit();
				}

				return true;
			}
		});

		district_listview.setOnGroupClickListener(dla);
		district_listview_container.setVisibility(View.VISIBLE);
		isShowingDistrictList = true;
	}

	private void hideDistrictList() {
		district_listview_container.setVisibility(View.GONE);
		isShowingDistrictList = false;
	}

	public void onPreExecuteGetThanaList() {
		dialog.show();
	}

	public void onPostExecuteGetThanaList(ArrayList<PoliceThana> thanas) {

		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}

		this.thanasList = thanas;
		showThanaList();
	}

	private void showThanaList() {
		thana_listview.setAdapter(new PoliceThanaListAdapterForSettings(
				SettingsActivity.this, thanasList));

		thana_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> av, View v, int position,
					long id) {

				hideThanaList();

				PoliceThana selected_thana = SettingsActivity.this.thanasList
						.get(position);

				Gson gson = new Gson();
				String jsonThana = gson.toJson(selected_thana);
				pref.edit()
						.putString(Constants.SHARED_PREFERENCE.PRESENT_THANA,
								jsonThana).commit();
				present_thana = selected_thana;
				thana_name_textview.setText(present_thana
						.getName(language_preference));

				// Remove these codes//
				/*
				 * pref.edit()
				 * .putInt(Constants.SHARED_PREFERENCE.PRESENT_THANA_ID,
				 * present_thana_id).commit(); pref.edit() .putString(
				 * Constants.SHARED_PREFERENCE.PRESENT_THANA_NAME,
				 * present_thana_name).commit();
				 * 
				 * 
				 * pref.edit() .putString(
				 * Constants.SHARED_PREFERENCE.PRESENT_THANA_QUICK_NUMBER,
				 * quick_num).commit();
				 */
				// ////////////

			}
		});

		thana_listview_container.setVisibility(View.VISIBLE);
		isShowingThanaList = true;
	}

	private void hideThanaList() {
		thana_listview_container.setVisibility(View.GONE);
		isShowingThanaList = false;
	}

	private void showSimpleDialogWithMessage(String message) {
		Dialog dialog = new Dialog(SettingsActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		View view = ((LayoutInflater) SettingsActivity.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.simple_dialog_with_message, null);

		((TextView) view.findViewById(R.id.message)).setText(message);

		dialog.setContentView(view);
		dialog.show();
		return;
	}

}
