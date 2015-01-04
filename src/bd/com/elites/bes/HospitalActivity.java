package bd.com.elites.bes;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import bd.com.elites.bes.adapter.DistrictListAdapter;
import bd.com.elites.bes.adapter.HospitalListAdapter;
import bd.com.elites.bes.model.District;
import bd.com.elites.bes.model.Division;
import bd.com.elites.bes.model.HospitalModel;
import bd.com.elites.bes.utils.AsyncTaskHandler;
import bd.com.elites.bes.utils.BaseActivity;
import bd.com.elites.bes.utils.Constants;
import bd.com.elites.bes.utils.Globals;
import bd.com.elites.bes.utils.TransparentProgressDialog;
import bd.com.elites.bes.utils.UtilityFunctions;

public class HospitalActivity extends BaseActivity implements OnClickListener {

	ListView hospital_list;
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
		setContentView(R.layout.activity_hospital);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hospital, menu);
		return false;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();

		if (isShowingDistrictList) {
			hideDistrictList();
		} else {

			Intent home_activity = new Intent(HospitalActivity.this,
					HomeActivity.class);
			startActivity(home_activity);
			HospitalActivity.this.finish();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		dialog = new TransparentProgressDialog(HospitalActivity.this);

		if (!(Globals.DIVISION_LIST.size() > 0)) {
			AsyncTaskHandler asyncTask = new AsyncTaskHandler(
					HospitalActivity.this,
					Constants.ASYNCTASK_ACTIONS.GET_DISTRICT_LIST_FROM_HOSPITAL);
			asyncTask.execute();
		} else {
			setDefaultListWithPresetDistrict();
		}

		findViewById(R.id.actionBar).setOnClickListener(this);

		district_listview = (ExpandableListView) findViewById(R.id.district_listview);
		district_selector = (LinearLayout) findViewById(R.id.district_selector);
		district_selector.setOnClickListener(this);
		district_listview_container = (LinearLayout) findViewById(R.id.district_listview_container);

		hospital_list = (ListView) findViewById(R.id.hospital_list);

		contribute = (TextView) findViewById(R.id.contribute);
		contribute.setOnClickListener(this);
		submit_complaint = (TextView) findViewById(R.id.submit_complaint);
		submit_complaint.setOnClickListener(this);
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
			Intent contributionIntent = new Intent(HospitalActivity.this,
					ContributionActivity.class);
			contributionIntent.putExtra("contributionTo", 3);
			startActivity(contributionIntent);
			HospitalActivity.this.finish();

			break;

		case R.id.submit_complaint:

			Intent complaintIntent = new Intent(HospitalActivity.this,
					ComplaintActivity.class);
			complaintIntent.putExtra("complaintTo", 3);
			startActivity(complaintIntent);
			break;

		default:
			break;
		}

	}

	private void showDistrictList() {

		DistrictListAdapter dla = new DistrictListAdapter(HospitalActivity.this);
		district_listview.setAdapter(dla);
		district_listview.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView elv, View v,
					int groupPosition, int childPosition, long id) {

				hospital_list.setVisibility(View.GONE);
				District district = Globals.DIVISION_LIST.get(groupPosition).districts
						.get(childPosition);
				((TextView) findViewById(R.id.district_name_textview)).setText(district.getName(UtilityFunctions
						.getLanguageFromSharedPreference(HospitalActivity.this)));

				if (HospitalActivity.this.selectedDistrictId != district.id) {
					hideDistrictList();

					HospitalActivity.this.selectedDistrictId = district.id;
					asyncTask = new AsyncTaskHandler(HospitalActivity.this,
							Constants.ASYNCTASK_ACTIONS.GET_HOSPITALS);
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

	private void showAdditionalInfo() {
		Dialog detailsDialog = new Dialog(HospitalActivity.this);
		detailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		View detailView = ((LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.hospital_additional_info, null);

		((TextView) detailView.findViewById(R.id.website))
				.setText("http://dghs.gov.bd/");

		detailsDialog.setContentView(detailView);
		detailsDialog.show();
	}

	public void onPreExecuteGetHospitals() {
		dialog.show();
	}

	public void onPostExecuteGetHospitals(ArrayList<HospitalModel> hospitals) {

		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}

		hospital_list.setAdapter(new HospitalListAdapter(HospitalActivity.this,
				hospitals));
		hospital_list.setVisibility(View.VISIBLE);
	}

	private void setDefaultListWithPresetDistrict() {
		present_district = UtilityFunctions
				.getPresentDistrictFromSharedPreference(HospitalActivity.this);

		if (present_district != null) {

			((TextView) findViewById(R.id.district_name_textview))
					.setText(present_district.getName(UtilityFunctions
							.getLanguageFromSharedPreference(HospitalActivity.this)));
			asyncTask = new AsyncTaskHandler(HospitalActivity.this,
					Constants.ASYNCTASK_ACTIONS.GET_HOSPITALS);
			asyncTask.execute("" + present_district.id);
		}

	}

}
