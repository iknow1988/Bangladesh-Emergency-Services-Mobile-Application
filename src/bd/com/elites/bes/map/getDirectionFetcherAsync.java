package bd.com.elites.bes.map;

import java.util.ArrayList;

import org.w3c.dom.Document;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public class getDirectionFetcherAsync extends AsyncTask<Void, Void, Void> {
	Document doc = null;
	GMapV2Direction md;
	LatLng src, dstn;
	MapDirectionCallBack mapDirCallBack;
	int color;

	public getDirectionFetcherAsync(LatLng src, LatLng dstn,
			MapDirectionCallBack mapDirCallBack, int color) {
		this.src = src;
		this.dstn = dstn;
		this.mapDirCallBack = mapDirCallBack;
		this.color = color;

	}

	@Override
	protected Void doInBackground(Void... arg0) {
		md = new GMapV2Direction();
		doc = md.getDocument(src, dstn, GMapV2Direction.MODE_DRIVING);
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		// super.onPostExecute(result);
		if (doc != null) {
			ArrayList<LatLng> directionPoint = md.getDirection(doc);
			PolylineOptions rectLine = new PolylineOptions().width(8).color(
					color);

			for (int i = 0; i < directionPoint.size(); i++) {
				rectLine.add(directionPoint.get(i));
			}
			mapDirCallBack.onDirectionGet(rectLine);
		} else {
			mapDirCallBack.onDirectionGet(null);
		}

	}

}
