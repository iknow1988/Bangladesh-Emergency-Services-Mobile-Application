package bd.com.elites.bes;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import bd.com.elites.bes.map.LocationPickerActivity;
import bd.com.elites.bes.utils.AsyncTaskHandler;
import bd.com.elites.bes.utils.BaseActivity;
import bd.com.elites.bes.utils.Constants;
import bd.com.elites.bes.utils.TransparentProgressDialog;
import bd.com.elites.bes.utils.UtilityFunctions;

public class ContributionActivity extends BaseActivity implements
		OnClickListener {

	TransparentProgressDialog dialog = null;
	public static final int GET_LOCATION = 1001;

	RadioGroup contibutionType, contibutionTo;

	EditText messageTextView, contributorName, contributorContactNumber,
			contributorAddress;

	TextView locationTextView;
	Button submit;

	String latitude = "";
	String longitude = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_in_from_right,
				R.anim.slide_out_to_left);
		setContentView(R.layout.activity_contribution);
	}

	@Override
	protected void onStart() {
		super.onStart();

		dialog = new TransparentProgressDialog(ContributionActivity.this);

		contibutionType = (RadioGroup) findViewById(R.id.contibutionType);
		contibutionTo = (RadioGroup) findViewById(R.id.contibutionTo);
		messageTextView = (EditText) findViewById(R.id.message);
		contributorName = (EditText) findViewById(R.id.contributorName);
		contributorContactNumber = (EditText) findViewById(R.id.contributorContactNumber);
		contributorAddress = (EditText) findViewById(R.id.contributorAddress);
		locationTextView = (TextView) findViewById(R.id.location);
		locationTextView.setOnClickListener(this);
		submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(this);

		Intent intent = getIntent();
		int to = intent.getIntExtra("contributionTo", -1);
		if (to > 0) {
			if (to != 1) {
				findViewById(R.id.toPolice).setVisibility(View.GONE);
			} else {
				findViewById(R.id.toPolice).setVisibility(View.VISIBLE);
				((RadioButton) findViewById(R.id.toPolice)).setChecked(true);
			}
			if (to != 2) {
				findViewById(R.id.toFireService).setVisibility(View.GONE);
			} else {
				findViewById(R.id.toFireService).setVisibility(View.VISIBLE);
				((RadioButton) findViewById(R.id.toFireService))
						.setChecked(true);
			}
			if (to != 3) {
				findViewById(R.id.toHospital).setVisibility(View.GONE);
			} else {
				findViewById(R.id.toHospital).setVisibility(View.VISIBLE);
				((RadioButton) findViewById(R.id.toHospital)).setChecked(true);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contribution, menu);
		return false;
	}

	@Override
	public void onBackPressed() {

		Intent home_activity = new Intent(ContributionActivity.this,
				HomeActivity.class);
		startActivity(home_activity);
		ContributionActivity.this.finish();
	}

	@Override
	public void onClick(View v) {

		int i = v.getId();
		switch (i) {
		case R.id.location:

			if (UtilityFunctions.isNetworkAvailable(ContributionActivity.this)) {
				Intent police_activity = new Intent(ContributionActivity.this,
						LocationPickerActivity.class);
				startActivityForResult(police_activity, GET_LOCATION);
			} else {
				showSimpleDialogWithMessage(getString(R.string.internet_connection_is_required));
			}

			break;

		case R.id.submit:
			Toast.makeText(ContributionActivity.this,
					"This feature will be enable soon", Toast.LENGTH_LONG)
					.show();
			// processSubmit();

			break;

		default:
			break;
		}

	}

	private void processSubmit() {
		int to = -1, type = -1;
		String text = "", name = "", number = "", address = "";

		// Validating contribution to
		switch (contibutionTo.getCheckedRadioButtonId()) {
		case R.id.toPolice:
			to = 1;
			break;

		case R.id.toFireService:
			to = 2;
			break;

		case R.id.toHospital:
			to = 3;
			break;

		default:
			to = -1;
			break;
		}

		if (to == -1) {
			showSimpleDialogWithMessage(getResources().getString(
					R.string.whom_to_contribute_alert));
			return;
		}

		// Validating contribution type
		switch (contibutionType.getCheckedRadioButtonId()) {
		case R.id.sendLocation:
			type = 1;
			break;

		case R.id.sendSuggestion:
			type = 2;
			break;

		case R.id.sendInformation:
			type = 3;
			break;

		default:
			type = -1;
			break;
		}

		if (type == -1) {
			showSimpleDialogWithMessage(getResources().getString(
					R.string.please_select_type));
			return;
		}

		// Validating text
		text = messageTextView.getText().toString();
		if (text.equals("")) {
			showSimpleDialogWithMessage(getResources().getString(
					R.string.please_write_some_related_text));
			return;
		}

		// Validation location : Location is required if the type is 1
		if (type == 1
				&& (this.latitude.equals("") || this.longitude.equals(""))) {
			showSimpleDialogWithMessage(getResources().getString(
					R.string.forgot_add_location_dialog));
			return;
		}

		name = contributorName.getText().toString();
		number = contributorContactNumber.getText().toString();
		address = contributorAddress.getText().toString();

		AsyncTaskHandler asyncTask = new AsyncTaskHandler(
				ContributionActivity.this,
				Constants.ASYNCTASK_ACTIONS.SUBMIT_CONTRIBUTION);
		asyncTask.execute(new String[] { String.valueOf(to),
				String.valueOf(type), text, latitude, longitude, name, number,
				address });

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == GET_LOCATION && resultCode == RESULT_OK) {
			Location location = data.getParcelableExtra("location");

			this.latitude = String.valueOf(location.getLatitude());
			this.longitude = String.valueOf(location.getLongitude());

			locationTextView.setText("Latitude: " + this.latitude
					+ "\nLongitude: " + this.longitude + "\n"
					+ R.string.click_to_chage_the_selected_location);

		}
	}

	private void showSimpleDialogWithMessage(String message) {
		Dialog dialog = new Dialog(ContributionActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		View view = ((LayoutInflater) ContributionActivity.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.simple_dialog_with_message, null);

		((TextView) view.findViewById(R.id.message)).setText(message);

		dialog.setContentView(view);
		dialog.show();
		return;
	}

	public void onPreExecuteSubmitContribution() {
		dialog.show();

	}

	public void onPostExecuteSubmitContribution(String resultCode) {

		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		if (resultCode.equals("200") || resultCode.equals("201")) {
			Dialog dialog = new Dialog(ContributionActivity.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

			View view = ((LayoutInflater) ContributionActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.simple_dialog_with_message, null);

			String message = getResources().getString(
					R.string.congratulation_for_contribution);

			((TextView) view.findViewById(R.id.message)).setText(message);

			dialog.setContentView(view);

			dialog.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					Intent home_activity = new Intent(
							ContributionActivity.this, HomeActivity.class);
					startActivity(home_activity);
					ContributionActivity.this.finish();

				}
			});

			dialog.show();
		}
	}

}
