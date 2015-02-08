package bd.com.elites.bes.map;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import bd.com.elites.bes.DistrictActivity;
import bd.com.elites.bes.HomeActivity;
import bd.com.elites.bes.R;
import bd.com.elites.bes.model.District;
import bd.com.elites.bes.utils.BaseActivity;
import bd.com.elites.bes.utils.Constants;
import bd.com.elites.bes.utils.Globals;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapViewActivity extends BaseActivity implements LocationListener,
		GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener, OnMarkerClickListener,
		OnInfoWindowClickListener, MapAsyncListener, MapDirectionCallBack,
		OnClickListener {

	int markerIcon[] = { R.drawable.ic_map_marker_police,
			R.drawable.ic_map_marker_fire_service,
			R.drawable.ic_map_marker_hospital };
	int pathColor[] = { R.color.police_path_color,
			R.color.fire_service_path_color, R.color.hospital_path_color };

	// private LocationClient mLocationClient;
	private GoogleApiClient mLocationClient;
	// These settings are the same as the settings for the map. They will in
	// fact give you updates
	// at the maximal rates currently possible.
	private static final LocationRequest REQUEST = LocationRequest.create()
			.setInterval(5000) // 5 seconds
			.setFastestInterval(16) // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	private GoogleMap mMap;
	Location myLocation = null;
	boolean isFirstTimeLoding = true;
	ProgressBar mapProgress;
	LinearLayout buttonSnapShot, buttonAllPoliceStations,
			buttonAllFireStations, buttonAllHospitals, bottomPanel;
	TextView textViewTitle;
	Marker myCustomPlacemarker;

	ArrayList<StationInfoForMap> minPolicestation = new ArrayList<StationInfoForMap>();
	ArrayList<StationInfoForMap> minFirestation = new ArrayList<StationInfoForMap>();
	ArrayList<StationInfoForMap> minHospitalstation = new ArrayList<StationInfoForMap>();
	// this three arraylist is needed for popping the info window of nearest
	// station marker
	HashMap<Integer, Integer> allPoliceStationMarkerMap; // <index, marker
															// number>
	HashMap<Integer, Integer> allFireStationMarkerMap;// <index, marker number>
	HashMap<Integer, Integer> allHospitalStationMarkerMap;// <index, marker
															// number>
	HashMap<Marker, StationInfoForMap> marker_station_info_map = new HashMap<Marker, StationInfoForMap>(); // <marker
																											// id,
	ArrayList<Polyline> allPolyline = new ArrayList<Polyline>();
	AsyncTaskHandlerForMap asyncMapDirection;
	AsyncTaskHandlerForMap asyncMapGeoCode;
	AsyncTaskHandlerForMap getDistrictFromDatabase;
	AsyncTaskHandlerForMap asyncmap;

	District district;
	int language_preference = Constants.LANGUAGE.ENGLISH;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		overridePendingTransition(R.anim.slide_in_from_right,
				R.anim.slide_out_to_left);

		setContentView(R.layout.activity_mapview);

		initializeView(savedInstanceState);
		setUpLocationClientIfNeeded();
		mLocationClient.connect();
		setUpMapIfNeeded();
		SharedPreferences pref = getSharedPreferences(
				Constants.SHARED_PREFERENCE.SHARED_PREFERENCE_NAME,
				Context.MODE_PRIVATE);

		language_preference = pref.getInt(
				Constants.SHARED_PREFERENCE.LANGUAGE_PREFERENCE,
				Constants.LANGUAGE.ENGLISH);
	}

	private void initializeView(Bundle savedInstanceState) {
		mapProgress = (ProgressBar) findViewById(R.id.progressBarMapLoading);
		buttonSnapShot = (LinearLayout) findViewById(R.id.ic_snapshot);
		buttonAllPoliceStations = (LinearLayout) findViewById(R.id.ic_police);
		buttonAllFireStations = (LinearLayout) findViewById(R.id.ic_fire);
		buttonAllHospitals = (LinearLayout) findViewById(R.id.ic_hospital);
		textViewTitle = (TextView) findViewById(R.id.action_bar_text);
		bottomPanel = (LinearLayout) findViewById(R.id.secondary_menu);
		bottomPanel.setVisibility(View.GONE);

		buttonSnapShot.setOnClickListener(this);
		buttonAllPoliceStations.setOnClickListener(this);
		buttonAllFireStations.setOnClickListener(this);
		buttonAllHospitals.setOnClickListener(this);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mLocationClient != null) {
			mLocationClient.disconnect();
		}
		if (asyncMapDirection != null)
			asyncMapDirection.cancel(true);
		if (asyncMapGeoCode != null)
			asyncMapGeoCode.cancel(true);
		if (getDistrictFromDatabase != null)
			getDistrictFromDatabase.cancel(true);
		if (asyncmap != null)
			asyncmap.cancel(true);

	}

	private void setUpLocationClientIfNeeded() {
		if (mLocationClient == null) {
			// mLocationClient = new LocationClient(getApplicationContext(),
			// this, // ConnectionCallbacks
			// this); // OnConnectionFailedListener
			mLocationClient = new GoogleApiClient.Builder(this)
					.addApi(LocationServices.API).addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this).build();
		}
	}

	private void setUpMapIfNeeded() {
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		// Hide the zoom controls as the button panel will cover it.
		mMap.getUiSettings().setZoomControlsEnabled(false);
		mMap.setMyLocationEnabled(true);

		// Setting an info window adapter allows us to change the both the
		// contents and look of the
		// info window.
		mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));

		mMap.setOnMarkerClickListener(this);
		mMap.setOnInfoWindowClickListener(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		myLocation = location;
		if (isFirstTimeLoding) {
			HideProgress();

			isFirstTimeLoding = false;
			LatLng latlng = new LatLng(location.getLatitude(),
					location.getLongitude());
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
					latlng, 10);
			mMap.animateCamera(cameraUpdate);
			addMyLocationMarker(latlng, true);
			ShowProgress();
			asyncMapGeoCode = new AsyncTaskHandlerForMap(this,
					AsyncTaskHandlerForMap.GET_ADDRESS_BY_GEOCODE, myLocation,
					this);
			asyncMapGeoCode.execute();
		} else if (myCustomPlacemarker != null) {
			myCustomPlacemarker.setPosition(new LatLng(location.getLatitude(),
					location.getLongitude()));
		}

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		myLocation = LocationServices.FusedLocationApi
				.getLastLocation(mLocationClient);
		// mLocationClient.requestLocationUpdates(REQUEST, this); //
		// LocationListener
		LocationServices.FusedLocationApi.requestLocationUpdates(
				mLocationClient, REQUEST, this);
	}

	private void addMyLocationMarker(LatLng latlng, boolean showAnim) {
		if (myCustomPlacemarker != null) {
			myCustomPlacemarker.remove();
		}
		MarkerOptions markerOpt = new MarkerOptions().position(latlng);
		int myLocationMarker = R.drawable.ic_man;
		markerOpt.icon(BitmapDescriptorFactory.fromResource(myLocationMarker));
		markerOpt.anchor(0.5f, 1.0f);
		myCustomPlacemarker = mMap.addMarker(markerOpt);
		if (showAnim)
			MapUtility.dropPinEffect(myCustomPlacemarker);
	}

	private void HideProgress() {
		mapProgress.setVisibility(View.GONE);
	}

	private void ShowProgress() {
		mapProgress.setVisibility(View.VISIBLE);
	}

	@Override
	public void onPreExecute() {
		ShowProgress();
	}

	@Override
	public void onError(Exception e) {
		HideProgress();

	}

	@Override
	public void onPostExecute(Object result, int type) {
		HideProgress();
		ArrayList<StationInfoForMap> allFocusedStations = new ArrayList<StationInfoForMap>();
		switch (type) {
		case AsyncTaskHandlerForMap.GET_NEAREST_PS:
			minPolicestation = new ArrayList<StationInfoForMap>(
					(ArrayList<StationInfoForMap>) result);
			addMarkersToMap(minPolicestation, type);
			allFocusedStations.clear();
			allFocusedStations = new ArrayList<StationInfoForMap>(
					minPolicestation);
			allFocusedStations.addAll(minFirestation);
			allFocusedStations.addAll(minHospitalstation);
			mapViewBound(myLocation, allFocusedStations);
			ShowProgress();
			asyncmap = new AsyncTaskHandlerForMap(this,
					AsyncTaskHandlerForMap.GET_NEAREST_FS, myLocation, this);
			asyncmap.execute(new String[] { district.id + "" });
			break;
		case AsyncTaskHandlerForMap.GET_NEAREST_FS:
			minFirestation = new ArrayList<StationInfoForMap>(
					(ArrayList<StationInfoForMap>) result);
			addMarkersToMap(minFirestation, type);
			allFocusedStations.clear();
			allFocusedStations = new ArrayList<StationInfoForMap>(
					minFirestation);
			allFocusedStations.addAll(minPolicestation);
			allFocusedStations.addAll(minHospitalstation);
			mapViewBound(myLocation, allFocusedStations);
			ShowProgress();
			asyncmap = new AsyncTaskHandlerForMap(this,
					AsyncTaskHandlerForMap.GET_NEAREST_HS, myLocation, this);
			asyncmap.execute(new String[] { district.id + "" });
			break;
		case AsyncTaskHandlerForMap.GET_NEAREST_HS:
			minHospitalstation = new ArrayList<StationInfoForMap>(
					(ArrayList<StationInfoForMap>) result);
			addMarkersToMap(minHospitalstation, type);
			allFocusedStations.clear();
			allFocusedStations = new ArrayList<StationInfoForMap>(
					minHospitalstation);
			allFocusedStations.addAll(minPolicestation);
			allFocusedStations.addAll(minFirestation);
			mapViewBound(myLocation, allFocusedStations);
			if (minFirestation.size() == 0 && minPolicestation.size() == 0
					&& minHospitalstation.size() == 0) {
				MapUtility
						.showErrorDialog(
								this,
								getString(R.string.sorry_cannot_find_any_nearby_service_station_));
			} else {
				showBottomPanel();
			}
			break;
		case AsyncTaskHandlerForMap.GET_ADDRESS_BY_GEOCODE:
			if (result != null) {
				String around = getResources().getString(R.string.around_)
						+ " ";
				textViewTitle.setText(around + (String) result);
				ShowProgress();
				getDistrictFromDatabase = new AsyncTaskHandlerForMap(this,
						AsyncTaskHandlerForMap.GET_DISTRICT_FROM_DB, this);
				getDistrictFromDatabase.execute((String) result);
			}

			break;

		case AsyncTaskHandlerForMap.GET_DISTRICT_FROM_DB:
			if (result != null) {
				this.district = (District) result;
				findViewById(R.id.headerLayout).setClickable(true);
				findViewById(R.id.headerLayout).setOnClickListener(this);
				TextView district_info = (TextView) findViewById(R.id.district_info);
				String str_district = getResources().getString(
						R.string.click_to_see_all_stations_list_in_);
				district_info.setText(str_district
						+ district.getName(language_preference));
				String around = getResources().getString(R.string.around_)
						+ " ";
				textViewTitle.setText(around
						+ district.getName(language_preference));
				district_info.setVisibility(View.VISIBLE);
				ShowProgress();
				asyncmap = new AsyncTaskHandlerForMap(this,
						AsyncTaskHandlerForMap.GET_NEAREST_PS, myLocation, this);
				asyncmap.execute(new String[] { district.id + "" });
			} else {
				MapUtility
						.showErrorDialog(
								this,
								getString(R.string.sorry_cannot_determine_your_location_please_search_manually_from_home_by_selecting_your_district));
			}

			break;

		case AsyncTaskHandlerForMap.GET_ALL_PS:
			ArrayList<StationInfoForMap> allPolicestation = new ArrayList<StationInfoForMap>(
					(ArrayList<StationInfoForMap>) result);
			mMap.clear();
			marker_station_info_map.clear();
			addMyLocationMarker(
					new LatLng(myLocation.getLatitude(),
							myLocation.getLongitude()), false);
			addMarkersToMap(allPolicestation, type - 10);
			allFocusedStations.clear();
			allFocusedStations = new ArrayList<StationInfoForMap>(
					allPolicestation);
			mapViewBound(myLocation, allFocusedStations);
			break;
		case AsyncTaskHandlerForMap.GET_ALL_FS:
			ArrayList<StationInfoForMap> allFirestation = new ArrayList<StationInfoForMap>(
					(ArrayList<StationInfoForMap>) result);
			mMap.clear();
			marker_station_info_map.clear();
			addMyLocationMarker(
					new LatLng(myLocation.getLatitude(),
							myLocation.getLongitude()), false);
			addMarkersToMap(allFirestation, type - 10);
			allFocusedStations.clear();
			allFocusedStations = new ArrayList<StationInfoForMap>(
					allFirestation);
			mapViewBound(myLocation, allFocusedStations);
			break;
		case AsyncTaskHandlerForMap.GET_ALL_HS:
			ArrayList<StationInfoForMap> allHospitalstation = new ArrayList<StationInfoForMap>(
					(ArrayList<StationInfoForMap>) result);
			mMap.clear();
			marker_station_info_map.clear();
			addMyLocationMarker(
					new LatLng(myLocation.getLatitude(),
							myLocation.getLongitude()), false);
			addMarkersToMap(allHospitalstation, type - 10);
			allFocusedStations.clear();
			allFocusedStations = new ArrayList<StationInfoForMap>(
					allHospitalstation);
			mapViewBound(myLocation, allFocusedStations);
			break;
		default:
			break;
		}
	}

	private void showBottomPanel() {
		bottomPanel.setVisibility(View.VISIBLE);
		findViewById(R.id.divider).setVisibility(View.VISIBLE);
	}

	private void addMarkersToMap(ArrayList<StationInfoForMap> allstation,
			int type) {
		allPoliceStationMarkerMap = new HashMap<Integer, Integer>();
		allFireStationMarkerMap = new HashMap<Integer, Integer>();
		allHospitalStationMarkerMap = new HashMap<Integer, Integer>();
		int totalMarkers = allstation.size();
		for (int i = 0; i < totalMarkers; i++) {
			StationInfoForMap psInfo = allstation.get(i);
			if (psInfo.getLocation().getLatitude() > 0) {
				LatLng latlng = psInfo.getLatlng();
				MarkerOptions markerMin3 = new MarkerOptions().position(latlng)
						.title(psInfo.getName(language_preference)).snippet("");
				markerMin3.icon(BitmapDescriptorFactory
						.fromResource(markerIcon[type - 1]));
				Marker marker = mMap.addMarker(markerMin3);
				marker_station_info_map.put(marker, psInfo);
			}

		}
	}

	private void mapViewBound(final Location location,
			final ArrayList<StationInfoForMap> minStations) {
		final View mapView = getSupportFragmentManager().findFragmentById(
				R.id.map).getView();
		if (mapView.getViewTreeObserver().isAlive()) {
			mapView.getViewTreeObserver().addOnGlobalLayoutListener(
					new OnGlobalLayoutListener() {
						@SuppressWarnings("deprecation")
						// We use the new method when supported
						@SuppressLint("NewApi")
						// We check which build version we are using.
						@Override
						public void onGlobalLayout() {
							LatLngBounds.Builder builder = new LatLngBounds.Builder();
							// for loop for adding all
							for (StationInfoForMap stationInfo : minStations) {
								if (stationInfo.getLatlng().latitude != -1)
									builder.include(stationInfo.getLatlng());
							}
							if (location != null) {
								LatLng src = new LatLng(location.getLatitude(),
										location.getLongitude());
								builder.include(src);
							}
							LatLngBounds bounds = builder.build();
							if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
								mapView.getViewTreeObserver()
										.removeGlobalOnLayoutListener(this);
							} else {
								mapView.getViewTreeObserver()
										.removeOnGlobalLayoutListener(this);
							}
							mMap.animateCamera(CameraUpdateFactory
									.newLatLngBounds(
											bounds,
											MapViewActivity.this
													.getResources()
													.getInteger(
															R.integer.map_viewbound_padding_size)));
						}
					});
		}

	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		if (marker.equals(myCustomPlacemarker)) {
			marker.hideInfoWindow();
			return true;
		} else {
			MapUtility.markerJumpAnim(marker);
		}
		return false;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		if (!marker.equals(myCustomPlacemarker)) {
			StationInfoForMap info = marker_station_info_map.get(marker);
			MapUtility
					.viewStationDetailsDialog(this, info, language_preference);
		}
		marker.hideInfoWindow();

	}

	public void showDirection(final StationInfoForMap info) {
		ShowProgress();
		MapUtility.removePolylines(allPolyline);
		LatLng src = new LatLng(myLocation.getLatitude(),
				myLocation.getLongitude());
		LatLng dstn = info.getLatlng();
		asyncMapDirection = new AsyncTaskHandlerForMap(src, dstn,
				MapViewActivity.this, getResources().getColor(
						pathColor[info.getType() - 1]),
				AsyncTaskHandlerForMap.GET_DIRECTION);
		asyncMapDirection.execute();
	}

	@Override
	public void onDirectionGet(PolylineOptions rectLine) {
		if (rectLine != null) {
			Polyline pl = mMap.addPolyline(rectLine);
			allPolyline.add(pl);
			HideProgress();
		}

	}

	@Override
	public void onClick(View v) {
		if (v == buttonSnapShot) {
			ShowProgress();
			takeSnapshot();
		} else if (v == buttonAllPoliceStations) {
			ShowProgress();
			asyncmap = new AsyncTaskHandlerForMap(this,
					AsyncTaskHandlerForMap.GET_ALL_PS, myLocation, this);
			asyncmap.execute(new String[] { district.id + "" });
		} else if (v == buttonAllFireStations) {
			ShowProgress();
			asyncmap = new AsyncTaskHandlerForMap(this,
					AsyncTaskHandlerForMap.GET_ALL_FS, myLocation, this);
			asyncmap.execute(new String[] { district.id + "" });
		} else if (v == buttonAllHospitals) {
			ShowProgress();
			asyncmap = new AsyncTaskHandlerForMap(this,
					AsyncTaskHandlerForMap.GET_ALL_HS, myLocation, this);
			asyncmap.execute(new String[] { district.id + "" });
		}

		if (v.getId() == R.id.headerLayout) {
			if (district != null && district.id > -1) {
				Intent districtIntent = new Intent(MapViewActivity.this,
						DistrictActivity.class);
				districtIntent.putExtra("district_id", district.id);
				districtIntent.putExtra("district_name",
						district.getName(language_preference));
				startActivity(districtIntent);
				MapViewActivity.this.finish();
			}
		}
	}

	private void takeSnapshot() {
		if (mMap == null) {
			return;
		}
		final SnapshotReadyCallback callback = new SnapshotReadyCallback() {
			@Override
			public void onSnapshotReady(Bitmap snapshot) {
				HideProgress();
				String path = MediaStore.Images.Media.insertImage(
						getContentResolver(), snapshot, "title", "desc");
				Intent intent = new Intent(MapViewActivity.this,
						SnapShotActivity.class);
				intent.putExtra("snapshot", path);
				startActivity(intent);
			}
		};
		mMap.snapshot(callback);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		Intent home = new Intent(MapViewActivity.this, HomeActivity.class);
		startActivity(home);
		MapViewActivity.this.finish();
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

	}

}
