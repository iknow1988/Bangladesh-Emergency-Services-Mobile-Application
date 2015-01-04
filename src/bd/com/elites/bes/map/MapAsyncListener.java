package bd.com.elites.bes.map;

public interface MapAsyncListener {
	public void onPreExecute();

	public void onPostExecute(Object result, int type);

	public void onError(Exception e);

}
