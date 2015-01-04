package bd.com.elites.bes.map;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import bd.com.elites.bes.R;
import bd.com.elites.bes.utils.BaseActivity;
import bd.com.elites.bes.utils.Constants;
import bd.com.elites.bes.utils.Globals;

public class SnapShotActivity extends BaseActivity implements OnClickListener {
	int onStartCount = 0;
	ImageView snapshot;
	Button backbtn, sharebtn;
	String imagePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.map_snapshot_activity);

		if (savedInstanceState == null) {
			this.overridePendingTransition(R.anim.slide_in_from_right,
					R.anim.slide_out_to_left);
		} else {
			onStartCount = 2;
		}

		backbtn = (Button) findViewById(R.id.buttonBack);
		sharebtn = (Button) findViewById(R.id.buttonShare);
		snapshot = (ImageView) findViewById(R.id.imageViewSnapshot);
		backbtn.setOnClickListener(this);
		sharebtn.setOnClickListener(this);
		Intent i = getIntent();
		imagePath = i.getStringExtra("snapshot");
		Log.d("tonmoy", imagePath);

		// Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

		Bitmap bitmap;
		try {
			bitmap = MediaStore.Images.Media.getBitmap(
					this.getContentResolver(), Uri.parse(imagePath));
			snapshot.setImageBitmap(bitmap);
			snapshot.invalidate();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Bitmap bitmap = Utility.decodeSampledBitmapFromResource(path, 320,
		// 480);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		if (onStartCount > 1) {
			this.overridePendingTransition(R.anim.slide_in_from_left,
					R.anim.slide_out_to_right);

		} else if (onStartCount == 1) {
			onStartCount++;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == backbtn) {
			this.finish();
		} else if (v == sharebtn) {
			Intent share = new Intent(Intent.ACTION_SEND);
			share.setType("image/*");
			share.putExtra(Intent.EXTRA_TEXT,
					getResources().getString(R.string.share_pic_body));
			share.putExtra(Intent.EXTRA_SUBJECT, "Map");
			share.putExtra(Intent.EXTRA_STREAM, Uri.parse(imagePath));
			startActivity(Intent.createChooser(share, "Share Image"));
		}

	}

}
