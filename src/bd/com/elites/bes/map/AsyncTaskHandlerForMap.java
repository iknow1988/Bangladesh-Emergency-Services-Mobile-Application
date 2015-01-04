package bd.com.elites.bes.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import bd.com.elites.bes.database.Datasource;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;

public class AsyncTaskHandlerForMap extends AsyncTask<String, Void, Object> {

	public static final int GET_NEAREST_PS = 1;
	public static final int GET_NEAREST_FS = 2;
	public static final int GET_NEAREST_HS = 3;
	public static final int GET_ADDRESS_BY_GEOCODE = 8;
	public static final int GET_DIRECTION = 9;
	public static final int GET_DISTRICT_FROM_DB = 10;
	public static final int GET_ALL_PS = 11;
	public static final int GET_ALL_FS = 12;
	public static final int GET_ALL_HS = 13;

	Activity callerActivity;
	int actionName;
	Datasource dataSource;
	MapAsyncListener mListener;
	private ArrayList<StationInfoForMap> allStations;
	Location userLocation;
	// for direction
	GMapV2Direction md;
	Document doc = null;
	LatLng src, dstn;
	MapDirectionCallBack mapDirCallBack;
	int color;
	// for address fetch
	String url;
	private String httpResponseStr = null;
	private int responseCode;

	public AsyncTaskHandlerForMap(Activity caller, int action,
			MapAsyncListener mListener) {

		this.callerActivity = caller;
		this.actionName = action;
		dataSource = new Datasource((Context) callerActivity);
		this.mListener = mListener;
	}

	public AsyncTaskHandlerForMap(Activity caller, int action,
			Location userLocation, MapAsyncListener mListener) {

		this.callerActivity = caller;
		this.actionName = action;
		dataSource = new Datasource((Context) callerActivity);
		this.mListener = mListener;
		this.userLocation = userLocation;
		url = MapUtility.getGeoCodingURL(userLocation);
	}

	public AsyncTaskHandlerForMap(LatLng src, LatLng dstn,
			MapDirectionCallBack mapDirCallBack, int color, int action) {
		this.src = src;
		this.dstn = dstn;
		this.mapDirCallBack = mapDirCallBack;
		this.color = color;
		this.actionName = action;

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		switch (this.actionName) {

		case GET_NEAREST_PS: {
			mListener.onPreExecute();
			break;
		}
		case GET_NEAREST_FS: {
			mListener.onPreExecute();
			break;
		}

		case GET_NEAREST_HS: {
			mListener.onPreExecute();
			break;
		}

		case GET_DISTRICT_FROM_DB: {
			mListener.onPreExecute();
			break;
		}

		default:
			break;
		}
	}

	@Override
	protected Object doInBackground(String... strings) {

		Object returnObject = null;

		switch (this.actionName) {
		case GET_NEAREST_PS: {
			allStations = new ArrayList<StationInfoForMap>(
					dataSource.getAllPoliceStationsForMapByDistrict(strings[0]));
			return getMinLocation(userLocation, allStations, null, null);
		}

		case GET_NEAREST_FS: {
			allStations = new ArrayList<StationInfoForMap>(
					dataSource.getAllFireStationsForMapByDistrict(strings[0]));
			return getMinLocation(userLocation, allStations, null, null);
		}

		case GET_NEAREST_HS: {
			allStations = new ArrayList<StationInfoForMap>(
					dataSource
							.getAllHospitalStationsForMapByDistrict(strings[0]));
			return getMinLocation(userLocation, allStations, null, null);
		}

		case GET_ADDRESS_BY_GEOCODE: {
			if (MapUtility.isNetworkAvailable(callerActivity)) {
				try {
					HttpResponse httpResponse = null;
					httpResponse = getRequest(url);

					responseCode = httpResponse.getStatusLine().getStatusCode();
					httpResponseStr = MapUtility
							.getResponseString(httpResponse);
					String town = parseJson(httpResponseStr);
					// Log.d("qs url", httpResponseStr);
					return town;

				} catch (Exception e) {
					// if(listener != null)
					mListener.onError(e);
					Log.d("qs url", "Error while fetching");
					return null;
				}
			}
			return null;
		}
		case GET_DIRECTION: {
			md = new GMapV2Direction();
			doc = md.getDocument(src, dstn, GMapV2Direction.MODE_DRIVING);
			return doc;
		}

		case GET_DISTRICT_FROM_DB: {
			return dataSource.getDistrictByName(strings[0]);
		}

		case GET_ALL_PS: {
			allStations = new ArrayList<StationInfoForMap>(
					dataSource.getAllPoliceStationsForMapByDistrict(strings[0]));
			return allStations;
		}

		case GET_ALL_FS: {
			allStations = new ArrayList<StationInfoForMap>(
					dataSource.getAllFireStationsForMapByDistrict(strings[0]));
			return allStations;
		}

		case GET_ALL_HS: {
			allStations = new ArrayList<StationInfoForMap>(
					dataSource
							.getAllHospitalStationsForMapByDistrict(strings[0]));
			return allStations;
		}

		default:
			break;
		}
		return returnObject;
	}

	@Override
	protected void onPostExecute(Object result) {

		// super.onPostExecute(result);

		switch (this.actionName) {

		case GET_NEAREST_PS:
			mListener.onPostExecute(result, GET_NEAREST_PS);
			break;
		case GET_NEAREST_FS:
			mListener.onPostExecute(result, GET_NEAREST_FS);
			break;
		case GET_NEAREST_HS:
			mListener.onPostExecute(result, GET_NEAREST_HS);
			break;
		case GET_ADDRESS_BY_GEOCODE:
			mListener.onPostExecute(result, GET_ADDRESS_BY_GEOCODE);
			break;
		case GET_DIRECTION:
			if (doc != null) {
				ArrayList<LatLng> directionPoint = md.getDirection(doc);
				PolylineOptions rectLine = new PolylineOptions().width(8)
						.color(color);

				for (int i = 0; i < directionPoint.size(); i++) {
					rectLine.add(directionPoint.get(i));
				}
				mapDirCallBack.onDirectionGet(rectLine);
			} else {
				mapDirCallBack.onDirectionGet(null);
			}
			break;

		case GET_DISTRICT_FROM_DB:
			mListener.onPostExecute(result, GET_DISTRICT_FROM_DB);
			break;
		case GET_ALL_PS:
			mListener.onPostExecute(result, GET_ALL_PS);
			break;
		case GET_ALL_FS:
			mListener.onPostExecute(result, GET_ALL_FS);
			break;
		case GET_ALL_HS:
			mListener.onPostExecute(result, GET_ALL_HS);
			break;
		default:
			break;
		}
	}

	private ArrayList<StationInfoForMap> getMinLocation(Location userLocation,
			ArrayList<StationInfoForMap> src, List<Marker> popUpMarker,
			HashMap<Integer, Integer> markerHashmap) {
		int min1 = 0, min2 = 1, min3 = 2;
		int number_of_real_locations = 0;
		if (userLocation != null) {
			float min1dist = 999999999, min2dist = 999999999, min3dist = 999999999;
			Log.d("MapActivity", "allstation size" + src.size());
			for (int index = 0; index < src.size(); index++) {
				StationInfoForMap psInfo = src.get(index);
				if (psInfo.getLocation().getLatitude() > 0) {
					number_of_real_locations++;
					Location latlong = psInfo.getLocation();
					float dis = userLocation.distanceTo(latlong);
					if (dis < min1dist) {
						min3 = min2;
						min2 = min1;
						min1 = index;
						min3dist = min2dist;
						min2dist = min1dist;
						min1dist = dis;
					} else if (dis < min2dist) {
						min3 = min2;
						min2 = index;
						min3dist = min2dist;
						min2dist = dis;
					} else if (dis <= min3dist) {
						min3 = index;
						min3dist = dis;
					}
				}

			}
		}
		ArrayList<StationInfoForMap> minThreestation = new ArrayList<StationInfoForMap>();
		if (number_of_real_locations > 0) {
			if (markerHashmap != null && markerHashmap.get(min1) != null) {
				popUpMarker.get(markerHashmap.get(min1)).showInfoWindow();
			}

			minThreestation.add(src.get(min1));
		}

		// minThreestation.add(src.get(min2));
		// minThreestation.add(src.get(min3));
		return minThreestation;
	}

	public HttpResponse getRequest(String url) throws Exception {
		try {
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("content-type",
					"application/x-www-form-urlencoded");

			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse hr = httpclient.execute(httpGet);
			Log.d("Status", hr.getStatusLine().getStatusCode() + "");
			return hr;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("error in ApiCaller.sendJsonPostRequest()", e.toString());
			throw new Exception("No Network Connection");
		}

	}

	private String parseJson(String responseStr) {
		// TODO Auto-generated method stub
		JSONObject j;
		try {
			j = new JSONObject(responseStr);

			final JSONArray resultsArray = j.getJSONArray("results");
			String townName = "";
			int size = resultsArray.length();

			for (int i = 0; i < size; i++) {
				JSONObject jb = resultsArray.getJSONObject(i);
				JSONArray ja = jb.getJSONArray("address_components");
				int compSize = ja.length();
				for (int k = 0; k < compSize; k++) {
					JSONObject oneComponent = ja.getJSONObject(k);
					JSONArray types = oneComponent.getJSONArray("types");
					String firstElement = types.optString(0);
					String secondElement = types.optString(1);

					if (firstElement.equals("administrative_area_level_2")
							&& secondElement.equals("political")) {
						townName = oneComponent.getString("long_name");
					}
				}
				return townName;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
