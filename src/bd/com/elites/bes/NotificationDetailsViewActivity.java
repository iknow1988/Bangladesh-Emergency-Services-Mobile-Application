package bd.com.elites.bes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class NotificationDetailsViewActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification_details);
		Intent i = getIntent();
		String msg = i.getStringExtra("msg");
		String from = i.getStringExtra("from");
		showMsgDialog(msg, from);
	}

	private void showMsgDialog(String msg, String from) {
		Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.notification_dialog, null);

		((TextView) view.findViewById(R.id.message)).setText(msg);
		if (from.equals("1")) {
			((ImageView) view.findViewById(R.id.from_image))
					.setImageResource(R.drawable.ic_police_1);
			((TextView) view.findViewById(R.id.from)).setText(getResources()
					.getString(R.string.bangladesh_police));
		} else if (from.equals("2")) {
			((ImageView) view.findViewById(R.id.from_image))
					.setImageResource(R.drawable.ic_fire_2);
			((TextView) view.findViewById(R.id.from))
					.setText(getResources().getString(
							R.string.bangladesh_fire_service_and_civil_defence));
		}

		dialog.setContentView(view);
		dialog.show();
		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				finish();
			}
		});
	}

}
