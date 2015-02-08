package bd.com.elites.bes;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
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
import bd.com.elites.bes.utils.AsyncTaskHandler;
import bd.com.elites.bes.utils.BaseActivity;
import bd.com.elites.bes.utils.Constants;
import bd.com.elites.bes.utils.TransparentProgressDialog;

public class ComplaintActivity extends BaseActivity implements OnClickListener {

	TransparentProgressDialog dialog = null;
	int to = 1;
	RadioGroup complainTo;
	EditText messageTextView, complainantName, complainantContactNumber,
			complainantAddress;
	Button submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_complaint);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.complaint, menu);
		return false;
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		dialog = new TransparentProgressDialog(ComplaintActivity.this);
		complainTo = (RadioGroup) findViewById(R.id.complainTo);
		messageTextView = (EditText) findViewById(R.id.message);
		complainantName = (EditText) findViewById(R.id.complainantName);
		complainantContactNumber = (EditText) findViewById(R.id.complainantContactNumber);
		complainantAddress = (EditText) findViewById(R.id.complainantAddress);
		submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(this);

		Intent intent = getIntent();
		int to = intent.getIntExtra("complaintTo", -1);
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
	public void onBackPressed() {
		ComplaintActivity.this.finish();
	}

	@Override
	public void onClick(View v) {

		int i = v.getId();

		switch (i) {
		case R.id.submit:
			Toast.makeText(ComplaintActivity.this,
					"This feature will be enable soon", Toast.LENGTH_LONG)
					.show();
//			processSubmit();
			break;

		default:
			break;
		}

	}

	private void processSubmit() {
		int to = -1;
		String text = "", name = "", number = "", address = "";

		// Validating complain to
		switch (complainTo.getCheckedRadioButtonId()) {
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

		// Validating text
		text = messageTextView.getText().toString();
		if (text.equals("")) {
			showSimpleDialogWithMessage(getString(R.string.please_write_your_complain_));
			return;
		}

		name = complainantName.getText().toString();
		number = complainantContactNumber.getText().toString();
		address = complainantAddress.getText().toString();

		AsyncTaskHandler asyncTask = new AsyncTaskHandler(
				ComplaintActivity.this,
				Constants.ASYNCTASK_ACTIONS.SUBMIT_COMPLAIN);
		asyncTask.execute(new String[] { String.valueOf(to), text, name,
				number, address });

	}

	public void onPreExecuteSubmitComplain() {
		dialog.show();

	}

	public void onPostExecuteSubmitComplain(String resultCode) {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		if (resultCode.equals("200")||resultCode.equals("201")) {
			Dialog dialog = new Dialog(ComplaintActivity.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

			View view = ((LayoutInflater) ComplaintActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.simple_dialog_with_message, null);

			String message = getString(R.string.your_complain_has_been_submitted_);
			((TextView) view.findViewById(R.id.message)).setText(message);

			dialog.setContentView(view);

			dialog.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					ComplaintActivity.this.finish();

				}
			});

			dialog.show();
		}

	}

	private void showSimpleDialogWithMessage(String message) {
		Dialog dialog = new Dialog(ComplaintActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		View view = ((LayoutInflater) ComplaintActivity.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.simple_dialog_with_message, null);

		((TextView) view.findViewById(R.id.message)).setText(message);

		dialog.setContentView(view);
		dialog.show();
		return;
	}

}
