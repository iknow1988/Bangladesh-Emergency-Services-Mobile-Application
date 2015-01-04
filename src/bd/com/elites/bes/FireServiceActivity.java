package bd.com.elites.bes;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import bd.com.elites.bes.adapter.DistrictListAdapter;
import bd.com.elites.bes.adapter.FireStationListAdapter;
import bd.com.elites.bes.model.District;
import bd.com.elites.bes.model.Division;
import bd.com.elites.bes.model.FireserviceStation;
import bd.com.elites.bes.utils.AsyncTaskHandler;
import bd.com.elites.bes.utils.BaseActivity;
import bd.com.elites.bes.utils.Constants;
import bd.com.elites.bes.utils.Globals;
import bd.com.elites.bes.utils.TransparentProgressDialog;
import bd.com.elites.bes.utils.UtilityFunctions;

public class FireServiceActivity extends BaseActivity implements
		OnClickListener {

	ListView fire_stations_list;
	ExpandableListView district_listview;
	LinearLayout district_selector, district_listview_container;
	boolean isShowingDistrictList = false;

	TextView contribute, submit_complaint;

	AsyncTaskHandler asyncTask;

	int selectedDistrictId = -1;
	District present_district = null;

	TransparentProgressDialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		overridePendingTransition(R.anim.slide_in_from_right,
				R.anim.slide_out_to_left);
		setContentView(R.layout.activity_fire_service);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		dialog = new TransparentProgressDialog(FireServiceActivity.this);

		if (!(Globals.DIVISION_LIST.size() > 0)) {
			AsyncTaskHandler asyncTask = new AsyncTaskHandler(
					FireServiceActivity.this,
					Constants.ASYNCTASK_ACTIONS.GET_DISTRICT_LIST_FROM_FIRESERVICE);
			asyncTask.execute();
		} else {
			setDefaultListWithPresetDistrict();
		}

		findViewById(R.id.actionBar).setOnClickListener(this);

		district_listview = (ExpandableListView) findViewById(R.id.district_listview);
		district_selector = (LinearLayout) findViewById(R.id.district_selector);
		district_selector.setOnClickListener(this);
		district_listview_container = (LinearLayout) findViewById(R.id.district_listview_container);

		fire_stations_list = (ListView) findViewById(R.id.fire_stations_list);

		contribute = (TextView) findViewById(R.id.contribute);
		contribute.setOnClickListener(this);
		submit_complaint = (TextView) findViewById(R.id.submit_complaint);
		submit_complaint.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();

		if (isShowingDistrictList) {
			hideDistrictList();
		} else {

			Intent home_activity = new Intent(FireServiceActivity.this,
					HomeActivity.class);
			startActivity(home_activity);
			FireServiceActivity.this.finish();
		}
	}

	@Override
	public void onClick(View v) {

		int id = v.getId();
		switch (id) {
		case R.id.district_selector:
			showDistrictList();
			break;

		case R.id.actionBar:
			showAdditionalInfo();
			break;

		case R.id.contribute:
			Intent contributionIntent = new Intent(FireServiceActivity.this,
					ContributionActivity.class);
			contributionIntent.putExtra("contributionTo", 2);
			startActivity(contributionIntent);
			FireServiceActivity.this.finish();

			break;

		case R.id.submit_complaint:

			Intent complaintIntent = new Intent(FireServiceActivity.this,
					ComplaintActivity.class);
			complaintIntent.putExtra("complaintTo", 2);
			startActivity(complaintIntent);
			break;

		default:
			break;
		}

	}

	private void showDistrictList() {

		DistrictListAdapter dla = new DistrictListAdapter(
				FireServiceActivity.this);
		district_listview.setAdapter(dla);
		district_listview.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView elv, View v,
					int groupPosition, int childPosition, long id) {

				fire_stations_list.setVisibility(View.GONE);
				District district = Globals.DIVISION_LIST.get(groupPosition).districts
						.get(childPosition);
				((TextView) findViewById(R.id.district_name_textview))
						.setText(district.getName(UtilityFunctions.getLanguageFromSharedPreference(FireServiceActivity.this)));

				if (FireServiceActivity.this.selectedDistrictId != district.id) {
					hideDistrictList();

					FireServiceActivity.this.selectedDistrictId = district.id;
					asyncTask = new AsyncTaskHandler(FireServiceActivity.this,
							Constants.ASYNCTASK_ACTIONS.GET_FIRE_STATIONS);
					asyncTask.execute("" + district.id);
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

	public void onPreExecuteGetFireStations() {
		dialog.show();
	}

	public void onPostExecuteGetFireStations(
			ArrayList<FireserviceStation> fireStations) {

		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}

		fire_stations_list.setAdapter(new FireStationListAdapter(
				FireServiceActivity.this, fireStations));
		fire_stations_list.setVisibility(View.VISIBLE);
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
				Globals.DIVISION_LIST.add(new Division(district.division));
				Globals.DIVISION_LIST.get(Globals.DIVISION_LIST.size() - 1)
						.addDistrict(district);
			} else {
				Globals.DIVISION_LIST.get(Globals.DIVISION_LIST.size() - 1)
						.addDistrict(district);
			}
		}

		setDefaultListWithPresetDistrict();

	}

	private void showAdditionalInfo() {
		Dialog detailsDialog = new Dialog(FireServiceActivity.this);
		detailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		View detailView = ((LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.fireservice_additional_info, null);

		((TextView) detailView.findViewById(R.id.website))
				.setText("http://www.fireservice.gov.bd/");

		detailsDialog.setContentView(detailView);
		detailsDialog.show();

	}

	private void setDefaultListWithPresetDistrict() {
		present_district = UtilityFunctions
				.getPresentDistrictFromSharedPreference(FireServiceActivity.this);

		if (present_district != null) {

			((TextView) findViewById(R.id.district_name_textview))
					.setText(present_district.getName(UtilityFunctions
							.getLanguageFromSharedPreference(FireServiceActivity.this)));
			asyncTask = new AsyncTaskHandler(FireServiceActivity.this,
					Constants.ASYNCTASK_ACTIONS.GET_FIRE_STATIONS);
			asyncTask.execute("" + present_district.id);

		}

	}

}
