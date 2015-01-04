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
import bd.com.elites.bes.model.PoliceSubUnit;
import bd.com.elites.bes.utils.UtilityFunctions;

public class PoliceSubUnitListAdapter extends ArrayAdapter<PoliceSubUnit> {

	private Context context;
	private ArrayList<PoliceSubUnit> policeSubUnits;

	public PoliceSubUnitListAdapter(Context context,
			ArrayList<PoliceSubUnit> policeSubUnits) {
		super(context, -1, policeSubUnits);

		this.context = context;
		this.policeSubUnits = policeSubUnits;
	}

	@Override
	public int getCount() {
		return this.policeSubUnits.size();
		// return 3;
	}

	@Override
	public PoliceSubUnit getItem(int position) {
		return this.policeSubUnits.get(position);
	}

	@Override
	public long getItemId(int position) {
		return this.policeSubUnits.get(position).id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.police_sub_unit_list_item,
					null);
		}

		TextView txtTitle = (TextView) convertView
				.findViewById(R.id.sub_unit_name);
		txtTitle.setText(this.policeSubUnits.get(position).getSubUnitName(UtilityFunctions.getLanguageFromSharedPreference(context)));

		Animation inAnimation = AnimationUtils.loadAnimation(context,
				R.anim.slide_in_from_bottom);
		convertView.startAnimation(inAnimation);

		return convertView;
	}
}
