package bd.com.elites.bes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import bd.com.elites.bes.R;
import bd.com.elites.bes.utils.Globals;

public class MainMenuAdapter extends BaseAdapter {

	Context context;

	public MainMenuAdapter(Context c) {
		// TODO Auto-generated constructor stub
		this.context = c;
		Globals.MAIN_MENU_ITEMS = this.context.getResources().getStringArray(R.array.MAIN_MENU_ITEMS);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Globals.MAIN_MENU_ITEMS.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return Globals.MAIN_MENU_ITEMS[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View converterView, ViewGroup parent) {
		// TODO Auto-generated method stub

		if (converterView == null) {
			converterView = ((LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.main_menu_item, null);
		}

		((TextView) converterView.findViewById(R.id.name))
				.setText(Globals.MAIN_MENU_ITEMS[position]);

		String defaultIcon = "ic_launcher";
		int iconId = context.getResources().getIdentifier(
				Globals.MAIN_MENU_ICONS[position].toLowerCase(), "drawable",
				context.getPackageName());
		if (iconId == 0) {
			iconId = context.getResources().getIdentifier(defaultIcon,
					"drawable", context.getPackageName());
		}
		((ImageView) converterView.findViewById(R.id.icon))
				.setImageResource(iconId);

		Animation inAnimation = AnimationUtils.loadAnimation(context,
				R.anim.grow_from_middle);

		converterView.startAnimation(inAnimation);

		return converterView;
	}

}
