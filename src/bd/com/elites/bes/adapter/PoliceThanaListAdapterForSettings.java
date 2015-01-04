package bd.com.elites.bes.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import bd.com.elites.bes.R;
import bd.com.elites.bes.model.PoliceThana;
import bd.com.elites.bes.utils.UtilityFunctions;

public class PoliceThanaListAdapterForSettings extends
		ArrayAdapter<PoliceThana> {

	private Context context;
	private ArrayList<PoliceThana> policeThanas;

	public PoliceThanaListAdapterForSettings(Context context,
			ArrayList<PoliceThana> policeThanas) {
		super(context, -1, policeThanas);

		this.context = context;
		this.policeThanas = policeThanas;
	}

	@Override
	public int getCount() {
		return this.policeThanas.size();
		// return 3;
	}

	@Override
	public PoliceThana getItem(int position) {
		return this.policeThanas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return this.policeThanas.get(position).id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(
					R.layout.thana_list_item_for_settings, null);
		}

		TextView txtTitle = (TextView) convertView
				.findViewById(R.id.thana_name);
		txtTitle.setText(this.policeThanas.get(position).getName(UtilityFunctions.getLanguageFromSharedPreference(context)));

		Animation inAnimation = AnimationUtils.loadAnimation(context,
				R.anim.slide_in_from_bottom);
		// convertView.startAnimation(inAnimation);

		return convertView;
	}
}
