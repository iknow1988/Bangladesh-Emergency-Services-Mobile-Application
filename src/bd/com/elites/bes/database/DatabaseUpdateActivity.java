package bd.com.elites.bes.database;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import bd.com.elites.bes.R;

public class DatabaseUpdateActivity extends Activity {
	Button button1;
	ProgressBar prog1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database_update);
		button1 = (Button) findViewById(R.id.button1);
		prog1 = (ProgressBar) findViewById(R.id.progressBar1);
		button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AsyncTaskHandlerForUpdate as = new AsyncTaskHandlerForUpdate(
						DatabaseUpdateActivity.this, 1);
				as.execute();
			}
		});
	}

	public void onPostExecuteGetHospitals(Object o) {
		Log.d("tonmoy", "finished");
		prog1.setVisibility(View.GONE);

	}

}
