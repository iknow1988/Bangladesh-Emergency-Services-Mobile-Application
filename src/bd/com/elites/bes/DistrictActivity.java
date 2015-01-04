package bd.com.elites.bes;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import bd.com.elites.bes.adapter.FireStationListAdapter;
import bd.com.elites.bes.adapter.HospitalListAdapter;
import bd.com.elites.bes.adapter.PoliceThanaListAdapter;
import bd.com.elites.bes.model.FireserviceStation;
import bd.com.elites.bes.model.HospitalModel;
import bd.com.elites.bes.model.PoliceThana;
import bd.com.elites.bes.utils.AsyncTaskHandler;
import bd.com.elites.bes.utils.BaseActivity;
import bd.com.elites.bes.utils.Constants;
import bd.com.elites.bes.utils.TransparentProgressDialog;

public class DistrictActivity extends BaseActivity implements OnClickListener {

	int id = -1;
	String district_name = "";

	AsyncTaskHandler asyncTask = null;
	TransparentProgressDialog dialog = null;

	ListView ps_listview, fs_listview, h_listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		overridePendingTransition(R.anim.slide_in_from_right,
				R.anim.slide_out_to_left);
		setContentView(R.layout.activity_district);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		Intent intent = getIntent();
		if (intent != null) {
			id = intent.getIntExtra("district_id", -1);
			district_name = intent.getStringExtra("district_name");
		}

		dialog = new TransparentProgressDialog(DistrictActivity.this);

		((TextView) findViewById(R.id.action_bar_text)).setText(district_name);

		findViewById(R.id.police_stations_header).setOnClickListener(this);
		findViewById(R.id.fire_stations_header).setOnClickListener(this);
		findViewById(R.id.hospital_header).setOnClickListener(this);

		ps_listview = (ListView) findViewById(R.id.ps_listview);
		fs_listview = (ListView) findViewById(R.id.fs_listview);
		h_listview = (ListView) findViewById(R.id.h_listview);

		asyncTask = new AsyncTaskHandler(DistrictActivity.this,
				Constants.ASYNCTASK_ACTIONS.GET_POLICE_STATIONS_FOR_DISTRICT);
		asyncTask.execute("" + id);

	}

	public void onPreExecuteGetPoliceStations() {
		dialog.show();
	}

	public void onPostExecuteGetPoliceStations(
			ArrayList<PoliceThana> policeStations) {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}

		// police_stations_list.setVisibility(View.VISIBLE);
		ps_listview.setAdapter(new PoliceThanaListAdapter(
				DistrictActivity.this, policeStations));

		asyncTask = new AsyncTaskHandler(DistrictActivity.this,
				Constants.ASYNCTASK_ACTIONS.GET_FIRE_STATIONS_FOR_DISTRICT);
		asyncTask.execute("" + id);

	}

	public void onPreExecuteGetFireStations() {
		dialog.show();
	}

	public void onPostExecuteGetFireStations(
			ArrayList<FireserviceStation> fireStations) {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}

		fs_listview.setAdapter(new FireStationListAdapter(
				DistrictActivity.this, fireStations));
		
		asyncTask = new AsyncTaskHandler(DistrictActivity.this,
				Constants.ASYNCTASK_ACTIONS.GET_HOSPITALS_FOR_DISTRICT);
		//asyncTask.execute("" + id);
		asyncTask.execute("" + 14);
	}
	
	public void onPreExecuteGetHospitals() {
		dialog.show();
	}

	public void onPostExecuteGetHospitals(
			ArrayList<HospitalModel> hospitals) {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}

		h_listview.setAdapter(new HospitalListAdapter(
				DistrictActivity.this, hospitals));
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();

		Intent home_activity = new Intent(DistrictActivity.this,
				HomeActivity.class);
		startActivity(home_activity);
		DistrictActivity.this.finish();
	}

	@Override
	public void onClick(View view) {

		int i = view.getId();

		switch (i) {
		case R.id.police_stations_header:
			setPolieStationList();

			break;
		case R.id.fire_stations_header:
			setFireStationList();

			break;
		case R.id.hospital_header:
			setHospitalList();
			break;

		default:
			break;
		}

	}

	private void setPolieStationList() {

		LinearLayout police_stations_container = (LinearLayout) findViewById(R.id.police_stations_container);
		LinearLayout fire_stations_container = (LinearLayout) findViewById(R.id.fire_stations_container);
		LinearLayout hospital_container = (LinearLayout) findViewById(R.id.hospitals_container);

		LinearLayout top = (LinearLayout) findViewById(R.id.top);
		LinearLayout bottom = (LinearLayout) findViewById(R.id.bottom);

		top.removeAllViews();
		bottom.removeAllViews();

		top.addView(police_stations_container);
		findViewById(R.id.ps_listview_container).setVisibility(View.VISIBLE);

		bottom.addView(fire_stations_container);
		findViewById(R.id.fs_listview_container).setVisibility(View.GONE);

		bottom.addView(hospital_container);
		findViewById(R.id.h_listview_container).setVisibility(View.GONE);

	}

	private void setFireStationList() {
		LinearLayout police_stations_container = (LinearLayout) findViewById(R.id.police_stations_container);
		LinearLayout fire_stations_container = (LinearLayout) findViewById(R.id.fire_stations_container);
		LinearLayout hospital_container = (LinearLayout) findViewById(R.id.hospitals_container);

		LinearLayout top = (LinearLayout) findViewById(R.id.top);
		LinearLayout bottom = (LinearLayout) findViewById(R.id.bottom);

		top.removeAllViews();
		bottom.removeAllViews();

		top.addView(fire_stations_container);
		findViewById(R.id.fs_listview_container).setVisibility(View.VISIBLE);

		bottom.addView(police_stations_container);
		findViewById(R.id.ps_listview_container).setVisibility(View.GONE);
		bottom.addView(hospital_container);
		findViewById(R.id.h_listview_container).setVisibility(View.GONE);
	}

	private void setHospitalList() {
		LinearLayout police_stations_container = (LinearLayout) findViewById(R.id.police_stations_container);
		LinearLayout fire_stations_container = (LinearLayout) findViewById(R.id.fire_stations_container);
		LinearLayout hospital_container = (LinearLayout) findViewById(R.id.hospitals_container);

		LinearLayout top = (LinearLayout) findViewById(R.id.top);
		LinearLayout bottom = (LinearLayout) findViewById(R.id.bottom);

		top.removeAllViews();
		bottom.removeAllViews();

		top.addView(hospital_container);
		findViewById(R.id.h_listview_container).setVisibility(View.VISIBLE);

		bottom.addView(fire_stations_container);
		findViewById(R.id.fs_listview_container).setVisibility(View.GONE);
		bottom.addView(police_stations_container);
		findViewById(R.id.ps_listview_container).setVisibility(View.GONE);

	}

}
