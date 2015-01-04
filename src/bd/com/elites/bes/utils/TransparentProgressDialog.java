package bd.com.elites.bes.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import bd.com.elites.bes.R;

public class TransparentProgressDialog extends Dialog {
	private View dialogView;
	private ImageView circle;
	private Context context;

	public TransparentProgressDialog(Context context) {
		super(context, R.style.TransparentProgressDialog);

		this.context = context;

		setTitle(null);
		setCancelable(false);
		setOnCancelListener(null);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		dialogView = inflater.inflate(R.layout.transparent_dialog, null);
		circle = (ImageView) dialogView.findViewById(R.id.circle);
		setContentView(dialogView);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void show() {
		super.show();
		RotateAnimation anim = new RotateAnimation(0.0f, 360.0f,
				Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF,
				.5f);
		anim.setInterpolator(new LinearInterpolator());
		anim.setRepeatCount(Animation.INFINITE);
		anim.setDuration(1000);
		circle.setAnimation(anim);
		circle.startAnimation(anim);

		Animation anim2 = AnimationUtils.loadAnimation(context, R.anim.fade_in);
		anim2.setRepeatCount(Animation.INFINITE);
		anim2.setDuration(300);
		dialogView.findViewById(R.id.wait).setAnimation(anim2);
		dialogView.findViewById(R.id.wait).startAnimation(anim2);

		// Toast.makeText(context, "Please wait a moment",
		// Toast.LENGTH_SHORT).show();

	}

}
