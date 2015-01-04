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
import bd.com.elites.bes.model.PoliceSubTwoUnit;
import bd.com.elites.bes.utils.UtilityFunctions;

public class PoliceSubTwoUnitListAdapter extends ArrayAdapter<PoliceSubTwoUnit> {

	private Context context;
	private ArrayList<PoliceSubTwoUnit> policeSubTwoUnits;

	public PoliceSubTwoUnitListAdapter(Context context,
			ArrayList<PoliceSubTwoUnit> policeSubTwoUnits) {
		super(context, -1, policeSubTwoUnits);

		this.context = context;
		this.policeSubTwoUnits = policeSubTwoUnits;
	}

	@Override
	public int getCount() {
		return this.policeSubTwoUnits.size();
		// return 3;
	}

	@Override
	public PoliceSubTwoUnit getItem(int position) {
		return this.policeSubTwoUnits.get(position);
	}

	@Override
	public long getItemId(int position) {
		return this.policeSubTwoUnits.get(position).id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(
					R.layout.police_sub_two_unit_list_item, null);
		}

		TextView txtTitle = (TextView) convertView
				.findViewById(R.id.sub_two_unit_name);
		txtTitle.setText(this.policeSubTwoUnits.get(position).getSubTwoUnitName(UtilityFunctions.getLanguageFromSharedPreference(context)));

		Animation inAnimation = AnimationUtils.loadAnimation(context,
				R.anim.slide_in_from_bottom);
		convertView.startAnimation(inAnimation);

		return convertView;
	}
}
