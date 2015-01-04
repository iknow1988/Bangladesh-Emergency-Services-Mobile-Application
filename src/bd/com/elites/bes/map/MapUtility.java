package bd.com.elites.bes.map;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import bd.com.elites.bes.HomeActivity;
import bd.com.elites.bes.R;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

public class MapUtility {

	// This causes the marker to bounce into position when it
	// is clicked.
	public static void markerJumpAnim(final Marker marker) {
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		final long duration = 1500;

		final Interpolator interpolator = new BounceInterpolator();

		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = Math.max(
						1 - interpolator.getInterpolation((float) elapsed
								/ duration), 0);
				marker.setAnchor(0.5f, 1.0f + 2 * t);

				if (t > 0.0) {
					// Post again 16ms later.
					handler.postDelayed(this, 16);
				}
			}
		});
	}

	public static void dropPinEffect(final Marker marker,
			final ArrayList<Polyline> allPolyline) {
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		final long duration = 1500;
		final Interpolator interpolator = new BounceInterpolator();
		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = Math.max(
						1 - interpolator.getInterpolation((float) elapsed
								/ duration), 0);
				marker.setAnchor(0.5f, 1.0f + 14 * t);

				if (t > 0.0) {
					// Post again 16ms later.
					handler.postDelayed(this, 15);
				} else {
					marker.setAnchor(0.5f, 0.302480f);
					removePolylines(allPolyline);
				}
			}
		});
	}

	public static void dropPinEffect(final Marker marker) {
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		final long duration = 1500;
		final Interpolator interpolator = new BounceInterpolator();
		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = Math.max(
						1 - interpolator.getInterpolation((float) elapsed
								/ duration), 0);
				marker.setAnchor(0.5f, 1.0f + 14 * t);

				if (t > 0.0) {
					// Post again 16ms later.
					handler.postDelayed(this, 15);
				} else {
					marker.setAnchor(0.5f, 1.0f);
				}
			}
		});
	}

	public static void removePolylines(ArrayList<Polyline> allPoly) {
		for (Polyline pl : allPoly) {
			pl.remove();
		}
		allPoly.clear();

	}

	public static void bottomPanelAnim(final View v1, final View v2) {
		TranslateAnimation animate = new TranslateAnimation(0, -v1.getWidth(),
				0, 0);
		animate.setDuration(200);
		animate.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				v2.setVisibility(View.VISIBLE);
				v1.setVisibility(View.GONE);

				slideToRight(v2);
			}

		});
		animate.setFillAfter(false);
		v1.startAnimation(animate);

	}

	// To animate view slide out from left to right
	public static void slideToRight(View view) {
		TranslateAnimation animate = new TranslateAnimation(-view.getWidth(),
				0, 0, 0);
		animate.setDuration(200);
		animate.setFillAfter(true);

		view.startAnimation(animate);
		// view.setVisibility(View.GONE);
	}

	public static void slideToLeft(View view) {
		TranslateAnimation animate = new TranslateAnimation(0,
				-view.getWidth(), 0, 0);
		animate.setDuration(500);
		animate.setFillAfter(true);
		view.startAnimation(animate);
		// view.setVisibility(View.GONE);
	}

	public static int getWidestViewOfAdapter(Context context, Adapter adapter) {
		int maxWidth = 0;
		View view = null;
		FrameLayout fakeParent = new FrameLayout(context);
		for (int i = 0, count = adapter.getCount(); i < count; i++) {
			view = adapter.getView(i, view, fakeParent);
			view.measure(View.MeasureSpec.UNSPECIFIED,
					View.MeasureSpec.UNSPECIFIED);
			int width = view.getMeasuredWidth();
			if (width > maxWidth) {
				maxWidth = width;
			}
		}
		return maxWidth;
	}

	public static Uri getOutputMediaFileUri() {
		return Uri.fromFile(getOutputMediaFile());
	}

	private static File getOutputMediaFile() {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"BES_SnapShot");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("BES_SnapShot", "failed to create directory");
				return null;
			}
		}
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + timeStamp + ".jpg");

		return mediaFile;
	}

	public static Bitmap decodeSampledBitmapFromResource(String path,
			int reqWidth, int reqHeight) {
		Log.d("imageFragment", "decodeSampledBitmapFromResource path: " + path
				+ "width: " + reqWidth + "height" + reqHeight);
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		// Calculate inSampleSize
		// BitmapFactory.decod
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		Bitmap result = BitmapFactory.decodeFile(path, options);
		return result;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		Log.d("test", height + "height");
		Log.d("test", width + "width");
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
			Log.d("test", inSampleSize + "inSampleSize");
		}

		return inSampleSize;
	}

	public static void saveImage(Bitmap bmp, String url) {
		File file = new File(url);
		FileOutputStream fOut;
		try {
			fOut = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			fOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String getRealPathFromURI(Uri contentURI, Context ct) {
		Cursor cursor = ct.getContentResolver().query(contentURI, null, null,
				null, null);
		if (cursor == null) { // Source is Dropbox or other similar local file
								// path
			return contentURI.getPath();
		} else {
			cursor.moveToFirst();
			int idx = cursor
					.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			String path = cursor.getString(idx);
			cursor.close();
			return path;
		}
	}

	public static void makeCall(Context ct, String number) {
		String uri = "tel:" + number.trim();
		Intent intent = new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.parse(uri));
		ct.startActivity(intent);
	}

	public static String getGeoCodingURL(Location loc) {
		String URL = "http://maps.googleapis.com/maps/api/geocode/json?latlng="
				+ loc.getLatitude() + "," + loc.getLongitude() + "&sensor=true";
		return URL;
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public static String getResponseString(HttpResponse response) {
		int charRead;
		int BUFFER_SIZE = 4000;
		char[] inputBuffer = new char[BUFFER_SIZE];

		String returnString = "";

		try {
			// Log.d("responseString in Apicaller.getResponseString()",
			// response.toString());
			HttpEntity responseEntity = response.getEntity();
			InputStream responseStream = responseEntity.getContent();

			InputStreamReader isr = new InputStreamReader(responseStream);
			while ((charRead = isr.read(inputBuffer)) > 0) {
				// ---convert the chars to a String---
				String readString = String
						.copyValueOf(inputBuffer, 0, charRead);
				returnString += readString;
				inputBuffer = new char[BUFFER_SIZE];
			}
		} catch (Exception e) {
			Log.d("error in Apicaller.getResponseString()", e.toString());
			// DebugLog.log(e.toString());
			return null;
		}
		return returnString;
	}

	public static void viewStationDetailsDialog(final Context context,
			final StationInfoForMap info, final int language_preference) {

		final Dialog detailsDialog = new Dialog(context);
		detailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		View detailView = ((LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.map_station_details, null);

		((TextView) detailView.findViewById(R.id.stationName)).setText(info
				.getName(language_preference));

		String telephone = context.getResources().getString(R.string.telephone);
		String mobile = context.getResources().getString(R.string.mobile);
		String address = context.getResources().getString(R.string.address);
		((TextView) detailView.findViewById(R.id.telephone)).setText(telephone
				+ info.getPhoneNo1());

		((TextView) detailView.findViewById(R.id.mobile)).setText(mobile
				+ info.getPhoneNo2());

		((TextView) detailView.findViewById(R.id.stationAddress))
				.setText(address+" : " + info.getAddress(language_preference));

		detailView.findViewById(R.id.telephone_container).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						makeCall(context, info.getPhoneNo1());
						detailsDialog.dismiss();

					}
				});

		detailView.findViewById(R.id.mobile_container).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						makeCall(context, info.getPhoneNo2());
						detailsDialog.dismiss();
					}
				});

		detailView.findViewById(R.id.share).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						detailsDialog.dismiss();

						String msg = info.getName(language_preference);
						msg += "\n" + info.getPhoneNo1();
						msg += "\n" + info.getPhoneNo2();
						msg += "\n" + info.getAddress();

						Intent intent = new Intent(Intent.ACTION_SEND);
						intent.setType("text/plain");
						intent.putExtra(Intent.EXTRA_SUBJECT,
								info.getName(language_preference));
						intent.putExtra(Intent.EXTRA_TEXT, msg);
						context.startActivity(Intent.createChooser(intent,
								"Send Via:"));
					}
				});

		detailView.findViewById(R.id.viewPath).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						((MapViewActivity) context).showDirection(info);
						detailsDialog.dismiss();
					}
				});

		detailsDialog.setContentView(detailView);
		detailsDialog.show();
	}

	public static void showErrorDialog(final Activity activity, String message) {
		Dialog dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		View view = ((LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.simple_dialog_with_message, null);

		((TextView) view.findViewById(R.id.message)).setText(message);

		dialog.setContentView(view);
		dialog.show();
		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				activity.startActivity(new Intent(activity, HomeActivity.class));
				activity.finish();
			}
		});
		return;
	}
}
