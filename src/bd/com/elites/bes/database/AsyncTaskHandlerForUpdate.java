package bd.com.elites.bes.database;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import bd.com.elites.bes.ComplaintActivity;
import bd.com.elites.bes.ContributionActivity;
import bd.com.elites.bes.DistrictActivity;
import bd.com.elites.bes.FireServiceActivity;
import bd.com.elites.bes.GCMRegistrationService;
import bd.com.elites.bes.HomeActivity;
import bd.com.elites.bes.HospitalActivity;
import bd.com.elites.bes.PoliceActivity;
import bd.com.elites.bes.PushNotificationService;
import bd.com.elites.bes.R;
import bd.com.elites.bes.SettingsActivity;
import bd.com.elites.bes.SplashActivity;
import bd.com.elites.bes.database.Datasource;
import bd.com.elites.bes.model.District;
import bd.com.elites.bes.model.FireserviceStation;
import bd.com.elites.bes.model.HospitalModel;
import bd.com.elites.bes.model.PoliceSubTwoUnit;
import bd.com.elites.bes.model.PoliceSubUnit;
import bd.com.elites.bes.model.PoliceThana;
import bd.com.elites.bes.model.PoliceUnit;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class AsyncTaskHandlerForUpdate extends AsyncTask<String, Void, Object> {

	Activity callerActivity;
	int actionName;
	Datasource dataSource;
	List<NameValuePair> nameValuePairs;

	public AsyncTaskHandlerForUpdate(Activity caller, int action) {

		this.callerActivity = caller;
		this.actionName = action;
		dataSource = new Datasource((Context) callerActivity);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	@Override
	protected Object doInBackground(String... strings) {

		Object returnObject = null;

		returnObject = dataSource.encrypt();

		return returnObject;
	}

	@Override
	protected void onPostExecute(Object result) {

		super.onPostExecute(result);

		((DatabaseUpdateActivity) callerActivity)
				.onPostExecuteGetHospitals(result);

	}
	
	

}
