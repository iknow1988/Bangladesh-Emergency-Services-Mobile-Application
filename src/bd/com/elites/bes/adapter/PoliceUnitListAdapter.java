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
import bd.com.elites.bes.model.PoliceUnit;
import bd.com.elites.bes.utils.UtilityFunctions;

public class PoliceUnitListAdapter extends ArrayAdapter<PoliceUnit> {

	private Context context;
	private ArrayList<PoliceUnit> policeUnits;

	public PoliceUnitListAdapter(Context context,
			ArrayList<PoliceUnit> policeUnits) {
		super(context, -1, policeUnits);

		this.context = context;
		this.policeUnits = policeUnits;
	}

	@Override
	public int getCount() {
		return this.policeUnits.size();
		// return 3;
	}

	@Override
	public PoliceUnit getItem(int position) {
		return this.policeUnits.get(position);
	}

	@Override
	public long getItemId(int position) {
		return this.policeUnits.get(position).id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = inflater
					.inflate(R.layout.police_unit_list_item, null);
		}

		TextView txtTitle = (TextView) convertView.findViewById(R.id.unit_name);
		txtTitle.setText(this.policeUnits.get(position).getUnitName(
				UtilityFunctions.getLanguageFromSharedPreference(context)));

		Animation inAnimation = AnimationUtils.loadAnimation(context,
				R.anim.slide_in_from_bottom);
		convertView.startAnimation(inAnimation);

		return convertView;
	}
}
