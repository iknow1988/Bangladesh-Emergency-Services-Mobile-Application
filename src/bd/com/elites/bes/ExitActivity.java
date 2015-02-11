package bd.com.elites.bes;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import bd.com.elites.bes.utils.BaseActivity;
import bd.com.elites.bes.utils.UtilityFunctions;

public class ExitActivity extends BaseActivity implements OnClickListener {

	boolean isBackPressed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_in_from_left,
				R.anim.fade_out_fast);
		setContentView(R.layout.activity_exit);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onStart() {

		super.onStart();

		isBackPressed = false;

		findViewById(R.id.exitLikeThis).setOnClickListener(this);
		findViewById(R.id.exitRateIt).setOnClickListener(this);
		findViewById(R.id.exitTellFriends).setOnClickListener(this);
		findViewById(R.id.exitFeedback).setOnClickListener(this);
		findViewById(R.id.exitScreenBackButton).setOnClickListener(this);
		findViewById(R.id.exitScreenExitButton).setOnClickListener(this);
		findViewById(R.id.poweredBy).setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {

		if (isBackPressed) {

			ExitActivity.this.finish();

		} else {
			Toast.makeText(this, "Press EXIT to quit.", Toast.LENGTH_LONG)
					.show();
			isBackPressed = true;
		}
	}

	@Override
	public void onClick(View v) {

		Intent intent;

		switch (v.getId()) {
		case R.id.exitLikeThis:

			intent = UtilityFunctions.getFacebookPageIntent(ExitActivity.this,
					"115662305228449", "elitesbd");
			startActivity(intent);

			break;

		case R.id.exitRateIt:

			Uri uri = Uri.parse("market://details?id="
					+ ExitActivity.this.getPackageName());
			intent = new Intent(Intent.ACTION_VIEW, uri);
			try {
				startActivity(intent);
			} catch (ActivityNotFoundException e) {

				Toast.makeText(ExitActivity.this, "Failed to visit market.",
						Toast.LENGTH_LONG).show();
			}

			break;

		case R.id.exitTellFriends:

			String msg = "Hello, check this awesome app and stay safer than yesterday: https://play.google.com/store/apps/details?id="
					+ ExitActivity.this.getPackageName();

			intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_SUBJECT, "An Awesome App");
			intent.putExtra(Intent.EXTRA_TEXT, msg);
			startActivity(Intent.createChooser(intent, "Tell Your Friend Via:"));

			break;

		case R.id.exitFeedback:

			intent = UtilityFunctions.getFacebookPageIntent(ExitActivity.this,
					"115662305228449", "elitesbd");
			startActivity(intent);

			break;

		case R.id.exitScreenBackButton:
			intent = new Intent(ExitActivity.this, HomeActivity.class);
			ExitActivity.this.finish();
			startActivity(intent);

			break;

		case R.id.exitScreenExitButton:
			ExitActivity.this.finish();
			break;

		case R.id.poweredBy:
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://elitesbd.com/"));
			startActivity(browserIntent);

			break;

		default:
			break;
		}

	}

}
