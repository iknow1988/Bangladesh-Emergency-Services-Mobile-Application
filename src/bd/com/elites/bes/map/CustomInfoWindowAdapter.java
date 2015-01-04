package bd.com.elites.bes.map;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import bd.com.elites.bes.R;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements InfoWindowAdapter {

	// These a both viewgroups containing an ImageView with id "badge" and
	// two TextViews with id
	// "title" and "snippet".
	private final View mWindow;
	private Context context;

	public CustomInfoWindowAdapter(Context c) {

		LayoutInflater inflater = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mWindow = inflater.inflate(R.layout.custom_info_window, null);
		context=c;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		render(marker, mWindow);
		return mWindow;
	}

	private void render(Marker marker, View view) {
		String title = marker.getTitle();
		TextView titleUi = ((TextView) view.findViewById(R.id.title));
		if (title != null) {
			// Spannable string allows us to edit the formatting of the
			// text.
			SpannableString titleText = new SpannableString(title);
			titleText.setSpan(new ForegroundColorSpan(Color.WHITE), 0,
					titleText.length(), 0);
			titleUi.setText(titleText);
		} else {
			titleUi.setText("");
		}

		TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));

		String click_to_call = context.getResources().getString(R.string.click_for_details);
		snippetUi.setText(click_to_call);

	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
