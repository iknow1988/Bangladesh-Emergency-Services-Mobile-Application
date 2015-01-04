package bd.com.elites.bes;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebView;
import bd.com.elites.bes.utils.BaseActivity;

public class UserguideActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_in_from_right,
				R.anim.slide_out_to_left);
		setContentView(R.layout.activity_userguide);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.userguide, menu);
		return false;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();

		Intent home = new Intent(UserguideActivity.this, HomeActivity.class);
		startActivity(home);
		UserguideActivity.this.finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		WebView wv = (WebView) findViewById(R.id.webViewUserGuide);
		String fileUrl = getResources().getString(
				R.string.user_guide_html_location);
		wv.loadUrl(fileUrl);

	}

}
