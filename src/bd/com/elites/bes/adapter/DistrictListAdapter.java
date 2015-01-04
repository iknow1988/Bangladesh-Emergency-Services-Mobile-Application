package bd.com.elites.bes.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;
import bd.com.elites.bes.R;
import bd.com.elites.bes.utils.Constants;
import bd.com.elites.bes.utils.Globals;

public class DistrictListAdapter extends BaseExpandableListAdapter implements
		OnGroupClickListener {

	private Context context;
	int language_preference = Constants.LANGUAGE.ENGLISH;

	public DistrictListAdapter(Context c) {
		this.context = c;
		SharedPreferences pref = context.getSharedPreferences(
				Constants.SHARED_PREFERENCE.SHARED_PREFERENCE_NAME,
				Context.MODE_PRIVATE);

		language_preference = pref.getInt(
				Constants.SHARED_PREFERENCE.LANGUAGE_PREFERENCE,
				Constants.LANGUAGE.ENGLISH);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return Globals.DIVISION_LIST.get(groupPosition).districts
				.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return Globals.DIVISION_LIST.get(groupPosition).districts
				.get(childPosition).id;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View converterView, ViewGroup parent) {

		if (converterView == null) {
			converterView = ((LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.district_list_item, null);
		}

		TextView tv = (TextView) converterView.findViewById(R.id.district_name);
		tv.setText(Globals.DIVISION_LIST.get(groupPosition).districts
				.get(childPosition).getName(language_preference));

		Animation inAnimation = AnimationUtils.loadAnimation(context,
				R.anim.slide_in_from_bottom);
		converterView.startAnimation(inAnimation);
		return converterView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return Globals.DIVISION_LIST.get(groupPosition).districts.size();
	}

	@Override
	public Object getGroup(int position) {
		return Globals.DIVISION_LIST.get(position);
	}

	@Override
	public int getGroupCount() {
		return Globals.DIVISION_LIST.size();
	}

	@Override
	public long getGroupId(int postion) {
		return postion;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View converterView, ViewGroup parent) {

		if (converterView == null) {
			converterView = ((LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.division_list_item, null);
		}

		TextView tv = (TextView) converterView.findViewById(R.id.division_name);
		tv.setText( Globals.DIVISION_LIST.get(groupPosition).getName(language_preference));

		if (isExpanded) {

		} else {

		}

		Animation inAnimation = AnimationUtils.loadAnimation(context,
				R.anim.slide_in_from_bottom);
		converterView.startAnimation(inAnimation);
		return converterView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public boolean onGroupClick(ExpandableListView elv, View v,
			int groupPosition, long id) {

		int groupCount = getGroupCount();
		for (int i = 0; i < groupCount; i++) {
			if (i != groupPosition)
				elv.collapseGroup(i);
		}
		elv.expandGroup(groupPosition);
		elv.setSelectionFromTop(groupPosition, 0);

		return true;
	}

}
