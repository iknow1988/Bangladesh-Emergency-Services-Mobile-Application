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
import bd.com.elites.bes.model.PoliceThana;
import bd.com.elites.bes.utils.Constants;
import bd.com.elites.bes.utils.UtilityFunctions;

public class PoliceThanaListAdapter extends ArrayAdapter<PoliceThana> implements
		OnClickListener {

	private Context context;
	private ArrayList<PoliceThana> policeThanas;
	int language_preference = Constants.LANGUAGE.ENGLISH;
	

	public PoliceThanaListAdapter(Context context,
			ArrayList<PoliceThana> policeThanas) {
		super(context, -1, policeThanas);

		this.context = context;
		this.policeThanas = policeThanas;
		language_preference = UtilityFunctions.getLanguageFromSharedPreference(this.context);
	}

	@Override
	public int getCount() {
		return this.policeThanas.size();
		// return 3;
	}

	@Override
	public PoliceThana getItem(int position) {
		this.policeThanas.get(position).decrypt();
		return this.policeThanas.get(position);
	}

	@Override
	public long getItemId(int position) {
		this.policeThanas.get(position).decrypt();
		return this.policeThanas.get(position).id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.thana_list_item, null);
		}
		this.policeThanas.get(position).decrypt();
		TextView txtTitle = (TextView) convertView
				.findViewById(R.id.thana_name);
		txtTitle.setText(this.policeThanas.get(position).getName(language_preference));

		convertView.findViewById(R.id.thana_header).setTag(
				Integer.valueOf(position));
		convertView.findViewById(R.id.thana_header).setOnClickListener(this);

		if (policeThanas.get(position).phone_number_2.equals("")) {
			((TextView) convertView.findViewById(R.id.call_duty_officer))
					.setText("");
			convertView.findViewById(R.id.call_duty_officer).setEnabled(false);
		} else {
			((TextView) convertView.findViewById(R.id.call_duty_officer))
					.setText(context.getResources().getString(R.string.call_duty_officer));
			convertView.findViewById(R.id.call_duty_officer).setEnabled(true);
			convertView.findViewById(R.id.call_duty_officer).setTag(
					Integer.valueOf(position));
			convertView.findViewById(R.id.call_duty_officer)
					.setOnClickListener(this);
		}
		if (policeThanas.get(position).phone_number_1.equals("")) {
			((TextView) convertView.findViewById(R.id.call_oc)).setText("");
			convertView.findViewById(R.id.call_oc).setEnabled(false);
		} else {

			((TextView) convertView.findViewById(R.id.call_oc))
					.setText(context.getResources().getString(R.string.call_oc));
			convertView.findViewById(R.id.call_oc).setEnabled(true);
			convertView.findViewById(R.id.call_oc).setTag(
					Integer.valueOf(position));
			convertView.findViewById(R.id.call_oc).setOnClickListener(this);
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
		case R.id.thana_header:
			viewDetails((Integer) v.getTag());
			break;

		case R.id.call_duty_officer:
			callDutyOfficer((Integer) v.getTag());
			break;

		case R.id.call_oc:
			callOc((Integer) v.getTag());
			break;

		default:
			break;
		}

	}

	private void viewDetails(final int position) {

		final Dialog detailsDialog = new Dialog(context);
		detailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		View detailView = ((LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.police_station_details, null);

		((TextView) detailView.findViewById(R.id.thanaName))
				.setText(policeThanas.get(position).getName(language_preference));

		((TextView) detailView.findViewById(R.id.dutyOfficer))
				.setText(context.getResources().getString(R.string.duty_office)
						+ policeThanas.get(position).phone_number_2);

		((TextView) detailView.findViewById(R.id.oc)).setText(context.getResources().getString(R.string.oc)
				+ policeThanas.get(position).phone_number_1);

		((TextView) detailView.findViewById(R.id.thanaAddress))
				.setText(context.getResources().getString(R.string.address_for_details_view) + policeThanas.get(position).getCombinedAddress(language_preference));

		String lat = policeThanas.get(position).latitude;
		String lon = policeThanas.get(position).longitude;
		if (lat.trim().equals("") || lon.trim().equals("")) {
			detailView.findViewById(R.id.viewMapTextView).setVisibility(
					View.GONE);
		} else {
			detailView.findViewById(R.id.viewMapTextView).setVisibility(
					View.VISIBLE);
			detailView.findViewById(R.id.thanaAddressContainer)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							detailsDialog.dismiss();
							try {
								String geoString = "geo:"
										+ policeThanas.get(position).latitude
										+ ","
										+ policeThanas.get(position).longitude
										+ "?q="
										+ URLEncoder.encode(
												policeThanas.get(position).thana_name
														+ " Police Station",
												"UTF-8");
								context.startActivity(new Intent(
										Intent.ACTION_VIEW, Uri
												.parse(geoString)));
							} catch (Exception e) {
								String geoString = "http://maps.google.com/maps?q="
										+ policeThanas.get(position).latitude
										+ ","
										+ policeThanas.get(position).longitude;
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

						String msg = policeThanas.get(position).thana_name;
						msg += "\n" + policeThanas.get(position).phone_number_1;
						msg += "\n" + policeThanas.get(position).phone_number_2;
						msg += "\n" + policeThanas.get(position).address;

						Intent intent = new Intent(Intent.ACTION_SEND);
						intent.setType("text/plain");
						intent.putExtra(Intent.EXTRA_SUBJECT,
								policeThanas.get(position).thana_name);
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
								policeThanas.get(position).thana_name);
						addContact.putExtra(
								ContactsContract.Intents.Insert.PHONE,
								policeThanas.get(position).phone_number_1);
						addContact
								.putExtra(
										ContactsContract.Intents.Insert.SECONDARY_PHONE,
										policeThanas.get(position).phone_number_2);
						addContact.putExtra(
								ContactsContract.Intents.Insert.POSTAL,
								policeThanas.get(position).address);
						context.startActivity(addContact);

					}
				});

		detailsDialog.setContentView(detailView);
		detailsDialog.show();
	}

	private void callDutyOfficer(final int position) {

		AlertDialog.Builder dialogBuilder = new Builder(context);
		dialogBuilder
				.setTitle(context.getResources().getString(R.string.making_phone_call))
				.setMessage(
						context.getResources().getString(R.string.call_to)
								+ policeThanas.get(position).getName(language_preference) + "?")
				.setPositiveButton(context.getResources().getString(R.string.yes),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String url = "tel:"
										+ policeThanas.get(position).phone_number_1
												.trim();
								Intent call = new Intent(Intent.ACTION_CALL,
										Uri.parse(url));
								context.startActivity(call);
								dialog.dismiss();

							}
						})
				.setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}

	private void callOc(final int position) {

		AlertDialog.Builder dialogBuilder = new Builder(context);
		dialogBuilder
				.setTitle(context.getResources().getString(R.string.making_phone_call))
				.setMessage(
						context.getResources().getString(R.string.call_to) + policeThanas.get(position).getName(language_preference)
								+ "?")
				.setPositiveButton(context.getResources().getString(R.string.yes),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String url = "tel:"
										+ policeThanas.get(position).phone_number_2
												.trim();
								Intent call = new Intent(Intent.ACTION_CALL,
										Uri.parse(url));
								context.startActivity(call);
								dialog.dismiss();

							}
						})
				.setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();

	}
}
