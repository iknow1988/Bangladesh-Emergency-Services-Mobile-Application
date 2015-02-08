package bd.com.elites.bes;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import bd.com.elites.bes.adapter.PoliceSubTwoUnitListAdapter;
import bd.com.elites.bes.adapter.PoliceSubUnitListAdapter;
import bd.com.elites.bes.adapter.PoliceThanaListAdapter;
import bd.com.elites.bes.adapter.PoliceUnitListAdapter;
import bd.com.elites.bes.model.District;
import bd.com.elites.bes.model.PoliceSubTwoUnit;
import bd.com.elites.bes.model.PoliceSubUnit;
import bd.com.elites.bes.model.PoliceThana;
import bd.com.elites.bes.model.PoliceUnit;
import bd.com.elites.bes.utils.AsyncTaskHandler;
import bd.com.elites.bes.utils.BaseActivity;
import bd.com.elites.bes.utils.Constants;
import bd.com.elites.bes.utils.TransparentProgressDialog;
import bd.com.elites.bes.utils.UtilityFunctions;

public class PoliceActivity extends BaseActivity implements OnClickListener {

	AsyncTaskHandler asyncTask = null;
	TransparentProgressDialog dialog = null;
	ArrayList<PoliceUnit> policeUnits = null;
	ArrayList<PoliceSubUnit> policeSubUnits = null;
	ArrayList<PoliceSubTwoUnit> policeSubTwoUnits = null;

	int language_preference = Constants.LANGUAGE.ENGLISH;

	LinearLayout unit_selector, sub_unit_selector, sub_two_unit_selector;
	LinearLayout unit_listview_container, sub_unit_listview_container,
			sub_two_unit_listview_container;
	ListView unit_listview, sub_unit_listview, sub_two_unit_listview,
			police_stations_list;
	int selectedUnitId = -1;
	int selectedSubUnitId = -1;
	int selectedSubTwoUnitId = -1;

	boolean isShowingUnitSelector = false;
	boolean isShowingSubUnitSelector = false;
	boolean isShowingSubTwoUnitSelector = false;

	TextView contribute, submit_complaint;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		overridePendingTransition(R.anim.slide_in_from_right,
				R.anim.slide_out_to_left);
		setContentView(R.layout.activity_police);

		language_preference = UtilityFunctions
				.getLanguageFromSharedPreference(PoliceActivity.this);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		dialog = new TransparentProgressDialog(PoliceActivity.this);
		if (selectedUnitId == -1) {
			setDefaultListWithPresetDistrict();
		}
		findViewById(R.id.actionBar).setOnClickListener(this);

		unit_selector = (LinearLayout) findViewById(R.id.unit_selector);
		unit_selector.setOnClickListener(this);
		sub_unit_selector = (LinearLayout) findViewById(R.id.sub_unit_selector);
		sub_unit_selector.setOnClickListener(this);
		sub_two_unit_selector = (LinearLayout) findViewById(R.id.sub_two_unit_selector);
		sub_two_unit_selector.setOnClickListener(this);

		unit_listview_container = (LinearLayout) findViewById(R.id.unit_listview_container);
		unit_listview = (ListView) findViewById(R.id.unit_listview);

		sub_unit_listview_container = (LinearLayout) findViewById(R.id.sub_unit_listview_container);
		sub_unit_listview = (ListView) findViewById(R.id.sub_unit_listview);

		sub_two_unit_listview_container = (LinearLayout) findViewById(R.id.sub_two_unit_listview_container);
		sub_two_unit_listview = (ListView) findViewById(R.id.sub_two_unit_listview);

		police_stations_list = (ListView) findViewById(R.id.police_stations_list);

		contribute = (TextView) findViewById(R.id.contribute);
		contribute.setOnClickListener(this);
		submit_complaint = (TextView) findViewById(R.id.submit_complaint);
		submit_complaint.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();

		if (isShowingUnitSelector) {

			hideUnitListviewContainer();

		} else if (isShowingSubUnitSelector) {

			hideSubUnitListViewContainer();

		} else if (isShowingSubTwoUnitSelector) {

			hideSubTwoUnitListViewContainer();

		} else {
			Intent home_activity = new Intent(PoliceActivity.this,
					HomeActivity.class);
			startActivity(home_activity);
			PoliceActivity.this.finish();
		}
	}

	public void onPreExecuteGetUnitNames() {
		dialog.show();

	}

	public void onPostExecuteGetUnitNames(ArrayList<PoliceUnit> policeUnits) {

		if (dialog.isShowing()) {
			dialog.dismiss();
		}

		PoliceActivity.this.policeUnits = policeUnits;

		((TextView) findViewById(R.id.unit_name_textview))
				.setText(getResources().getString(R.string.select_unit));

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		switch (id) {
		case R.id.unit_selector:
			showUnitListviewContainer();
			break;

		case R.id.sub_unit_selector:
			showSubUnitListViewContainer();
			break;

		case R.id.sub_two_unit_selector:
			showSubTwoUnitListViewContainer();
			break;

		case R.id.actionBar:
			showAdditionalInfo();
			break;

		case R.id.contribute:
			Intent contributionIntent = new Intent(PoliceActivity.this,
					ContributionActivity.class);
			contributionIntent.putExtra("contributionTo", 1);
			startActivity(contributionIntent);
			PoliceActivity.this.finish();

			break;

		case R.id.submit_complaint:
			Intent complaintIntent = new Intent(PoliceActivity.this,
					ComplaintActivity.class);
			complaintIntent.putExtra("complaintTo", 1);
			startActivity(complaintIntent);
			break;

		default:
			break;
		}

	}

	private void showUnitListviewContainer() {

		hideSubUnitListViewContainer();
		hideSubTwoUnitListViewContainer();

		unit_listview.setAdapter(new PoliceUnitListAdapter(PoliceActivity.this,
				PoliceActivity.this.policeUnits));
		unit_listview_container.setVisibility(View.VISIBLE);
		isShowingUnitSelector = true;

		unit_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> av, View view, int position,
					long id) {

				hideUnitListviewContainer();

				PoliceUnit unit = (PoliceUnit) av.getAdapter()
						.getItem(position);
				((TextView) findViewById(R.id.unit_name_textview)).setText(unit
						.getUnitName(language_preference));
				if (PoliceActivity.this.selectedUnitId != unit.id) {

					hideSubUnitSelector();

					PoliceActivity.this.selectedUnitId = unit.id;
					asyncTask = new AsyncTaskHandler(PoliceActivity.this,
							Constants.ASYNCTASK_ACTIONS.GET_POLICE_SUB_UNITS);
					asyncTask.execute("" + unit.id);
				}

			}

		});
	}

	private void hideUnitListviewContainer() {
		unit_listview_container.setVisibility(View.GONE);
		isShowingUnitSelector = false;
	}

	private void showSubUnitListViewContainer() {

		hideSubTwoUnitListViewContainer();

		sub_unit_listview.setAdapter(new PoliceSubUnitListAdapter(
				PoliceActivity.this, PoliceActivity.this.policeSubUnits));
		sub_unit_listview_container.setVisibility(View.VISIBLE);
		isShowingSubUnitSelector = true;

		sub_unit_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> av, View view, int position,
					long id) {

				hideSubUnitListViewContainer();

				PoliceSubUnit subUnit = (PoliceSubUnit) av.getAdapter()
						.getItem(position);
				((TextView) findViewById(R.id.sub_unit_name_textview))
						.setText(subUnit.getSubUnitName(language_preference));
				if (PoliceActivity.this.selectedSubUnitId != subUnit.id) {

					hideSubTwoUnitSelector();

					PoliceActivity.this.selectedSubUnitId = subUnit.id;

					sub_two_unit_selector.setVisibility(View.GONE);

					asyncTask = new AsyncTaskHandler(
							PoliceActivity.this,
							Constants.ASYNCTASK_ACTIONS.GET_POLICE_SUB_TWO_UNITS);
					asyncTask.execute("" + subUnit.id);

				}

			}

		});

	}

	private void hideSubUnitListViewContainer() {
		sub_unit_listview_container.setVisibility(View.GONE);
		isShowingSubUnitSelector = false;
	}

	public void onPreExecuteGetSubUnitNames() {
		dialog.show();

	}

	public void onPostExecuteGetSubUnitNames(
			ArrayList<PoliceSubUnit> policeSubUnits) {

		if (dialog.isShowing()) {
			dialog.dismiss();
		}

		if (policeSubUnits.size() > 0) {
			PoliceActivity.this.policeSubUnits = policeSubUnits;

			((TextView) findViewById(R.id.sub_unit_name_textview))
					.setText(getResources().getString(R.string.select_sub_unit));
			sub_unit_selector.setVisibility(View.VISIBLE);
		}

	}

	public void onPreExecuteGetSubTwoUnitNames() {
		dialog.show();

	}

	public void onPostExecuteGetSubTwoUnitNames(
			ArrayList<PoliceSubTwoUnit> policeSubTwoUnits) {

		if (dialog.isShowing()) {
			dialog.dismiss();
		}

		if (policeSubTwoUnits.size() > 0) {
			PoliceActivity.this.policeSubTwoUnits = policeSubTwoUnits;

			((TextView) findViewById(R.id.sub_two_unit_name_textview))
					.setText(getResources().getString(
							R.string.select_sub_two_unit));
			sub_two_unit_selector.setVisibility(View.VISIBLE);
		} else {

			asyncTask = new AsyncTaskHandler(PoliceActivity.this,
					Constants.ASYNCTASK_ACTIONS.GET_POLICE_STATIONS);
			asyncTask.execute(new String[] {
					"" + PoliceActivity.this.selectedUnitId,
					"" + PoliceActivity.this.selectedSubUnitId, "" + -1 });
		}

	}

	private void showSubTwoUnitListViewContainer() {

		sub_two_unit_listview.setAdapter(new PoliceSubTwoUnitListAdapter(
				PoliceActivity.this, PoliceActivity.this.policeSubTwoUnits));
		sub_two_unit_listview_container.setVisibility(View.VISIBLE);
		isShowingSubTwoUnitSelector = true;

		sub_two_unit_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> av, View view, int position,
					long id) {

				hideSubTwoUnitListViewContainer();

				PoliceSubTwoUnit subTwoUnit = (PoliceSubTwoUnit) av
						.getAdapter().getItem(position);
				((TextView) findViewById(R.id.sub_two_unit_name_textview))
						.setText(subTwoUnit
								.getSubTwoUnitName(language_preference));
				if (PoliceActivity.this.selectedSubTwoUnitId != subTwoUnit.id) {

					PoliceActivity.this.selectedSubTwoUnitId = subTwoUnit.id;

					asyncTask = new AsyncTaskHandler(PoliceActivity.this,
							Constants.ASYNCTASK_ACTIONS.GET_POLICE_STATIONS);
					asyncTask.execute(new String[] {
							"" + PoliceActivity.this.selectedUnitId,
							"" + PoliceActivity.this.selectedSubUnitId,
							"" + PoliceActivity.this.selectedSubTwoUnitId });

				}

			}

		});

	}

	private void hideSubTwoUnitListViewContainer() {
		sub_two_unit_listview_container.setVisibility(View.GONE);
		isShowingSubTwoUnitSelector = false;
	}

	public void onPreExecuteGetPoliceStations() {
		dialog.show();
	}

	public void onPostExecuteGetPoliceStations(
			ArrayList<PoliceThana> policeStations) {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}

		police_stations_list.setVisibility(View.VISIBLE);
		police_stations_list.setAdapter(new PoliceThanaListAdapter(
				PoliceActivity.this, policeStations));

	}

	private void hideSubTwoUnitSelector() {
		sub_two_unit_selector.setVisibility(View.GONE);
		police_stations_list.setVisibility(View.GONE);
	}

	private void hideSubUnitSelector() {
		sub_unit_selector.setVisibility(View.GONE);
		hideSubTwoUnitSelector();
	}

	private void showAdditionalInfo() {
		Dialog detailsDialog = new Dialog(PoliceActivity.this);
		detailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		View detailView = ((LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.police_additional_info, null);

		((TextView) detailView.findViewById(R.id.website))
				.setText("http://www.police.gov.bd/");

		detailsDialog.setContentView(detailView);
		detailsDialog.show();
	}

	private void setDefaultListWithPresetDistrict() {
		District present_district = UtilityFunctions
				.getPresentDistrictFromSharedPreference(this);
		PoliceThana presentPoliceThana = UtilityFunctions
				.getPresentThanaFromSharedPreference(this);
		if (present_district != null) {
			asyncTask = new AsyncTaskHandler(
					this,
					Constants.ASYNCTASK_ACTIONS.GET_STATIONS_FROM_POLICE_BY_DISTRICT);
			asyncTask.execute("" + present_district.id,
					presentPoliceThana.thana_name);
		} else {
			asyncTask = new AsyncTaskHandler(
					this,
					Constants.ASYNCTASK_ACTIONS.GET_STATIONS_FROM_POLICE_BY_DISTRICT);
			asyncTask.execute("" + 14, "");
		}

	}

	public void OnPreExecuteSetDafaultView() {
		dialog.show();
	}

	public void OnPostExecuteSetDafaultView(
			ArrayList<PoliceThana> policeStations) {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
		police_stations_list.setVisibility(View.VISIBLE);
		police_stations_list.setAdapter(new PoliceThanaListAdapter(
				PoliceActivity.this, policeStations));
		if (policeUnits == null || policeUnits.size() == 0) {
			asyncTask = new AsyncTaskHandler(PoliceActivity.this,
					Constants.ASYNCTASK_ACTIONS.GET_POLICE_UNITS);
			asyncTask.execute();
		}

	}

}
