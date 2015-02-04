package bd.com.elites.bes.utils;

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

public class AsyncTaskHandler extends AsyncTask<String, Void, Object> {

	Activity callerActivity;
	int actionName;
	Datasource dataSource;
	Service callerService;
	List<NameValuePair> nameValuePairs;

	public AsyncTaskHandler(Activity caller, int action) {

		this.callerActivity = caller;
		this.actionName = action;
		dataSource = new Datasource((Context) callerActivity);
	}

	public AsyncTaskHandler(Service caller, int action) {

		this.callerService = caller;
		this.actionName = action;
		dataSource = new Datasource((Context) callerService);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		switch (this.actionName) {
		case Constants.ASYNCTASK_ACTIONS.GET_DISTRICT_LIST:
			((SplashActivity) callerActivity).onPreExecuteAsyncTask();
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_DISTRICT_LIST_FROM_HOME:
			((HomeActivity) callerActivity).onPreExecuteAsyncTask();
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_DISTRICT_LIST_FROM_FIRESERVICE:
			((FireServiceActivity) callerActivity)
					.onPreExecuteGetDistrictList();
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_DISTRICT_LIST_FROM_HOSPITAL:
			((HospitalActivity) callerActivity).onPreExecuteGetDistrictList();
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_DISTRICT_LIST_FROM_SETTINGS:
			((SettingsActivity) callerActivity).onPreExecuteGetDistrictList();
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_POLICE_UNITS:
			((PoliceActivity) callerActivity).onPreExecuteGetUnitNames();
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_POLICE_SUB_UNITS:
			((PoliceActivity) callerActivity).onPreExecuteGetSubUnitNames();
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_POLICE_SUB_TWO_UNITS:
			((PoliceActivity) callerActivity).onPreExecuteGetSubTwoUnitNames();
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_POLICE_STATIONS:
			((PoliceActivity) callerActivity).onPreExecuteGetPoliceStations();
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_FIRE_STATIONS:
			((FireServiceActivity) callerActivity)
					.onPreExecuteGetFireStations();
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_HOSPITALS:
			((HospitalActivity) callerActivity).onPreExecuteGetHospitals();
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_POLICE_STATIONS_FOR_DISTRICT:
			((DistrictActivity) callerActivity).onPreExecuteGetPoliceStations();
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_FIRE_STATIONS_FOR_DISTRICT:
			((DistrictActivity) callerActivity).onPreExecuteGetFireStations();
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_HOSPITALS_FOR_DISTRICT:
			((DistrictActivity) callerActivity).onPreExecuteGetHospitals();
			break;

		case Constants.ASYNCTASK_ACTIONS.SUBMIT_CONTRIBUTION:
			((ContributionActivity) callerActivity)
					.onPreExecuteSubmitContribution();
			break;
		case Constants.ASYNCTASK_ACTIONS.SUBMIT_COMPLAIN:
			((ComplaintActivity) callerActivity).onPreExecuteSubmitComplain();
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_THANA_LIST_FROM_SETTINGS:
			((SettingsActivity) callerActivity).onPreExecuteGetThanaList();
			break;
		case Constants.ASYNCTASK_ACTIONS.GET_STATIONS_FROM_POLICE_BY_DISTRICT:
			((PoliceActivity) callerActivity).OnPreExecuteSetDafaultView();
			break;

		default:
			break;
		}
	}

	@Override
	protected Object doInBackground(String... strings) {

		Object returnObject = null;

		switch (this.actionName) {
		case Constants.ASYNCTASK_ACTIONS.GET_DISTRICT_LIST:
			returnObject = dataSource.get_districts();
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_DISTRICT_LIST_FROM_HOME:
			returnObject = dataSource.get_districts();
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_DISTRICT_LIST_FROM_FIRESERVICE:
			returnObject = dataSource.get_districts();
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_DISTRICT_LIST_FROM_HOSPITAL:
			returnObject = dataSource.get_districts();
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_DISTRICT_LIST_FROM_SETTINGS:
			returnObject = dataSource.get_districts();
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_POLICE_UNITS:
			returnObject = dataSource.get_police_units();
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_POLICE_SUB_UNITS:
			returnObject = dataSource.get_police_sub_units(strings[0]);
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_POLICE_SUB_TWO_UNITS:
			returnObject = dataSource.get_police_sub_two_units(strings[0]);
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_POLICE_STATIONS:
			returnObject = dataSource.get_police_stations(strings[0],
					strings[1], strings[2]);
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_FIRE_STATIONS:
			returnObject = dataSource.get_fire_stations(strings[0]);
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_HOSPITALS:
			returnObject = dataSource.get_hospitals(strings[0]);
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_POLICE_STATIONS_FOR_DISTRICT:
			returnObject = dataSource
					.get_police_stations_by_district(strings[0],"");
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_FIRE_STATIONS_FOR_DISTRICT:
			returnObject = dataSource.get_fire_stations(strings[0]);
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_HOSPITALS_FOR_DISTRICT:
			returnObject = dataSource.get_hospitals(strings[0]);
			break;

		case Constants.ASYNCTASK_ACTIONS.SUBMIT_CONTRIBUTION:
			String url = callerActivity.getResources().getString(
					R.string.submit_contribution_url);
			nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("contributionTo",
					strings[0]));
			nameValuePairs.add(new BasicNameValuePair("contributionType",
					strings[1]));
			nameValuePairs.add(new BasicNameValuePair("message", strings[2]));
			nameValuePairs.add(new BasicNameValuePair("latitude", strings[3]));
			nameValuePairs.add(new BasicNameValuePair("longitude", strings[4]));
			nameValuePairs.add(new BasicNameValuePair("contributorName",
					strings[5]));
			nameValuePairs.add(new BasicNameValuePair(
					"contributorContactNumber", strings[6]));
			nameValuePairs.add(new BasicNameValuePair("contributorAddress",
					strings[7]));
			nameValuePairs.add(new BasicNameValuePair("gcmRegId", "absjsb"));
			nameValuePairs.add(new BasicNameValuePair("submit", "submit"));
			returnObject = sendHTTPPostRequest(url, nameValuePairs);
			break;

		case Constants.ASYNCTASK_ACTIONS.SUBMIT_COMPLAIN:
			String m_url = callerActivity.getResources().getString(
					R.string.submit_complain_url);
			nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs
					.add(new BasicNameValuePair("complainTo", strings[0]));
			nameValuePairs.add(new BasicNameValuePair("message", strings[1]));
			nameValuePairs.add(new BasicNameValuePair("complainantName",
					strings[2]));
			nameValuePairs.add(new BasicNameValuePair(
					"complainantContactNumber", strings[3]));
			nameValuePairs.add(new BasicNameValuePair("complainantAddress",
					strings[4]));
			nameValuePairs.add(new BasicNameValuePair("gcmRegId", "absjsb"));
			nameValuePairs.add(new BasicNameValuePair("submit", "submit"));
			returnObject = sendHTTPPostRequest(m_url, nameValuePairs);
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_THANA_LIST_FROM_SETTINGS:
			returnObject = dataSource
					.get_police_stations_by_district(strings[0],"");
			break;
		case Constants.ASYNCTASK_ACTIONS.GET_GCM_REG_ID:
			returnObject = "";
			try {
				GoogleCloudMessaging gcm = GoogleCloudMessaging
						.getInstance(callerService);
				returnObject = gcm.register(strings[0]);
			} catch (IOException ex) {
			}
			break;
		case Constants.ASYNCTASK_ACTIONS.SEND_GCM_REG_ID:

			nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("gcmRegId", strings[1]));
			nameValuePairs
					.add(new BasicNameValuePair("app_version", strings[2]));
			nameValuePairs.add(new BasicNameValuePair("database_version",
					strings[3]));
			nameValuePairs.add(new BasicNameValuePair("submit", "submit"));
			returnObject = sendHTTPPostRequest(strings[0], nameValuePairs);
			break;
		case Constants.ASYNCTASK_ACTIONS.GET_PUSH_NOTIFICATION:

			nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("gcmRegId", strings[1]));
			nameValuePairs.add(new BasicNameValuePair("submit", "submit"));
			returnObject = sendHTTPPostRequestForData(strings[0],
					nameValuePairs);
			break;
		case Constants.ASYNCTASK_ACTIONS.GET_STATIONS_FROM_POLICE_BY_DISTRICT:
			returnObject = dataSource
					.get_police_stations_by_district(strings[0],strings[1]);
			break;

		default:
			break;
		}
		return returnObject;
	}

	private String sendHTTPPostRequest(String url,
			List<NameValuePair> nameValuePairs) {
		String responseCode = "";
		HttpPost httppost = new HttpPost(url);
		httppost.addHeader("content-type", "application/x-www-form-urlencoded");
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			Log.d("Post Data", nameValuePairs.toString());
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse hr = httpclient.execute(httppost);
			Log.d("Status", hr.getStatusLine().getStatusCode() + "");
			responseCode = hr.getStatusLine().getStatusCode() + "";
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseCode;
	}

	private String sendHTTPPostRequestForData(String url,
			List<NameValuePair> nameValuePairs) {
		String response = "";
		HttpPost httppost = new HttpPost(url);
		httppost.addHeader("content-type", "application/x-www-form-urlencoded");
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			Log.d("Post Data", nameValuePairs.toString());
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse hr = httpclient.execute(httppost);
			Log.d("Status", hr.getStatusLine().getStatusCode() + "");
			response = UtilityFunctions.getResponseString(hr);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	@Override
	protected void onPostExecute(Object result) {

		super.onPostExecute(result);

		switch (this.actionName) {
		case Constants.ASYNCTASK_ACTIONS.GET_DISTRICT_LIST:
			((SplashActivity) callerActivity)
					.onPostExecuteAsyncTask((ArrayList<District>) result);
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_DISTRICT_LIST_FROM_HOME:
			((HomeActivity) callerActivity)
					.onPostExecuteAsyncTask((ArrayList<District>) result);
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_DISTRICT_LIST_FROM_FIRESERVICE:
			((FireServiceActivity) callerActivity)
					.onPostExecuteGetDistrictList((ArrayList<District>) result);
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_DISTRICT_LIST_FROM_HOSPITAL:
			((HospitalActivity) callerActivity)
					.onPostExecuteGetDistrictList((ArrayList<District>) result);
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_DISTRICT_LIST_FROM_SETTINGS:
			((SettingsActivity) callerActivity)
					.onPostExecuteGetDistrictList((ArrayList<District>) result);
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_POLICE_UNITS:
			((PoliceActivity) callerActivity)
					.onPostExecuteGetUnitNames((ArrayList<PoliceUnit>) result);
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_POLICE_SUB_UNITS:
			((PoliceActivity) callerActivity)
					.onPostExecuteGetSubUnitNames((ArrayList<PoliceSubUnit>) result);
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_POLICE_SUB_TWO_UNITS:
			((PoliceActivity) callerActivity)
					.onPostExecuteGetSubTwoUnitNames((ArrayList<PoliceSubTwoUnit>) result);
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_POLICE_STATIONS:
			((PoliceActivity) callerActivity)
					.onPostExecuteGetPoliceStations((ArrayList<PoliceThana>) result);
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_FIRE_STATIONS:
			((FireServiceActivity) callerActivity)
					.onPostExecuteGetFireStations((ArrayList<FireserviceStation>) result);
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_HOSPITALS:
			((HospitalActivity) callerActivity)
					.onPostExecuteGetHospitals((ArrayList<HospitalModel>) result);
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_POLICE_STATIONS_FOR_DISTRICT:
			((DistrictActivity) callerActivity)
					.onPostExecuteGetPoliceStations((ArrayList<PoliceThana>) result);
			break;
		case Constants.ASYNCTASK_ACTIONS.GET_FIRE_STATIONS_FOR_DISTRICT:
			((DistrictActivity) callerActivity)
					.onPostExecuteGetFireStations((ArrayList<FireserviceStation>) result);
			break;

		case Constants.ASYNCTASK_ACTIONS.GET_HOSPITALS_FOR_DISTRICT:
			((DistrictActivity) callerActivity)
					.onPostExecuteGetHospitals((ArrayList<HospitalModel>) result);
			break;

		case Constants.ASYNCTASK_ACTIONS.SUBMIT_CONTRIBUTION:
			((ContributionActivity) callerActivity)
					.onPostExecuteSubmitContribution((String) result);
			break;
		case Constants.ASYNCTASK_ACTIONS.SUBMIT_COMPLAIN:
			((ComplaintActivity) callerActivity)
					.onPostExecuteSubmitComplain((String) result);
			break;
		case Constants.ASYNCTASK_ACTIONS.GET_THANA_LIST_FROM_SETTINGS:
			((SettingsActivity) callerActivity)
					.onPostExecuteGetThanaList((ArrayList<PoliceThana>) result);
			break;
		case Constants.ASYNCTASK_ACTIONS.GET_GCM_REG_ID:
			((GCMRegistrationService) callerService)
					.onGCMIDFound((String) result);
			break;
		case Constants.ASYNCTASK_ACTIONS.SEND_GCM_REG_ID:
			((GCMRegistrationService) callerService)
					.onGCMIDPosted((String) result);
			break;
		case Constants.ASYNCTASK_ACTIONS.GET_PUSH_NOTIFICATION:
			((PushNotificationService) callerService).onFinishFetcher(result);
			break;
		case Constants.ASYNCTASK_ACTIONS.GET_STATIONS_FROM_POLICE_BY_DISTRICT:
			((PoliceActivity) callerActivity)
					.OnPostExecuteSetDafaultView((ArrayList<PoliceThana>) result);
			break;

		default:
			break;
		}
	}

}
