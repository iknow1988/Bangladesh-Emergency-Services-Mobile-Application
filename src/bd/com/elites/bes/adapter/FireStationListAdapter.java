package bd.com.elites.bes.adapter;

import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import bd.com.elites.bes.R;
import bd.com.elites.bes.model.FireserviceStation;
import bd.com.elites.bes.utils.Constants;

public class FireStationListAdapter extends ArrayAdapter<FireserviceStation>
		implements OnClickListener {

	private Context context;
	int language_preference = Constants.LANGUAGE.ENGLISH;
	private ArrayList<FireserviceStation> fireStations;

	public FireStationListAdapter(Context context,
			ArrayList<FireserviceStation> fireStations) {
		super(context, -1, fireStations);

		this.context = context;
		SharedPreferences pref = context.getSharedPreferences(
				Constants.SHARED_PREFERENCE.SHARED_PREFERENCE_NAME,
				Context.MODE_PRIVATE);

		language_preference = pref.getInt(
				Constants.SHARED_PREFERENCE.LANGUAGE_PREFERENCE,
				Constants.LANGUAGE.ENGLISH);

		this.fireStations = fireStations;
	}

	@Override
	public int getCount() {
		return this.fireStations.size();
		// return 3;
	}

	@Override
	public FireserviceStation getItem(int position) {
		return this.fireStations.get(position);
	}

	@Override
	public long getItemId(int position) {
		return this.fireStations.get(position).id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.fire_stations_list_item,
					null);
		}

		TextView txtTitle = (TextView) convertView
				.findViewById(R.id.station_name);
		txtTitle.setText(this.fireStations.get(position).getStationName(
				language_preference));

		convertView.findViewById(R.id.station_header).setTag(
				Integer.valueOf(position));
		convertView.findViewById(R.id.station_header).setOnClickListener(this);

		if (fireStations.get(position).telephone_number.equals("")) {
			((TextView) convertView.findViewById(R.id.call_telephone))
					.setText("");
			convertView.findViewById(R.id.call_telephone).setEnabled(false);
		} else {
			((TextView) convertView.findViewById(R.id.call_telephone))
					.setText(context.getResources().getString(
							R.string.call_on_telephone));
			convertView.findViewById(R.id.call_telephone).setEnabled(true);

			convertView.findViewById(R.id.call_telephone).setTag(
					Integer.valueOf(position));
			convertView.findViewById(R.id.call_telephone).setOnClickListener(
					this);
		}

		if (fireStations.get(position).mobile_number.equals("")) {
			((TextView) convertView.findViewById(R.id.call_mobile)).setText("");
			convertView.findViewById(R.id.call_mobile).setEnabled(false);
		} else {

			((TextView) convertView.findViewById(R.id.call_mobile))
					.setText(context.getResources().getString(
							R.string.call_on_mobile));
			convertView.findViewById(R.id.call_mobile).setEnabled(true);
			convertView.findViewById(R.id.call_mobile).setTag(
					Integer.valueOf(position));
			convertView.findViewById(R.id.call_mobile).setOnClickListener(this);
		}

		Animation inAnimation = AnimationUtils.loadAnimation(context,
				R.anim.slide_in_from_bottom);
		// convertView.startAnimation(inAnimation);

		return convertView;
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();

		switch (i) {
		case R.id.station_header:
			viewDetails((Integer) v.getTag());
			break;

		case R.id.call_telephone:
			callTelephone((Integer) v.getTag());
			break;

		case R.id.call_mobile:
			callMobile((Integer) v.getTag());
			break;

		default:
			break;
		}

	}

	private void callMobile(final int position) {

		AlertDialog.Builder dialogBuilder = new Builder(context);
		dialogBuilder
				.setTitle(
						context.getResources().getString(
								R.string.making_phone_call))
				.setMessage(
						context.getResources().getString(R.string.call_to)
								+ fireStations.get(position).getStationName(
										language_preference) + "?")
				.setPositiveButton(
						context.getResources().getString(R.string.yes),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String url = "tel:"
										+ fireStations.get(position).mobile_number
												.trim();
								Intent call = new Intent(Intent.ACTION_CALL,
										Uri.parse(url));
								context.startActivity(call);
								dialog.dismiss();

							}
						})
				.setNegativeButton(
						context.getResources().getString(R.string.no),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).show();

	}

	private void callTelephone(final int position) {
		AlertDialog.Builder dialogBuilder = new Builder(context);
		dialogBuilder
				.setTitle(
						context.getResources().getString(
								R.string.making_phone_call))
				.setMessage(
						context.getResources().getString(R.string.call_to)
								+ fireStations.get(position).getStationName(
										language_preference) + "?")
				.setPositiveButton(
						context.getResources().getString(R.string.yes),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String url = "tel:"
										+ fireStations.get(position).telephone_number
												.trim();
								Intent call = new Intent(Intent.ACTION_CALL,
										Uri.parse(url));
								context.startActivity(call);
								dialog.dismiss();

							}
						})
				.setNegativeButton(
						context.getResources().getString(R.string.no),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).show();

	}

	private void viewDetails(final int position) {

		final Dialog detailsDialog = new Dialog(context);
		detailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		View detailView = ((LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.fire_station_details, null);

		((TextView) detailView.findViewById(R.id.stationName))
				.setText(fireStations.get(position).getStationName(
						language_preference));

		((TextView) detailView.findViewById(R.id.telephone)).setText(context
				.getResources().getString(R.string.telephone)
				+ fireStations.get(position).telephone_number);

		((TextView) detailView.findViewById(R.id.mobile)).setText(context
				.getResources().getString(R.string.mobile)
				+ fireStations.get(position).mobile_number);

		((TextView) detailView.findViewById(R.id.stationAddress))
				.setText(context.getResources().getString(
						R.string.address_for_details_view)
						+ fireStations.get(position).getCombinedAddress(
								language_preference));

		String lat = fireStations.get(position).latitude;
		String lon = fireStations.get(position).longitude;
		if (lat.trim().equals("") || lon.trim().equals("")) {
			detailView.findViewById(R.id.viewMapTextView).setVisibility(
					View.GONE);
		} else {
			detailView.findViewById(R.id.viewMapTextView).setVisibility(
					View.VISIBLE);
			detailView.findViewById(R.id.stationAddressContainer)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							detailsDialog.dismiss();
							try {
								String geoString = "geo:"
										+ fireStations.get(position).latitude
										+ ","
										+ fireStations.get(position).longitude
										+ "?q="
										+ URLEncoder.encode(
												fireStations.get(position).station_name,
												"UTF-8");
								context.startActivity(new Intent(
										Intent.ACTION_VIEW, Uri
												.parse(geoString)));
							} catch (Exception e) {
								String geoString = "http://maps.google.com/maps?q="
										+ fireStations.get(position).latitude
										+ ","
										+ fireStations.get(position).longitude;
								context.startActivity(new Intent(
										Intent.ACTION_VIEW, Uri
												.parse(geoString)));
							}
						}
					});
		}

		detailView.findViewById(R.id.share).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						detailsDialog.dismiss();

						String msg = fireStations.get(position).station_name;
						msg += "\n"
								+ fireStations.get(position).telephone_number;
						msg += "\n" + fireStations.get(position).mobile_number;
						msg += "\n" + fireStations.get(position).address;

						Intent intent = new Intent(Intent.ACTION_SEND);
						intent.setType("text/plain");
						intent.putExtra(Intent.EXTRA_SUBJECT,
								fireStations.get(position).station_name);
						intent.putExtra(Intent.EXTRA_TEXT, msg);
						context.startActivity(Intent.createChooser(intent,
								"Send Via:"));
					}
				});

		detailView.findViewById(R.id.addToContact).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						detailsDialog.dismiss();

						Intent addContact = new Intent(Intent.ACTION_INSERT);
						addContact
								.setType(ContactsContract.Contacts.CONTENT_TYPE);
						addContact.putExtra(
								ContactsContract.Intents.Insert.NAME,
								fireStations.get(position).station_name);
						addContact.putExtra(
								ContactsContract.Intents.Insert.PHONE,
								fireStations.get(position).mobile_number);
						addContact
								.putExtra(
										ContactsContract.Intents.Insert.SECONDARY_PHONE,
										fireStations.get(position).telephone_number);
						addContact.putExtra(
								ContactsContract.Intents.Insert.POSTAL,
								fireStations.get(position).address);
						context.startActivity(addContact);

					}
				});

		detailsDialog.setContentView(detailView);
		detailsDialog.show();
	}
}
