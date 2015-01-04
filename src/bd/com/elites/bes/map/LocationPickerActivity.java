package bd.com.elites.bes.map;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import bd.com.elites.bes.R;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationPickerActivity extends FragmentActivity implements
		LocationListener, ConnectionCallbacks, OnConnectionFailedListener,
		OnClickListener, OnMarkerDragListener, OnMapLongClickListener {

	boolean isMarkerIsTouched = false;

	private LocationClient mLocationClient;

	private static final LocationRequest REQUEST = LocationRequest.create()
			.setInterval(5000) // 5 seconds
			.setFastestInterval(16) // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	private GoogleMap mMap;
	Location myLocation = null;
	boolean isFirstTimeLoding = true;
	ProgressBar mapProgress;

	TextView textViewTitle;
	Button buttonSetLocation;
	Marker myCustomPlacemarker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_location_picker);

		initializeView(savedInstanceState);
		setUpLocationClientIfNeeded();
		mLocationClient.connect();
		setUpMapIfNeeded();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/*
		 * Toast.makeText( this,
		 * "For changing the marker's position tap on desired location or drag the marker"
		 * , Toast.LENGTH_LONG).show();
		 */
	}

	private void initializeView(Bundle savedInstanceState) {
		mapProgress = (ProgressBar) findViewById(R.id.progressBarMapLoading);
		textViewTitle = (TextView) findViewById(R.id.action_bar_text);
		buttonSetLocation = (Button) findViewById(R.id.buttonSetLocation);
		buttonSetLocation.setOnClickListener(this);
	}

	private void setUpLocationClientIfNeeded() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(getApplicationContext(), this, // ConnectionCallbacks
					this); // OnConnectionFailedListener
		}
	}

	private void setUpMapIfNeeded() {
		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		mMap.getUiSettings().setZoomControlsEnabled(false);
		mMap.setMyLocationEnabled(true);
		mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));
		mMap.setOnMarkerDragListener(this);
		mMap.setOnMapLongClickListener(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		if (isFirstTimeLoding) {
			HideProgress();
			myLocation = location;
			isFirstTimeLoding = false;
			LatLng latlng = new LatLng(location.getLatitude(),
					location.getLongitude());
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
					latlng, 10);
			mMap.animateCamera(cameraUpdate);
			addMyLocationMarker(latlng);
			// Toast.makeText(
			// this,
			// "For changing the marker's position tap on desired location or drag the marker",
			// Toast.LENGTH_LONG).show();
		} else if (myCustomPlacemarker != null && !isMarkerIsTouched) {
			myLocation = location;
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
		mLocationClient.requestLocationUpdates(REQUEST, this); // LocationListener
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
	}

	private void addMyLocationMarker(LatLng latlng) {
		if (myCustomPlacemarker != null) {
			myCustomPlacemarker.remove();
		}
		MarkerOptions markerOpt = new MarkerOptions().position(latlng);
		markerOpt.draggable(true);
		markerOpt.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.ic_location));
		myCustomPlacemarker = mMap.addMarker(markerOpt);
	}

	private void HideProgress() {
		mapProgress.setVisibility(View.GONE);
	}

	private void ShowProgress() {
		mapProgress.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		if (v == buttonSetLocation) {
			Intent data = new Intent();
			data.putExtra("location", myLocation);
			setResult(RESULT_OK, data);
			this.finish();

		}
	}

	@Override
	public void onMarkerDragEnd(Marker marker) {
		isMarkerIsTouched = true;
		LatLng location = marker.getPosition();
		myLocation.setLatitude(location.latitude);
		myLocation.setLongitude(location.longitude);

		final CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(location) // Sets the center of the map to Mountain
									// View
				.zoom(mMap.getCameraPosition().zoom) // Sets the zoom
				.bearing(mMap.getCameraPosition().bearing) // Sets the
															// orientation of
															// the camera to
															// east
				.tilt(mMap.getCameraPosition().tilt) // Sets the tilt of the
														// camera to 30 degrees
				.build();
		mMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));

	}

	@Override
	public void onMarkerDrag(Marker arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMarkerDragStart(Marker arg0) {
		isMarkerIsTouched = true;
	}

	@Override
	public void onMapLongClick(LatLng location) {
		// TODO Auto-generated method stub
		myCustomPlacemarker.setPosition(location);
		isMarkerIsTouched = true;

		myLocation.setLatitude(location.latitude);
		myLocation.setLongitude(location.longitude);

		final CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(location) // Sets the center of the map to Mountain
									// View
				.zoom(mMap.getCameraPosition().zoom) // Sets the zoom
				.bearing(mMap.getCameraPosition().bearing) // Sets the
															// orientation of
															// the camera to
															// east
				.tilt(mMap.getCameraPosition().tilt) // Sets the tilt of the
														// camera to 30 degrees
				.build();
		mMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));

	}
}
