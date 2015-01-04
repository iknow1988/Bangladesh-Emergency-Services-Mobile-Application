package bd.com.elites.bes.utils;

public interface FetcherListener {
	public void onStartFetcher();

	public void onFinishFetcher(Object responseStr);

	public void onErrorFetcher(Exception e);

	public void onNoInternetConnection();
}
